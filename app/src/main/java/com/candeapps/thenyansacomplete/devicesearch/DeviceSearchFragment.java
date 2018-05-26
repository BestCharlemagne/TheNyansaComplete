package com.candeapps.thenyansacomplete.devicesearch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.candeapps.thenyansacomplete.Objects.AP;
import com.candeapps.thenyansacomplete.Objects.Client;
import com.candeapps.thenyansacomplete.Objects.DataItem;
import com.candeapps.thenyansacomplete.Objects.Radio;
import com.candeapps.thenyansacomplete.R;
import com.candeapps.thenyansacomplete.recyclers.TitleRecycler;
import com.candeapps.thenyansacomplete.utils.CookieParcelable;
import com.candeapps.thenyansacomplete.utils.JsonResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * Created by mchan on 12/03/17.
 */

public class DeviceSearchFragment extends Fragment implements JsonResponse {

    private static String apiToken;
    private static String apiUrl;

    private EditText searchBox;
    private Button searchButton;

    private String searchItem;

    public ArrayList<AP> apArrayList = new ArrayList<>();
    public ArrayList<Client> clientArrayList = new ArrayList<>();


    private ArrayList<LinkedHashMap<String,String>> allAps = new ArrayList<>();
    private ArrayList<LinkedHashMap<String,String>> allClients = new ArrayList<>();


    public ArrayList<ArrayList<ArrayList<LinkedHashMap<String,Object>>>> allInfoObject = new ArrayList<>();

    private static String[] apInfo;
    private static String[] clientInfo ;
    private static String[] radioInfo;
    private static String[] connectedClientsInfo;
    private static String[] radioStrings;

    private View rootView;
    private Boolean checkAp = false;
    private Boolean checkClient = false;
    private boolean hideLayout = true;

    private ProgressBar progressBar;

    private LinearLayout searchLayout;

    public DeviceSearchFragment() {
        //Needs to exist, but we don't need it to do anything.
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiUrl = DeviceSearchActivity.apiUrl;
        apiToken = DeviceSearchActivity.apiToken;

        radioInfo = DeviceSearchActivity.radioInfo;
        connectedClientsInfo = DeviceSearchActivity.connectedClientsInfo;
        apInfo = DeviceSearchActivity.apInfo;
        clientInfo = DeviceSearchActivity.clientInfo;
        radioStrings = DeviceSearchActivity.radioStrings;
    }

