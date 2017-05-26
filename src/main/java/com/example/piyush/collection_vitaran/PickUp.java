package com.example.piyush.collection_vitaran;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static com.android.volley.Request.*;
/*

 Few features yet to be implemented in the collection app which are missing are as follows :

    1)  When a donor is selected from the spinner list and is confirmed pickup ,donor must be removed instantaneously from the list ,but currently it is
        removed when app runs another time.

    2)  When we select a donor from the spinner it is displayed in the non editable textbox,it must be focused as the current pin on google map ,instead
        the last donor which present in the collctionunit table is zoomed upon.

    3)  The final functionality of checkout button is missing currently,because the database are not yet integrated,once done this button will make sure
        that all successful donations are removed from the collection unit table.


 */

public class PickUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    //URL of the php file in the server that is responsible for sending data from 'collectionunit' table to the android app
    String mapURL = "http://192.168.137.1/showMap.php";
    //URL of the php file in the server that is responsible for updating data when items are colected by the collection unit from respective donors
    String updateURL = "http://192.168.137.1/updatecollection.php";
    RequestQueue requestQueue;
    String selected;
    String[] points;//String array used for populating spinner
    ArrayAdapter<String> adapter;
    android.support.v7.widget.AppCompatSpinner smap;
    int count;
    EditText editText;
    TextView tv,tv_total;//tv_total represents total number of lines or rows present in the collectionunit table in vitaran database
    Button back,chkout,cnfrm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        smap = (AppCompatSpinner) findViewById(R.id.spinner);
        smap.setOnItemSelectedListener(this);
        editText = (EditText)findViewById(R.id.editText);
        cnfrm = (Button)findViewById(R.id.button5);
        tv = (TextView)findViewById(R.id.textView);
        tv_total = (TextView)findViewById(R.id.textView2);
        back = (Button)findViewById(R.id.button3);
        ///chkout =(Button)findViewById(R.id.button4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PickUp.this,MapsActivity.class);
               try{ if(selected.equals(null)){
                    Toast.makeText(getBaseContext(),"Server Connection Error..!",Toast.LENGTH_LONG).show();
                }else{
                i.putExtra("itemSelected",selected);
                startActivity(i);
                }}
               catch (Exception e){Toast.makeText(getBaseContext()," ServerError ",Toast.LENGTH_LONG).show();}
            }
        });


        requestQueue = Volley.newRequestQueue(this);
        //tv.setText("outside JSON");
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST,mapURL ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                     count=0;
                    //tv.setText("inside try");
                    JSONArray aitem = response.getJSONArray("maps");
                    tv_total.append(" "+aitem.length());
                    points = new String[aitem.length()];
                    int counter =0;
                    for (int i = 0; i < aitem.length(); i++) {
                        JSONObject item = aitem.getJSONObject(i);
                        String name  = item.getString("dName");
                        String id = item.getString("dID");
                        String lat = item.getString("latitude");
                        String lng = item.getString("longitude");
                        String chk = item.getString("confirm");
                        if(chk.equals("0"))
                            points[i]=(id+" "+name);
                            else {
                            /*
                               Bug ID-36:3 related to Improvements
                               Bug Status : Solved
                               Suggestion : display Confirmed : 2 abc instead of --
                               Solution : checking if confirm field is 1 (confirm).
                            */
                            points[i] = ("Confirmed: "+id + " " + name );
                            counter++;
                            }

                        //tv.append(points[i]+"\n");
                    }

                    // Create an ArrayAdapter using the string array and a default spinner layoutu
                      adapter = new ArrayAdapter(PickUp.this,android.R.layout.simple_spinner_item,points);
// Specify the layout to use when the list of choices appears
                      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

                      smap.setAdapter(adapter);
                    if(counter==aitem.length())
                    {
                        Toast.makeText(getBaseContext(),"All donations have been picked up",Toast.LENGTH_LONG).show();
                        cnfrm.setVisibility(View.INVISIBLE);
                        editText.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    //tv.setText("inside catch");
                    Toast.makeText(getBaseContext(),"Error in connecting to the server",Toast.LENGTH_LONG).show();
                    //points[0]="No donors are present";
                    cnfrm.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    adapter = new ArrayAdapter(PickUp.this,android.R.layout.simple_spinner_item,points);
// Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                    smap.setAdapter(adapter);
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


        cnfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringTokenizer str = new StringTokenizer(selected," ");
                final String id2 = str.nextToken();
                final String res = "1";
                Toast.makeText(getBaseContext(),"Successfully updated", Toast.LENGTH_LONG).show();

                StringRequest stringRequest = new StringRequest(Method.POST, updateURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameter = new HashMap<String,String>();
                        parameter.put("dID",id2);
                        parameter.put("confirm",res);
                        return parameter;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getBaseContext(),"onResumeCalled",Toast.LENGTH_LONG).show();
                    // Create an ArrayAdapter using the string array and a default spinner layoutu
                   // adapter = new ArrayAdapter(PickUp.this,android.R.layout.simple_spinner_item,points);
// Specify the layout to use when the list of choices appears
                  //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                  //  smap.setAdapter(adapter);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       try{// Toast.makeText(getBaseContext(),"started",Toast.LENGTH_LONG).show();
        selected = parent.getItemAtPosition(position).toString();

        editText.setText("Selected Donor : "+selected);}
       catch (Exception e)
       {
           editText.setText("All Items Have been Picked Up");
       }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getBaseContext(), "Nothing selected: " , Toast.LENGTH_LONG).show();
    }
}
