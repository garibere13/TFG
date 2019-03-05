package proyecto.codigo.acceso;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.View.OnClickListener;
import java.util.ArrayList;

public class Fragment_View_Hole extends Fragment {


    String id_campo;
    String nombre_hoyo;
    String username;
    View v;
    String ip_config;
    String URL;
    String URL1;
    String URL2;
    String URL3;
    JSONParser jsonParser=new JSONParser();
    TextView tv_nombre;
    TextView tv_metros;
    TextView tv_par;
    TextView tv_creator;
    TextView tv_fecha;
    ToggleButton button_favourite;



    public String db_nombre_hoyo;
    public String db_nombre_campo;
    public String db_descripcion;
    public String db_metros;
    public String db_par;
    public String db_creador;
    public String db_id_campo;
    public String db_date_dia;
    public String db_date_mes;
    public String db_date_año;

    public String isHoleInUsersFavourite;

    public  SpannableString ss_creador;
    public ClickableSpan clickableSpan_creador;
    public  SpannableString ss_campo;
    public ClickableSpan clickableSpan_campo;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-hole-data.php";
        URL1="http://"+ip_config+"/TFG/BD/create-favourite-hole.php";
        URL2="http://"+ip_config+"/TFG/BD/delete-favourite-hole.php";
        URL3="http://"+ip_config+"/TFG/BD/find-is-hole-user-favourite.php";


        if (bundle!=null)
        {
            id_campo=bundle.getString("id_campo");
            nombre_hoyo=bundle.getString("nombre");
            username=((MainActivity)getActivity()).username;

            v=inflater.inflate(R.layout.fragment_view_hole, container, false);
            tv_nombre=v.findViewById(R.id.hole_name);
            tv_par=v.findViewById(R.id.hole_par);
            tv_metros=v.findViewById(R.id.hole_meters);
            tv_creator=v.findViewById(R.id.hole_creator);
            tv_fecha=v.findViewById(R.id.view_hole_date);
            button_favourite=v.findViewById(R.id.togglebutton_hole_favourite);


            AttemptFindHoleData attemptFindHoleData=new AttemptFindHoleData();
            attemptFindHoleData.execute();
        }


        AttemptFindIsHoleUserFavourite attemptFindIsHoleUserFavourite=new AttemptFindIsHoleUserFavourite();
        attemptFindIsHoleUserFavourite.execute();


        clickableSpan_creador = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Profile fvp=new Fragment_View_Profile();
                final Bundle bundle = new Bundle();
                bundle.putString("username", db_creador);
                fvp.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        clickableSpan_campo = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Field fvf=new Fragment_View_Field();
                final Bundle bundle = new Bundle();
                bundle.putString("id", db_id_campo);
                fvf.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvf).commit();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        button_favourite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button_favourite.isChecked())
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Añadido a favoritos",Toast.LENGTH_LONG).show();
                    AttemptCreateFavourite attemptCreateFavourite=new AttemptCreateFavourite();
                    attemptCreateFavourite.execute();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Quitado de favoritos",Toast.LENGTH_LONG).show();
                    AttemptDeleteFavourite attemptDeleteFavourite=new AttemptDeleteFavourite();
                    attemptDeleteFavourite.execute();
                }
            }
        });

        return v;
    }


    //////////////////////////////////////////////////////////////////////////////

    private class AttemptFindIsHoleUserFavourite extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args)
        {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre_hoyo", nombre_hoyo));
            params.add(new BasicNameValuePair("id_campo", id_campo));
            params.add(new BasicNameValuePair("username", username));
            String json = jsonParser.makeHttpRequestString(URL3, "POST", params);

            return json;
        }

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    isHoleInUsersFavourite = obj.getString("existe");
                }
                button_favourite.setChecked(Boolean.parseBoolean(isHoleInUsersFavourite));

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindHoleData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre_hoyo", nombre_hoyo));
            params.add(new BasicNameValuePair("id_campo", id_campo));
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

                    db_nombre_hoyo = obj.getString("nombre_hoyo");
                    db_id_campo=obj.getString("id_campo");
                    db_nombre_campo = obj.getString("nombre_campo");
                    db_descripcion = obj.getString("descripcion");
                    db_metros = obj.getString("metros");
                    db_par = obj.getString("par");
                    db_date_año = obj.getString("anyo");
                    db_date_mes = obj.getString("mes");
                    db_date_dia = obj.getString("dia");
                    db_creador = obj.getString("creador");
                }

                    tv_metros.setText(db_metros);
                    tv_par.setText(db_par);
                    tv_fecha.append(db_date_dia+"/"+db_date_mes+"/"+db_date_año);

                    ss_creador = new SpannableString("@"+db_creador);
                    ss_creador.setSpan(clickableSpan_creador, 0, ss_creador.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_creator.append(ss_creador);
                    tv_creator.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_creator.setHighlightColor(Color.TRANSPARENT);

                    ss_campo = new SpannableString(db_nombre_campo);
                    ss_campo.setSpan(clickableSpan_campo, 0, ss_campo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_nombre.setText(db_nombre_hoyo+" (");
                    tv_nombre.append(ss_campo);
                    tv_nombre.append(")");
                    tv_nombre.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_nombre.setHighlightColor(Color.TRANSPARENT);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////7

    private class AttemptCreateFavourite extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre_hoyo", nombre_hoyo));
            params.add(new BasicNameValuePair("id_campo", id_campo));
            params.add(new BasicNameValuePair("username", username));
            JSONObject json = jsonParser.makeHttpRequest(URL1, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                if(result != null)
                {
                    Toast.makeText(getActivity().getApplicationContext(),result.getString("mensaje"),Toast.LENGTH_LONG).show();

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////7

    private class AttemptDeleteFavourite extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre_hoyo", nombre_hoyo));
            params.add(new BasicNameValuePair("id_campo", id_campo));
            params.add(new BasicNameValuePair("username", username));
            JSONObject json = jsonParser.makeHttpRequest(URL2, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                if(result != null)
                {
                    Toast.makeText(getActivity().getApplicationContext(),result.getString("mensaje"),Toast.LENGTH_LONG).show();

                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}