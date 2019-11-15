package com.example.newdatafetching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    MyDatabaseHelper myDatabaseHelper;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList = new ArrayList<String>();
    Map<String, List<Double>> map = new HashMap<String, List<Double>>();
    List<Double> list = new ArrayList<Double>();
    String a = "";
    double aa=0.0,bb=0.0;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        listView = (ListView) findViewById(R.id.items_list);
        //getResults();
        //textView.setText(a);
        getResults();
        listView.setOnItemClickListener(this);
    }
    /*
    *
    *
    * This method adds data in the list
     */
    public void addDatas(View view){
        Cursor cursor = myDatabaseHelper.retrieveData();

        if(cursor.getCount()==0) return;
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            //Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
            List<Double> list = new ArrayList<Double>();
            list.add(cursor.getDouble(2));
            list.add(cursor.getDouble(3));
            map.put(cursor.getString(1), list);
        }

        for (Map.Entry<String, List<Double>> entry : map.entrySet()) {

            String key = entry.getKey();

            arrayList.add(key);

        }
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String val =(String) parent.getItemAtPosition(position);
        List<Double> newList = (ArrayList<Double>)map.get(val);

        Double lat = newList.get(0);
        Double longt = newList.get(1);
        //System.out.println(lat+" "+longt);

        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("name",val);
        bundle.putDouble("lat",lat);
        bundle.putDouble("longt",longt);
        intent.putExtras(bundle);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(),val,Toast.LENGTH_SHORT).show();

    }


    /*
    .
    .
    .This method gets all the data from the servers
     */
    public void getResults(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Fetcher>> call = api.fetcher();

        call.enqueue(new Callback<List<Fetcher>>() {
            @Override
            public void onResponse(Call<List<Fetcher>> call, Response<List<Fetcher>> response) {
                List<Fetcher>  fetchers  = response.body();
                for (Fetcher f:fetchers){
                    Log.d("name",f.getName());
                    String nameGet = f.getName().toString();
                    a = a+f.getName().toString()+" ";
                    Gson gson = new Gson();
                    Object obj = f.getLocation();
                    String json = gson.toJson(obj);
                    if (obj!=null){
                        try {
                            JSONObject locationObj = new JSONObject(json);
                            aa = Double.parseDouble(locationObj.getString("latitude"));
                            bb = Double.parseDouble(locationObj.getString("longitude"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //aa = (String) jsonObject.get("latitude");
                        //bb = (String) jsonObject.get("longitude");
                    }
                    else {
                        aa = 0.0;
                        bb = 0.0;
                    }
                    long rowId = myDatabaseHelper.insertData(nameGet,aa,bb);
                    if(rowId>0){
                        //list.add(aa);
                        //list.add(bb);
                        //map.put(nameGet, list);
                        //a = a+" "+aa+" "+bb+"\n";
                        //textView.setText(a);*/
                        Toast.makeText(getApplicationContext(),"Added data",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(getApplicationContext(),"failed to add",Toast.LENGTH_SHORT).show();
                    }


                }

            }

            @Override
            public void onFailure(Call<List<Fetcher>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
