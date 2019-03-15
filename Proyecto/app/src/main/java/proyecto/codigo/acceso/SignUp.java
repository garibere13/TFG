package proyecto.codigo.acceso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.signup_input_name) EditText nombre;
    @BindView(R.id.signup_input_ape1) EditText ape1;
    @BindView(R.id.signup_input_ape2) EditText ape2;
    @BindView(R.id.signup_input_username) EditText username;
    @BindView(R.id.signup_input_handicap) EditText handicap;
    @BindView(R.id.signup_input_pass1) EditText pass1;
    @BindView(R.id.signup_input_pass2) EditText pass2;
    @BindView(R.id.btn_signup) Button signupButton;
    @BindView(R.id.link_login) TextView loginLink;



    String ip_config;
    String URL;
    JSONParser jsonParser=new JSONParser();

    ImageView imagen;
    Animation mAnim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupscreen_user);
        ButterKnife.bind(this);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/login-signup.php";

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (nombre.getText().toString().length()==0 || ape1.getText().toString().length()==0 ||
                        ape2.getText().toString().length()==0 || username.getText().toString().length()==0 ||
                        pass1.getText().toString().length()==0 || pass2.getText().toString().length()==0 ||
                        handicap.getText().toString().length()==0)
                {
                    Toast.makeText(getApplicationContext(), R.string.rellenar_datos, Toast.LENGTH_LONG).show();
                }

                else if(nombre.getText().toString().trim().length()<2 || nombre.getText().toString().trim().length()>25)
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


                else if(!(pass1.getText().toString().matches(pass2.getText().toString())))
                {
                    Toast.makeText(getApplicationContext(), R.string.con_dif, Toast.LENGTH_LONG).show();
                }
                else if(pass1.getText().toString().trim().length()<6 || pass1.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getApplicationContext(), R.string.con_tam, Toast.LENGTH_LONG).show();
                }
                else
                {
                    AttemptSignUp attemptSignUp = new AttemptSignUp();
                    attemptSignUp.execute(nombre.getText().toString().trim(), ape1.getText().toString().trim(),
                            ape2.getText().toString().trim(), username.getText().toString().trim(),
                            pass1.getText().toString().trim(), handicap.getText().toString().trim());
                }

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        imagen = (ImageView) findViewById(R.id.imagen_signup);
        mAnim = AnimationUtils.loadAnimation(this, R.anim.view_animation);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            imagen.startAnimation(mAnim);
        }
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

                    final ProgressDialog progressDialog = new ProgressDialog(SignUp.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Creando cuenta...");
                    progressDialog.show();

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onSignupSuccess or onSignupFailed
                                    // depending on success
                                    //onSignupSuccess();
                                    // onSignupFailed();
                                    progressDialog.dismiss();
                                }
                            }, 2000);



                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
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
