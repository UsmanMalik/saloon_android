package com.saloon.android.bluecactus.app.Network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.saloon.android.bluecactus.app.Models.Division;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by usman on 10/4/16.
 */
public class NetworkRequests {

    Context context;
    static String url = "http://httpbin.org/get?site=code&network=tutsplus";
    static  String RESULT_TAG = "Json Result: ";

    public NetworkRequests(Context context) {
        this.context = context;

    }

    public NetworkRequests() {
    }

    public void fetchDummyData(){
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            response = response.getJSONObject("args");
                            String site = response.getString("site"),
                                    network = response.getString("network");
                            System.out.println("Site: "+site+"\nNetwork: "+network);
                            Log.d(RESULT_TAG, "onResponse: " + response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(context).add(jsonRequest);

    }

    public ArrayList<Division> getDivisions(){

        final ArrayList<Division> divisionList = new ArrayList<Division>();
        String temp_url = "http://192.168.43.108:3000/api/category/complete_data";


        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, temp_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            for (int i=0 ; i< response.length(); i++){
                                JSONObject jsonObject =  response.getJSONObject(i);
                                Division division = new Division(jsonObject.getString("title"), jsonObject.getString("avatar_url_medium"), jsonObject.getString("description"));
                                Log.i("response object " + i + " :"  ,jsonObject.getString("title"));
                                divisionList.add(division);
                                Log.i("Division adding..", divisionList.size()+"");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(context).add(jsonRequest);
        Log.i("Network Class: ", divisionList.size()+"");

        return divisionList;
    }

}
