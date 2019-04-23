package proyecto.codigo.acceso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;


public class Fragment_View_Comments extends Fragment {

    String ip_config;
    String URL;
    String id_campo;
    String creador;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        v=inflater.inflate(R.layout.comentarios, container, false);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-usernames.php";

        return v;
    }

}