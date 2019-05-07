package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment_View_Following_Followers extends Fragment {

    String username;
    View v;
    ListView lv_foto;
    ListView lv_name;
    String[] followers_usernames_aux;
    String[] following_usernames_aux;
    String[] url;
    JSONParser jsonParser=new JSONParser();
    String URL;
    String ip_config;
    List<HashMap<String, String>> aList;
    View clickSource;
    View touchSource;
    int offset = 0;
    ListAdapter simpleAdapter_foto;
    ListAdapter simpleAdapter_nombre;
    int size;
    String[] following_usernames;
    String[] following_names;
    String[] following_ape1;
    String[] following_ape2;
    String[] following_url;
    String[] followers_usernames;
    String[] followers_names;
    String[] followers_ape1;
    String[] followers_ape2;
    String[] followers_url;
    String quien;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-following-followers.php";
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            username=bundle.getString("username");
            quien=bundle.getString("quien");
        }
        if(quien.equals("destino"))
        {
            AttemptFindFollowers attemptFindFollowers=new AttemptFindFollowers();
            attemptFindFollowers.execute(username);
        }
        else
        {
            AttemptFindFollowing attemptFindFollowing=new AttemptFindFollowing();
            attemptFindFollowing.execute(username);
        }
        return inflater.inflate(R.layout.favoritos, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

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
                    Fragment_View_Profile fvp=new Fragment_View_Profile();
                    final Bundle bundle = new Bundle();
                    if(quien.equals("destino"))
                    {
                        bundle.putString("username", followers_usernames[position]);
                    }
                    else
                    {
                        bundle.putString("username", following_usernames[position]);
                    }
                    fvp.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
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


    ////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindFollowers extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("following_followers", "origen"));
            String json = jsonParser.makeHttpRequestString(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);
                followers_usernames=new String[jsonArray.length()];
                followers_usernames_aux=new String[jsonArray.length()];
                followers_names=new String[jsonArray.length()];
                followers_ape1=new String[jsonArray.length()];
                followers_ape2=new String[jsonArray.length()];
                followers_url=new String[jsonArray.length()];

                aList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    followers_usernames[i] = obj.getString("username");
                    followers_usernames_aux[i] = obj.getString("username");
                    followers_names[i] = obj.getString("nombre");
                    followers_ape1[i] = obj.getString("apellido1");
                    followers_ape2[i] = obj.getString("apellido2");
                    followers_url[i] = obj.getString("url");

                    HashMap<String, String> hm = new HashMap<String, String>();

                    if(followers_url[i]!="null")
                    {
                        followers_url[i]="http://"+ip_config+followers_url[i];
                    }
                    hm.put("fotos", followers_url[i]);
                    hm.put("nombre_completo", followers_names[i]+" "+followers_ape1[i]+" "+followers_ape2[i]);

                    followers_usernames_aux[i]="@"+followers_usernames_aux[i];
                    hm.put("usernames", followers_usernames_aux[i]);

                    aList.add(hm);
                }

                String[] from = {"fotos"};
                int[] to = {R.id.listview_image};

                simpleAdapter_foto = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_photo, from, to);
                lv_foto.setAdapter(simpleAdapter_foto);

                String[] from1 = {"usernames", "nombre_completo"};
                int[] to1 = {R.id.listview_item_short_description, R.id.listview_item_comentario};

                simpleAdapter_nombre = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_comment, from1, to1);
                lv_name.setAdapter(simpleAdapter_nombre);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }


    private class AttemptFindFollowing extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("following_followers", "destino"));
            String json = jsonParser.makeHttpRequestString(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);
                following_usernames=new String[jsonArray.length()];
                following_usernames_aux=new String[jsonArray.length()];
                following_names=new String[jsonArray.length()];
                following_ape1=new String[jsonArray.length()];
                following_ape2=new String[jsonArray.length()];
                following_url=new String[jsonArray.length()];
                aList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    following_usernames[i] = obj.getString("username");
                    following_usernames_aux[i] = obj.getString("username");
                    following_names[i] = obj.getString("nombre");
                    following_ape1[i] = obj.getString("apellido1");
                    following_ape2[i] = obj.getString("apellido2");
                    following_url[i] = obj.getString("url");

                    HashMap<String, String> hm = new HashMap<String, String>();

                    if(following_url[i]!="null")
                    {
                        following_url[i]="http://"+ip_config+following_url[i];
                    }
                    hm.put("fotos", following_url[i]);
                    hm.put("nombre_completo", following_names[i]+" "+following_ape1[i]+" "+following_ape2[i]);

                    following_usernames_aux[i]="@"+following_usernames_aux[i];
                    hm.put("usernames", following_usernames_aux[i]);

                    aList.add(hm);
                }
                String[] from = {"fotos"};
                int[] to = {R.id.listview_image};

                simpleAdapter_foto = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_photo, from, to);
                lv_foto.setAdapter(simpleAdapter_foto);

                String[] from1 = {"usernames", "nombre_completo"};
                int[] to1 = {R.id.listview_item_short_description, R.id.listview_item_comentario};

                simpleAdapter_nombre = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_comment, from1, to1);
                lv_name.setAdapter(simpleAdapter_nombre);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}