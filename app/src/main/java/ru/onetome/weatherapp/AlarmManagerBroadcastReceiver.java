package ru.onetome.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public static void scheduleAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, CheckCitiesIntentService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleAlarms(context);
    }
}
