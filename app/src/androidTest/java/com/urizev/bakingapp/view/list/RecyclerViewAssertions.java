package com.urizev.bakingapp.view.list;

import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


class RecyclerViewAssertions {
    static ViewAssertion containsItemCount(int expectedCount) {
        return (view, noViewFoundException) -> {
            assertThat(view, isAssignableFrom(RecyclerView.class));
            RecyclerView rv = (RecyclerView) view;
            RecyclerView.Adapter adapter = rv.getAdapter();
            assertNotNull(adapter);
            assertEquals(expectedCount, adapter.getItemCount());
        };
    }
}
