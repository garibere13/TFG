package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.net.URL;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;


public class Fragment_Find_Friend extends Fragment {

    String URL="http://192.168.1.40/TFG/BD/find-usernames.php";
    AutoCompleteTextView textView;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        v=inflater.inflate(R.layout.fragment_find_friend, container, false);
        textView=(AutoCompleteTextView) v.findViewById(R.id.autocomplete_username);

        Fragment_Find_Friend.AttemptFindUsernames attemptLogIn=new AttemptFindUsernames();
        attemptLogIn.execute();

        textView.setOnItemClickListener(new OnItemClickListener() {

            // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                Toast.makeText(getActivity().getApplicationContext(), "Ha seleccionado: " +
                        arg0.getAdapter().getItem(arg2), Toast.LENGTH_SHORT).show();
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
                //creating a URL
                URL url = new URL(URL);

                //Opening the URL using HttpURLConnection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //StringBuilder object to read the string from the service
                StringBuilder sb = new StringBuilder();

                //We will use a buffered reader to read the string from service
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                //A simple string to read values from each line
                String json;

                //reading until we don't find null
                while ((json = bufferedReader.readLine()) != null) {

                    //appending it to string builder
                    sb.append(json + "\n");
                }

                //finally returning the read string
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

    private void loadUsernamesAutoCompleteTextView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            heroes[i] = obj.getString("username"); //Según lo que se haya puesto en el while del php
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.friend_username_item, heroes);
        textView.setAdapter(adapter);
    }
}