package com.example.pratyeshsingh.accoliteassignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pratyeshsingh on 19/09/16.
 */
public class JsonParser {

    public static void parseData(JSONArray res, ArrayList<Content> listData) {
        try {
            for (int i = 0; i < res.length(); i++) {

                JSONObject data = res.getJSONObject(i);

                Content mMyContent = new Content();

                String country = "";
                if (data.has("country"))
                    country = data.getString("country");

                mMyContent.setCountry(country);


                String name = "";
                if (data.has("name"))
                    name = data.getString("name");

                mMyContent.setName(name);

                String abbr = "";
                if (data.has("abbr"))
                    abbr = data.getString("abbr");

                mMyContent.setAbbr(abbr);


                String largest_city = "";
                if (data.has("largest_city"))
                    largest_city = data.getString("largest_city");

                mMyContent.setLargest_city(largest_city);


                String capital = "";
                if (data.has("capital"))
                    capital = data.getString("capital");

                mMyContent.setCapital(capital);


                listData.add(mMyContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static MapContent parseData(JSONObject data) {
        MapContent mMapContent = new MapContent();


        try {

//        "countryIso2": "US",
//                "stateAbbr": "CA",
//                "postal": "94043",
//                "continent": "North America",
//                "state": "California",
//                "longitude": "-122.0574",
//                "latitude": "37.4192",
//                "ds": "II",
//                "network": "AS15169 Google Inc.",
//                "city": "Mountain View",
//                "country": "United States",
//                "ip": "172.217.3.14"

            String countryIso2 = "";
            if (data.has("countryIso2"))
                countryIso2 = data.getString("countryIso2");
            mMapContent.setCountryIso2(countryIso2);


            String longitude = "";
            if (data.has("longitude"))
                longitude = data.getString("longitude");
            mMapContent.setLongitude(longitude);


            String latitude = "";
            if (data.has("latitude"))
                latitude = data.getString("latitude");
            mMapContent.setLatitude(latitude);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mMapContent;

    }
}
