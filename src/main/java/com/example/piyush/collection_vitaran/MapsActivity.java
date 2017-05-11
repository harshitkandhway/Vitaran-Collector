package com.example.piyush.collection_vitaran;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String mapURL = "http://192.168.1.2/showMap.php";
    BitmapDescriptor bitmapd;
    LatLng[] sydn= new LatLng[30];
    private GoogleMap mMap;
    double lat, lng;
    String chk,nam;
    RequestQueue requestQueue;
    ArrayList<String> points = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //original
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mapURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray aitem = response.getJSONArray("maps");
                    for (int i = 0; i < aitem.length(); i++) {
                        JSONObject item = aitem.getJSONObject(i);
                        String nam = item.getString("dName");
                        String lat = item.getString("latitude");
                        String lng = item.getString("longitude");
                        String chk = item.getString("confirm");
                        if (chk.equals("1")) {
                bitmapd
                        = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN);
            } else {
                bitmapd
                        = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED);

            }
                        sydn[i] = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                        mMap.addMarker(new MarkerOptions().position(sydn[i])
                                .title(nam)).setIcon(bitmapd);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydn[i]));
                        //points.add(i + " " + lat + " " + lng + " " + chk + "\n");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", "ERROR");
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);


//map2

        //original


        mMap = googleMap;

        //  for (int i = 0; i < points.size(); i++) {
        //  StringTokenizer st = new StringTokenizer(points.get(i), " ");
        //  nam = st.nextToken();
        //  lat = Double.parseDouble(st.nextToken());
        //  lng = Double.parseDouble(st.nextToken());
        //  chk = st.nextToken();
        //  Toast.makeText(getBaseContext(),"latitude :"+lat+"\n"+"longitude :"+lng,Toast.LENGTH_LONG).show();
        // Add a marker in Sydney and move the camera
        //  LatLng donor = new LatLng(lat, lng);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //    bitmapd
        //             = BitmapDescriptorFactory.defaultMarker(
        //             BitmapDescriptorFactory.HUE_YELLOW);
        //    if (chk.equals("0")) {
       /*         bitmapd
                        = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN);
            } else {
                bitmapd
                        = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED);

            }

            mMap.addMarker(new MarkerOptions().position(donor).title("nam").icon(bitmapd));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(donor));

        }}*/
    }
}
