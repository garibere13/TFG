package proyecto.codigo.acceso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.ListFragment;
import android.support.v4.app.Fragment;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.TextView;


public class Fragment_View_Field_Holes extends Fragment {
    //public class Fragment_View_Field_Holes extends ListFragment {

    int id_campo;
    int num_hoyos;
    View v;
    ListView lv;
    String[] hoyos;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            id_campo=Integer.parseInt(bundle.getString("id_campo"));
            num_hoyos=Integer.parseInt(bundle.getString("num_hoyos"));
        }

        return inflater.inflate(R.layout.fragment_view_field_holes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        hoyos=new String[num_hoyos];

        for(int i=0;i<num_hoyos;i++)
        {
            hoyos[i]="Hoyo "+(i+1);
        }
        lv = (ListView)getView().findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1, hoyos);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(),hoyos[position]+" // "+id_campo, Toast.LENGTH_SHORT).show();

            }
        });
    }
}