package proyecto.codigo.acceso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;


public class Fragment_Create_Hole extends Fragment {


    EditText descripcion;
    EditText metros;
    EditText par;

    View v;


    Button crear_button;
    Button cancelar_button;

    String username=((MainActivity)getActivity()).username;

    int id_campo;
    String nombre;


    String ip_config;
    String URL;

    JSONParser jsonParser=new JSONParser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            id_campo=Integer.parseInt(bundle.getString("id_campo"));
            nombre=bundle.getString("nombre");
        }

        v=inflater.inflate(R.layout.signupscreen_hole, container, false);

        ip_config=getResources().getString(R.string.ip_config);

        URL="http://"+ip_config+"/TFG/BD/create-hole.php";

        descripcion=v.findViewById(R.id.signup_hole_des_text);
        metros=v.findViewById(R.id.signup_hole_metros_text);
        par=v.findViewById(R.id.signup_hole_par_text);

        crear_button=v.findViewById(R.id.crear_hole_button);
        cancelar_button=v.findViewById(R.id.cancelar_hole_button);


        cancelar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent ma=new Intent(getActivity(), MainActivity.class);
                ma.putExtra("username", username);
                startActivity(ma);
            }
        });




        crear_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if (descripcion.getText().toString().length()==0 || metros.getText().toString().length()==0 ||
                        par.getText().toString().length()==0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.rellenar_datos, Toast.LENGTH_LONG).show();
                }
                else if(descripcion.getText().toString().trim().length()<2 || descripcion.getText().toString().trim().length()>250)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "La descripción debe tener entre 2 y 250 caracteres", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(metros.getText().toString().trim())<20 || Integer.parseInt(metros.getText().toString().trim())>1000)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El hoyo debe tener entre 20 y 1000 metros", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(par.getText().toString().trim())<1 || Integer.parseInt(par.getText().toString().trim())>10)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El par del hoyo debe estar entre 1 y 10", Toast.LENGTH_LONG).show();
                }

                else
                {
                    AttemptCreateHole attemptCreateHole = new AttemptCreateHole();
                    attemptCreateHole.execute(descripcion.getText().toString().trim(),
                            metros.getText().toString().trim(), par.getText().toString().trim());
                }
            }
        });

        return v;
    }



    //////////////////////////////////////////////////////////////////////////////

    private class AttemptCreateHole extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String descripcion= args[0];
            String metros = args[1];
            String par = args[2];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("id_campo", Integer.toString(id_campo)));
            params.add(new BasicNameValuePair("descripcion", descripcion));
            params.add(new BasicNameValuePair("metros", metros));
            params.add(new BasicNameValuePair("par", par));
            params.add(new BasicNameValuePair("creador", username));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result1)
        {
            Toast.makeText(getActivity().getApplicationContext(),"Hoyo registrado correctamente",Toast.LENGTH_LONG).show();

            Intent ma = new Intent(getActivity(), MainActivity.class);
            ma.putExtra("username", username);
            startActivity(ma);
        }
    }

}
