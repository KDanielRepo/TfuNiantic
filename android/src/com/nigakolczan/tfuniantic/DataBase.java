package com.nigakolczan.tfuniantic;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBase {
    public DataBase(){
        //connection();
        downloadJson("http://localhost/testing/db.php");
    }
    /*public void connection(){
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost/javas?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        try{
            Class.forName(driver);
            java.sql.Connection connection = DriverManager.getConnection(url);
            String query = "SELECT * FROM pokemon";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                Object name = resultSet.getObject("name");
                Object damage = resultSet.getObject("damage");
                System.out.println(name + " " + damage);
            }
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/

    public void downloadJson(final String url){
        class GetJson extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                System.out.println(s);
                /*try {
                    //loadIntoListView(s);
                }catch (JSONException e){
                    e.printStackTrace();
                }*/
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine())!=null){
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch (Exception e){
                    return null;
                }
            }
        }
        GetJson getJson = new GetJson();
        getJson.execute();
    }
    private void loadIntoListView(String json)throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] tekst = new String[jsonArray.length()];
        for (int i = 0; i<jsonArray.length();i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            tekst[i] = obj.getString("name")+" "+obj.getString("price");
        }
    }
    public void getPokemons(){

    }
    public void getMoves(){

    }

}
