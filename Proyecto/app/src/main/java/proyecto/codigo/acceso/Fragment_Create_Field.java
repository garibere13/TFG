package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Fragment_Create_Field extends Fragment {


    EditText nombre;
    EditText descripcion;
    EditText num_hoyos;
    AutoCompleteTextView pueblo;
    Button location;

    public String ID_pueblo;


    Double latitud;
    Double longitud;

    View v;


    Button crear_button;
    Button cancelar_button;

    String username=((MainActivity)getActivity()).username;


    String ip_config;
    String URL;
    String URL1;

    String[] towns_id;
    String[] towns_name;

    JSONParser jsonParser=new JSONParser();

    public boolean is_pueblo_selected=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        v=inflater.inflate(R.layout.signupscreen_field, container, false);

        ip_config=getResources().getString(R.string.ip_config);

        URL="http://"+ip_config+"/TFG/BD/find-towns.php";
        URL1="http://"+ip_config+"/TFG/BD/create-field.php";

        nombre=v.findViewById(R.id.signup_field_name_text);
        descripcion=v.findViewById(R.id.signup_field_des_text);
        location=v.findViewById(R.id.signup_field_location_button);
        num_hoyos=v.findViewById(R.id.signup_field_num_text);
        pueblo=v.findViewById(R.id.signup_field_pueblo_autocomplete);

        crear_button=v.findViewById(R.id.crear_field_button);
        cancelar_button=v.findViewById(R.id.cancelar_field_button);


        AttemptFindTowns attemptFindTown=new AttemptFindTowns();
        attemptFindTown.execute();


        pueblo.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
           public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                is_pueblo_selected=true;
                   for(int i=0;i<towns_name.length;i++)
                   {
                       if(towns_name[i]==(String) arg0.getAdapter().getItem(arg2))
                       {
                           ID_pueblo=towns_id[i];
                           break;
                       }
                   }
            }
        });

        pueblo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                is_pueblo_selected=false;
            }
            @Override
            public void afterTextChanged(Editable s){}
        });


        cancelar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent ma=new Intent(getActivity(), MainActivity.class);
                ma.putExtra("username", username);
                startActivity(ma);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent msl=new Intent(getActivity(), Maps_Select_Location.class);
                startActivity(msl);
            }
        });


        crear_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                latitud=((MainActivity)getActivity()).latitud;
                longitud=((MainActivity)getActivity()).longitud;


                if (nombre.getText().toString().length()==0 || descripcion.getText().toString().length()==0 ||
                        num_hoyos.getText().toString().length()==0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.rellenar_datos, Toast.LENGTH_LONG).show();
                }
                else if(nombre.getText().toString().trim().length()<2 || nombre.getText().toString().trim().length()>60)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El nombre debe tener entre 2 y 60 caracteres", Toast.LENGTH_LONG).show();
                }
                else if(descripcion.getText().toString().trim().length()<2 || descripcion.getText().toString().trim().length()>250)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "La descripción debe tener entre 2 y 250 caracteres", Toast.LENGTH_LONG).show();
                }
                else if(latitud==0.0 || longitud==0.0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Seleccione una ubicación en el mapa", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(num_hoyos.getText().toString().trim())<1 || Integer.parseInt(num_hoyos.getText().toString().trim())>25)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El número de hoyos debe estar entre 1 y 25", Toast.LENGTH_LONG).show();
                }
                else if(!is_pueblo_selected)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Seleccione el nombre del pueblo de la lista", Toast.LENGTH_LONG).show();
                }
                else
                {
                    AttemptCreateField attemptCreateField = new AttemptCreateField();
                    attemptCreateField.execute(nombre.getText().toString().trim(), descripcion.getText().toString().trim(),
                            num_hoyos.getText().toString().trim());
                }
            }
        });

        return v;
    }



    //////////////////////////////////////////////////////////////////////////////

    private class AttemptCreateField extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String nombre= args[0];
            String descripcion = args[1];
            String num_hoyos = args[2];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre",  StringUtils.stripAccents(nombre)));
            params.add(new BasicNameValuePair("descripcion",  StringUtils.stripAccents(descripcion)));
            params.add(new BasicNameValuePair("num_hoyos", num_hoyos));
            params.add(new BasicNameValuePair("cod_pueblo", ID_pueblo));
            params.add(new BasicNameValuePair("latitud", Double.toString(latitud)));
            params.add(new BasicNameValuePair("longitud", Double.toString(longitud)));
            params.add(new BasicNameValuePair("username", username));

            JSONObject json = jsonParser.makeHttpRequest(URL1, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result1)
        {
            Toast.makeText(getActivity().getApplicationContext(),"Campo registrado correctamente",Toast.LENGTH_LONG).show();

            Intent ma = new Intent(getActivity(), MainActivity.class);
            ma.putExtra("username", username);
            startActivity(ma);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindTowns extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(Void... voids) {

            try {
                java.net.URL url = new URL(URL);
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
                loadTownsAutoCompleteTextView(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void loadTownsAutoCompleteTextView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        towns_name = new String[jsonArray.length()];
        towns_id=new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            towns_id[i]=obj.getString("id_municipio");
            towns_name[i] = obj.getString("nombre"); //Según lo que se haya puesto en el while del php
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.find_item, towns_name);
        pueblo.setAdapter(adapter);
    }
}
