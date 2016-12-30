/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kratav.tinySurprise.notification;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.gcm.GcmListenerService;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.Splash;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * This is class which is used to handle the payload which comes from GCM Server.Play with this carefully
 */

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private String title, subtitle, bigMsg, smallIcon, largeIcon, collapse_key;
    PendingIntent pendingIntent;
    private Notification notification;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //[{subtitle=hello, smallIcon=NA, sound=1, title=Hiii, vibrate=1, largeIcon=NA, message=Hello Ashutosh, collapse_key=type2}]
        title = data.getString("title");
        subtitle = data.getString("subtitle");
        bigMsg = data.getString("message");
        smallIcon = data.getString("smallIcon");
        largeIcon = data.getString("largeIcon");
        collapse_key = data.getString("collapse_key");
        //Log.d(TAG, "bundle data: " + data.toString());
        Log.d(TAG, "data: " + data.toString());


        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }


        Intent dialogShowIntent = new Intent(QuickstartPreferences.SHOW_ALERT_DIALOG);
        dialogShowIntent.putExtra("message", data.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(dialogShowIntent);
        prepareNotification();
        // [END_EXCLUDE]

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (collapse_key.equals("type1")) { //Image Type
                notification = notificationType1();
            } else if (collapse_key.equals("type2")) { // TextType
                notification = notificationType2();
            }
        } else {
            notification = preAPI16();
        }

        if(notification != null) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notification);
        }
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void prepareNotification() {
        Intent intent = new Intent(this, Splash.class);
        intent.putExtra("NotificationMessage", bigMsg);
        //intent.putExtra("notificationId",notificationId);
        //intent.putExtra("showDetails",true);
        //intent.putExtra("isActive",isRunning());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public boolean isRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }

    private Bitmap getMeBitmap(String url) {
        try {
            return Glide.with(this).load(url).asBitmap().into(200, 200).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private Notification notificationType2() {
            Bitmap smallIconBitmap = getMeBitmap(smallIcon);
            if (smallIconBitmap == null) return null;

            // Constructs the Builder object.
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle(title)
                            .setTicker(title)
                            .setSubText(subtitle)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setLargeIcon(smallIconBitmap)
                            .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(bigMsg))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            builder.setContentIntent(pendingIntent);
            return builder.build();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification notificationType1() {
        Bitmap smallIconBitmap = getMeBitmap(smallIcon);
        if (smallIconBitmap == null) return null;
        Bitmap largeImageBitmap = getMeBitmap(largeIcon);
        if (largeImageBitmap == null) return null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setTicker(title)
                .setSubText(subtitle)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(smallIconBitmap)
                .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(largeImageBitmap)).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setContentIntent(pendingIntent);
        return builder.build();

    }

    private Notification preAPI16() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(subtitle)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        return notificationBuilder.build();
    }
}
