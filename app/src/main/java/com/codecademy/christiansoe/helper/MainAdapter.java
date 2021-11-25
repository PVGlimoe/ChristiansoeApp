package com.codecademy.christiansoe.helper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.codecademy.christiansoe.activity.RouteActivity;
import com.codecademy.christiansoe.model.Route;
import com.codecademy.christiansoe.model.Theme;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainAdapter extends BaseExpandableListAdapter {
    //Initialize variable
    List<String> listGroup;
    HashMap<String, ArrayList<String>> listChild;

    public MainAdapter(List<String> listGroup, HashMap<String, ArrayList<String>> listChild) {
        this.listGroup = listGroup;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        //Return group list size
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //Return child list
        return listChild.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        //Return group item
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //Return child item
        return listChild.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //Initialize view
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_expandable_list_item_1
                        ,parent, false);


        //Initialize and assign variable
        TextView textView = convertView.findViewById(android.R.id.text1);
        //Initialize string
        String sGroup = String.valueOf(getGroup(groupPosition));
        //Set text on text view
        textView.setText(sGroup);
        //Set text style bold
        textView.setTypeface(null, Typeface.BOLD);
        //Set text color
        textView.setTextColor(Color.WHITE);
        //Set backgroundcolor
        textView.setBackgroundColor(Color.parseColor("#B0635A5A"));



        //Return view
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Initialize view
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_selectable_list_item
                        ,parent, false);

        //Initialize and assign variable
        TextView textView = convertView.findViewById(android.R.id.text1);
        //Initialize string
        String sChild = String.valueOf(getChild(groupPosition, childPosition));
        //Set text on textview
        textView.setText(sChild);
        textView.setBackgroundColor(Color.parseColor("#28635A5A"));


        //Set on click listener
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent intent = new Intent(view.getContext(), RouteActivity.class);
                intent.putExtra("clickedRoute", sChild);
                view.getContext().startActivity(intent);

            }
        });

        //Return view
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
