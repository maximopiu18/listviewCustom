package com.examle.listadorest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    String TAG="Main Activity";
    RequestQueue requestQueue;
     public static  ListView lista;
    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key=AIzaSyDqLifooX1uMPIHleIFsugUkZPptfU1zzw";
    String Lugares[];
    public static ArrayAdapter<String> adapterJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista = (ListView)findViewById(R.id.main_Listview);
        ConsultaRest();
    }

    public void ConsultaRest(){
        requestQueue = Volley.newRequestQueue(this);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray jResults = jObject.getJSONArray("results");
                    Log.e(TAG,"results: " + jResults.toString());

                    Lugares = new String[jResults.length()];
                    for (int j = 0; j < jResults.length(); j++) {
                        Lugares[j] = "Lugar : " +j;
                        JSONObject jPlaceObj = jResults.getJSONObject(j);
                        JSONObject jGeometry =  jPlaceObj.getJSONObject("geometry");
                        JSONObject jLocation = jGeometry.getJSONObject("location");

                        String lat = jLocation.getString("lat");
                        String lng = jLocation.getString("lng");
                        //Log.e(TAG,"lat" + lat);
                        //Log.e(TAG,"lng" + lng);
                        Log.e(TAG,"Lugar: " +Lugares[j]);
                    }
                    adapterJson = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,Lugares);
                    lista.setAdapter(adapterJson);

                } catch (JSONException e) {
                    Log.e("error","error" + e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error"+ error);
            }
        });
        requestQueue.add(getRequest);
    }

}
