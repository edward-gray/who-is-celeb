package com.example.guesstheceleberity;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class DownloadWebContent extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpsURLConnection httpsURLConnection;

        try {
            url = new URL(strings[0]);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();

            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();

            StringBuilder result = new StringBuilder();
            while (data != -1) {
                char current = (char) data;
                result.append(current);
                data = inputStreamReader.read();
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed!";
        }
    }

    public String download(String url) {
        String result = null;
        DownloadWebContent task = new DownloadWebContent();
        try {
            result = task.execute(url).get();
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}