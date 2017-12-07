package com.urizev.bakingapp;

import android.app.Application;

import com.squareup.moshi.Moshi;
import com.urizev.bakingapp.model.RecipeRepository;
import com.urizev.bakingapp.model.RecipeService;
import com.urizev.bakingapp.model.adapters.AutoValueAdapterFactory;
import com.urizev.bakingapp.model.adapters.ImmutableListAdapter;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public class App extends Application {
    private static final long MAX_CACHE_SIZE = 1024 * 1024;

    private RecipeRepository mRecipeRepository;

    @Override
    public void onCreate() {
        setup();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        super.onCreate();
    }

    public RecipeRepository getmRecipeRepository() {
        return mRecipeRepository;
    }

    private void setup() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message));
        RecipeService service = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net")
                .client(new OkHttpClient.Builder()
                        .cache(new Cache(getCacheDir(), MAX_CACHE_SIZE))
                        .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .build())
                .addConverterFactory(MoshiConverterFactory.create(new Moshi.Builder()
                        .add(ImmutableListAdapter.FACTORY)
                        .add(AutoValueAdapterFactory.create())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RecipeService.class);
        this.mRecipeRepository = new RecipeRepository(service);
    }
}
