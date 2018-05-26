package com.candeapps.thenyansacomplete.Objects;

import android.content.Context;

import com.candeapps.thenyansacomplete.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AP extends DataItem {

    //5 GHz Radio
    private Radio five;
    //2.4 GHz Radio
    private Radio two;

    private Context context;

    public AP(LinkedHashMap<String, String> self, Radio five, Radio two, Context context) {
        this.context = context;
        this.five = five;
        this.two = two;
        super.item = self;
        super.locale = "_ap";

        ArrayList<DataItem> radios = new ArrayList<>();
        radios.add(five);
        radios.add(two);
        super.subGroup = radios;
        super.title = context.getResources().getString(R.string.ap_title) + self.get("apName");
    }

    @Override
    public int size() {
        return super.item.size() + 1;
    }

    public Radio getFive() {
        return five;
    }

    public Radio getTwo() {
        return two;
    }

    public int numClients() {
        return two.numClients() + five.numClients();
    }

    public void update(ArrayList<Client> clients) {
        two.updateClient(clients);
                five.updateClient(clients);
    }
//    public AP(ArrayList<ArrayList<LinkedHashMap<String, Object>>> allInfo){
//        LinkedHashMap<String, Object> converter = new LinkedHashMap<>();
//        for (String string: allInfo.get(0).get(0).keySet()) {
//            converter.put(string+"_ap", allInfo.get(0).get(0).get(string));
//        }
//        self = converter;
//        self.put("title",self.get("apName"));
//    }


//    public String isConnectedTo(String mac){
//        if(five.containsKey("connectedClients") && five.get("connectedClients") instanceof ArrayList){
//            ArrayList<String> list = (ArrayList<String>) five.get("connectedClients");
//            if(list.contains(mac)) {
//                return "5ghz";
//            }
//        }
//        else if(two.containsKey("connectedClients") && two.get("connectedClients") instanceof ArrayList){
//            ArrayList<String> list = (ArrayList<String>) two.get("connectedClients");
//            if(list.contains(mac)) {
//                return "2_4ghz";
//            }
//        }
//
//        return "error";
//    }


}
