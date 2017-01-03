package ch.jtaf.jtafandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            LoadSeriesTask loadSeriesTask = new LoadSeriesTask();
            AsyncTask asyncTask = loadSeriesTask.execute();
            JSONArray jsonArray = (JSONArray) asyncTask.get();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject series = jsonArray.getJSONObject(i);
                sb.append(series.get("name"));
                sb.append("\n");
            }

            TextView seriesList = (TextView) findViewById(R.id.textView);
            seriesList.setText(sb.toString());

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    private class LoadSeriesTask extends AsyncTask<Object, Object, JSONArray> {

        @Override
        protected JSONArray doInBackground(Object... params) {
            try {

                URL url = new URL("https://jtaf-simas.rhcloud.com/jtaf/res/spaces/series");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONArray(response.toString());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
