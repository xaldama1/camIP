package org.xas.uned.camip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.xas.uned.camip.dao.DeviceTokenDAO;
import org.xas.uned.camip.model.Device;
import org.xas.uned.camip.model.SyncData;
import org.xas.uned.camip.task.SynckTask;
import org.xas.uned.camip.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private static final String USER_TOKEN_KEY = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("camip", Context.MODE_PRIVATE);
        String token = sharedPref.getString(USER_TOKEN_KEY, null);
        if(token == null || token.equals("")){
            token = "test-token";
            Toast.makeText(MainActivity.this, "Need to sync with device", Toast.LENGTH_SHORT).show();
        }

        Constants.setUserToken(token);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SynckTask("192.168.0.12"){
                    @Override
                    protected void onPostExecute(SyncData syncData) {
                        Constants.setUserToken(syncData.getUserToken());
                        DeviceTokenDAO deviceTokenDAO = DeviceTokenDAO.getInstance();
                        deviceTokenDAO.clear();
                        for(Device device : syncData.getDevices()){
                            deviceTokenDAO.addTokens(device.getMac(), device.getTokens());
                        }
                    }
                }.execute();
                Snackbar.make(view, "Trying to synchronize with the server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        System.out.println("*****-"+token);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("camip")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "subscribe_failed";
                        }
                        System.out.println(msg);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}