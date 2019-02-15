package proyecto.codigo.acceso;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker m=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.1423435, -2.3184953), 9));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point)
            {
                if(m==null)
                {
                    m=mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(point.latitude, point.longitude))
                            .title("Your Destination"));
                }
                else
                {
                    m.setPosition(new LatLng(point.latitude, point.longitude));
                }
            }
        });
    }
}
