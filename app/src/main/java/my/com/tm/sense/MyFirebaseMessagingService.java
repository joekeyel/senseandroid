package my.com.tm.sense;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
             //   scheduleJob();
            } else {
                // Handle message within 10 seconds
               // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


            sendNotification(remoteMessage.getNotification().getBody());


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private void sendNotification(String msg) {


        Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);




            if(FirebaseAuth.getInstance().getCurrentUser() == null) {

                Intent in = new Intent(getApplicationContext(), LoginActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(in);

                PendingIntent contentIntenti = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                int numMessages = 0;
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        this).setSmallIcon(R.mipmap.senseicon)
                        .setContentTitle("SENSE Notification")
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setAutoCancel(false)
                        .setContentText(msg)
                        .setNumber(++numMessages)
                        .setContentIntent(contentIntenti)
                        .setOnlyAlertOnce(false)
                        .setTicker("SENSE Notification");


                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                Log.d(TAG, "Notification sent successfully.");
            }
            else{

                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                in.putExtra("Notif", msg);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(in);

                PendingIntent contentIntenti = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                int numMessages = 0;
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        this).setSmallIcon(R.mipmap.senseicon)
                        .setContentTitle("SENSE Notification")
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setAutoCancel(false)
                        .setContentText(msg)
                        .setNumber(++numMessages)
                        .setContentIntent(contentIntenti)
                        .setOnlyAlertOnce(false)
                        .setTicker("SENSE Notification");


                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                Log.d(TAG, "Notification sent successfully.");

            }







    }
}
