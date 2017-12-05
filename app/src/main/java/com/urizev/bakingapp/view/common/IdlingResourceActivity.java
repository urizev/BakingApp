package com.urizev.bakingapp.view.common;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

public abstract class IdlingResourceActivity extends AppCompatActivity {
    @Nullable
    private SemaphoreIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public SemaphoreIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SemaphoreIdlingResource();
        }
        return idlingResource;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getIdlingResource();
    }

    class SemaphoreIdlingResource implements IdlingResource {
        @Nullable private volatile IdlingResource.ResourceCallback callback;

        private int counter;

        SemaphoreIdlingResource() {
            this.counter = 0;
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

        @Override
        public synchronized boolean isIdleNow() {
            return counter == 0;
        }

        synchronized void increment() {
            ++this.counter;
        }

        synchronized void decrement() {
            if (this.counter <= 0) {
                counter = 0;
                return;
            }

            --this.counter;

            if (this.counter <= 0) {
                counter = 0;
                ResourceCallback callback = this.callback;
                if (callback != null) {
                    callback.onTransitionToIdle();
                }
            }
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback callback) {
            this.callback = callback;
        }
    }
}
