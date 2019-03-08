package proyecto.codigo.acceso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    //Imageloader to load images
    private ImageLoader imageLoader;

    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> comentarios;

    /*public GridViewAdapter (Context context, ArrayList<String> images, ArrayList<String> comentarios){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.comentarios = comentarios;
    }*/

    public GridViewAdapter (Context context, ArrayList<String> images){
        //Getting all the values
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        NetworkImageView networkImageView = new NetworkImageView(context);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(images.get(position), ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        imageLoader.get(images.get(position), ImageLoader.getImageListener(networkImageView, R.drawable.ic_launcher, android.R.drawable.ic_dialog_alert));

        networkImageView.setImageUrl(images.get(position),imageLoader);

        TextView textView = new TextView(context);
        //textView.setText(comentarios.get(position));

        networkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        networkImageView.setLayoutParams(new GridView.LayoutParams(450,450));

        //linearLayout.addView(textView);
        linearLayout.addView(networkImageView);

        //Returnint the layout
        return linearLayout;
    }
}