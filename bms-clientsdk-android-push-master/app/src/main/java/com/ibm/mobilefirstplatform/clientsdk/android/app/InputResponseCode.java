package com.ibm.mobilefirstplatform.clientsdk.android.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class InputResponseCode extends Activity {

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
        coherencyCode = r.nextInt(9999 - 1001);
        lblPinValue.setText("PIN#: " + coherencyCode.toString());
    }

    public void onBtnOK_Click(View view) {
        if (Objects.equals(txtBoxPinInput.getText().toString(), coherencyCode.toString())) {
            this.showMessage("Matches!");
        } else {
            this.showMessage("Does not match!");
        }
    }

//    public void onBtnCallEMS_Click(View view) {
//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(icon, "This is a test", when);
//
//        Intent notificationIntent = new Intent(context, HomeActivity.class);
//
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent intent = PendingIntent.getActivity(context, 0,
//                notificationIntent, 0);
//
//        notification.setLatestEventInfo(context, title, message, intent);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify(0, notification);
//    }

    private void showMessage(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
