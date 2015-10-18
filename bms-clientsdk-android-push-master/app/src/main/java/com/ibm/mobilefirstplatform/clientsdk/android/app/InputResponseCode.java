package com.ibm.mobilefirstplatform.clientsdk.android.app;

import android.app.Activity;
import android.content.Context;
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

    private void showMessage(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
