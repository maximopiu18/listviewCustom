package com.examle.listadorest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    int contador=0;
    JSONArray jResults;
    String TAG="Main Activity";
    RequestQueue requestQueue;
     public static  ListView lista;
    public static  ListView lista2;
    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&key=AIzaSyDqLifooX1uMPIHleIFsugUkZPptfU1zzw";
    String Lugares[];
    String LugaresFavoritos[];
    public static ArrayAdapter<String> adapterJson;
    public static int position_item;
    public static  Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = MainActivity.this;
        lista = (ListView)findViewById(R.id.main_Listview);
        lista2 = (ListView)findViewById(R.id.main_Listview2);
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Lista");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Favoritos");
        host.addTab(spec);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {

                Log.e(TAG,"tab id" +  tabId);
                if(tabId.equals("1")) {
                    //
                    ConsultaRest();
                    //EventListenerList();
                }
                else if(tabId.equals("2")) {
                    //
                    llenarListViewFavoritos();
                }
            }});
        ConsultaRest();
        //EventListenerList();
    }

    public void ConsultaRest(){
        requestQueue = Volley.newRequestQueue(this);
        final List<ItemObject> listViewItems = new ArrayList<ItemObject>();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    jResults = jObject.getJSONArray("results");
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
                        listViewItems.add(new ItemObject(Lugares[j]));
                    }
                    //adapterJson = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,Lugares);

                    lista.setAdapter(new CustomListView(MainActivity.this, listViewItems));


                    // llenar listview

                    //lista.setAdapter(adapterJson);

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
    public void EventListenerList(){
        lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                position_item = Integer.valueOf(LugaresFavoritos[position]);
                Intent intent = new Intent(MainActivity.this, ActivityItemDescription.class);
                startActivity(intent);
            }
        });
    }
    public void llenarListViewFavoritos(){

        //ConsultaRestFavoritos();
        consultar_preferencia();
        try{
            lista2.setAdapter(null);
            adapterJson = null;
        }catch (Exception e)
        {
            Log.e("Error","error" + e);
        }

        if(LugaresFavoritos.length>0) {
            adapterJson = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, LugaresFavoritos);
            lista2.setAdapter(adapterJson);
            EventListenerList();
        }
    }

    public void consultar_preferencia(){
        SharedPreferences pref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        final List<ItemObject> listViewItems2 = new ArrayList<ItemObject>();
        int i =0;
        contador=0;
        while(i<jResults.length()) {
            String item = pref.getString("item_" + i, "null");
            if(item.equals("1"))
            {
               contador++;
    //            Log.e("item", "lugar: " + i + " favorito: " + item);

            }
            i++;
        }
        LugaresFavoritos = new String[contador];
        //LugaresFavoritosPositionReal = new String[contador];
//        Log.e("lengt","length" + LugaresFavoritos.length);
        i=0;
        int h =0;
        while(i<jResults.length()) {
            String item = pref.getString("item_" + i, "null");
            if(item.equals("1"))
            {
                //contador++;
                //Log.e("item", "lugar: " + i + " favorito: " + item);
                LugaresFavoritos[h] = ""+ i;
                Log.e("Lugares: F", "Favoritos" + LugaresFavoritos[h]);
                h++;
            }
            i++;
        }
    }

}
