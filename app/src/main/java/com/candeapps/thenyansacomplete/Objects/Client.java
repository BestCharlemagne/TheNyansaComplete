package com.candeapps.thenyansacomplete.Objects;

import android.content.Context;

import com.candeapps.thenyansacomplete.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Client extends DataItem {
    private String radio;
    private Context context;

    public Client(LinkedHashMap<String,String> clientInfo,Context context){
        this.context = context;
        super.item = clientInfo;
        super.locale = "_client";
        setTitle();
    }

    @Override
    public int size(){
        return item.size();
    }

    public void updateSelf(ArrayList<Client> clientArrayList){
        for(Client client: clientArrayList){
            if(client.get("uuid").equals(item.get("uuid"))){
                client.setTitle();
                super.item = client.item;
            }
        }
    }

    private void setTitle(){
        String titleString = context.getResources().getString(R.string.client_title);
        if(item.containsKey("userName") && !item.get("userName").contains("null")){
            super.title = titleString + item.get("userName");
        }
        if(item.containsKey("uuid")&& !item.get("uuid").contains("null")){
            super.title = titleString + item.get("uuid");
        }
        else{
            super.title = "Client";
        }
    }

    public void setRadio(String radio){
        this.radio = radio;
    }

    public String getRadio(){
        return radio;
    }

    public String get(String key){
        return item.get(key);
    }
}