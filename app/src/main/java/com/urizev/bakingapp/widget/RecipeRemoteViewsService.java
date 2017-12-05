package com.urizev.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.urizev.bakingapp.R;

public class RecipeRemoteViewsService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsFactory {
        private String [] ingredients;

        Context mContext;

        ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            this.ingredients = intent.getStringArrayExtra(RecipeWidgetProvider.EXTRA_INGREDIENTS_LIST);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredients == null) {
                return 0;
            }
            return ingredients.length;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.cell_widget_list_item);

            String ingredient = ingredients[i];

            view.setTextViewText(R.id.cell_widget_item_text, ingredient);

            Intent intent = new Intent();
            view.setOnClickFillInIntent(R.id.cell_widget_list_item, intent);

            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
