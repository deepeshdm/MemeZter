package com.deepesh.memezter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    ImageView memeImageView;
    MemeAPI memeAPI;
    OkHttpClient okHttpClient;
    Button nextMemeButton;
    String currentUrl;
    ProgressBar progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memeImageView = findViewById(R.id.memeImageview);
        progressBar1 = findViewById(R.id.progressBar1);
        nextMemeButton = findViewById(R.id.next_button);
        nextMemeButton.setOnClickListener(v -> NextMeme());


        // RETROFIT LOG INTERCEPTOR
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        NextMeme();

    }

    public void NextMeme() {

        progressBar1.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://meme-api.herokuapp.com/")
                .client(okHttpClient)
                .build();

        memeAPI = retrofit.create(MemeAPI.class);

        Call<MemeModal> call = memeAPI.getMeme();

        call.enqueue(new Callback<MemeModal>() {
            @Override
            public void onResponse(Call<MemeModal> call, Response<MemeModal> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: RESPONSE IS NOT SUCESSFUL : " + response.message());
                    return;
                }
                MemeModal meme = response.body();
                String memeUrl = meme.getUrl();
                currentUrl = memeUrl;

                LoadImageIntoImageView(memeUrl);
            }

            @Override
            public void onFailure(Call<MemeModal> call, Throwable t) {
                Log.d(TAG, "onFailure: FAILURE ON REQUEST : " + t.getMessage());
            }
        });

    }

    public void ShareMeme(View view) {

       if (currentUrl==null){
           return;
       }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Hey ! See this amazing reddit meme I got at " + currentUrl );
        intent.setType("text/plain");
       Intent chooser = Intent.createChooser(intent,"Share this with...");
        startActivity(chooser);
    }


    //Glide without Listener
    //Glide.with(MainActivity.this).load(url).into(memeImageView);

    // Just Pass the URL & it'll Load it into ImageView.
    void LoadImageIntoImageView(String url) {
        Glide.with(MainActivity.this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                progressBar1.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar1.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(memeImageView);
    }



}