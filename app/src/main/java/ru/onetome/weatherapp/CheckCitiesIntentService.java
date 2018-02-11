package ru.onetome.weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;


public class CheckCitiesIntentService extends IntentService {

    private static final String TAG = "CheckCitiesServiceTAG";
    private final String FILE_NAME = "cities.list.json";

    public CheckCitiesIntentService() {
        super("CheckCitiesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String md5FromWeb = null;
        String md5FromFile = null;

        File file = new File(getApplicationContext().getFilesDir(), FILE_NAME);
        Log.i(TAG, "FilePath: " + file.getAbsolutePath());
        if (file.exists()) {
            Log.i(TAG, "CitiesFile is exist");
            md5FromFile = CheckCitiesMD5FromFile(file);
            md5FromWeb = CheckCitiesMD5FromWeb();
        } else {
            Log.i(TAG, "CitiesFile isn`t exist");
            SaveNewCitiesList(file);
        }
        if (md5FromFile != null && !md5FromFile.equals(md5FromWeb)) {
            SaveNewCitiesList(file);
        }
    }

    private String CheckCitiesMD5FromWeb() {
        String md5 = null;
        try {
            URL url = new URL("http://bulk.openweathermap.org/sample/city.list.json.gz");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream gzip = new GZIPInputStream(connection.getInputStream());
//            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
//            byte[] byteArray = new byte[1024];
//            int bytesCount = 0;
//            while ((bytesCount = gzip.read(byteArray)) != -1) {
//                md5Digest.update(byteArray, 0, bytesCount);
//            }
//            byte[] bytes = md5Digest.digest();
//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i< bytes.length ;i++)
//            {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            md5 = sb.toString();

            byte[] data = DigestUtils.md5(gzip);
            char[] md5Chars = Hex.encodeHex(data);
            md5 = String.valueOf(md5Chars);
            gzip.close();
        } catch (Exception e) {
            Log.i(TAG, "CheckCitiesException: " + e.toString());
        }
        return md5;
    }

    private String CheckCitiesMD5FromFile(File file) {
        String md5 = null;
        try {
            InputStream fis = new FileInputStream(file);
//            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
//            byte[] byteArray = new byte[1024];
//            int bytesCount = 0;
//            while ((bytesCount = fis.read(byteArray)) != -1) {
//                md5Digest.update(byteArray, 0, bytesCount);
//            }
//            byte[] bytes = md5Digest.digest();
//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i< bytes.length ;i++)
//            {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            md5 = sb.toString();



            byte[] data = DigestUtils.md5(fis);
            char[] md5Chars = Hex.encodeHex(data);
            md5 = String.valueOf(md5Chars);
            fis.close();
        } catch (Exception e) {
            Log.i(TAG, "CheckCitiesMD5FromFileException: " + e.toString());
        }
        return md5;
    }

    private void SaveNewCitiesList(File file) {
        try {
            URL url = new URL("http://bulk.openweathermap.org/sample/city.list.json.gz");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            GZIPInputStream gzip = new GZIPInputStream(connection.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(file, false);
            byte[] b = new byte[1024];
            int count;
            while ((count = gzip.read(b)) != -1) {
                outputStream.write(b, 0, count);
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder(1024);
            ArrayList<String> stringList = new ArrayList<>();
            String tempData;
            while ((tempData = reader.readLine()) != null) {
                if (tempData.equals("[")) {
                } else if (tempData.contains("{")) {
                    sb.append(tempData);
                } else if (tempData.contains("},")) {
                    sb.append(tempData.replace("},", "}"));
                    stringList.add(sb.toString());
                    if (stringList.size() == 200) {
                        Intent fillCitiesIntentService = new Intent(getApplicationContext(), FillCitiesIntentService.class);
                        fillCitiesIntentService.putExtra(FillCitiesIntentService.INTENT_KEY, stringList);
                        startService(fillCitiesIntentService);
                        stringList.clear();
                    }
                    sb.setLength(0);
                    sb.trimToSize();
                } else if (tempData.contains("]")) {
                    Intent fillCitiesIntentService = new Intent(getApplicationContext(), FillCitiesIntentService.class);
                    stringList.add(sb.toString());
                    fillCitiesIntentService.putExtra(FillCitiesIntentService.INTENT_KEY, stringList);
                    startService(fillCitiesIntentService);
                    sb.setLength(0);
                    sb.trimToSize();
                } else {
                    sb.append(tempData);
                }
            }

            outputStream.close();
            reader.close();
            gzip.close();
        } catch (Exception e) {
            Log.i(TAG, "CheckCitiesException: " + e.toString());
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
        Toast.makeText(this, "CheckCitiesService done", Toast.LENGTH_SHORT).show();
    }
}
