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
    String mapURL = "http://192.168.137.1/showMap.php";
    BitmapDescriptor bitmapd;
    LatLng[] sydn= new LatLng[30];
    private GoogleMap mMap;
    double lat, lng;
    String chk,nam,ID,NAME,ITEM;
    RequestQueue requestQueue;
    ArrayList<String> points = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String value = getIntent().getExtras().getString("itemSelected");
        StringTokenizer st = new StringTokenizer(value," ");
        ID=st.nextToken();


        if(ID.equals("Confirmed:"))
        {

            ID=st.nextToken();
             Toast.makeText(getBaseContext(),"id is"+ID,Toast.LENGTH_LONG).show();

            //NAME=st.nextToken();
           // st.nextToken();
           // ITEM = st.nextToken();
        }
        else
        {
           // StringTokenizer st2 = new StringTokenizer(st.nextToken(),":");
            //NAME=st.nextToken();
            //st.nextToken();
            //ITEM = st.nextToken();
        }
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
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mapURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray aitem = response.getJSONArray("maps");
                    for (int i = 0; i < aitem.length(); i++) {
                        JSONObject item = aitem.getJSONObject(i);
                        String id = item.getString("dID");
                        String nam = item.getString("dName");
                        String lat = item.getString("latitude");
                        String lng = item.getString("longitude");
                        String chk = item.getString("confirm");
                        String phn = item.getString("dPhone");
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
                        /*
                            Bug ID-35 related to testcase Quarks-4
                            Bug Status :  Solved
                            Bug : Show the name along with the contact number of a donor.
                            Solution : Contact number fetched into variable phn and added it to title of Marker.
                        */
//Toast.makeText(getBaseContext(),nam,Toast.LENGTH_LONG).show();
                        //String x = nam.split(":");
                        mMap.addMarker(new MarkerOptions().position(sydn[i])
                                .title(nam+" "+phn)).setIcon(bitmapd);
                        /*
                        Bug ID-36:1 related to Improvements
                        Bug Status : Solved
                        Suggestion : Zoom in on selected donor.
                        Solution : match name and donor ID with those of the donor selected, if they match then zoom in to that
                        particular donor, zoom level 20.
                         */

                        if(id.equals(ID)) {
                            Toast.makeText(getBaseContext(),"checkedID is:"+id,Toast.LENGTH_LONG).show();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydn[i], 20));
                        }
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
    }
}