    private void decodeDeviceLocationDetails(String apJson, String clientJson) {

        try {
            if(checkAp) {
                JSONObject apData = new JSONObject(apJson);

                JSONArray apInfoArray = apData.getJSONObject("accessPointList").getJSONArray("accessPoints");

                if (!apInfoArray.toString().contains("errors")) {

                    //Get all APs
                    for (int i = 0; i < apInfoArray.length(); i++) {

                        JSONObject apObject = (JSONObject) apInfoArray.get(i);
                        //Get radio information
                        JSONArray radioObject = apObject.getJSONArray("apRadios");

                        ArrayList<Radio> radioList = new ArrayList<>();

                        for(int j = 0; j < radioStrings.length; j++){
                            //Get basic radio information ex: clients connected, channel
                            String radio2 = radioStrings[j];
                            ArrayList<Client> clients2 = new ArrayList<>();
                            LinkedHashMap<String,String> radioInfo2 = new LinkedHashMap<>();
                            for(String s: radioInfo){
                                    String radio = radioObject.getJSONObject(j).getString(s);
                                    radioInfo2.put(s,radio);
                            }

                            //Get associated clients
                            JSONArray object1 = radioObject.getJSONObject(j).getJSONArray("associatedClientList");

                            for(int individual = 0; individual < object1.length(); individual++){
                                JSONObject object2 = object1.getJSONObject(individual);
                                LinkedHashMap<String,String> constructor = new LinkedHashMap<>();
                                for(String string: connectedClientsInfo){
                                    constructor.put(string,object2.get(string).toString());
                                }
                                Client newClient = new Client(constructor,this.getActivity().getApplicationContext());
                                clients2.add(newClient);
                            }
                            radioList.add(new Radio(radio2, clients2, radioInfo2,this.getActivity().getApplicationContext()));
                        }

                        LinkedHashMap<String, String> aps = new LinkedHashMap<>();
                        //Basic AP information
                        for (String s : apInfo) {
                            aps.put(s, apObject.getString(s));
                        }

                        apArrayList.add(new AP(aps,radioList.get(0),radioList.get(1),this.getActivity().getApplicationContext()));
                        allAps.add(aps);
                    }
                }
                else{
                    Toast.makeText(this.getContext(),"Device not found", Toast.LENGTH_SHORT).show();
                    checkAp = false;
                }

            }


            if(checkClient) {
                JSONObject clientData = new JSONObject(clientJson);
                JSONArray clientInfoArray = clientData.getJSONObject("deviceList").getJSONArray("clients");

                if (!clientInfoArray.toString().equals("[]")) {
                    for (int i = 0; i < clientInfoArray.length(); i++) {
                        LinkedHashMap<String, String> client = new LinkedHashMap<>();
                        JSONObject clientObject = (JSONObject) clientInfoArray.get(i);
                        for (String s : clientInfo) {
                            client.put(s, clientObject.getString(s));
                        }
                        clientArrayList.add(new Client(client, this.getActivity().getApplicationContext()));
                        allClients.add(client);
                    }
                    if(apArrayList != null) {
                        for (AP ap : apArrayList) {
                            ap.update(clientArrayList);
                        }
                    }
                }
                else{
                    Toast.makeText(this.getContext(),"Device not found", Toast.LENGTH_SHORT).show();
                    checkClient = false;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_device_search, container, false);

        //Linear Layout holding search
        searchLayout = rootView.findViewById(R.id.main);


        // Save a reference to the search button.
        searchButton = rootView.findViewById(R.id.buttonSearch);

        // Save a reference to the Searched Item in EditText.
        searchBox = rootView.findViewById(R.id.searchBox);
        searchTextWatcher(searchBox, searchButton);
        progressBar = rootView.findViewById(R.id.progress_spinner);

        //Initialize the adapter for the titles with null values
        RecyclerView titleRecyclerView = rootView.findViewById(R.id.title_recycler_view);
        RecyclerView.LayoutManager titleManager = new LinearLayoutManager(getActivity());
        titleRecyclerView.setLayoutManager(titleManager);
        RecyclerView.Adapter titleAdapterClient = new TitleRecycler(null);
        titleRecyclerView.setAdapter(titleAdapterClient);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Populate the searchItem field if there is a pre-existing value.
        if(searchItem != null) {
            searchBox.setText(searchItem);
        }
    }

    // The text watcher will enable the search button when there are 16 characters
    // in the mac address field.
    private void searchTextWatcher(final EditText searchText, final Button search) {
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search.setEnabled(true);
            }

        });
        if(searchText.length() == 0) {
            search.setEnabled(false);
        }
    }

    public void searchDeviceClick(View view) {

        //Reset all lists.
        allAps.clear();
        allClients.clear();
        checkAp = false;
        checkClient = false;
        allInfoObject.clear();
        apArrayList.clear();
        clientArrayList.clear();

        RecyclerView recyclerView = rootView.findViewById(R.id.title_recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Save the searched item.
        searchItem = searchBox.getText().toString();

        // Disable widgets.
        searchButton.setEnabled(false);
        searchBox.setEnabled(false);

        // Create device search task.
        DeviceSearchAsyncTask deviceSearchTask = new DeviceSearchAsyncTask();
        deviceSearchTask.delegate = this;
        deviceSearchTask.execute(apiUrl, apiToken, searchItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                // Handle successful scan
                searchItem = contents;
                searchBox.setText(contents);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    @Override
    public void setJsonResponse(ArrayList<String> response) {
        String apResults = "";
        String clientResults = "";

        if(response != null) {
            try {

                String apListing = response.get(0);
                String clientListing = response.get(1);

                // Inspect the JSON results to see if we got any results about an AP.
                JSONObject apJson = new JSONObject(apListing);
                if (!apJson.toString().contains("errors")) {
                    apResults = apJson.getString("data");
                    checkAp = true;
                }
                // Inspect the JSON results to see if we got any results about an Client.
                JSONObject clientJson = new JSONObject(clientListing);
                if (!clientJson.toString().contains("errors")){
                    clientResults = clientJson.getString("data");
                    checkClient = true;
                }

                if (!checkClient && !checkAp) {
                    // Empty results. We did not find the device.
                    //Reveal the error
                    if(clientJson.toString().contains("errors")) {
                        Toast.makeText(this.getContext(), clientJson.getString("errors"), Toast.LENGTH_LONG).show();
                    }
                    else if(apJson.toString().contains("errors")){
                        Toast.makeText(this.getContext(), apJson.getString("errors"), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this.getContext(), "Device not found", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    ArrayList<CookieParcelable> csParcel = new ArrayList<>();
                    csParcel.add(buildCookieParcelable(apResults));
                    csParcel.add(buildCookieParcelable(clientResults));

                    String apInfo = csParcel.get(0).getData();
                    String clientInfo = csParcel.get(1).getData();

                    decodeDeviceLocationDetails(apInfo, clientInfo);

                    progressBar.setVisibility(View.GONE);
                    setViewList();

                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
        // Re-enable widgets.
        searchButton.setEnabled(true);
        searchBox.setEnabled(true);
    }

    private CookieParcelable buildCookieParcelable(String arg) {
        try {
            return new CookieParcelable(apiUrl,arg);
        } catch(Exception ex) {
            ex.getMessage();
        }
        return null;
    }
    public void setMacAddress(String searchItem) {
        this.searchItem = searchItem;
    }

    private void setViewList() {

        //Complete only if necessary
        if(checkAp || checkClient){
            ArrayList<DataItem> dataItemsList = new ArrayList<>();
            if(checkAp){
                dataItemsList.addAll(apArrayList);
            }
            if (checkClient) {
                dataItemsList.addAll(clientArrayList);
            }
                RecyclerView recyclerView = rootView.findViewById(R.id.title_recycler_view);
                recyclerView.setVisibility(View.VISIBLE);
                RecyclerView.Adapter adapterClient = new TitleRecycler(dataItemsList);
                recyclerView.setAdapter(adapterClient);
                adapterClient.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

