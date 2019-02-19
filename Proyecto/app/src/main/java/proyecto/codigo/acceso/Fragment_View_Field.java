package proyecto.codigo.acceso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.text.SpannableString;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.TextPaint;
import java.util.ArrayList;
import android.graphics.Color;
import android.widget.Toast;


public class Fragment_View_Field extends Fragment {


    String id_field;
    View v;
    String ip_config;
    String URL;
    JSONParser jsonParser=new JSONParser();
    TextView tv_nombre_campo;
    TextView tv_n_hoyos;
    TextView tv_creator;
    TextView tv_nombre_provincia;

    public String db_nombre;
    public String db_descripcion;
    public String db_num_hoyos;
    public String db_provincia;
    public String db_pueblo;
    public String db_latitud;
    public String db_longitud;
    public String db_creador;

    public  SpannableString ss_creador;
    public ClickableSpan clickableSpan_creador;
    public  SpannableString ss_ubicacion;
    public ClickableSpan clickableSpan_ubicacion;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-field-data.php";


        if (bundle!=null)
        {
            id_field=bundle.getString("id");

            v=inflater.inflate(R.layout.fragment_view_field, container, false);
            tv_nombre_campo=v.findViewById(R.id.field_name);
            tv_n_hoyos=v.findViewById(R.id.field_n_hoyos);
            tv_creator=v.findViewById(R.id.field_creator);
            tv_nombre_provincia=v.findViewById(R.id.field_nombre_provincia);

            AttemptFindFieldData attemptFindFieldData=new AttemptFindFieldData();
            attemptFindFieldData.execute(id_field);
        }


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

        clickableSpan_ubicacion = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Intent msl=new Intent(getActivity(), Maps_Show_Location.class);
                msl.putExtra("latitud", db_latitud);
                msl.putExtra("longitud", db_longitud);
                startActivity(msl);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        return v;
    }



    /////////////////////////////////////////////////////////////////////////////////////////



    private class AttemptFindFieldData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id_field));
            String json = jsonParser.makeHttpRequestString(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    db_nombre=obj.getString("nombre");
                    db_descripcion=obj.getString("descripcion");
                    db_num_hoyos=obj.getString("num_hoyos");
                    db_provincia=obj.getString("provincia");
                    db_pueblo=obj.getString("pueblo");
                    db_latitud=obj.getString("latitud");
                    db_longitud=obj.getString("longitud");
                    db_creador=obj.getString("creador");



                    tv_nombre_campo.setText(db_nombre);
                    tv_n_hoyos.setText(db_num_hoyos);


                    ss_creador = new SpannableString("@"+db_creador);
                    ss_creador.setSpan(clickableSpan_creador, 0, ss_creador.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_creator.append(ss_creador);
                    tv_creator.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_creator.setHighlightColor(Color.TRANSPARENT);

                    ss_ubicacion = new SpannableString(db_pueblo+" ("+db_provincia+")");
                    ss_ubicacion.setSpan(clickableSpan_ubicacion, 0, ss_ubicacion.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_nombre_provincia.append(ss_ubicacion);
                    tv_nombre_provincia.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_nombre_provincia.setHighlightColor(Color.TRANSPARENT);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}