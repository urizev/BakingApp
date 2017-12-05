package com.urizev.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.detail.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipeWidgetProvider extends AppWidgetProvider {
    static final String EXTRA_INGREDIENTS_LIST = "ingredientList";

    private List<String> ingredients = new ArrayList<>();

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);

        appIntent.addCategory(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,appWidgetId,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

        Intent intent = new Intent(context, RecipeRemoteViewsService.class);
        remoteViews.setRemoteAdapter(R.id.widget_list, intent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));

        final String action = intent.getAction();

        if (action != null &&
                intent.hasExtra(EXTRA_INGREDIENTS_LIST) &&
                action.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
            String [] ingredients = intent.getStringArrayExtra(EXTRA_INGREDIENTS_LIST);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
            //Now update all widgets
            updateRecipeWidgets(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
    }
}
