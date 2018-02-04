package ru.onetome.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;


public class CheckCitiesIntentService extends IntentService {

    private final String TAG = "CheckCitiesService";

    public CheckCitiesIntentService() {
        super("CheckCitiesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            URL url = new URL("http://bulk.openweathermap.org/sample/city.list.json.gz");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            GZIPInputStream gzip = new GZIPInputStream(connection.getInputStream());
//            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream("F:/gawiki-20090614-stub-meta-history.xml.gz"));
            BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
            br.readLine();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.w(TAG, "onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
