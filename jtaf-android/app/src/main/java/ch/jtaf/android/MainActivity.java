package ch.jtaf.android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

            final List<Series> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject series = jsonArray.getJSONObject(i);
                list.add(new Series(series));
            }

            ListView seriesList = (ListView) findViewById(R.id.series_list);
            final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            seriesList.setAdapter(adapter);

            seriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                    final Series series = (Series) parent.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, RankingActivity.class);
                    intent.putExtra(Series.class.getName(), series.getId().toString());
                    startActivity(intent);
                }

            });

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<Series> {

        public StableArrayAdapter(Context context, int textViewResourceId, List<Series> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public long getItemId(int position) {
            Series series = getItem(position);
            return series.getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Series series = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_series, parent, false);
            }
            TextView tvId = (TextView) convertView.findViewById(R.id.series_id);
            TextView tvName = (TextView) convertView.findViewById(R.id.series_name);
            tvId.setText(series.getId().toString());
            tvName.setText(series.getName());
            return convertView;
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
