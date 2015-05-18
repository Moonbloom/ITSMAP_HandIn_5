package grp14.itsmap.com.hi514.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import grp14.itsmap.com.hi514.models.Train;
import grp14.itsmap.com.hi514.viewmodels.MainActivity;

@SuppressWarnings("FieldCanBeLocal")
public class TrainDownloadService extends Service {

    private final String url = "http://stog.itog.dk/itog/action/list/format/json";

    private final IBinder binder = new TrainBinder();

    public void downloadList() {
        new RequestTask().execute(url);
    }

    private void sendListReadyLocalBroadcast(ArrayList<Train> list) {
        //Broadcast the update saying the list has been downloaded and can be used
        Intent intent = new Intent(MainActivity.localBroadcastUpdateMsg);
        intent.putExtra(MainActivity.listExtraTag, list);
        LocalBroadcastManager.getInstance(TrainDownloadService.this).sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class TrainBinder extends Binder {
        public TrainDownloadService getService() {
            return TrainDownloadService.this;
        }
    }

    private class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urlString) {
            String responseString = "";
            try {
                URL url = new URL(urlString[0]);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                    responseString = stringBuilder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new Gson();
            ArrayList<Train> list = gson.fromJson(result, new TypeToken<ArrayList<Train>>(){}.getType());

            sendListReadyLocalBroadcast(list);
        }
    }
}