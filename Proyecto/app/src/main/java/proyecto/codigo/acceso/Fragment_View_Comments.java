package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Fragment_View_Comments extends Fragment {

    String ip_config;
    String URL;
    String URL1;
    String id_campo;
    String nombre_hoyo;
    String creador;
    View v;
    ImageButton btn_enviar;
    EditText comentario;
    JSONParser jsonParser=new JSONParser();
    ListView lv;
    ArrayAdapter<String> adapter;
    String[] comentarios;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = getArguments();
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


        lv = (ListView)getView().findViewById(R.id.list_comments);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Hole fvh=new Fragment_View_Hole();
                final Bundle bundle = new Bundle();
                bundle.putString("id_campo", id_campo[position]);
                bundle.putString("nombre", nombre_hoyo[position]);
                bundle.putString("creador", nombre_creador[position]);
                fvh.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvh).commit();*/
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

                //nombre_hoyo=new String[jsonArray.length()];
                //id_campo=new String[jsonArray.length()];
                comentarios=new String[jsonArray.length()];
                //nombre_completo=new String[jsonArray.length()];
                //nombre_creador=new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    //nombre_hoyo[i]=obj.getString("nombre_hoyo");
                    comentarios[i]=obj.getString("comentario");
                    /*nombre_campo[i]=obj.getString("nombre_campo");
                    nombre_completo[i]=nombre_campo[i]+" ("+nombre_hoyo[i]+")";
                    nombre_creador[i]=obj.getString("creador");*/
                }
                //adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simple_expandable_list_item_1, comentarios);
                adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_comments, comentarios);
                lv.setAdapter(adapter);
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