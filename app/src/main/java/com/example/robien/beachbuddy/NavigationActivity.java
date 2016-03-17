package com.example.robien.beachbuddy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

/**
 * Created by Calvin on 2/24/2016.
 */
public class NavigationActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;
    private ListView listView;

    Button search;
    EditText searchClass;
    String coursename;
    String JSON_String;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.drawerList);
        search = (Button)findViewById(R.id.search);
        searchClass = (EditText)findViewById(R.id.classSearch);
        //JSON_String = (EditText)findViewById(R.id.classSearch)
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



/*
    public void searchByName(View v) {
        // edit text and button initalizations
        coursename = searchClass.getText().toString();
        String method = "search";
        BackgroundTask bt = new BackgroundTask(this);
        bt.execute(method, coursename);
        finish();
    }
    */

    public void getJSON(View v) {

        new SearchBackground().execute();
    }

        class SearchBackground extends AsyncTask<Void, Void, String> {
            String json_url;
            String cName = searchClass.getText().toString();

            @Override
            protected void onPreExecute() {
                json_url = "http://cecs492beachbuddy.site88.net/searchTest.php";;
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String sData = URLEncoder.encode("cName", "UTF-8")+ "=" + URLEncoder.encode(cName, "UTF-8");
                    bufferedWriter.write(sData);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                    StringBuilder stringBuilder = new StringBuilder();
                    while((JSON_String = bufferedReader.readLine()) != null) {
                        stringBuilder.append(JSON_String + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                }
                catch(MalformedURLException e) {
                    e.printStackTrace();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }

            @Override
            protected void onPostExecute(String result) {
                TextView resultView = (TextView)findViewById(R.id.resultView);
                resultView.setText(result);
            }
        }

}
