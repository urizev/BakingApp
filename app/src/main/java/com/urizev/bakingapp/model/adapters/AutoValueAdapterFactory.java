package com.urizev.bakingapp.model.adapters;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

/**
 * Creado por jcvallejo en 29/11/17.
 */
@MoshiAdapterFactory
public abstract class AutoValueAdapterFactory implements JsonAdapter.Factory {
    public static JsonAdapter.Factory create() {
        return new AutoValueMoshi_AutoValueAdapterFactory();
    }

}