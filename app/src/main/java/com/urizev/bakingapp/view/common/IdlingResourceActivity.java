package com.urizev.bakingapp.view.common;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;

public abstract class IdlingResourceActivity extends AppCompatActivity {
    @Nullable
    private CountingIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new CountingIdlingResource(getClass().getName());
        }
        return idlingResource;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getIdlingResource();
    }
}
