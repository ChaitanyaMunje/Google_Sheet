package com.gtappdevelopers.googlesheet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewItem extends AppCompatActivity {

    ListView listView;
    ListAdapter adapter;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        listView = (ListView) findViewById(R.id.lv_items);

        getItems();
    }

    private void getItems() {

        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.googleusercontent.com/macros/echo?user_content_key=5N8qQBgplhJbdEmgQaF3EgA3NMC3UkwKNA68F-doyzMDHlagF-oODuYTJBN0EiX-2W0LZPqgbg79BW3c8JK0qMicHzyWmEsmOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa1GhPSVukpSQTydEwAEXFXgt_wltjJcH3XHUaaPC1fv5o9XyvOto09QuWI89K6KjOu0SP2F-BdwUrGj46PUSjop7MRieMiATaAujFx6n3R2NsVIiWpjdRVyvebAjiJsmOpt5IyRdRang430Be-9krzI0AnO3yg7ckg&lib=MnrE7b2I2PjfH799VodkCPiQjIVyBAxva",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void parseItems(String jsonResposnce) {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("Items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String itemName = jo.getString("Item_Name");
                String brand = jo.getString("Brand");
                String price = jo.getString("Price");


                Log.e("DATA","data = "+itemName+brand+price);

                HashMap<String, String> item = new HashMap<>();
                item.put("itemName", itemName);
                item.put("brand", brand);
                item.put("price", price);

                list.add(item);


            }
        } catch (JSONException e) {
            Toast.makeText(this, "error = "+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("Error","eeror msg = "+e);

        }


        adapter = new SimpleAdapter(this, list, R.layout.list_item_row,
                new String[]{"itemName", "brand", "price"}, new int[]{R.id.tv_item_name, R.id.tv_brand, R.id.tv_price});


        listView.setAdapter(adapter);
        loading.dismiss();
    }
}
