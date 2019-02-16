package proyecto.codigo.acceso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class Fragment_View_Map extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker m=null;
    MapView mv;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        v=inflater.inflate(R.layout.fragment_view_map, container, false);

        /*SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

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

        CameraPosition cp=CameraPosition.builder().target(new LatLng(43.1423435, -2.3184953)).zoom(9).build();


        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
     /*   mMap = googleMap;


        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        UiSettings uis=mMap.getUiSettings();
        uis.setZoomControlsEnabled(true);

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
    */}
}
