package org.xas.uned.camip.task;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.xas.uned.camip.model.DeviceCheckData;
import org.xas.uned.camip.model.Token;
import org.xas.uned.camip.utils.Constants;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class APICallTask extends AsyncTask<DeviceCheckData, String, Void> {

    private static final String URL = ":8080/check/use";

    private final String host;
    private final ObjectMapper om = new ObjectMapper();

    public APICallTask(String host){
        this.host = host;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(DeviceCheckData... params) {

        String token = Constants.getUserToken();
        token="aaaaa";

        DeviceCheckData checkData = params[0];
        checkData.setResponseToken(token);

        OutputStream out = null;

        try {
            URL url = new URL("http://"+host+URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("camip-token", token);


            out = new BufferedOutputStream(urlConnection.getOutputStream());

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"))) {

                writer.write(om.writeValueAsString(checkData));
                writer.flush();
                writer.close();
                out.close();

            //    urlConnection.connect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            int responseCode = urlConnection.getResponseCode();
            //System.out.println(responseCode);
            //urlConnection.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
