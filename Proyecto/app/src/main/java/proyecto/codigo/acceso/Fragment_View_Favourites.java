package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class Fragment_View_Favourites extends Fragment {

    String username;
    View v;
    ListView lv;

    String[] nombre_hoyo;
    String[] id_campo;
    String[] nombre_campo;
    String[] nombre_completo;
    String[] nombre_creador;

    ArrayAdapter<String> adapter;

    JSONParser jsonParser=new JSONParser();
    String URL;
    String ip_config;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        username=((MainActivity)getActivity()).username;
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-user-favourites.php";

        return inflater.inflate(R.layout.fragment_view_user_favourites, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        AttemptFindUserFavourites attemptFindUserFavourites= new AttemptFindUserFavourites();
        attemptFindUserFavourites.execute();


        lv = (ListView)getView().findViewById(R.id.list_favourites);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                Toast.makeText(getActivity().getApplicationContext(), nombre_creador[position], Toast.LENGTH_LONG).show();



                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Hole fvh=new Fragment_View_Hole();
                final Bundle bundle = new Bundle();
                bundle.putString("id_campo", id_campo[position]);
                bundle.putString("nombre", nombre_hoyo[position]);
                bundle.putString("creador", nombre_creador[position]);
                fvh.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvh).commit();

            }
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

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    nombre_hoyo[i]=obj.getString("nombre_hoyo");
                    id_campo[i]=obj.getString("id_campo");
                    nombre_campo[i]=obj.getString("nombre_campo");
                    nombre_completo[i]=nombre_campo[i]+" ("+nombre_hoyo[i]+")";
                    nombre_creador[i]=obj.getString("creador");
                }
                adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, nombre_completo);
                lv.setAdapter(adapter);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}