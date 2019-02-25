package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class Fragment_View_Field_Holes extends Fragment {

    int id_campo;
    int num_hoyos;
    View v;
    ListView lv;
    String[] hoyos;
    JSONParser jsonParser=new JSONParser();
    String URL;
    String ip_config;

    String db_nombre;
    String db_id_campo;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/check-hole-exists.php";
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            id_campo=Integer.parseInt(bundle.getString("id_campo"));
            num_hoyos=Integer.parseInt(bundle.getString("num_hoyos"));
        }

        return inflater.inflate(R.layout.fragment_view_field_holes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        hoyos=new String[num_hoyos];

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
                        FragmentManager fm=getActivity().getSupportFragmentManager();
                        Fragment_View_Hole fvh=new Fragment_View_Hole();
                        final Bundle bundle = new Bundle();
                        bundle.putString("id_campo", db_id_campo);
                        bundle.putString("nombre", db_nombre);
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

}