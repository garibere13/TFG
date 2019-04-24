package proyecto.codigo.acceso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class Fragment_View_Comments extends Fragment {

    String ip_config;
    String URL;
    String id_campo;
    String nombre_hoyo;
    String creador;
    View v;
    ImageButton btn_enviar;
    EditText comentario;
    JSONParser jsonParser=new JSONParser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            id_campo=bundle.getString("id_campo");
            nombre_hoyo=bundle.getString("nombre_hoyo");
        }

        v=inflater.inflate(R.layout.comentarios, container, false);

        btn_enviar=v.findViewById(R.id.send);
        comentario=v.findViewById(R.id.commenttext);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/create-comment.php";


        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comentario.getText().toString().trim().length()<2 || comentario.getText().toString().trim().length()>250)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El comentario debe tener entre 2 y 250 caracteres", Toast.LENGTH_LONG).show();
                }
                else
                {
                    AttemptCreateComment attemptCreateComment = new AttemptCreateComment();
                    attemptCreateComment.execute(comentario.getText().toString().trim(), id_campo, nombre_hoyo);
                }

            }
        });

        return v;
    }



    //////////////////////////////////////////////////////////////////////////////

    private class AttemptCreateComment extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String comentario= args[0];
            String id_campo = args[1];
            String nombre_hoyo = args[2];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("comentario", comentario));
            params.add(new BasicNameValuePair("id_campo", id_campo));
            params.add(new BasicNameValuePair("nombre_hoyo", nombre_hoyo));
            params.add(new BasicNameValuePair("username", ((MainActivity)getActivity()).username));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result1)
        {
            comentario.setText("");
            Toast.makeText(getActivity().getApplicationContext(),"Comentario registrado correctamente",Toast.LENGTH_LONG).show();
        }
    }

}