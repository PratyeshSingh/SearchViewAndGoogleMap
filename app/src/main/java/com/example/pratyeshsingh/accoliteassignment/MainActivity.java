package com.example.pratyeshsingh.accoliteassignment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    private String URL = "http://services.groupkt.com/state/search/IND?text=";//pradesh

    ArrayList<Content> listData = new ArrayList<>();

    MyAdapter<Content> mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        mMyAdapter = new MyAdapter<>(this, listData);
        listView.setAdapter(mMyAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered

                //Toast.makeText(MainActivity.this, newText, Toast.LENGTH_LONG).show();

                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                reloadList(query);
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    private void reloadList(String searchText) {
        final Hashtable<String, String> header = new Hashtable<>();
        final String url = URL + searchText;

        AsyncTaskExecuter.AsyncTaskExecuterListener asyncTaskExecuterListener = new AsyncTaskExecuter.AsyncTaskExecuterListener() {

            @Override
            public void notifyRespons(Object[] result) {
                listData.clear();

                if (result != null && (Integer) result[0] == 200) {

                    JSONObject da = (JSONObject) result[1];
                    try {
                        JsonParser.parseData(da.getJSONObject("RestResponse").getJSONArray("result"), listData);
                        mMyAdapter.refresh();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.internal_error), Toast.LENGTH_LONG).show();

            }
        };

        if (checkInternetConnection(this)) {
            new AsyncTaskExecuter(this, asyncTaskExecuterListener, url, "", "GET", header, true).execute();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection_found), Toast.LENGTH_LONG).show();
        }
    }


    public static boolean checkInternetConnection(Activity activity) {

        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search_IP) {
            startActivity(new Intent(this, Search.class));
            return false;
        } else
            return super.onOptionsItemSelected(item);
    }
}
