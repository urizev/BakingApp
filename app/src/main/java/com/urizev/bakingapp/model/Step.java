package com.urizev.bakingapp.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Step {
    public abstract int id();
    public abstract String shortDescription();
    public abstract String description();
    public abstract String videoURL();
    public abstract String thumbnailURL();

    public static Step create(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        return new AutoValue_Step(id, shortDescription, description, videoURL, thumbnailURL);
    }


    public static JsonAdapter<Step> jsonAdapter(Moshi moshi) {
        return new AutoValue_Step.MoshiJsonAdapter(moshi);
    }
}
