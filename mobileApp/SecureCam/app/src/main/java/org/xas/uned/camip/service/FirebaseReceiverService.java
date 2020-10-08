package org.xas.uned.camip.service;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.xas.uned.camip.MainActivity;
import org.xas.uned.camip.R;
import org.xas.uned.camip.dao.DeviceTokenDAO;
import org.xas.uned.camip.dao.FollowedAppsDAO;
import org.xas.uned.camip.model.DeviceCheckData;
import org.xas.uned.camip.model.Token;
import org.xas.uned.camip.task.APICallTask;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FirebaseReceiverService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            handle(remoteMessage.getData());
        }
    }

    private void handle(Map<String, String> data){
        if("1".equals(data.get("type"))){
            handleDoubleCheck(data);
        }
    }

    private void handleDoubleCheck(Map<String, String> data){
        System.out.println("********Receiver:"+System.currentTimeMillis());

        //String mac = data.get("mac");
        //String index = data.get("index");
        //Token token = DeviceTokenDAO.getInstance().getDeviceToken(mac, Long.valueOf(index));

        Token token = new Token();
        token.setId(1);
        token.setToken("testdevicetoken");
/*
        DeviceCheckData deviceCheckData = new DeviceCheckData();
        deviceCheckData.setRequestToken(token);
        deviceCheckData.setIp(data.get("toIP"));
        deviceCheckData.setFromIp(data.get("fromIP"));
        deviceCheckData.setMac(mac);

 */
        //new APICallTask(data.get("ip")).execute(deviceCheckData);

        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();

        List<String> followed = FollowedAppsDAO.getInstance().getFollowedApps();
        while(i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
            try {
                //CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                //if(followed.contains(c.toString())){
                if(followed.contains(info.processName)){
                    String mac = data.get("mac");
                    String index = data.get("index");
                    //Token token = DeviceTokenDAO.getInstance().getDeviceToken(mac, Long.valueOf(index));
                    DeviceCheckData deviceCheckData = new DeviceCheckData();
                    deviceCheckData.setRequestToken(token);
                    deviceCheckData.setIp(data.get("toIP"));
                    deviceCheckData.setFromIp(data.get("fromIP"));
                    deviceCheckData.setMac(mac);
                    new APICallTask(data.get("ip")).execute(deviceCheckData);
                    break;
                }
            }catch(Exception e) {
                //Name Not FOund Exception
            }

        }


    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String messageBody) {


/*        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , notificationBuilder.build());
*/
    }
}
