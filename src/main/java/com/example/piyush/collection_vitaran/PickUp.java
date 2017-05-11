package com.example.piyush.collection_vitaran;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class PickUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String mapURL = "http://192.168.1.2/showMap.php";
    String updateURL = "http://192.168.1.2/updatecollection.php";
    RequestQueue requestQueue;
    String selected="";
    String[] points;
    ArrayAdapter<String> adapter;
    Spinner smap;
    int count;
    EditText editText;
    TextView tv,tv_total;
    Button back,chkout,cnfrm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        smap = (Spinner)findViewById(R.id.spinner);
        smap.setOnItemSelectedListener(this);
        editText = (EditText)findViewById(R.id.editText);
        cnfrm = (Button)findViewById(R.id.button5);
        tv = (TextView)findViewById(R.id.textView);
        tv_total = (TextView)findViewById(R.id.textView2);
        back = (Button)findViewById(R.id.button3);
        chkout =(Button)findViewById(R.id.button4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PickUp.this,MapsActivity.class);
                startActivity(i);
            }
        });

        chkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Successfully collected all items",Toast.LENGTH_LONG).show();
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
                    for (int i = 0; i < aitem.length(); i++) {
                        JSONObject item = aitem.getJSONObject(i);
                        String name  = item.getString("dName");
                        String id = item.getString("dID");
                        String lat = item.getString("latitude");
                        String lng = item.getString("longitude");
                        String chk = item.getString("confirm");
                        if(chk.equals("0")){
                            points[count]=(id+" "+name);
                            count++;
                        }

                        //tv.append(points[i]+"\n");
                    }

                    // Create an ArrayAdapter using the string array and a default spinner layoutu
                      adapter = new ArrayAdapter(PickUp.this,android.R.layout.simple_spinner_item,points);
// Specify the layout to use when the list of choices appears
                      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                      smap.setAdapter(adapter);

                } catch (Exception e) {
                    //tv.setText("inside catch");
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
                Toast.makeText(getBaseContext(),"SUCCESSFULLY UPDATED", Toast.LENGTH_LONG).show();

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
        Toast.makeText(getBaseContext(),"onResumeCalled",Toast.LENGTH_LONG).show();
                    // Create an ArrayAdapter using the string array and a default spinner layoutu
                   // adapter = new ArrayAdapter(PickUp.this,android.R.layout.simple_spinner_item,points);
// Specify the layout to use when the list of choices appears
                  //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                  //  smap.setAdapter(adapter);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getBaseContext(),"started",Toast.LENGTH_LONG).show();
        selected = parent.getItemAtPosition(position).toString();
        editText.setText(selected);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getBaseContext(), "Nothing selected: " , Toast.LENGTH_LONG).show();
    }
}
