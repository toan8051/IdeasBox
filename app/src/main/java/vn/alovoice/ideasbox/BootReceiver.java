package vn.alovoice.ideasbox;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by VIENTHONG on 8/13/2015.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getSimpleName();
    //Định kỳ 15 phút nạp về một lần
    private static final long DEFAULT_INTERVAL =
            AlarmManager.INTERVAL_FIFTEEN_MINUTES;
    @Override
    public void onReceive(Context context, Intent intent) {

        long interval = DEFAULT_INTERVAL;
        //Đưa dịch vụ nạp danh sách ý tưởng vào PendingIntent để chuẩn bị cho việc nạp chạy tự động theo chu kỳ
        PendingIntent operation = PendingIntent.getService(context, -1,
                new Intent(context, RefreshService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Lấy biến định thời
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE); //

        //Kiểm tra nếu interval = 0 thì hủy nạp.
        if (interval == 0) {
            alarmManager.cancel(operation);
            Log.d(TAG, "cancelling repeat operation");
        } else {
            //Tự động nạp RefreshService thông qua operation cứ interval(15 phut) một lần.
            alarmManager.setInexactRepeating(AlarmManager.RTC,
                    System.currentTimeMillis(), interval, operation);
            Log.d(TAG, "setting repeat operation for: " + interval);
        }

        //context.startService(new Intent(context, RefreshService.class));
        Log.e("BootReceiver", "onReceived");
    }
}
