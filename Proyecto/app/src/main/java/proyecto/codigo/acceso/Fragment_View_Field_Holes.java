package proyecto.codigo.acceso;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Fragment_View_Field_Holes extends Fragment {

    int id_campo;
    int num_hoyos;
    View v;
    ListView lv;
    //String[] hoyos;
    JSONParser jsonParser=new JSONParser();
    String URL;
    String URL1;
    String ip_config;
    String[] nombre_hoyos;
    String[] urls;
    List<HashMap<String, String>> aList;
    View clickSource;
    View touchSource;
    int offset = 0;

    String db_nombre;
    String db_id_campo;
    String db_creador;
    ListView lv_foto;
    ListView lv_name;
    ListAdapter simpleAdapter_foto;
    ListAdapter simpleAdapter_nombre;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/check-hole-exists.php";
        URL1="http://"+ip_config+"/TFG/BD/check-hole-information.php";
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            id_campo=Integer.parseInt(bundle.getString("id_campo"));
            num_hoyos=Integer.parseInt(bundle.getString("num_hoyos"));
        }
        AttemptCheckHoleInformation attemptCheckHoleInformation=new AttemptCheckHoleInformation();
        attemptCheckHoleInformation.execute();

        return inflater.inflate(R.layout.favoritos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        /*hoyos=new String[num_hoyos];

        for(int i=0;i<num_hoyos;i++)
        {
            hoyos[i]="Hoyo "+(i+1);
        }
        lv = (ListView)getView().findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, hoyos);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AttemptCheckHoleExists attemptCheckHoleExists= new AttemptCheckHoleExists();
                attemptCheckHoleExists.execute(Integer.toString(id_campo), hoyos[position]);
            }
        });*/

        lv_foto = (ListView)getView().findViewById(R.id.list_favorites_photo);
        lv_name = (ListView)getView().findViewById(R.id.list_favorites_name);



        lv_foto.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    lv_name.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });
        lv_name.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    lv_foto.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });


        lv_foto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent == clickSource) {

                    AttemptCheckHoleExists attemptCheckHoleExists= new AttemptCheckHoleExists();
                    attemptCheckHoleExists.execute(Integer.toString(id_campo), nombre_hoyos[position]);
                }
            }
        });



        lv_foto.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                {
                    lv_name.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
        lv_name.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                {
                    lv_foto.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
    }


    //////////////////////////////////////////////////////////////////////////////////

    private class AttemptCheckHoleExists extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            db_id_campo=args[0];
            db_nombre=args[1];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_campo", db_id_campo));
            params.add(new BasicNameValuePair("nombre", db_nombre));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                if(result != null)
                {

                    if(result.getString("success")=="0")
                    {
                        FragmentManager fm=getActivity().getSupportFragmentManager();
                        Fragment_Create_Hole fch=new Fragment_Create_Hole();
                        final Bundle bundle = new Bundle();
                        bundle.putString("id_campo", db_id_campo);
                        bundle.putString("nombre", db_nombre);
                        fch.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.contenedor, fch).commit();
                    }
                    else
                    {
                        db_creador=result.getString("creador");

                        FragmentManager fm=getActivity().getSupportFragmentManager();
                        Fragment_View_Hole fvh=new Fragment_View_Hole();
                        final Bundle bundle = new Bundle();
                        bundle.putString("id_campo", db_id_campo);
                        bundle.putString("nombre", db_nombre);
                        bundle.putString("creador", db_creador);
                        fvh.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.contenedor, fvh).commit();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    private class AttemptCheckHoleInformation extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_campo", Integer.toString(id_campo)));
            params.add(new BasicNameValuePair("num_hoyos", Integer.toString(num_hoyos)));
            String json = jsonParser.makeHttpRequestString(URL1, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);

                nombre_hoyos=new String[jsonArray.length()];
                urls=new String[jsonArray.length()];

                aList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    nombre_hoyos[i]=obj.getString("nombre_hoyo");
                    urls[i]=obj.getString("url");

                    HashMap<String, String> hm = new HashMap<String, String>();

                    if(urls[i]!="null")
                    {
                        urls[i]="http://"+ip_config+urls[i];
                    }
                    hm.put("fotos", urls[i]);
                    hm.put("nombre", nombre_hoyos[i]);
                    aList.add(hm);
                }
                String[] from = {"fotos"};
                int[] to = {R.id.listview_image};

                simpleAdapter_foto = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_photo, from, to);
                lv_foto.setAdapter(simpleAdapter_foto);
                //adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, nombre_completo);
                //lv_photos.setAdapter(adapter);

                String[] from1 = {"nombre",};
                int[] to1 = {R.id.listview_favorites_name};

                simpleAdapter_nombre = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_name, from1, to1);
                lv_name.setAdapter(simpleAdapter_nombre);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

}