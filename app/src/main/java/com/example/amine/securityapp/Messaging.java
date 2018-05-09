package com.example.amine.securityapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.amine.securityapp.R.id.textView;

public class Messaging extends AppCompatActivity {

    Button buttonSend;
    EditText contactiid, editMessage;
    IntentFilter intentFilter;



    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView inTxt = (TextView) findViewById(R.id.receivetext);
            inTxt.setText(intent.getExtras().getString("message"));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Messaging");


        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        buttonSend = (Button) findViewById(R.id.buttonSend);
        contactiid = (EditText) findViewById(R.id.contactiid);
        editMessage = (EditText) findViewById(R.id.editMessage);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myMsg = editMessage.getText().toString();
                String theNumber = contactiid.getText().toString();
                sendMsg (theNumber, myMsg);

            }
        });
    }

    protected void sendMsg (String theNumber, String myMsg){

        String SENT = "Messgage sent";
        String DELIVERED = "Message delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, sentPI, deliveredPI);
    }

    @Override
    protected void onResume(){
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause(){
        unregisterReceiver(intentReceiver);
        super.onPause();
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
