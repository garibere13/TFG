package proyecto.codigo.acceso;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class Fragment_View_Map extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mv;
    View v;

    String ip_config;
    String URL;

    String[] fields_id;
    String[] fields_name;
    String[] fields_lat;
    String[] fields_long;
    String[] fields_creador;

    Marker myMarker;


    static double latitud = 43.01;
    static double longitud = -2.34;


    private static final int REQUEST_LOCATION = 1;
    //Button button;
    //TextView textView;
    LocationManager locationManager;
    //String lattitude, longitude;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_view_map, container, false);


        ip_config = getResources().getString(R.string.ip_config);
        URL = "http://" + ip_config + "/TFG/BD/find-fields-map-location.php";



        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mv = v.findViewById(R.id.view_map_fragment);

        AttemptFindFieldMapLocations attemptFindFields = new AttemptFindFieldMapLocations();
        attemptFindFields.execute();


        if (mv != null) {
            mv.onCreate(null);
            mv.onResume();
            mv.getMapAsync(this);
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                latitud = latti;
                longitud = longi;

                //Toast.makeText(getActivity(), "Your current location is" + "\n" + "Lattitude = " + latitud
                  //      + "\n" + "Longitude = " + longitud, Toast.LENGTH_SHORT).show();

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                latitud = latti;
                longitud = longi;

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                latitud = latti;
                longitud = longi;

            } else {

                Toast.makeText(getActivity(), "Unable to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
    }


    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void onMapReady(GoogleMap googleMap) {


        MapsInitializer.initialize(getContext());

        mMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        UiSettings uis = mMap.getUiSettings();
        uis.setZoomControlsEnabled(true);

        //azken parametrua zooma da --> zenbaki haundigua, geo ta zoom gehio

        CameraPosition cp = CameraPosition.builder().target(new LatLng(latitud, longitud)).zoom(9).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));


        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mMap.setMyLocationEnabled(true);



        for(int i=0;i<fields_id.length;i++)
        {
            myMarker=googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(fields_lat[i]), Double.parseDouble(fields_long[i])))
                    .title(fields_name[i]));
            myMarker.setTag(fields_id[i]+"."+fields_creador[i]);
        }


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            public void onInfoWindowClick(Marker arg0) {


                String filename=(String)arg0.getTag();
                String[] parts = filename.split("\\."); // String array, each element is text between dots
                String id = parts[0];
                String creador = parts[1];


                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Field fvf=new Fragment_View_Field();
                final Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("creador", creador);
                fvf.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvf).commit();
            }
        });


}

    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindFieldMapLocations extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(Void... voids) {

            try {
                java.net.URL url = new URL(URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {

                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                loadFields(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void loadFields(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        fields_id = new String[jsonArray.length()];
        fields_name=new String[jsonArray.length()];
        fields_lat = new String[jsonArray.length()];
        fields_long=new String[jsonArray.length()];
        fields_creador=new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            fields_id[i]=obj.getString("id");
            fields_name[i] = obj.getString("nombre"); //Según lo que se haya puesto en el while del php
            fields_lat[i]=obj.getString("latitud");
            fields_long[i]=obj.getString("longitud");
            fields_creador[i]=obj.getString("creador");
        }
    }
}