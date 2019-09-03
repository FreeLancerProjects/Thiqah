package com.creative.share.apps.thiqah.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.creative.share.apps.thiqah.R;
import com.creative.share.apps.thiqah.activities_fragments.activity_notification.NotificationActivity;
import com.creative.share.apps.thiqah.models.FireBaseNotModel;
import com.creative.share.apps.thiqah.models.UserModel;
import com.creative.share.apps.thiqah.preferences.Preferences;
import com.creative.share.apps.thiqah.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.newInstance();
    private Map<String,String> map;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size()>0)
        {
             map= remoteMessage.getData();
            for (String key : map.keySet())
            {
                Log.e("Key :"+key,"__ value :"+map.get(key));
            }

            if (getSession().equals(Tags.session_login))
            {
                String phone = getUserData().getUser().getMobile_number();
                String to_phone = map.get("to_phone");

                if (phone.equals(to_phone))
                {
                    manageNotification();

                }


            }
        }

    }

    private void manageNotification() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            sendNotificationNew();
        }else
            {
                sendNotificationOld();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationNew()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo_512);

        String channel_id = "my_channel_02";
        CharSequence channel_name = "my_channel_name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(channel_id,channel_name,importance);
        mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        mChannel.enableLights(true);
        mChannel.setShowBadge(true);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setChannelId(channel_id);
        builder.setSmallIcon(R.drawable.ic_not);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setAutoCancel(true);
        builder.setLargeIcon(bitmap);
        builder.setContentTitle(map.get("title"));
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .setBigContentTitle(map.get("title"))
                .bigText(map.get("body"))

        );

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("notification",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager!=null)
        {
            FireBaseNotModel fireBaseNotModel = new FireBaseNotModel(true);
            EventBus.getDefault().post(fireBaseNotModel);

            manager.createNotificationChannel(mChannel);
            manager.notify("thiqah",0,builder.build());

        }
    }

    private void sendNotificationOld()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo_512);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_not);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setAutoCancel(true);
        builder.setLargeIcon(bitmap);
        builder.setContentTitle(map.get("title"));
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .setBigContentTitle(map.get("title"))
                .bigText(map.get("body"))

        );

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("notification",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager!=null)
        {
            FireBaseNotModel fireBaseNotModel = new FireBaseNotModel(true);
            EventBus.getDefault().post(fireBaseNotModel);

            manager.notify("thiqah",0,builder.build());

        }
    }

    private UserModel getUserData()
    {
        return preferences.getUserData(this);
    }

    private String getSession()
    {
        return preferences.getSession(this);
    }
}
