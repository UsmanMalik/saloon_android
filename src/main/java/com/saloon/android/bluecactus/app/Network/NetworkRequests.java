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
    static String url = "http://192.168.43.108:3000/api/";
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
        String divisions_url = url + "divisions";


        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, divisions_url, null, new Response.Listener<JSONArray>() {
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

    public boolean setAppointment(final String time,final String date, final String details, final String userID){

        Log.i("Set Appointment: " , time + " " + date + " " + details);
        String appointments_url = url + "appointments";


        StringRequest postRequest = new StringRequest(Request.Method.POST, appointments_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Appointment: ", response);
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
                params.put("time", time);
                params.put("date", date);
                params.put("details", details);
                params.put("user_id", userID); // Hardcoded need to get from shardprefs
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

        return false;
    }

    public boolean registerUser(final User user){

        String register_url = url + "register";


        StringRequest postRequest = new StringRequest(Request.Method.POST, register_url,
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

                            editor.putString("register_result", result);
                            editor.putString("id", user.getString("id"));
//                            editor.putString("first_name", user.getString("first_name"));
//                            editor.putString("email", user.getString("email"));


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

    public boolean loginUser(final String email, final String password){

        String login_url = url + "sessions";


        StringRequest postRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("Login Response", response);
                            JSONObject jsonResponse = new JSONObject(response);

                            String api_key = jsonResponse.getString("api_key");
                            JSONObject user = jsonResponse.getJSONObject("user");
                            Log.d("first_name", user.getString("first_name"));
                            SharedPreferences sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("api_key", api_key);
                            editor.putString("id", user.getString("id"));
                            editor.putString("first_name", user.getString("first_name"));
                            editor.putString("email", user.getString("email"));


                            editor.commit();


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
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

        return false;
    }

    public boolean registerUserGCM(final String regId, final String userId){

        String register_gcm_url = url + "register/register_device";


        StringRequest postRequest = new StringRequest(Request.Method.POST, register_gcm_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("Register Device: ", response);

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
                params.put("user_id", userId);
                params.put("reg_id", regId);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);




        return false;
    }

}


