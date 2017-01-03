package ch.jtaf.jtafandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Intent intent = getIntent();
        String seriesId = intent.getStringExtra(Series.class.getName());

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("https://jtaf-simas.rhcloud.com/jtaf/series_ranking.html?id=" + seriesId);
    }
}
