//package com.walhalla.vibro;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
//
//import com.walhalla.ui.DLog;
//
//public class FirebaseUtil {
//
//    public static void printKey() {
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        if (task.getException() != null) {
//                            DLog.handleException(task.getException());
//                        }
//                        return;
//                    }
//
//                    // Get new Instance ID token
//                    final InstanceIdResult result = task.getResult();
//                    if (result != null) {
//                        String token = task.getResult().getToken();
//                        DLog.d("Token --> " + token);
//                    }
//                });
//    }
//
//    public static void subscribe(String topic) {
//
//        FirebaseMessaging.getInstance().subscribeToTopic(topic)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "subscribed";
//                        if (!task.isSuccessful()) {
//                            msg = "subscribe_failed";
//                        }
//                        DLog.d(msg);
//                    }
//                });
//    }
//}
