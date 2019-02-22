package proyecto.codigo.acceso;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends Activity {


    EditText username;
    EditText password;

    Button loginButton;
    Button signupButton;

    String ip_config;
    String URL;
    JSONParser jsonParser=new JSONParser();

    String username_string;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/login-signup.php";

        username=findViewById(R.id.login_username_text);
        password=findViewById(R.id.login_pass_text);

        loginButton=findViewById(R.id.button_aceptar);
        signupButton=findViewById(R.id.button_signup);

        username.setText("garibere13");
        password.setText("123456");
        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                AttemptLogIn attemptLogIn= new AttemptLogIn();
                attemptLogIn.execute(username.getText().toString(), password.getText().toString());
            }
        });

        signupButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                    Intent su = new Intent(LogIn.this, SignUp.class);
                    startActivity(su);
                }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////

    private class AttemptLogIn extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String username=args[0];
            String password=args[1];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
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
                    if(result.getString("success")=="1")
                    {
                        Intent ma = new Intent(LogIn.this, MainActivity.class);
                        ma.putExtra("username", username.getText().toString());
                        startActivity(ma);
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