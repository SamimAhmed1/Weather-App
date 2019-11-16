package com.example.weatherapppppp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityField;
    TextView resultWeather;
    String cityToFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        cityField=(EditText)findViewById(R.id.editName);
        resultWeather=(TextView)findViewById(R.id.resultView);

        //"http://api.openweathermap.org/data/2.5/weather?q="+cityToFind+"&APPID=7f249ee14f6471ca2edae2415423c732"


    }

    public void FindWeather(View v){
        cityToFind=cityField.getText().toString();

        try {
            ExecuteTask tasky=new ExecuteTask();
            tasky.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityToFind+"&APPID=7f249ee14f6471ca2edae2415423c732");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class ExecuteTask extends AsyncTask<String, Void, String>{

        String result="";
        URL url;
        HttpURLConnection urlConnection = null;

        @Override
        protected String doInBackground(String... strings) {

            String result="";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url= new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream is=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(is);
                int data=reader.read();

                while (data != -1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                String message="";
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                //Log.i("Website content",weatherInfo);

                JSONArray arr=new JSONArray(weatherInfo);

                for (int i=0;i<arr.length();i++){
                    JSONObject jsonPart=arr.getJSONObject(i);
                    String main="";
                    String description;

                    main=jsonPart.getString("main");
                    description=jsonPart.getString("description");

                    if(main != "" && description !=""){

                        message ="Main : "+ message+ main +"\n"+"Description : "+description + "\r\n";
                    }
                    //Log.i("main",jsonPart.getString("main"));
                   // Log.i("description",jsonPart.getString("description"));
                }
                if (message != ""){

                    resultWeather.setText(message);
                }else {
                    Toast.makeText(MainActivity.this,"An Error Occured",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
}


