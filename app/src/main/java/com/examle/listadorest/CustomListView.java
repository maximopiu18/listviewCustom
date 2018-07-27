package com.examle.listadorest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class CustomListView extends BaseAdapter {
    public static int position;
    private LayoutInflater lInflater;
    private List<ItemObject> listStorage;
    ViewHolder listViewHolder;

    public CustomListView(Context context, List<ItemObject> customizedListView) {
        lInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.item_listview, parent, false);

            listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.textview);
            listViewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.checkBox.setChecked(false);
        listViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (listViewHolder.checkBox.isChecked()){
                    listViewHolder.checkBox.setChecked(false);
                }else{
                    listViewHolder.checkBox.setChecked(true);
                }*/
            }
        });
        listViewHolder.textInListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("position","positon" + position);
               // Log.e(TAG,"position" + position);
                MainActivity.position_item = position;
                Intent intent = new Intent(MainActivity.c,ActivityItemDescription.class);
                MainActivity.c.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder{

        TextView textInListView;
        CheckBox checkBox;
    }
}