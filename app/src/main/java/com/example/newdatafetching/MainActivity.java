package com.example.newdatafetching;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    MyDatabaseHelper myDatabaseHelper;
    Map<String, List<Double>> map = new HashMap<String, List<Double>>();
    List<Double> list = new ArrayList<Double>();
    String a = "";
    double aa=0.0,bb=0.0;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        textView = (TextView)findViewById(R.id.text_appear);
        //getResults();
        //textView.setText(a);
    }


    public void getResults(View view){
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
                        list.add(aa);
                        list.add(bb);
                        map.put(nameGet, list);
                        a = a+" "+aa+" "+bb+"\n";
                        textView.setText(a);
                        Toast.makeText(getApplicationContext(),"Added data",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"failed to add",Toast.LENGTH_SHORT).show();
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
