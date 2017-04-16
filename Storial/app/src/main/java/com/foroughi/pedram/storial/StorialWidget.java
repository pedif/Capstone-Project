package com.foroughi.pedram.storial;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.foroughi.pedram.storial.Common.FirebaseConstants;
import com.foroughi.pedram.storial.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class StorialWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.storial_widget);

        FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.TABLE_STORY)
        .orderByChild(FirebaseConstants.COLUMN_HIT_COUNT).limitToFirst(3)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot == null)
                    return;
                int index= 0 ;
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Story story = data.getValue(Story.class);
                    story.setId(data.getKey());

                    populateItem(views,index++,story);

                }

                appWidgetManager.updateAppWidget(appWidgetId, views);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void populateItem(RemoteViews views, int index,Story story) {

        switch (index){
            case 0 :
                views.setTextViewText(R.id.widget_tv_title_1,story.getTitle());
                views.setTextViewText(R.id.widget_tv_hit_1,String.valueOf(-story.getHitCount()));
                break;

            case 1:
                views.setTextViewText(R.id.widget_tv_title_2,story.getTitle());
                views.setTextViewText(R.id.widget_tv_hit_2,String.valueOf(-story.getHitCount()));
                break;

            case 2 :
                views.setTextViewText(R.id.widget_tv_title_3,story.getTitle());
                views.setTextViewText(R.id.widget_tv_hit_3,String.valueOf(-story.getHitCount()));
                break;


        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

