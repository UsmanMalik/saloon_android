package com.saloon.android.bluecactus.app.Network;

/**
 * Created by usman on 10/9/16.
 */
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.saloon.android.bluecactus.R;
import com.saloon.android.bluecactus.app.UI.MainActivity;
import com.saloon.android.bluecactus.app.Utils.Locator;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM Demo";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
//                for (int i = 0; i < 5; i++) {
//                    Log.i(TAG, "Working... " + (i + 1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

                String operationKey = extras.getString("operation_key");
                Log.i(TAG, "Operation Key: " + operationKey);

                if (operationKey.equals("operation_approved_appointment")) {
                    Log.i(TAG, "operation_approved_appointment: " + extras.toString());
                    if (extras.containsKey("approved_appointment")){
                        sendNotification(extras.getString("approved_appointment"));
                    }
                }
                if (operationKey.equals("operation_decline_appointment")) {
                    Log.i(TAG, "operation_decline_appointment: " + extras.toString());
                    if (extras.containsKey("declined_appointment")){
                        sendNotification(extras.getString("declined_appointment"));
                    }
                }

                if (operationKey.equals("operation_get_location")) {
                    getLocation();
                }

                getLocation(); // TEST


                    // Post notification of received message.
//                sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
                        .setContentTitle("Spin Saloon")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    private void getLocation(){
        Locator locator=new Locator(this);
        locator.getLocation(Locator.Method.NETWORK_THEN_GPS,listener);
    }

    private Locator.Listener listener =new Locator.Listener() {
        @Override
        public void onLocationFound(Location location) {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());

            Log.d("latitude", String.valueOf(location.getLatitude()));
            Log.d("longitude", String.valueOf(location.getLongitude()));

            SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String userId = (shared.getString("id", ""));

            Log.d("Location userid: ", userId );

            if (userId != null && !userId.isEmpty() && latitude != null && !latitude.isEmpty() ){

                NetworkRequests networkRequests = new NetworkRequests(getApplicationContext());
                networkRequests.sendUserLocationToServer(latitude,longitude, userId);

            }

        }

        @Override
        public void onLocationNotFound() {
//            savingFormToDB();
        }
    };
}
