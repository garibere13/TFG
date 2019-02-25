package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    String ip_config;
    String URL;
    JSONParser jsonParser=new JSONParser();
    TextView tv_name;
    TextView tv_username;
    TextView tv_puntuacion;
    TextView tv_fecha;
    ImageButton ib_edit;

    public String db_nombre;
    public String db_apellido1;
    public String db_apellido2;
    public String db_username;
    public String db_password;
    public String db_date_dia;
    public String db_date_mes;
    public String db_date_año;
    public String db_puntuacion;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-username-data.php";
        if (bundle!=null)
        {
            username=bundle.getString("username");

            v=inflater.inflate(R.layout.fragment_view_profile, container, false);
            tv_name=v.findViewById(R.id.profile_name);
            tv_username=v.findViewById(R.id.profile_username);
            tv_puntuacion=v.findViewById(R.id.view_user_puntuacion);
            tv_fecha=v.findViewById(R.id.view_user_date);
        }
        else
        {
            username=((MainActivity)getActivity()).username;
            v=inflater.inflate(R.layout.fragment_view_my_profile, container, false);
            ib_edit=v.findViewById(R.id.profile_edit_button);
            tv_name=v.findViewById(R.id.profile_name);
            tv_username=v.findViewById(R.id.profile_username);
            tv_puntuacion=v.findViewById(R.id.view_my_puntuacion);
            tv_fecha=v.findViewById(R.id.view_my_date);

            ib_edit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_Edit_Profile fep=new Fragment_Edit_Profile();
                    final Bundle bundle = new Bundle();
                    bundle.putString("nombre", db_nombre);
                    bundle.putString("apellido1", db_apellido1);
                    bundle.putString("apellido2", db_apellido2);
                    bundle.putString("username", db_username);
                    bundle.putString("password", db_password);
                    fep.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fep).commit();

                }
            });
        }

        tv_name=v.findViewById(R.id.profile_name);
        tv_username=v.findViewById(R.id.profile_username);

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

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    db_nombre = obj.getString("nombre");
                    db_apellido1 = obj.getString("apellido1");
                    db_apellido2 = obj.getString("apellido2");
                    db_username = obj.getString("username");
                    db_password = obj.getString("password");
                    db_date_año = obj.getString("anyo");
                    db_date_mes = obj.getString("mes");
                    db_date_dia = obj.getString("dia");
                    db_puntuacion = obj.getString("puntuacion");

                }
                    tv_name.setText(db_nombre+" "+db_apellido1+" "+db_apellido2);
                    tv_username.setText("@"+db_username);
                    tv_fecha.append(db_date_dia+"/"+db_date_mes+"/"+db_date_año);
                    tv_puntuacion.append(db_puntuacion);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}