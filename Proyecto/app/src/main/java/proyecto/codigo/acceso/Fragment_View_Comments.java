package proyecto.codigo.acceso;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_View_Comments extends Fragment {

    String ip_config;
    String URL;
    String URL1;
    String id_campo;
    String nombre_hoyo;
    String creador;
    //View root;
    View v;
    //View v1;
    ImageButton btn_enviar;
    EditText comentario;
    JSONParser jsonParser=new JSONParser();
    //ListView lv;
    ArrayAdapter<String> adapter;
    String[] comentarios;
    String[] usernames;
    String[] fechas;
    String[] urls;




    int[] listviewImage = new int[]{
            R.drawable.app_icon, R.drawable.el_tapon, R.drawable.favourite, R.drawable.golf_campo,
            R.drawable.home, R.drawable.log_out, R.drawable.nobody, R.drawable.pelota_golf,
    };

    int[] listviewLike = new int[]{
            R.drawable.like, R.drawable.like, R.drawable.like, R.drawable.like,
            R.drawable.like, R.drawable.like, R.drawable.like, R.drawable.like,
    };



    List<HashMap<String, String>> aList;
    ListAdapter simpleAdapter;
    ListView lv;
    CircleImageView image;



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
        //root=inflater.inflate(R.layout.comentarios_hoyos_principal, container, false);

        //v = root.findViewById(R.id.comentarios_principal);
        //v1 = root.findViewById(R.id.android_list_view_tutorial_with_example);



        //v1=inflater.inflate(R.layout.listview_activity, container, false);










        btn_enviar=v.findViewById(R.id.send);
        comentario=v.findViewById(R.id.commenttext);
        //image=v1.findViewById(R.id.listview_image);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/create-comment.php";
        URL1="http://"+ip_config+"/TFG/BD/find-hole-comments.php";


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

        /*List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < comentarios.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            hm.put("listview_like", Integer.toString(listviewLike[i]));
            aList.add(hm);


            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("comentario", comentarios[i]);
            hm.put("username_fecha", usernames[i]+" - "+fechas[i]);
            hm.put("listview_like", Integer.toString(listviewLike[i]));
            aList.add(hm);
        }*/

        /*lv = (ListView)getView().findViewById(R.id.list_comments);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });*/

        lv = v.findViewById(R.id.list_comments);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

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
                comentarios=new String[jsonArray.length()];
                usernames=new String[jsonArray.length()];
                fechas=new String[jsonArray.length()];
                urls=new String[jsonArray.length()];

                aList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    comentarios[i]=obj.getString("comentario");
                    fechas[i]=obj.getString("fecha");
                    usernames[i]=obj.getString("username");
                    urls[i]=obj.getString("url");

                    HashMap<String, String> hm = new HashMap<String, String>();

                    if(urls[i]!="null")
                    {
                        urls[i]="http://"+ip_config+urls[i];
                        //Picasso.get().load(urls[i]).into(image);
                    }
                    hm.put("fotos", urls[i]);
                    hm.put("comentario", comentarios[i]);
                    hm.put("username_fecha", usernames[i]+" - "+fechas[i]);
                    hm.put("listview_like", Integer.toString(listviewLike[i]));
                    aList.add(hm);
                }


                //Toast.makeText(getApplicationContext(), R.string.con_tam, Toast.LENGTH_LONG).show();


                String[] from = {"fotos", "comentario", "username_fecha", "listview_like"};
                int[] to = {R.id.listview_image, R.id.listview_item_comentario, R.id.listview_item_short_description, R.id.listview_imageeeeee};

                String c;
                c=urls[0];


                //String[] from = {"comentario", "username_fecha", "listview_like"};
                //int[] to = {R.id.listview_item_comentario, R.id.listview_item_short_description, R.id.listview_imageeeeee};

                simpleAdapter = new ListAdapter(getActivity().getBaseContext(), aList, R.layout.listview_activity, from, to, c, R.id.listview_image);
                lv.setAdapter(simpleAdapter);

                //adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_expandable_list_item_1, comentarios);
                //adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_comments, comentarios);
                //lv.setAdapter(adapter);
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