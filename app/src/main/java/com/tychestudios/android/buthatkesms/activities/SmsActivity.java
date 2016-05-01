package com.tychestudios.android.buthatkesms.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tychestudios.android.buthatkesms.R;
import com.tychestudios.android.buthatkesms.adapters.MessagesAdapter;
import com.tychestudios.android.buthatkesms.model.Message;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SmsActivity extends AppCompatActivity {
    List<Message> messages;
    String contact;
    ListView listView;
    EditText sendMessage;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        String jsonMyObject =null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Messages");
            contact = extras.getString("Contact");
        }
        Type listType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        messages = new Gson().fromJson(jsonMyObject, listType);

        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(new MessagesAdapter(this,messages));
        sendMessage = (EditText) findViewById(R.id.sendMessage);
        send = (Button) findViewById(R.id.send);

        sendMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()/160>1){
                    Toast.makeText(SmsActivity.this,s.length()/160+" SMS",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = sendMessage.getText().toString();
                if(!contact.equals("")&&!message.equals("")){
                    SmsManager smsManager = SmsManager.getDefault();
                    if(message.length()>160){
                        ArrayList<String> parts = smsManager.divideMessage(message);
                        smsManager.sendMultipartTextMessage(contact,null,parts,null,null);
                    }
                    else{
                        smsManager.sendTextMessage(contact,null,message,null,null);
                    }

                }
            }
        });

    }
}
