package proyecto.codigo.acceso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import android.widget.Toast;

public class SignUp extends Activity {

    EditText name;
    EditText ape1;
    EditText ape2;
    EditText username;
    EditText handicap;
    EditText con1;
    EditText con2;

    Button registrar_button;
    Button cancelar_button;

    String ip_config;
    String URL;
    JSONParser jsonParser=new JSONParser();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupscreen);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/login-signup.php";

        name=findViewById(R.id.signup_name_text);
        ape1=findViewById(R.id.signup_ape1_text);
        ape2=findViewById(R.id.signup_ape2_text);
        username=findViewById(R.id.signup_username_text);
        handicap=findViewById(R.id.signup_handicap_text);
        con1=findViewById(R.id.signup_con1_text);
        con2= findViewById(R.id.signup_con2_text);

        registrar_button=findViewById(R.id.registrar_button);
        cancelar_button=findViewById(R.id.cancelar_button);

        cancelar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    Intent li = new Intent(SignUp.this, LogIn.class);
                    startActivity(li);
            }
        });

        registrar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (name.getText().toString().length()==0 || ape1.getText().toString().length()==0 ||
                        ape2.getText().toString().length()==0 || username.getText().toString().length()==0 ||
                        con1.getText().toString().length()==0 || con2.getText().toString().length()==0 ||
                        handicap.getText().toString().length()==0)
                {
                    Toast.makeText(getApplicationContext(), R.string.rellenar_datos, Toast.LENGTH_LONG).show();
                }

                else if(name.getText().toString().trim().length()<2 || name.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getApplicationContext(), R.string.name_tam, Toast.LENGTH_LONG).show();
                }

                else if(ape1.getText().toString().trim().length()<2 || ape1.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getApplicationContext(), R.string.ape1_tam, Toast.LENGTH_LONG).show();
                }

                else if(ape2.getText().toString().trim().length()<2 || ape2.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getApplicationContext(), R.string.ape2_tam, Toast.LENGTH_LONG).show();
                }
                else if(username.getText().toString().trim().length()<2 || username.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getApplicationContext(), R.string.user_tam, Toast.LENGTH_LONG).show();
                }

                else if(Float.parseFloat(handicap.getText().toString().trim())<-10 || Float.parseFloat(handicap.getText().toString().trim())>36)
                {
                    Toast.makeText(getApplicationContext(), "El handicap debe estar entre -10 y 36", Toast.LENGTH_LONG).show();
                }


                else if(!(con1.getText().toString().matches(con2.getText().toString())))
                {
                    Toast.makeText(getApplicationContext(), R.string.con_dif, Toast.LENGTH_LONG).show();
                }
                else if(con1.getText().toString().trim().length()<6 || con1.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getApplicationContext(), R.string.con_tam, Toast.LENGTH_LONG).show();
                }
                else
                {
                    AttemptSignUp attemptSignUp = new AttemptSignUp();
                    attemptSignUp.execute(name.getText().toString().trim(), ape1.getText().toString().trim(),
                            ape2.getText().toString().trim(), username.getText().toString().trim(),
                            con1.getText().toString().trim(), handicap.getText().toString().trim());
                }
            }

        });


    }


    private class AttemptSignUp extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String nombre= args[0];
            String apellido1 = args[1];
            String apellido2 = args[2];
            String username = args[3];
            String password = args[4];
            String handicap = args[5];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre",  StringUtils.stripAccents(nombre)));
            params.add(new BasicNameValuePair("apellido1", StringUtils.stripAccents(apellido1)));
            params.add(new BasicNameValuePair("apellido2", StringUtils.stripAccents(apellido2)));
            params.add(new BasicNameValuePair("username", StringUtils.stripAccents(username)));
            params.add(new BasicNameValuePair("password", StringUtils.stripAccents(password)));
            params.add(new BasicNameValuePair("handicap", handicap));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                if(result != null)
                {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    Intent li=new Intent(SignUp.this, LogIn.class);
                    startActivity(li);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
