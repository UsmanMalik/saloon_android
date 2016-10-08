package com.saloon.android.bluecactus.app.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saloon.android.bluecactus.app.Models.Division;
import com.saloon.android.bluecactus.app.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by usman on 10/4/16.
 */
public class NetworkRequests {

    Context context;
    static String url = "http://httpbin.org/get?site=code&network=tutsplus";
    static  String RESULT_TAG = "Json Result: ";
//    public static final String MyPREFERENCES = "MyPrefs" ;
//    SharedPreferences sharedpreferences;


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
        String temp_url = "http://192.168.43.108:3000/api/divisions";


        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, temp_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            for (int i=0 ; i< response.length(); i++){
                                JSONObject jsonObject =  response.getJSONObject(i);
                                Division division = new Division(jsonObject.getString("main"), jsonObject.getString("description"),jsonObject.getString("avatar_url_original"), jsonObject.getString("avatar_url_thumb"),jsonObject.getString("avatar_url_medium") );
                                Log.i("response object " + i + " :"  ,jsonObject.getString("main"));
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

    public boolean setAppointment(final String time,final String date, final String details){

        Log.i("Set Appointment: " , time + " " + date + " " + details);
        String temp_url = "http://192.168.43.108:3000/api/appointments";


        StringRequest postRequest = new StringRequest(Request.Method.POST, temp_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response).getJSONObject("result");

                            System.out.println("Site: "+jsonResponse.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("time", time);
                params.put("date", date);
                params.put("details", details);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

        return false;
    }

    public boolean registerUser(final User user){

        String temp_url = "http://192.168.43.108:3000/api/register";


        StringRequest postRequest = new StringRequest(Request.Method.POST, temp_url,
                new Response.Listener<String>() {
                    boolean  requestResult =false;
                    @Override
                    public void onResponse(String response) {
                        try {
                          JSONObject jsonResponse = new JSONObject(response);
                            String result = jsonResponse.getString("result");
                            JSONObject user = jsonResponse.getJSONObject("user");
                            Log.d("first_name", user.getString("first_name"));

                            SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("result", result);
                            editor.putString("first_name", user.getString("first_name"));
                            editor.putString("email", user.getString("email"));
                            editor.putString("password", user.getString("password"));


                            editor.commit();


                            System.out.println("Site: "+ response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("first_name", user.getFirst_name());
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

        return true; // need to implement callback
    }

}


