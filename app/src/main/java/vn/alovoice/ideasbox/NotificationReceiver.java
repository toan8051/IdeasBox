package vn.alovoice.ideasbox;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by VIENTHONG on 8/13/2015.
 */
public class NotificationReceiver extends BroadcastReceiver { //
    public static final int NOTIFICATION_ID = 42;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Lay bien quan ly thong bao
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Lay so y tuong moi
        int count = intent.getIntExtra("count", 0);

        //Nap thong bao vao giao dien chinh MainActivity
        PendingIntent operation = PendingIntent.getActivity(context, -1,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_ONE_SHOT);

        //Khai bao thong tin thong bao de gui len hop thong bao
        Notification notification = new Notification.Builder(context)
                .setContentTitle("New Idea!")
                .setContentText("You've got " + count + " new Ideas")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .getNotification();
        //dua thong tin len hop thong bao
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}