package com.tychestudios.android.buthatkesms.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tychestudios.android.buthatkesms.R;
import com.tychestudios.android.buthatkesms.activities.SmsActivity;
import com.tychestudios.android.buthatkesms.model.Message;

import java.util.List;

/**
 * Created by Harish Vishwakarma on 4/30/2016.
 */
public class MessagesAdapter extends BaseAdapter {
    List<Message> messageList;
    Context context;
    private static LayoutInflater inflater=null;
    public MessagesAdapter(SmsActivity smsActivity, List<Message> messageList) {
        // TODO Auto-generated constructor stub
        this.messageList = messageList;
        context=smsActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView message,date,time;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.smslistrow, null);
        holder.message=(TextView) rowView.findViewById(R.id.message);
        holder.date=(TextView) rowView.findViewById(R.id.date);
        holder.time=(TextView) rowView.findViewById(R.id.time);
        if(holder.message==null) Log.d("MessagesAdapter","message object is null");
        Log.d("MessagesAdapter","Message is: "+messageList.get(position).getSmsBody());
        holder.message.setText(messageList.get(position).getSmsBody());
        return rowView;
    }
}