package proyecto.codigo.acceso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

public class Fragment_View_Images extends Fragment {


    //Web api url
    String ip_config;
    String URL;

    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "url";
    public static final String TAG_COMENTARIO = "comentario";

    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> comentarios;


    Button button;
    String id_campo;
    String tipo;
    String nombre_hoyo;
    String username;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/getImages.php";

        tipo=bundle.getString("tipo");



        v=inflater.inflate(R.layout.fragment_view_grid_images, container, false);

        gridView=v.findViewById(R.id.gridView);
        button=v.findViewById(R.id.button_subir_foto);

        images = new ArrayList<>();
        comentarios = new ArrayList<>();

        //Calling the getData method

        username=bundle.getString("username");
        if(tipo=="campo")
        {
            id_campo=bundle.getString("id_campo");
            getDataCampo();
        }
        else if(tipo=="hoyo")
        {
            id_campo=bundle.getString("id_campo");
            nombre_hoyo=bundle.getString("nombre_hoyo");
            getDataHoyo();
        }
        else
        {

            if(!(username.equals(((MainActivity)getActivity()).username)))
            {
                button.setEnabled(false);
            }
            getDataUsuario();
        }

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(tipo=="campo")
                {
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_Upload_Image fui=new Fragment_Upload_Image();
                    final Bundle bundle = new Bundle();
                    bundle.putString("tipo", tipo);
                    bundle.putString("username", username);
                    bundle.putString("id_campo", id_campo);
                    fui.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fui).commit();
                }

                else if(tipo=="hoyo")
                {
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_Upload_Image fui=new Fragment_Upload_Image();
                    final Bundle bundle = new Bundle();
                    bundle.putString("tipo", tipo);
                    bundle.putString("username", username);
                    bundle.putString("id_campo", id_campo);
                    bundle.putString("nombre_hoyo", nombre_hoyo);
                    fui.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fui).commit();
                }

                else
                {
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_Upload_Image fui=new Fragment_Upload_Image();
                    final Bundle bundle = new Bundle();
                    bundle.putString("tipo", tipo);
                    bundle.putString("username", username);
                    fui.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fui).commit();
                }

            }
        });

        return v;
    }

    private void getDataCampo()
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...","Fetching data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            showGrid(jsonArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_campo", id_campo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    private void getDataHoyo()
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...","Fetching data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            showGrid(jsonArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_campo", id_campo);
                params.put("nombre_hoyo", nombre_hoyo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    private void getDataUsuario()
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...","Fetching data...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        try {
                            JSONArray jsonArr = new JSONArray(response);
                            showGrid(jsonArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    private void showGrid(JSONArray jsonArray){

        String aux;

        for(int i = 0; i<jsonArray.length(); i++){
            JSONObject obj = null;
            try {
                obj = jsonArray.getJSONObject(i);
                aux="http://"+ip_config+obj.getString(TAG_IMAGE_URL);
                images.add(aux);
                comentarios.add(obj.getString(TAG_COMENTARIO));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images, comentarios);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), images);
        gridView.setAdapter(gridViewAdapter);
    }

}