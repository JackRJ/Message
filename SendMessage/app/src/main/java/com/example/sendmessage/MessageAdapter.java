package com.example.sendmessage;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 帅郑 on 2018/8/12.
 */

public class MessageAdapter extends ArrayAdapter<Person>{
    private int resourceId;
    public MessageAdapter(Context context, @LayoutRes int resource, List<Person> people){
        super(context,resource,people);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Person person = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)view.findViewById(R.id.mname);
            viewHolder.phonenumber = (TextView)view.findViewById(R.id.mphone);
            viewHolder.content = (TextView)view.findViewById(R.id.mcontent);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.name.setText(person.getName());
        viewHolder.phonenumber.setText(person.getPhoneNumber());
        viewHolder.content.setText(person.getContent());
        return view;
    }

    public class ViewHolder{
        TextView name;
        TextView phonenumber;
        TextView content;
    }
}
