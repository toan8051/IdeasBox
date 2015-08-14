package vn.alovoice.ideasbox;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by VIENTHONG on 8/14/2015.
 */

public class IdeaWidget extends AppWidgetProvider { //
    private static final String TAG = IdeaWidget.class.getSimpleName();
    //Cap nhat y tuong moi
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) { //
        Log.d(TAG, "onUpdate");
// Get the latest tweet
        Cursor cursor = context.getContentResolver().query(
                IdeaContract.CONTENT_URI, null, null, null,
                IdeaContract.DEFAULT_SORT); //
        if (!cursor.moveToFirst()) //
            return;
//
        String dienthoai = cursor.getString(cursor
                .getColumnIndex(IdeaContract.Column.DIENTHOAI));
        String noidung = cursor.getString(cursor
                .getColumnIndex(IdeaContract.Column.NOIDUNG));
        long ngaytao = cursor.getLong(cursor
                .getColumnIndex(IdeaContract.Column.NGAYTAO));

        PendingIntent operation = PendingIntent.getActivity(context, -1,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

// Loop through all the instances of YambaWidget
        for (int appWidgetId : appWidgetIds) { //
// Update the view
            RemoteViews view = new RemoteViews(context.getPackageName(),
                    R.layout.widget); //
// Update the remote view
            view.setTextViewText(R.id.list_item_text_dienthoai, dienthoai);
            view.setTextViewText(R.id.list_item_text_noidung, noidung);
            view.setTextViewText(R.id.list_item_text_ngaytao,
                    DateUtils.getRelativeTimeSpanString(ngaytao));
            view.setOnClickPendingIntent(R.id.list_item_text_dienthoai,
                    operation);
            view.setOnClickPendingIntent(R.id.list_item_text_noidung,
                    operation);
// Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, view); //
        }
    }

    //khi co y tuong moi thi tu dong c?p nhat widgets
    @Override
    public void onReceive(Context context, Intent intent) { //
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context); //
        this.onUpdate(context, appWidgetManager, appWidgetManager
                .getAppWidgetIds(new ComponentName(context,
                        IdeaWidget.class))); //
    }
}

