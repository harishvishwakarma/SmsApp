package com.tychestudios.android.buthatkesms.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

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
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        String jsonMyObject =null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("Messages");
        }
        Type listType = new TypeToken<ArrayList<Message>>() {
        }.getType();
        messages = new Gson().fromJson(jsonMyObject, listType);

        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(new MessagesAdapter(this,messages));

    }
}
