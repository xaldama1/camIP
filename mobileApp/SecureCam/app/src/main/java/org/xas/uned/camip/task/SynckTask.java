package org.xas.uned.camip.task;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.xas.uned.camip.model.DeviceCheckData;
import org.xas.uned.camip.model.SyncData;
import org.xas.uned.camip.utils.Constants;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SynckTask extends AsyncTask<DeviceCheckData, String, SyncData> {

    private static final String URL = ":8080/device/sync";

    private final String host ;

    private final ObjectMapper om = new ObjectMapper();

    public SynckTask(String host){
        this.host = host;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected SyncData doInBackground(DeviceCheckData... params) {

        String token = Constants.getUserToken();

        HttpURLConnection urlConnection = null;
        InputStream is = null;
        InputStreamReader ir = null;

        try {
            URL url = new URL(host+URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setRequestMethod("GET");

            if(token != null) {
                urlConnection.setRequestProperty("token", token);
            }

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = urlConnection.getInputStream();
                ir = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(ir);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                if (response != null) {
                    return om.readValue(response.toString(), SyncData.class);

                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            if(is != null){
                try {
                    is.close();
                }catch (Exception e){}
            }
            if(ir != null){
                try {
                    ir.close();
                }catch (Exception e){}
            }
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }
}
