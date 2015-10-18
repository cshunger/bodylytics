package com.ibm.mobilefirstplatform.clientsdk.android.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class InputResponseCode extends Activity {

    private static final Integer EXTRA_EVENT_ID = 2;
    private Integer coherencyCode;
    private TextView lblPinValue;
    private EditText txtBoxPinInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_response_code);

        lblPinValue = (TextView) findViewById(R.id.lblPinValue);
        txtBoxPinInput = (EditText) findViewById(R.id.txtBox);

        Random r = new Random();
        coherencyCode = r.nextInt(8999 - 1001)+1000;
        lblPinValue.setText("PIN#: " + coherencyCode.toString());
    }

    public void onBtnOK_Click(View view) {
        if (Objects.equals(txtBoxPinInput.getText().toString(), coherencyCode.toString())) {
            this.showMessage("Matches!");
        } else {
            this.showMessage("Does not match!");
        }
    }

    public void onBtnCallEMS_Click(View view) {

    }

    private void showMessage(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    //com.ibm.mobilefirstplatform.clientsdk.android.app.InputResponseCode

//    private void notificationMethod(){
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this.getApplicationContext())
//                        .setContentTitle("title")
//                        .setContentText("text");
//        // Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(this, InputResponseCode.class);
//
//        // The stack builder object will contain an artificial back stack for the
//        // started Activity.
//        // This ensures that navigating backward from the Activity leads out of
//        // your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(InputResponseCode.this);
//        // Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // mId allows you to update the notification later on.
//        int mId=001;;
//        mNotificationManager.notify(mId, mBuilder.build());
//    }
}
