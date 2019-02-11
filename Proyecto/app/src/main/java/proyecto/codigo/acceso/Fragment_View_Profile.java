package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class Fragment_View_Profile extends Fragment {


    String username;
    View v;
    String URL="http://192.168.1.40/TFG/BD/find-username-data.php";
    JSONParser jsonParser=new JSONParser();
    TextView tv_name;
    TextView tv_username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            username=bundle.getString("username");
        }
        else
        {
            username=((MainActivity)getActivity()).username;
        }
        //username=((MainActivity)getActivity()).username;

        v=inflater.inflate(R.layout.fragment_view_profile, container, false);
        tv_name=(TextView) v.findViewById(R.id.profile_name);
        tv_username=(TextView) v.findViewById(R.id.profile_username);

        AttemptFindUsernameData attemptFindData=new AttemptFindUsernameData();
        attemptFindData.execute(username);

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

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    nombre=obj.getString("nombre");
                    apellido1=obj.getString("apellido1");
                    apellido2=obj.getString("apellido2");
                    username=obj.getString("username");
                    password=obj.getString("password");

                    tv_name.setText(nombre+" "+apellido1+" "+apellido2);
                    tv_username.setText("@"+username);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}