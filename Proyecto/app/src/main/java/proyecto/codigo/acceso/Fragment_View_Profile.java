package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class Fragment_View_Profile extends Fragment {


    String username=((MainActivity)getActivity()).username;
    View v;
    String URL="http://192.168.1.40/TFG/BD/find-username-data.php";
    JSONParser jsonParser=new JSONParser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        v=inflater.inflate(R.layout.fragment_view_profile, container, false);

        AttemptFindUsernameData attemptFindData=new AttemptFindUsernameData();
        attemptFindData.execute("garibere13");

        return v;
    }



    /////////////////////////////////////////////////////////////////////////////////////////



    private class AttemptFindUsernameData extends AsyncTask<String, String, String> {

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
                //String[] heroes = new String[jsonArray.length()];
                String nombre;
                String apellido1;
                String apellido2;
                String username;
                String password;
                String todo1;

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    nombre=obj.getString("nombre");
                    apellido1=obj.getString("apellido1");
                    apellido2=obj.getString("apellido2");
                    username=obj.getString("username");
                    password=obj.getString("password");
                    todo1=nombre+" "+apellido1+" "+apellido2+": "+username+" / "+password;

                    Toast.makeText(getActivity().getApplicationContext(), todo1, Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}