package proyecto.codigo.acceso;

import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_View_Hole extends Fragment {


    String id_campo;
    String nombre;
    View v;
    String ip_config;
    String URL;
    JSONParser jsonParser=new JSONParser();
    TextView tv_nombre;
    TextView tv_metros;
    TextView tv_par;
    TextView tv_creator;


    public String db_nombre_hoyo;
    public String db_nombre_campo;
    public String db_descripcion;
    public String db_metros;
    public String db_par;
    public String db_creador;

    public  SpannableString ss_creador;
    public ClickableSpan clickableSpan_creador;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-hole-data.php";


        if (bundle!=null)
        {
            id_campo=bundle.getString("id_campo");
            nombre=bundle.getString("nombre");

            v=inflater.inflate(R.layout.fragment_view_hole, container, false);
            tv_nombre=v.findViewById(R.id.hole_name);
            tv_par=v.findViewById(R.id.hole_par);
            tv_metros=v.findViewById(R.id.hole_meters);
            tv_creator=v.findViewById(R.id.hole_creator);

            AttemptFindHoleData attemptFindHoleData=new AttemptFindHoleData();
            attemptFindHoleData.execute(id_campo, nombre);
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
        
        return v;
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
            params.add(new BasicNameValuePair("id_campo", id_campo));
            params.add(new BasicNameValuePair("nombre", nombre));
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

                    db_nombre_hoyo=obj.getString("nombre_hoyo");
                    db_nombre_campo=obj.getString("nombre_campo");
                    db_descripcion=obj.getString("descripcion");
                    db_metros=obj.getString("metros");
                    db_par=obj.getString("par");
                    db_creador=obj.getString("creador");

                    tv_nombre.setText(db_nombre_hoyo+" ("+db_nombre_campo+")");
                    tv_metros.setText(db_metros);
                    tv_par.setText(db_par);


                    ss_creador = new SpannableString("@"+db_creador);
                    ss_creador.setSpan(clickableSpan_creador, 0, ss_creador.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_creator.append(ss_creador);
                    tv_creator.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_creator.setHighlightColor(Color.TRANSPARENT);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}