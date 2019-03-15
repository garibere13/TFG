package proyecto.codigo.acceso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.net.SocketException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import android.os.StrictMode;


public class Fragment_Edit_Profile extends Fragment {


    EditText name;
    EditText ape1;
    EditText ape2;
    EditText username;
    EditText handicap;
    EditText con1;
    EditText con2;

    String _nombre;
    String _apellido1;
    String _apellido2;
    String _username;
    String _password;
    String _handicap;

    Button registrar_button;
    Button cancelar_button;

    String ip_config;

    String URL;


    JSONParser jsonParser=new JSONParser();

    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/edit-user.php";

        _nombre=bundle.getString("nombre");
        _apellido1=bundle.getString("apellido1");
        _apellido2=bundle.getString("apellido2");
        _username=bundle.getString("username");
        _handicap=bundle.getString("handicap");
        _password=bundle.getString("password");

        v=inflater.inflate(R.layout.signupscreen_user, container, false);

        name=v.findViewById(R.id.signup_input_name);
        ape1=v.findViewById(R.id.signup_input_ape1);
        ape2=v.findViewById(R.id.signup_input_ape2);
        username=v.findViewById(R.id.signup_input_username);
        handicap=v.findViewById(R.id.signup_input_handicap);
        con1=v.findViewById(R.id.signup_input_pass1);
        con2=v.findViewById(R.id.signup_input_pass2);

        registrar_button=v.findViewById(R.id.btn_signup);
        cancelar_button=v.findViewById(R.id.link_login);


        registrar_button.setText("Modificar");

        //WifiManager wm = (WifiManager) getActivity().getSystemService(WIFI_SERVICE);

        //WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);

        //String ip = Formatter.getHostAddress(wm.getConnectionInfo().getIpAddress());
        //String ip=getHostAddress();
        //String ip="IP";

        /*InetAddress ia=new InetAddress();

        WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(getContext().WIFI_SERVICE);
        ip = InetAddress.getHostAddress(wm.getConnectionInfo().getIpAddress());
        */

       /* try
        {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ip=inetAddress.getHostName();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
        //IP=Utils.getIPAddress(true);
        //IP=getLocalIpAddress();
      // URL="http://10.207.58.150/TFG/BD/edit-user.php";
       //URL="http://"+IP+"/TFG/BD/edit-user.php";

        /*try
        {
            InetAddress IP=InetAddress.getLocalHost();
            //System.out.println("IP of my system is := "+IP.getHostAddress());
            name.setText(IP.getHostAddress());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/


      /*  String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // *EDIT*
                    if (addr instanceof Inet6Address) continue;

                    ip = addr.getHostAddress();
                    name.setText(ip);
                    //System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }*/


      /*  InetAddress ip;
        try {

            name.setText("bytfr");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ip = InetAddress.getLocalHost();
            name.setText(InetAddress.getLocalHost().toString());
            //System.out.println("Current IP address : " + ip.getHostAddress());

        } catch (Exception e) {

            e.printStackTrace();

        }*/



        name.setText(_nombre);
        ape1.setText(_apellido1);
        ape2.setText(_apellido2);
        username.setText(_username);
        handicap.setText(_handicap);
        con1.setText(_password);
        con2.setText(_password);

        username.setEnabled(false);

        cancelar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Profile fvp=new Fragment_View_Profile();
                fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
            }
        });



        registrar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (name.getText().toString().length()==0 || ape1.getText().toString().length()==0 ||
                        ape2.getText().toString().length()==0 || username.getText().toString().length()==0 ||
                        con1.getText().toString().length()==0 || con2.getText().toString().length()==0 ||
                        handicap.getText().toString().length()==0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.rellenar_datos, Toast.LENGTH_LONG).show();
                }

                else if(name.getText().toString().trim().length()<2 || name.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.name_tam, Toast.LENGTH_LONG).show();
                }

                else if(ape1.getText().toString().trim().length()<2 || ape1.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.ape1_tam, Toast.LENGTH_LONG).show();
                }

                else if(ape2.getText().toString().trim().length()<2 || ape2.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.ape2_tam, Toast.LENGTH_LONG).show();
                }

                else if(username.getText().toString().trim().length()<2 || username.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.user_tam, Toast.LENGTH_LONG).show();
                }

                else if(Float.parseFloat(handicap.getText().toString().trim())<-10 || Float.parseFloat(handicap.getText().toString().trim())>36)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El handicap debe estar entre -10 y 36", Toast.LENGTH_LONG).show();
                }

                else if(!(con1.getText().toString().matches(con2.getText().toString())))
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.con_dif, Toast.LENGTH_LONG).show();
                }
                else if(con1.getText().toString().trim().length()<6 || con1.getText().toString().trim().length()>25)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.con_tam, Toast.LENGTH_LONG).show();
                }
                else
                {
                    AttemptEditProfile attemptEditProfile = new AttemptEditProfile();
                    attemptEditProfile.execute(name.getText().toString().trim(), ape1.getText().toString().trim(),
                            ape2.getText().toString().trim(), username.getText().toString().trim(),
                            con1.getText().toString().trim(), handicap.getText().toString().trim());
                }
            }

        });

        return v;
    }


    //////////////////////////////////////////////////////////////////////////////

    private class AttemptEditProfile extends AsyncTask<String, String, JSONObject> {

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
            params.add(new BasicNameValuePair("apellido1",  StringUtils.stripAccents(apellido1)));
            params.add(new BasicNameValuePair("apellido2",  StringUtils.stripAccents(apellido2)));
            params.add(new BasicNameValuePair("username",  StringUtils.stripAccents(username)));
            params.add(new BasicNameValuePair("password",  StringUtils.stripAccents(password)));
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
                    Toast.makeText(getActivity().getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_View_Profile fvp=new Fragment_View_Profile();
                    fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
