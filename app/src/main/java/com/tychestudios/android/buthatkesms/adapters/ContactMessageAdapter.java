package com.tychestudios.android.buthatkesms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tychestudios.android.buthatkesms.R;
import com.tychestudios.android.buthatkesms.activities.MainActivity;
import com.tychestudios.android.buthatkesms.activities.SmsActivity;
import com.tychestudios.android.buthatkesms.model.Message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Harish Vishwakarma on 4/30/2016.
 */
public class ContactMessageAdapter extends BaseAdapter implements Filterable {
    ArrayList<String> contactlist;
    LinkedHashMap<String, List<Message>> contactMessages;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public ContactMessageAdapter(MainActivity mainActivity, ArrayList<String> contactlist, LinkedHashMap<String, List<Message>> contactMessages) {
        // TODO Auto-generated constructor stub
        this.contactlist = contactlist;
        this.contactMessages = contactMessages;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return contactlist.size();
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

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.contactlistrow, null);
        holder.tv = (TextView) rowView.findViewById(R.id.contactNumber);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView);
        holder.tv.setText(contactlist.get(position));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SmsActivity.class);
                intent.putExtra("Messages", new Gson().toJson(contactMessages.get(contactlist.get(position))));
                intent.putExtra("Contact", holder.tv.getText().toString());
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint.toString().length() > 0) {
                    List<String> founded = new ArrayList<String>();
                    for (Message item : MainActivity.smsList) {
                        if (item.toString().toLowerCase().contains(constraint)) {
                            founded.add(item.getSmsFrom());
                        }
                    }

                    result.values = founded;
                    result.count = founded.size();
                } else {
                    result.values = contactlist;
                    result.count = contactlist.size();
                }
                return result;


            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                contactlist.clear();
                for (String item : (ArrayList<String>) results.values) {
                    contactlist.add(item);
                }
                notifyDataSetChanged();

            }

        };
    }
}
