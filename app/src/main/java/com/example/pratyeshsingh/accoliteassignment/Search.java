package com.example.pratyeshsingh.accoliteassignment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class Search extends AppCompatActivity {

    String URL = "http://geo.groupkt.com/ip/111.93.41.242/json";
//    http://geo.groupkt.com/ip/172.217.3.14/json

    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    private void init(double latitude, double longitude) {


//        // latitude and longitude
//        double latitude = 13.912817;// 13.98204587;
//        double longitude = 100.549049;// 100.85586548;


        // getChildFragmentManager().findFragmentById(R.id.map);
        // getFragmentManager().findFragmentById(R.id.map);
        if (googleMap == null) {
//            googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            } else {

                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("IMPACT Exhibition and Convention Center, Hall 2, 3 & 4, Bangkok, Thailand");

                // ROSE color icon
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                // adding marker
                googleMap.addMarker(marker);

                // Moving Camera to a Location with animation
                CameraPosition cameraPosition = new CameraPosition.Builder().target(marker.getPosition()).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                /*** ----------------------------------------- */
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
                reloadMap(query);
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }


    private void reloadMap(String searchText) {
        Toast.makeText(this, "Search" + searchText, Toast.LENGTH_LONG).show();

        final Hashtable<String, String> header = new Hashtable<>();

        final String url = "http://geo.groupkt.com/ip/" + searchText + "/json";

        AsyncTaskExecuter.AsyncTaskExecuterListener asyncTaskExecuterListener = new AsyncTaskExecuter.AsyncTaskExecuterListener() {

            @Override
            public void notifyRespons(Object[] result) {


                if (result != null && (Integer) result[0] == 200) {

                    JSONObject da = (JSONObject) result[1];
                    try {
                        MapContent mMapContent = JsonParser.parseData(da.getJSONObject("RestResponse").getJSONObject("result"));
                        init(Double.parseDouble(mMapContent.getLatitude()), Double.parseDouble(mMapContent.getLongitude()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {

                    }
                } else
                    Toast.makeText(Search.this, getResources().getString(R.string.internal_error), Toast.LENGTH_LONG).show();

            }
        };

        if (MainActivity.checkInternetConnection(this)) {
            new AsyncTaskExecuter(this, asyncTaskExecuterListener, url, "", "GET", header, true).execute();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection_found), Toast.LENGTH_LONG).show();
        }
    }
}