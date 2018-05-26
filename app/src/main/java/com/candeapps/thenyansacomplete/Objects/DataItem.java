package com.candeapps.thenyansacomplete.Objects;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DataItem{
    LinkedHashMap<String,String> item;
    ArrayList<DataItem> subGroup = null;
    String title;
    String locale;

    private int originalPositionSub = -2;
    private int originalPositionBody = -2;
    public String getSubtitle(Context context, int position){
        if(originalPositionSub == -2){
            originalPositionSub = position;
        }
        return context.getResources().getString(context.getResources().getIdentifier(item.keySet().toArray()[position-originalPositionSub].toString()+locale, "string", context.getPackageName()));
    }
    public String getBody(Context context, int position){
        if(originalPositionBody == -2){
            originalPositionBody = position;
        }

        return item.values().toArray()[position-originalPositionSub].toString();
    }

    public int size(){
        return 0;
    }
    public String toString(){
        if(subGroup != null){
            return item.toString() + subGroup.toString();
        }
        return item.toString();
    }
    public String getTitle(){
        return title;
    }
    public LinkedHashMap<String,String> getItem(){
        return item;
    }
    public int selfSize(){
        return item.size();
    }
    public ArrayList<DataItem> getSubGroup(){
        return subGroup;
    }
}