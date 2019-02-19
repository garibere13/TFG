package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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

public class Maps_Show_Location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker m=null;
    String latitud;
    String longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        latitud=getIntent().getExtras().getString("latitud");
        longitud=getIntent().getExtras().getString("longitud");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_show_activity);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        UiSettings uis=mMap.getUiSettings();
        uis.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)), 9));

        m=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud))));

    }
}
