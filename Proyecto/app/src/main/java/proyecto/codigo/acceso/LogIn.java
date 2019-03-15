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
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LogIn extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_login_username) EditText username;
    @BindView(R.id.input_login_password) EditText password;
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.login_link_signup) TextView signupLink;


    String ip_config;
    String URL;
    JSONParser jsonParser=new JSONParser();

    String username_string;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen_user);
        ButterKnife.bind(this);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/login-signup.php";

        username.setText("garibere13");
        password.setText("123456");

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //login();


                final ProgressDialog progressDialog = new ProgressDialog(LogIn.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Autenticando...");
                progressDialog.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                //onLoginSuccess();
                                // onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }, 2000);

                AttemptLogIn attemptLogIn= new AttemptLogIn();
                attemptLogIn.execute(username.getText().toString(), password.getText().toString());

            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }







/*public class LogIn extends Activity {


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
    }*/

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