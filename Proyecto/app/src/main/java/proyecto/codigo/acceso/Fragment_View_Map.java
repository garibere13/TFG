package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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


public class Fragment_View_Map extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker m=null;
    MapView mv;
    View v;

    String ip_config;
    String URL;

    String[] fields_id;
    String[] fields_name;
    String[] fields_lat;
    String[] fields_long;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        v=inflater.inflate(R.layout.fragment_view_map, container, false);

        ip_config=getResources().getString(R.string.ip_config);

        URL="http://"+ip_config+"/TFG/BD/find-fields-map-location.php";

        /*SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        AttemptFindFieldMapLocations attemptFindFields=new AttemptFindFieldMapLocations();
        attemptFindFields.execute();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mv=v.findViewById(R.id.view_map_fragment);

        if(mv!=null)
        {
            mv.onCreate(null);
            mv.onResume();
            mv.getMapAsync(this);
        }

    }

    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mMap=googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        UiSettings uis=mMap.getUiSettings();
        uis.setZoomControlsEnabled(true);

        //azken parametrua zooma da --> zenbaki haundigua, geo ta zoom gehio
        CameraPosition cp=CameraPosition.builder().target(new LatLng(43.1423435, -2.3184953)).zoom(9).build();


        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));



        for(int i=0;i<fields_id.length;i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(fields_lat[i]), Double.parseDouble(fields_long[i])))
                    .title(fields_name[i]));
        }
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

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            fields_id[i]=obj.getString("id");
            fields_name[i] = obj.getString("nombre"); //Según lo que se haya puesto en el while del php
            fields_lat[i]=obj.getString("latitud");
            fields_long[i]=obj.getString("longitud");
        }
    }
}
