package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Fragment_View_Favourites extends Fragment {

    String username;
    View v;
    ListView lv_foto;
    ListView lv_name;

    String[] nombre_hoyo;
    String[] id_campo;
    String[] nombre_campo;
    String[] nombre_completo;
    String[] nombre_creador;
    String[] urls;
    List<HashMap<String, String>> aList;
    ListAdapter simpleAdapter_foto;
    ListAdapter simpleAdapter_nombre;

    ArrayAdapter<String> adapter;

    JSONParser jsonParser=new JSONParser();
    String URL;
    String ip_config;

    View clickSource;
    View touchSource;
    int offset = 0;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        username=((MainActivity)getActivity()).username;
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-user-favourites.php";

        //return inflater.inflate(R.layout.fragment_view_user_favourites, container, false);
        return inflater.inflate(R.layout.favoritos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        AttemptFindUserFavourites attemptFindUserFavourites= new AttemptFindUserFavourites();
        attemptFindUserFavourites.execute();


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

                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_View_Hole fvh=new Fragment_View_Hole();
                    final Bundle bundle = new Bundle();
                    bundle.putString("id_campo", id_campo[position]);
                    bundle.putString("nombre", nombre_hoyo[position]);
                    bundle.putString("creador", nombre_creador[position]);
                    fvh.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvh).commit();
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


    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindUserFavourites extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            String json = jsonParser.makeHttpRequestString(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);

                nombre_hoyo=new String[jsonArray.length()];
                id_campo=new String[jsonArray.length()];
                nombre_campo=new String[jsonArray.length()];
                nombre_completo=new String[jsonArray.length()];
                nombre_creador=new String[jsonArray.length()];
                urls=new String[jsonArray.length()];
                aList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    nombre_hoyo[i]=obj.getString("nombre_hoyo");
                    id_campo[i]=obj.getString("id_campo");
                    nombre_campo[i]=obj.getString("nombre_campo");
                    nombre_completo[i]=nombre_campo[i]+" ("+nombre_hoyo[i]+")";
                    nombre_creador[i]=obj.getString("creador");
                    urls[i]=obj.getString("url");

                    HashMap<String, String> hm = new HashMap<String, String>();

                    if(urls[i]!="null")
                    {
                        urls[i]="http://"+ip_config+urls[i];
                    }
                    hm.put("fotos", urls[i]);
                    hm.put("nombre", nombre_completo[i]);
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