package com.madcamp.week4.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.madcamp.week4.R;
import com.madcamp.week4.adapters.ModelMaps;
import com.madcamp.week4.adapters.RecyclerViewAdapterMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FragmentMap extends Fragment implements OnMapReadyCallback, PlacesListener, RecyclerViewAdapterMap.MapListRecyclerClickListener {
    View v;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterMap adapter;
    private CardView cardView;
    static double latitude, longitude;
    final static int RADIUS = 1000;
    LatLng currentLocation;

    Location my_location = new Location("pointA");

    private GoogleMap mMap;
    private MapView mapView = null;
    List<MarkerOptions> previous_markerOptions = new ArrayList<>();

    private String jsonString;

    private CardView refresh;
    private List<ModelMaps> map_list = new ArrayList<>();

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public FragmentMap(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_map, container, false);
        recyclerView = v.findViewById(R.id.rv_map);
        refresh = v.findViewById(R.id.refresh_button);
        cardView = v.findViewById(R.id.my_location);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapterMap(getContext(),map_list, this);
        recyclerView.setAdapter(adapter);

        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLng(
                                new LatLng(my_location.getLatitude(),my_location.getLongitude())),
                        600,
                        null
                );
            }
        });


        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                download_places();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setVisibility(INVISIBLE);
                        recyclerView.setVisibility(VISIBLE);
                    }
                }, 3000);
            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location lmLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lmLocation == null) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
            lmLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        while (lmLocation == null) { } // Lock

        mMap.clear();

        latitude = lmLocation.getLatitude();
        longitude = lmLocation.getLongitude();
        currentLocation = new LatLng(latitude, longitude);
        my_location.setLatitude(latitude);
        my_location.setLongitude(longitude);

        MapsInitializer.initialize(getContext());
        mMap.addMarker(new MarkerOptions().position(currentLocation)
                .title("I am here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void download_places(){
        if (previous_markerOptions != null) previous_markerOptions.clear();
        showPlaceInformation(currentLocation);
    }

    @Override
    public void onPlacesFailure(PlacesException e) {
    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previous_markerOptions.clear();
                for (noman.googleplaces.Place place : places) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(place.getName()); // 가게 이름
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude()); // 가게 위치(경도, 위도)
                    markerOptions.position(latLng);

                    // for http request
                    String urlString = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                            + place.getPlaceId()
                            + "&fields=international_phone_number,rating&key="
                            + "AIzaSyC81l9pxNiBOqzBI8WbXFibDNGF9bxoRnY";
                    markerOptions.snippet(urlString);
                    previous_markerOptions.add(markerOptions);
                }

                // Distinct MarkerOptions
                HashSet<MarkerOptions> hashSet = new HashSet<>();
                hashSet.addAll(previous_markerOptions);
                previous_markerOptions.clear();
                previous_markerOptions.addAll(hashSet);

                int index;
                MarkerOptions restaurant;
                for(int i =0; i<previous_markerOptions.size(); i++){
                    index = i;
                    restaurant = previous_markerOptions.get(index);

                    String rating = "", phone_number = "";
                    receiveJsonString(restaurant.getSnippet());
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        rating = jsonObject.getJSONObject("result").getString("rating");
                        phone_number = jsonObject.getJSONObject("result").getString("international_phone_number");
                    } catch (JSONException e) {

                    }
                    if (rating.equals("")) continue;
                    else {
                        Double ratingDouble = Double.parseDouble(rating);

                        Log.d("gana check", restaurant.getTitle());
                        Log.d("gana check", Double.toString(ratingDouble));
                        if (Double.compare(ratingDouble, 3.5) < 0) continue;
                        String markerSnippet = "평점 : " + ratingDouble + "/5.0\n"
                                + "전화 : " + phone_number + "\n"
                                + "주소 : " + getCurrentAddress(restaurant.getPosition());
                        Location location = new Location("pointB");
                        location.setLatitude(restaurant.getPosition().latitude);
                        location.setLongitude(restaurant.getPosition().longitude);
                        Integer dist = Math.round(my_location.distanceTo(location));
                        map_list.add(new ModelMaps(restaurant.getTitle(), Double.toString(ratingDouble),Double.toString(location.getLatitude()),Double.toString(location.getLongitude()), Integer.toString(dist)));
                        restaurant.snippet(markerSnippet);

                        LatLng selected_location = new LatLng(location.getLatitude(),location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(selected_location)
                                .title(restaurant.getTitle())
                                .icon(BitmapDescriptorFactory.defaultMarker(200))
                                .alpha(0.7f));

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    public void showPlaceInformation(LatLng location) {
        if (previous_markerOptions != null) previous_markerOptions.clear();
        new NRPlaces.Builder()
                .listener((PlacesListener) this)
                .key("AIzaSyC81l9pxNiBOqzBI8WbXFibDNGF9bxoRnY")
                .latlng(location.latitude, location.longitude)
                .radius(RADIUS) // Meter
                .type("restaurant")
                .build()
                .execute();
    }

    public String getCurrentAddress(LatLng latlng) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        } catch (IOException ioException) { // Network Problem
            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public void receiveJsonString(String urlString) {
        final String tmpString = urlString;
        jsonString = "";
        Thread thread = new Thread(new Runnable() {
            public void run() {
                HttpURLConnection myConnection = null;
                String str;
                try {
                    URL url = new URL(tmpString);
                    myConnection = (HttpsURLConnection) url.openConnection();
                    myConnection.setRequestProperty("User-Agent", "com.madcamp.week4");
                    if (myConnection.getResponseCode() == myConnection.HTTP_OK) {
                        InputStreamReader responseBodyReader = new InputStreamReader(myConnection.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(responseBodyReader);
                        StringBuffer buffer = new StringBuffer();
                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }
                        jsonString = buffer.toString();
                        myConnection.disconnect();
                        reader.close();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    jsonString = e.toString();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapListClicked(int position) {
        Log.d("check Place selected",map_list.get(position).getPlace());
        mMap.animateCamera(
                CameraUpdateFactory.newLatLng(
                        new LatLng(Double.parseDouble(map_list.get(position).getLat()), Double.parseDouble(map_list.get(position).getLng()))),
                600,
                null
        );
    }
}
