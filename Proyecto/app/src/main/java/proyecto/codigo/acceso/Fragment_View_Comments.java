package proyecto.codigo.acceso;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_View_Comments extends Fragment {

    String ip_config;
    String URL;
    String URL1;
    String URL2;
    String id_campo;
    String nombre_hoyo;
    String creador;
    View v;
    ImageButton btn_enviar;
    EditText comentario;
    JSONParser jsonParser=new JSONParser();
    String[] ids;
    String[] comentarios;
    String[] usernames;
    String[] fechas;
    String[] urls;
    String[] votos;



    List<HashMap<String, String>> aList;
    ListAdapter simpleAdapter_foto;
    ListAdapter simpleAdapter_comentario;
    ListAdapter simpleAdapter_like;
    ListView lv_foto;
    ListView lv_comentario;
    ListView lv_like;
    CircleImageView image;
    View clickSource;
    View touchSource;
    int offset = 0;

    boolean doubleClick = false;
    Handler doubleHandler;

    public String id_comentario_voto;
    public String username_voto;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = getArguments();
        ip_config=getResources().getString(R.string.ip_config);
        if(bundle!=null)
        {
            id_campo=bundle.getString("id_campo");
            nombre_hoyo=bundle.getString("nombre_hoyo");
        }

        v=inflater.inflate(R.layout.comentarios, container, false);

        btn_enviar=v.findViewById(R.id.send);
        comentario=v.findViewById(R.id.commenttext);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/create-comment.php";
        URL1="http://"+ip_config+"/TFG/BD/find-hole-comments.php";
        URL2="http://"+ip_config+"/TFG/BD/create-vote.php";


        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comentario.getText().toString().trim().length()<2 || comentario.getText().toString().trim().length()>250)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El comentario debe tener entre 2 y 250 caracteres", Toast.LENGTH_LONG).show();
                }
                else
                {
                    AttemptCreateComment attemptCreateComment = new AttemptCreateComment();
                    attemptCreateComment.execute(comentario.getText().toString().trim(), id_campo, nombre_hoyo);
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        AttemptFindHoleComments attemptFindHoleComments= new AttemptFindHoleComments();
        attemptFindHoleComments.execute();




        lv_foto = v.findViewById(R.id.list_comments_photo);
        lv_comentario = v.findViewById(R.id.list_comments_comment);
        lv_like = v.findViewById(R.id.list_comments_like);



        lv_foto.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    lv_comentario.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                    /*lv_like.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }*/
                }

                return false;
            }
        });
        lv_comentario.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    lv_like.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                   /* lv_like.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }*/
                }

                return false;
            }
        });
        lv_like.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    lv_foto.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                    /*lv_comentario.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }*/
                }

                return false;
            }
        });


        ///////////////////////////////////////////7



        lv_foto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent == clickSource) {
                    // Do something with the ListView was clicked
                    //Log.e("eec", "Argazkia");
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_View_Profile fvp=new Fragment_View_Profile();
                    final Bundle bundle = new Bundle();
                    bundle.putString("username", usernames[position]);
                    fvp.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
                }
            }
        });
        lv_like.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent == clickSource) {
                    // Do something with the ListView was clicked
                    id_comentario_voto=ids[position];
                    AttemptCreateVote attemptCreateVote=new AttemptCreateVote();
                    attemptCreateVote.execute();
                }
            }
        });




        /////////////////////////////////////////

        lv_foto.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                {
                    lv_comentario.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                    lv_like.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
        lv_comentario.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                {
                    lv_foto.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                    lv_like.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
        lv_like.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                {
                    lv_foto.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                    lv_comentario.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });
    }





    //////////////////////////////////////////////////////////////////////////////////7

    private class AttemptCreateVote extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_comentario", id_comentario_voto));
            params.add(new BasicNameValuePair("username", ((MainActivity)getActivity()).username));
            JSONObject json = jsonParser.makeHttpRequest(URL2, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                if(result != null)
                {
                    Toast.makeText(getActivity().getApplicationContext(),result.getString("mensaje"),Toast.LENGTH_LONG).show();
                }
                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Comments fvc=new Fragment_View_Comments();
                final Bundle bundle = new Bundle();
                bundle.putString("id_campo", id_campo);
                bundle.putString("nombre_hoyo", nombre_hoyo);
                fvc.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvc).commit();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindHoleComments extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_campo", id_campo));
            params.add(new BasicNameValuePair("nombre_hoyo", nombre_hoyo));
            String json = jsonParser.makeHttpRequestString(URL1, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);
                ids=new String[jsonArray.length()];
                comentarios=new String[jsonArray.length()];
                usernames=new String[jsonArray.length()];
                fechas=new String[jsonArray.length()];
                urls=new String[jsonArray.length()];
                votos=new String[jsonArray.length()];

                aList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    ids[i]=obj.getString("id");
                    comentarios[i]=obj.getString("comentario");
                    fechas[i]=obj.getString("fecha");
                    usernames[i]=obj.getString("username");
                    urls[i]=obj.getString("url");
                    votos[i]=obj.getString("votos");

                    HashMap<String, String> hm = new HashMap<String, String>();

                    if(urls[i]!="null")
                    {
                        urls[i]="http://"+ip_config+urls[i];
                    }
                    hm.put("fotos", urls[i]);
                    hm.put("comentario", comentarios[i]);
                    hm.put("username_fecha", usernames[i]+" - "+votos[i]+" votos"+"    "+fechas[i]);
                    //hm.put("listview_like", Integer.toString(R.drawable.like));
                    hm.put("listview_like", "null-1");
                    aList.add(hm);
                }

                String[] from = {"fotos"};
                int[] to = {R.id.listview_image};

                simpleAdapter_foto = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_photo, from, to);
                lv_foto.setAdapter(simpleAdapter_foto);

                ///////////////////////////////

                String[] from1 = {"comentario", "username_fecha"};
                int[] to1 = {R.id.listview_item_comentario, R.id.listview_item_short_description};

                simpleAdapter_comentario = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_comment, from1, to1);
                lv_comentario.setAdapter(simpleAdapter_comentario);

                ///////////////////////////////


                String[] from2 = {"listview_like"};
                int[] to2 = {R.id.listview_like_image};

                simpleAdapter_like = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_comments_like, from2, to2);
                lv_like.setAdapter(simpleAdapter_like);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////

    private class AttemptCreateComment extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String comentario= args[0];
            String id_campo = args[1];
            String nombre_hoyo = args[2];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("comentario", comentario));
            params.add(new BasicNameValuePair("id_campo", id_campo));
            params.add(new BasicNameValuePair("nombre_hoyo", nombre_hoyo));
            params.add(new BasicNameValuePair("username", ((MainActivity)getActivity()).username));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result1)
        {
            comentario.setText("");
            Toast.makeText(getActivity().getApplicationContext(),"Comentario registrado correctamente",Toast.LENGTH_LONG).show();

            FragmentManager fm=getActivity().getSupportFragmentManager();
            Fragment_View_Comments fvc=new Fragment_View_Comments();
            final Bundle bundle = new Bundle();
            bundle.putString("id_campo", id_campo);
            bundle.putString("nombre_hoyo", nombre_hoyo);
            fvc.setArguments(bundle);
            fm.beginTransaction().replace(R.id.contenedor, fvc).commit();
        }
    }





}