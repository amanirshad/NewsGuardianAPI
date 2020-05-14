package com.amanirshad.news;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String URL_API = "https://content.guardianapis.com/search";
//    private static final String API_KEY_VALUE = "?api-key=3b5639ba-6744-4a9d-9900-0c398edf1149&show-tags=contributor";
private static final String API_KEY_VALUE = "3b5639ba-6744-4a9d-9900-0c398edf1149";

    private static final String API_KEY_TAG = "api-key";
    private static final String SECTION_TAG = "section";
    private static final String DATE_TAG = "from-date";
    private static final String CONTRIBUTOR_TAG = "show-tags";
    private static final String CONTRIBUTOR_VALUE = "contributor";

    private NewsAdapter adapter;

    ListView listView;
    TextView tvErrorMessage;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);
        tvErrorMessage = findViewById(R.id.mainactivity_error_message_textview);
        progressBar = findViewById(R.id.mainactivity_indicator_progressbar);

        setUI();

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            listView.setVisibility(View.GONE);
            tvErrorMessage.setText(R.string.no_internet_message);
            tvErrorMessage.setVisibility(View.VISIBLE);
        }


    }

    private void setUI() {
        adapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = adapter.getItem(position);
                view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl())));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String category = preferences.getString("order_by_tag", getString(R.string.tag_list_default_value));

        String date = preferences.getString("order_by_date", getString(R.string.date_list_2018_default_value));

        Uri uri = Uri.parse(URL_API);
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter(API_KEY_TAG, API_KEY_VALUE);
        builder.appendQueryParameter(SECTION_TAG, category);
        builder.appendQueryParameter(CONTRIBUTOR_TAG, CONTRIBUTOR_VALUE);
        builder.appendQueryParameter(DATE_TAG, date);


        return new NewsLoader(this, builder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        progressBar.setVisibility(View.GONE);

        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else {
            tvErrorMessage.setText(R.string.no_news);
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}