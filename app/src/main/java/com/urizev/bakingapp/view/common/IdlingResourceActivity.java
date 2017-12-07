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
    private SemaphoreIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public SemaphoreIdlingResource getmIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SemaphoreIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getmIdlingResource();
    }

    class SemaphoreIdlingResource implements IdlingResource {
        @Nullable private volatile IdlingResource.ResourceCallback mCallback;

        private int mCounter;

        SemaphoreIdlingResource() {
            this.mCounter = 0;
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

        @Override
        public synchronized boolean isIdleNow() {
            return mCounter == 0;
        }

        synchronized void increment() {
            ++this.mCounter;
        }

        synchronized void decrement() {
            if (this.mCounter <= 0) {
                mCounter = 0;
                return;
            }

            --this.mCounter;

            if (this.mCounter <= 0) {
                mCounter = 0;
                ResourceCallback callback = this.mCallback;
                if (callback != null) {
                    callback.onTransitionToIdle();
                }
            }
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback callback) {
            this.mCallback = callback;
        }
    }
}
