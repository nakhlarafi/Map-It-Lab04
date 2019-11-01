package com.example.newdatafetching;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String a = "";
    String aa="",bb="";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    a = a+f.getName().toString()+" ";
                    Gson gson = new Gson();
                    Object obj = f.getLocation();
                    String json = gson.toJson(obj);
                    if (obj!=null){
                        try {
                            JSONObject locationObj = new JSONObject(json);
                            aa = locationObj.getString("latitude");
                            bb = locationObj.getString("longitude");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //aa = (String) jsonObject.get("latitude");
                        //bb = (String) jsonObject.get("longitude");
                    }
                    else {
                        aa = "N/A";
                        bb = "N/A";
                    }
                    a = a+" "+aa+" "+bb+"\n";
                    //textView.setText(a);
                    //Log.d("latitude",f.getLocation().toString());
                    //Log.d("latitude",f.getLatitude().toString());
                }
                textView.setText(a);
            }

            @Override
            public void onFailure(Call<List<Fetcher>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
