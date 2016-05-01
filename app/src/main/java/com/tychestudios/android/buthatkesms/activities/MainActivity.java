package com.tychestudios.android.buthatkesms.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tychestudios.android.buthatkesms.R;
import com.tychestudios.android.buthatkesms.adapters.ContactMessageAdapter;
import com.tychestudios.android.buthatkesms.model.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView contactMessageList;
    static List<Message> smsList;
    public static boolean checkInbox = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog smsDialog = new Dialog(MainActivity.this);
                    smsDialog.setContentView(R.layout.dialog);
                    smsDialog.setTitle("Write SMS");

                    final Button sendSms = (Button) smsDialog.findViewById(R.id.btnSend);
                    final EditText smsMessage = (EditText) smsDialog.findViewById(R.id.chatMessage);
                    final AutoCompleteTextView searchContact = (AutoCompleteTextView) smsDialog.findViewById(R.id.searchContact);
                    List<String> contactList = getAllContactNames();
                    Log.d("MainActivity", Arrays.toString(contactList.toArray()));
                    smsMessage.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if(s.length()/160>1){
                                Toast.makeText(MainActivity.this,s.length()/160+" SMS",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    if(searchContact!=null) {
                        searchContact.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.single_contact,
                                R.id.tv_ContactName,
                                getAllContactNames()));
                    }

                    sendSms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String number = searchContact.getText().toString();
                            String message = smsMessage.getText().toString();
                            if(!number.equals("")&&!message.equals("")){
                                SmsManager smsManager = SmsManager.getDefault();
                                if(message.length()>160){
                                    ArrayList<String> parts = smsManager.divideMessage(message);
                                    smsManager.sendMultipartTextMessage(number,null,parts,null,null);
                                }
                                else{
                                    smsManager.sendTextMessage(number,null,message,null,null);
                                }

                            }

                        }
                    });

                    smsDialog.show();
                }
            });
        }

        if(checkInbox){
            smsList = readInbox();
        }
        HashMap<String, List<Message>> contactMessages = new HashMap<>();

        for(Message message: smsList){
            List<Message> lm = contactMessages.get(message.getSmsFrom());
            if(lm==null)lm = new ArrayList<Message>();
            lm.add(message);
            contactMessages.put(message.getSmsFrom(),lm);
        }

        ArrayList<String> contacts = new ArrayList<>();
        for(String key: contactMessages.keySet()){
            contacts.add(key);
        }

        contactMessageList = (ListView) findViewById(R.id.listView);
        contactMessageList.setAdapter(new ContactMessageAdapter(this,contacts,contactMessages));

    }

    private List<Message> readInbox(){
        Log.d("MainActivity","readInbox is called.");
        List<Message> smsList = new ArrayList<Message>();

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c= getContentResolver().query(uri, null, null ,null,null);
        startManagingCursor(c);

        if(c!=null) {
            if (c.moveToFirst()) {
                for (int i = 0; i < c.getCount(); i++) {
                    Message sms = new Message();
                    sms.setSmsBody(c.getString(c.getColumnIndexOrThrow("body")));
                    sms.setSmsFrom(c.getString(c.getColumnIndexOrThrow("address")));
                    String date = (c.getString(c.getColumnIndexOrThrow("date")));
                    Long timestamp = Long.parseLong(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(timestamp);
                    Date finaldate = calendar.getTime();
                    sms.setTime(finaldate);
                    smsList.add(sms);

                    c.moveToNext();
                }
            }

        }
        else {
            Toast.makeText(this,"Content not available on this device.",Toast.LENGTH_LONG).show();
        }
        checkInbox = false;
        Collections.sort(smsList, new Comparator<Message>() {
            public int compare(Message m1, Message m2) {
                return m1.getTime().compareTo(m2.getTime());
            }
        });
        return smsList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<String> getAllContactNames() {
        List<String> lContactNamesList = new ArrayList<String>();
        try {
            Cursor lPeople = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (lPeople != null) {
                while (lPeople.moveToNext()) {
                    lContactNamesList.add(lPeople.getString(lPeople.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                }
            }
        } catch (NullPointerException e) {
            Log.e("getAllContactNames()", e.getMessage());
        }
        return lContactNamesList;
    }
}
