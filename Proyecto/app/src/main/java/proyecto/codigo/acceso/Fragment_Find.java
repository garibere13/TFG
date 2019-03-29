package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.net.URL;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;


public class Fragment_Find extends Fragment {

    String ip_config;

    String URL;
    String URL1;
    AutoCompleteTextView textView_username;
    AutoCompleteTextView textView_field_name;

    String[] field_names;
    String[] field_ids;
    String[] field_creador;

    String id_campo;
    String creador;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        v=inflater.inflate(R.layout.fragment_find, container, false);
        textView_username=v.findViewById(R.id.autocomplete_username);
        textView_field_name=v.findViewById(R.id.autocomplete_field_name);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-usernames.php";
        URL1="http://"+ip_config+"/TFG/BD/find-field-names.php";

        AttemptFindUsernames attemptFindUsernames=new AttemptFindUsernames();
        attemptFindUsernames.execute();

        AttemptFindFieldNames attemptFindFieldNames=new AttemptFindFieldNames();
        attemptFindFieldNames.execute();

        textView_username.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Profile fvp=new Fragment_View_Profile();
                final Bundle bundle = new Bundle();
                bundle.putString("username", (String) arg0.getAdapter().getItem(arg2));
                fvp.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
            }
        });

        textView_field_name.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                for (int i=0;i<field_names.length;i++)
                {
                    if(field_names[i]==(String) arg0.getAdapter().getItem(arg2))
                    {
                        id_campo=field_ids[i];
                        creador=field_creador[i];
                    }
                }
                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Field fvf=new Fragment_View_Field();
                final Bundle bundle = new Bundle();
                bundle.putString("id", id_campo);
                bundle.putString("creador", creador);
                fvf.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvf).commit();
            }
        });
        return v;
    }


    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindUsernames extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {

                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                loadUsernamesAutoCompleteTextView(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindFieldNames extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(URL1);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {

                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                loadFieldNamesAutoCompleteTextView(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private void loadUsernamesAutoCompleteTextView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] usernames = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            usernames[i] = obj.getString("username"); //Según lo que se haya puesto en el while del php
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.find_item, usernames);
        textView_username.setAdapter(adapter);
    }

    private void loadFieldNamesAutoCompleteTextView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        field_names=new String[jsonArray.length()];
        field_ids=new String[jsonArray.length()];
        field_creador=new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            field_ids[i]=obj.getString("id");
            field_creador[i]=obj.getString("creador");
            field_names[i]=obj.getString("nombre"); //Según lo que se haya puesto en el while del php
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.find_item, field_names);
        textView_field_name.setAdapter(adapter);
    }
}