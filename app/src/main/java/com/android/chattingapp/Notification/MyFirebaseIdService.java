package com.android.chattingapp.Notification;

        import android.app.NotificationManager;
        import android.content.Context;

        import androidx.annotation.NonNull;
        import androidx.core.app.NotificationCompat;

        import com.android.chattingapp.R;
        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseIdService extends FirebaseMessagingService {
    String title,body;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    String titlel="caremee";
    String bdy="hi";


        title = titlel;
        body = bdy;

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.caremee)
                        .setContentTitle(title)
                        .setContentText(body);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
//
//
//        String sented = remoteMessage.getData().get("sented");
//        String user = remoteMessage.getData().get("user");
//
//        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
//        String currentUser = preferences.getString("currentuser", "none");
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
////        if (firebaseUser != null && sented.equals(firebaseUser.getUid())){
////            if (!currentUser.equals(user)) {
////
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    sendOreoNotification(remoteMessage);
////                } else {
//                    sendNotification(remoteMessage);
////                }
////            }
////        }
//    }
//
//    private void sendNotification(RemoteMessage remoteMessage) {
//
////        user = remoteMessage.getData().get("user");
////        String icon = remoteMessage.getData().get("icon");
////        String title = remoteMessage.getData().get("title");
////        String body = remoteMessage.getData().get("body");
//
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
//        Intent intent = new Intent(this, MessageActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userid", user);
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(Integer.parseInt(icon))
//                .setContentTitle(title)
//                .setContentText(body)
////                .setAutoCancel(true)
//                .setSound(defaultSound);
////                .setContentIntent(pendingIntent);
//        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int i = 0;
//        if (j > 0){
//            i = j;
//        }
//
//        noti.notify(0, builder.build());
//    }
//
//    private void sendOreoNotification(RemoteMessage remoteMessage){
//        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
//        String title = remoteMessage.getData().get("title");
//        String body = remoteMessage.getData().get("body");
//
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
//        Intent intent = new Intent(this, MessageActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userid", user);
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        OreoNotification oreoNotification = new OreoNotification(this);
//        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
//                defaultSound, icon);
//
//        int i = 0;
//        if (j > 0){
//            i = j;
//        }
//
//        oreoNotification.getManager().notify(i, builder.build());
//
//    }

}

