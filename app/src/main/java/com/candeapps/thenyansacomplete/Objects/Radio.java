package com.candeapps.thenyansacomplete.Objects;

import android.content.Context;
import android.support.annotation.NonNull;

import com.candeapps.thenyansacomplete.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Radio extends DataItem implements Iterable<Client>{
    private ArrayList<Client> clients;
    private String radio;
    private Context context;

    //Must be 5GHz or 2.4GHz
    public Radio(String radio,ArrayList<Client> clients,LinkedHashMap<String,String> radioInfo, Context context){
        this.context = context;
        this.radio = radio;
        this.clients = clients;
        super.item = radioInfo;
        super.locale = "_radio";
        super.title = context.getResources().getString(R.string.radio_title) + radio;
    }

    @NonNull
    public Iterator<Client> iterator() {
        return new MyIterator();
    }

    @Override
    public int size() {
        return super.item.size() + 1;
    }

    @Override
    public String toString(){
        return clients.toString();
    }
    @Override
    public ArrayList<DataItem> getSubGroup(){
        ArrayList<DataItem> dataClients = new ArrayList<>();
        dataClients.addAll(clients);
        super.subGroup = dataClients;
        return subGroup;
    }
    public void updateClient(ArrayList<Client> clientArrayList){
        for(Client client: clients){
            client.updateSelf(clientArrayList);
        }

    }
    public Client getClient(String uuid){
        for(Client client: clients){
            if(client.item.containsValue(uuid)){
                return client;
            }
        }
        return null;
    }
    public String getRadio(){
        return radio;
    }
    public void setRadioInfo(String key, String entry){
        super.item.put(key,entry);
    }
    public void addAll(ArrayList<Client> clients){
        this.clients.addAll(clients);
    }
    public void add(Client client){
        clients.add(client);
    }
    public void remove(Client client){
        clients.remove(client);
    }
    public int numClients(){
        return clients.size();
    }
        class MyIterator implements Iterator<Client> {

            private int index = 0;

            public boolean hasNext() {
                return index < clients.size();
            }

            public Client next() {
                return clients.get(index++);
            }

            public void remove() {
                throw new UnsupportedOperationException("not supported yet");

            }
        }

}
