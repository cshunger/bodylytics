package com.ibm.mobilefirstplatform.clientsdk.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Button;


import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Call;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

public class MainActivity extends Activity {

    private TextView txtVResult = null;

    private MFPPush push = null;
    private MFPPushNotificationListener notificationListener = null;

    private List<String> allTags;
    private List<String> subscribedTags;
    private EditText alertTextBox;
    private Button alertButton;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtVResult = (TextView) findViewById(R.id.display);
        alertButton = (Button) findViewById(R.id.alertButton);
        alertTextBox = (EditText) findViewById(R.id.alertText);

        updateTextView("Starting Push Android Sample..");

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postData(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException ex) {
                            // Something went wrong
                            Log.v("Response Failed", "Bad");
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                // Do what you want to do with the response.
                                Log.v("Response Success", "Good");
                            } else {
                                // Request not successful
                            }
                        }
                    });
                    Log.v("Posted Data", "data psoted");
                } catch (Exception ex){
                    //swallow exception
                    Log.e("MYAPP", "exception", ex);
                }
            }
        }); //End of onClickListenter

        try {
            BMSClient.getInstance().initialize(this, "https://pushingyoutest.mybluemix.net", "11df73b1-b967-4fd7-b7ba-97618427a67d");
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        MFPPush.getInstance().initialize(getApplicationContext());
        push = MFPPush.getInstance();

        push.register(new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String deviceId) {
                updateTextView("Device is registered with Push Service.");
                displayTags();
            }

            @Override
            public void onFailure(MFPPushException ex) {
                updateTextView("Error registering with Push Service...\n"
                        + "Push notifications will not be received.");
            }
        });

        final Activity activity = this;
        notificationListener = new MFPPushNotificationListener() {

                @Override
                public void onReceive(final MFPSimplePushNotification message) {
                showNotification(activity, message);

                }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (push != null) {
            push.listen(notificationListener);
        }
    }

    void showNotification(Activity activity, MFPSimplePushNotification message) {
        Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Notification Received : " + message.toString());
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void sendNotification(Activity activity){

    }

    void displayTags() {
        push.getTags(new MFPPushResponseListener<List<String>>() {
            @Override
            public void onSuccess(List<String> tags) {
                updateTextView("Retrieved Tags : " + tags);
                allTags = tags;
                displayTagSubscriptions();
            }

            @Override
            public void onFailure(MFPPushException ex) {
                updateTextView("Error getting tags..." + ex.getMessage());
            }
        });
    }

    void unregisterDevice(){
        push.unregisterDevice(new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String s) {
                updateTextView("Device is successfully unregistered. Success response is: " + s);
            }

            @Override
            public void onFailure(MFPPushException e) {
                updateTextView("Device unregistration failure. Failure response is: "+ e);
            }
        });
    }

    void unsubscribeFromTags(final String tag) {
        push.unsubscribe(tag, new MFPPushResponseListener<String>() {

            @Override
            public void onSuccess(String s) {
                updateTextView("Unsubscribing from tag");
                updateTextView("Successfully unsubscribed from tag . " + tag);
            }

            @Override
            public void onFailure(MFPPushException e) {
                updateTextView("Error while unsubscribing from tags. " + e.getMessage());
            }

        });
    }

    void displayTagSubscriptions() {
        push.getSubscriptions(new MFPPushResponseListener<List<String>>() {
            @Override
            public void onSuccess(List<String> tags) {
                updateTextView("Retrieved subscriptions : " + tags);
                System.out.println("Subscribed tags are: " + tags);
                subscribedTags = tags;
                subscribeToTag();
            }

            @Override
            public void onFailure(MFPPushException ex) {
                updateTextView("Error getting subscriptions.. "
                        + ex.getMessage());
            }
        });
    }

    void subscribeToTag() {
        System.out.println("subscribedTags: "+ subscribedTags+ "size is: "+subscribedTags.size());
        System.out.println("allTags: " + allTags + "Size is: " + allTags.size());

        if ((allTags.size() != 0)) {
            push.subscribe(allTags.get(0),
                    new MFPPushResponseListener<String>() {
                        @Override
                        public void onFailure(MFPPushException ex) {
                            updateTextView("Error subscribing to Tag1.."
                                    + ex.getMessage());
                        }

                        @Override
                        public void onSuccess(String arg0) {
                            updateTextView("Succesfully Subscribed to: "+ arg0);
                            //unsubscribeFromTags(arg0);
                        }
                    });
        } else {
            updateTextView("Not subscribing to any more tags.");
        }

    }

    public void updateTextView(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtVResult.append(str);
                txtVResult.append("\n");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (push != null) {
            push.hold();
        }

    }

    public Call postData(Callback callback) throws Exception{
        // Create a new HttpClient and Post Header
        OkHttpClient httpClient = new OkHttpClient();
        JSONObject message=new JSONObject();

        try {
            message.put("message", new String("\"message:\"alert\": \""+alertTextBox.getText().toString()+"\""));

        }catch (JSONException ex){
            Log.v("error", "JSONError");
        }

        RequestBody body = RequestBody.create(JSON, "{\"message\": {\"alert\": \""+alertTextBox.getText().toString()+"\"}}");
        Request request = new Request.Builder()
                .url("https://mobile.ng.bluemix.net/imfpush/v1/apps/11df73b1-b967-4fd7-b7ba-97618427a67d/messages")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("appSecret", "b24b1e2d-72fe-455d-b405-4214abae41ef")
                .header("Accept-Language", "en-US")
                .header("Application-mode", "SANDBOX")
                .post(body)
                .build();


        Call call = httpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
