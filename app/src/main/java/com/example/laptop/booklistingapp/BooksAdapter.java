package com.example.laptop.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class BooksAdapter extends ArrayAdapter<Custom> {

    public BooksAdapter(Context context){
        super(context,0);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Custom cust=getItem(position);
        TextView mTitle = (TextView) listItemView.findViewById(R.id.id1);
        mTitle.setText(cust.getTitle());
        TextView mAuthor = (TextView) listItemView.findViewById(R.id.id2);
        mAuthor.setText(cust.getAuthor());
        TextView mPublisher = (TextView) listItemView.findViewById(R.id.id3);
        mPublisher.setText(cust.getPublisher());
        TextView mdescription=(TextView)listItemView.findViewById(R.id.id4);
        mdescription.setText(cust.getDescc());
        return listItemView;
    }

}
