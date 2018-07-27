package com.examle.listadorest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maximopiu18 on 26/07/2018.
 */

public class ActivityItemDescription extends Activity{
    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key=AIzaSyDqLifooX1uMPIHleIFsugUkZPptfU1zzw";
    TextView tv_lat,tv_long,tv_lugar;
    ImageView img_lugar;
    RequestQueue requestQueue;
    String TAG = "ActivityItemDescription";
    String Lugares[];
    int Position_item;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_item);
        Position_item = MainActivity.position_item;
        tv_lat = (TextView)findViewById(R.id.tv_latitud);
        tv_long = (TextView)findViewById(R.id.tv_longitude);
        tv_lugar = (TextView)findViewById(R.id.tv_lugaritem);
        img_lugar = (ImageView)findViewById(R.id.img_lugar);
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
//                    Log.e(TAG,"results: " + jResults.toString());
                    Lugares = new String[jResults.length()];

                    JSONObject jPlaceObj = jResults.getJSONObject(Position_item);
                    JSONObject jGeometry =  jPlaceObj.getJSONObject("geometry");
                    JSONObject jLocation = jGeometry.getJSONObject("location");
                    String path = jResults.getJSONObject(Position_item).getString("icon").toString();
                    String lat = jLocation.getString("lat");
                    String lng = jLocation.getString("lng");
                    //Log.e(TAG,"path: " + path);
                    tv_lat.setText(lat+"");
                    tv_long.setText(lng+"");
                    tv_lugar.setText("Lugar No " + Position_item);
                    Picasso.get().load(path).into(img_lugar);


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
