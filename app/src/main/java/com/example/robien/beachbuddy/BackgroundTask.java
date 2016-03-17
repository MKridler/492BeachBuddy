package com.example.robien.beachbuddy;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Robien on 2/17/2016.
 */
public class BackgroundTask extends AsyncTask <String, Void, String> {

    Context ctx;
    AlertDialog alertDialog;
    public static String savedEmail = "";
    BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Result...");
    }

    @Override
    protected String doInBackground(String... params) {
        String acc_reg_url = "http://cecs492beachbuddy.site88.net/reg3.php"; // can use local device ip address
        String class_reg_url = "http://cecs492beachbuddy.site88.net/classreg2.php";  // TBD
        String login_url = "http://cecs492beachbuddy.site88.net/login3.php";  // TBD
        String search_url = "http://cecs492beachbuddy.site88.net/search.php"; // TBD
        String method = params[0];

        /**
         *  CHANGED it from just "register" to "accountregister" for registering your account (personal info)
         *  Have two different registers
         *  1) Personal info (account)
         *  2) Classes entered in the class registration form
         */
        if (method.equals("register")) {
            String sFirst = params[1];
            String sLast = params[2];
            //String id = params[3];
            String sEmail = params[3];
            String sPass = params[4];
            try {
                URL url = new URL(acc_reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                //httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("sFirst", "UTF-8") + "=" + URLEncoder.encode(sFirst, "UTF-8") + "&" +
                        URLEncoder.encode("sLast", "UTF-8") + "=" + URLEncoder.encode(sLast, "UTF-8") + "&" +
                        //URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + " & " +
                        URLEncoder.encode("sEmail", "UTF-8") + "=" + URLEncoder.encode(sEmail, "UTF-8") + "&" +
                        URLEncoder.encode("sPass", "UTF-8") + "=" + URLEncoder.encode(sPass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                //httpURLConnection.connect();
                httpURLConnection.disconnect();
                return "Registration Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(method.equals("classregister")) { // for registering your class(es)

            String cID = params[1];
            String cName = params[2];
            String cDate = params[3];
            String cInstructor = params[4];
            //String sEmail = params[5];
            String sEmail = savedEmail;
            try {
                URL url = new URL(class_reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                //httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("cID", "UTF-8") + "=" + URLEncoder.encode(cID, "UTF-8") + "&" +
                        URLEncoder.encode("cName", "UTF-8") + "=" + URLEncoder.encode(cName, "UTF-8") + "&" +
                        URLEncoder.encode("cDate", "UTF-8") + "=" + URLEncoder.encode(cDate, "UTF-8") + "&" +
                        URLEncoder.encode("cInstructor", "UTF-8") + "=" + URLEncoder.encode(cInstructor, "UTF-8") + "&" +
                        URLEncoder.encode("sEmail", "UTF-8") + "=" + URLEncoder.encode(sEmail, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String response = "";
                String line  = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response+= line;
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response;
                //IS.close();
                //httpURLConnection.connect();
                //httpURLConnection.disconnect();
                // return "Class Added";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(method.equals("login"))
        {
            String sEmail = params[1];
            String sPass = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("sEmail","UTF-8")+"="+URLEncoder.encode(sEmail,"UTF-8")+"&"+
                        URLEncoder.encode("sPass","UTF-8")+"="+URLEncoder.encode(sPass,"UTF-8");;
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line  = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        else if(method.equals("search")) {
            String sClasses = params[1];
            try{
                //JSONArray Jarray = new JSONArray(result);
                URL url = new URL(search_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String sData = URLEncoder.encode("sClasses","UTF-8");
                bufferedWriter.write(sData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line  = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    sb.append(line +"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // code for searching through DB to find names of people who are associated with the
            // searched class
            // select firstname,lastname from student where class = "?????" I'm not sure
            // My database professor kinda sucked..
        }

        else{
            savedEmail = params[0];
            return "saved";
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        //TextView resultView = (TextView)findViewById(R.id.resultView);

        //if(result.contains("Results")){
        //    resultView.setText(result);
        //}


        if(result.equals("Registration Success..."))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }

        if(result.contains("Login Success"))
        {
            Intent intent=new Intent(ctx, RegClassActivity.class);
            ctx.startActivity(intent);

        }
        if(result.contains("Login Failed"))
        {
            alertDialog.setMessage(result);
            alertDialog.show();

        }
        if(result.contains("Class Added"))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ctx, RegClassActivity.class);
            ctx.startActivity(intent);
        }
        if(result.contains("Error: Email does not exist "))
        {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ctx, RegClassActivity.class);
            ctx.startActivity(intent);
        }
        if(result.contains("saved"))
        {
            Intent intent=new Intent(ctx, RegClassActivity.class);
            ctx.startActivity(intent);
            //alertDialog.setMessage(result);
            //asdalertDialog.show();
            //Intent intent=new Intent(ctx, LoginActivity.class);
            //asdctx.startActivity(intent);
        }
        else{}

    }

}