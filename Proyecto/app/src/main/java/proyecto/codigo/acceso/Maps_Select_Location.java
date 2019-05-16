package proyecto.codigo.acceso;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Maps_Select_Location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker m=null;

    Button but;

    String ip_config;
    String URL;

    String[] fields_id;
    String[] fields_name;
    String[] fields_lat;
    String[] fields_long;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-fields-map-location.php";

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MainActivity.latitud=0.0;
        MainActivity.longitud=0.0;
        but=findViewById(R.id.select_location_button);

        AttemptShowExistingFields attemptShowExistingFields=new AttemptShowExistingFields();
        attemptShowExistingFields.execute();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        UiSettings uis=mMap.getUiSettings();
        uis.setZoomControlsEnabled(true);

       /* uis.setRotateGesturesEnabled(false);
        uis.setZoomGesturesEnabled(true);
        uis.setScrollGesturesEnabled(false);
        uis.setTiltGesturesEnabled(false);*/


        //azken parametrua zooma da --> zenbaki haundigua, geo ta zoom gehio
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.1423435, -2.3184953), 11));



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point)
            {
                if(m==null)
                {
                    m=mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(point.latitude, point.longitude))
                            .title("Nuevo Campo"));

                    MainActivity.latitud=point.latitude;
                    MainActivity.longitud=point.longitude;

                }
                else
                {
                    m.setPosition(new LatLng(point.latitude, point.longitude));

                    MainActivity.latitud=point.latitude;
                    MainActivity.longitud=point.longitude;
                }
            }
        });


        but.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(MainActivity.latitud!=0.0 && MainActivity.longitud!=0.0)
                {
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Seleccione una localización", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showFields()
    {
        for(int i=0;i<fields_id.length;i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(fields_lat[i]), Double.parseDouble(fields_long[i])))
                    .title(fields_name[i]));
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptShowExistingFields extends AsyncTask<Void, Void, String> {

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

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            fields_id[i]=obj.getString("id");
            fields_name[i] = obj.getString("nombre"); //Según lo que se haya puesto en el while del php
            fields_lat[i]=obj.getString("latitud");
            fields_long[i]=obj.getString("longitud");
        }
        showFields();
    }


}
