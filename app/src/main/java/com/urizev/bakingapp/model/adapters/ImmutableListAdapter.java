package com.urizev.bakingapp.model.adapters;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public class ImmutableListAdapter<T> extends JsonAdapter<ImmutableList<T>> {
    public static final Factory FACTORY = (type, annotations, moshi) -> {
        Class<?> rawType = Types.getRawType(type);
        if (!annotations.isEmpty()) return null;
        if (rawType == ImmutableList.class) {
            return newImmutableListAdapter(type, moshi).nullSafe();
        }
        return null;
    };

    private final JsonAdapter<T> elementAdapter;

    private ImmutableListAdapter(JsonAdapter<T> elementAdapter) {
        this.elementAdapter = elementAdapter;
    }

    private static <T> JsonAdapter<ImmutableList<T>> newImmutableListAdapter(Type type, Moshi moshi) {
        Type elementType = Types.collectionElementType(type, ImmutableList.class);
        JsonAdapter<T> elementAdapter = moshi.adapter(elementType);
        return new ImmutableListAdapter<>(elementAdapter);
    }

    @Override
    public ImmutableList<T> fromJson(@NonNull JsonReader reader) throws IOException {
        List<T> result = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            result.add(elementAdapter.fromJson(reader));
        }
        reader.endArray();
        return ImmutableList.copyOf(result);
    }

    @Override
    public void toJson(@NonNull JsonWriter writer, ImmutableList<T> value) throws IOException {
        writer.beginArray();
        for (T element : value) {
            elementAdapter.toJson(writer, element);
        }
        writer.endArray();
    }
}
