package com.tallerdeprogramacion.tallerandroidrene;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    //UI
    private TextView textViewDemo;
    private EditText editTextDemo;
    private Button buttonOk;
    private ListView listViewPostalCodes;

    //logic
    private int test;
    private PostalCode[] postalCodes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("taller", "onCreate()");


        textViewDemo = (TextView) findViewById(R.id.textViewDemo);
        editTextDemo = (EditText) findViewById(R.id.editTextDemo);
        buttonOk = (Button) findViewById(R.id.buttonOK);
        listViewPostalCodes = (ListView) findViewById(R.id.listViewPostalCodes);


        textViewDemo.setText("Texto en logica");
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("taller", "cliiiiiick");

                //loadFromInternet();
                loadFromLocal();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("taller", "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("taller", "onResume()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void loadFromLocal(){


        PostalCode cp1 = new PostalCode();
        PostalCode cp2 = new PostalCode();
        PostalCode cp3 = new PostalCode();

        cp1.setPlaceName("Colonia 1");
        cp1.setAdminName3("Leon");
        cp1.setAdminCode2("");

        cp2.setPlaceName("Colonia 2");
        cp2.setAdminName3("Leon");
        cp2.setAdminCode2("");

        cp3.setPlaceName("Colonia 3");
        cp3.setAdminName3("Leon");
        cp3.setAdminCode2("");

        postalCodes = new PostalCode[]{cp1,cp2,cp3};

        PostalCodeAdapter adapter = new PostalCodeAdapter(postalCodes);
        listViewPostalCodes.setAdapter(adapter);
    }


    void loadFromInternet(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://api.geonames.org/postalCodeSearchJSON?postalcode=38060&country=MX&maxRows=10&username=tallerandroid", new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.i("taller", response.toString());

                try {
                    JSONArray array = response.getJSONArray("postalCodes");

                    Gson gson = new Gson();
                    postalCodes = gson.fromJson(array.toString(),PostalCode[].class);

                    for(PostalCode cp : postalCodes){
                        Log.i("taller", cp.getPlaceName());
                    }


                    PostalCodeAdapter adapter = new PostalCodeAdapter(postalCodes);
                    listViewPostalCodes.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.e("taller", throwable.getMessage());
            }
        });
    }



    private class PostalCodeAdapter extends BaseAdapter{

        private PostalCode[] data;

        public PostalCodeAdapter(PostalCode[] dataIn){
            data = dataIn;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public PostalCode getItem(int i) {
            return data[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v = view;

            if(v == null){
                v = getLayoutInflater().inflate(R.layout.row_postalcode,viewGroup, false);
            }

            TextView textViewCPName = (TextView) v.findViewById(R.id.textViewCPName);
            TextView textViewCPCity = (TextView) v.findViewById(R.id.textViewCPCity);

            textViewCPName.setText(data[i].getPlaceName());
            textViewCPCity.setText(data[i].getAdminName3());

            return v;
        }
    }
}
