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
    EditText con1;
    EditText con2;

    String _nombre;
    String _apellido1;
    String _apellido2;
    String _username;
    String _password;

    Button registrar_button;
    Button cancelar_button;

    String URL= "http://192.168.1.40/TFG/BD/edit-user.php";
    //String URL= "http://10.207.58.150/TFG/BD/edit-user.php";


    JSONParser jsonParser=new JSONParser();

    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();

        _nombre=bundle.getString("nombre");
        _apellido1=bundle.getString("apellido1");
        _apellido2=bundle.getString("apellido2");
        _username=bundle.getString("username");
        _password=bundle.getString("password");

        v=inflater.inflate(R.layout.signupscreen, container, false);

        name=v.findViewById(R.id.signup_name_text);
        ape1=v.findViewById(R.id.signup_ape1_text);
        ape2=v.findViewById(R.id.signup_ape2_text);
        username=v.findViewById(R.id.signup_username_text);
        con1=v.findViewById(R.id.signup_con1_text);
        con2=v.findViewById(R.id.signup_con2_text);

        registrar_button=v.findViewById(R.id.registrar_button);
        cancelar_button=v.findViewById(R.id.cancelar_button);


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
                        con1.getText().toString().length()==0 || con2.getText().toString().length()==0)
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
                            con1.getText().toString().trim());
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

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("apellido1", apellido1));
            params.add(new BasicNameValuePair("apellido2", apellido2));
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
