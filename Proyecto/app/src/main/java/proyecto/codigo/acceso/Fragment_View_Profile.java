package proyecto.codigo.acceso;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_View_Profile extends Fragment {


    String username;
    View v;
    String ip_config;
    String URL;
    String URL1;
    String URL2;
    String URL3;
    String URL4;
    String URL5;
    JSONParser jsonParser=new JSONParser();
    TextView tv_name;
    TextView tv_username;
    TextView tv_puntuacion;
    TextView tv_fecha;
    ImageButton ib_edit;
    TextView tv_fotos;
    TextView tv_seguidores;
    TextView tv_siguiendo;
    ToggleButton amistad;

    public String db_nombre;
    public String db_apellido1;
    public String db_apellido2;
    public String db_username;
    public String db_password;
    public String db_date_dia;
    public String db_date_mes;
    public String db_date_año;
    public String db_puntuacion;
    public String db_handicap;
    public String db_num_fotos;
    public String db_url;
    public String db_seguidores;
    public String db_siguiendo;

    public  SpannableString ss_num_fotos;
    public ClickableSpan clickableSpan_num_fotos;
    public  SpannableString ss_num_seguidores;
    public ClickableSpan clickableSpan_num_seguidores;
    public  SpannableString ss_num_siguiendo;
    public ClickableSpan clickableSpan_num_siguiendo;


    CircleImageView image;

    String[] following;
    String[] followers;
    public String isMyFriend;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-username-data.php";
        URL1="http://"+ip_config+"/TFG/BD/getImagesCircle.php";
        URL2="http://"+ip_config+"/TFG/BD/createFriendship.php";
        URL3="http://"+ip_config+"/TFG/BD/deleteFriendship.php";
        URL4="http://"+ip_config+"/TFG/BD/find-following-followers.php";
        URL5="http://"+ip_config+"/TFG/BD/find-is-my-friend.php";

        if (bundle!=null)
        {
            username=bundle.getString("username");
            if(username.equals(((MainActivity)getActivity()).username))
            {
                v=inflater.inflate(R.layout.fragment_view_my_profile, container, false);
                ib_edit=v.findViewById(R.id.profile_edit_button);
                tv_name=v.findViewById(R.id.profile_name);
                tv_username=v.findViewById(R.id.profile_username);
                tv_puntuacion=v.findViewById(R.id.view_my_puntuacion);
                tv_fecha=v.findViewById(R.id.view_my_date);
                tv_fotos=v.findViewById(R.id.my_profile_fotos);
                tv_seguidores=v.findViewById(R.id.my_profile_seguidores);
                tv_siguiendo=v.findViewById(R.id.my_profile_siguiendo);
                image=v.findViewById(R.id.my_profile_image);

                ib_edit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        FragmentManager fm=getActivity().getSupportFragmentManager();
                        Fragment_Edit_Profile fep=new Fragment_Edit_Profile();
                        final Bundle bundle = new Bundle();
                        bundle.putString("nombre", db_nombre);
                        bundle.putString("apellido1", db_apellido1);
                        bundle.putString("apellido2", db_apellido2);
                        bundle.putString("username", db_username);
                        bundle.putString("password", db_password);
                        bundle.putString("handicap", db_handicap);
                        bundle.putString("url", db_url);
                        fep.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.contenedor, fep).commit();
                    }
                });
            }
            else
            {
                v=inflater.inflate(R.layout.fragment_view_profile, container, false);
                tv_name=v.findViewById(R.id.profile_name);
                tv_username=v.findViewById(R.id.profile_username);
                tv_puntuacion=v.findViewById(R.id.view_user_puntuacion);
                tv_fecha=v.findViewById(R.id.view_user_date);
                tv_fotos=v.findViewById(R.id.user_profile_fotos);
                tv_seguidores=v.findViewById(R.id.user_profile_seguidores);
                tv_siguiendo=v.findViewById(R.id.user_profile_siguiendo);
                image=v.findViewById(R.id.user_profile_image);
                amistad=v.findViewById(R.id.boton_amistad);

                amistad.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if(amistad.isChecked())
                        {
                            AttemptCreateFriendship attemptCreateFriendship= new AttemptCreateFriendship();
                            attemptCreateFriendship.execute((((MainActivity)getActivity()).username), username);
                        }
                        else
                        {
                            AttemptDeleteFriendship attemptDeleteFriendship= new AttemptDeleteFriendship();
                            attemptDeleteFriendship.execute((((MainActivity)getActivity()).username), username);
                        }
                    }
                });
            }
        }
        else
        {
            username=((MainActivity)getActivity()).username;
            v=inflater.inflate(R.layout.fragment_view_my_profile, container, false);
            ib_edit=v.findViewById(R.id.profile_edit_button);
            tv_name=v.findViewById(R.id.profile_name);
            tv_username=v.findViewById(R.id.profile_username);
            tv_puntuacion=v.findViewById(R.id.view_my_puntuacion);
            tv_fecha=v.findViewById(R.id.view_my_date);
            tv_fotos=v.findViewById(R.id.my_profile_fotos);
            tv_seguidores=v.findViewById(R.id.my_profile_seguidores);
            tv_siguiendo=v.findViewById(R.id.my_profile_siguiendo);
            image=v.findViewById(R.id.my_profile_image);

            ib_edit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_Edit_Profile fep=new Fragment_Edit_Profile();
                    final Bundle bundle = new Bundle();
                    bundle.putString("nombre", db_nombre);
                    bundle.putString("apellido1", db_apellido1);
                    bundle.putString("apellido2", db_apellido2);
                    bundle.putString("username", db_username);
                    bundle.putString("password", db_password);
                    bundle.putString("handicap", db_handicap);
                    bundle.putString("url", db_url);
                    fep.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fep).commit();

                }
            });
        }

        tv_name=v.findViewById(R.id.profile_name);
        tv_username=v.findViewById(R.id.profile_username);


        AttemptFindUsernameData attemptFindData=new AttemptFindUsernameData();
        attemptFindData.execute(username);

        AttemptFindFollowers attemptFindFollowers=new AttemptFindFollowers();
        attemptFindFollowers.execute(username);

        AttemptFindFollowing attemptFindFollowing=new AttemptFindFollowing();
        attemptFindFollowing.execute(username);

        AttemptFindIsMyFriend attemptFindIsMyFriend=new AttemptFindIsMyFriend();
        attemptFindIsMyFriend.execute();




        clickableSpan_num_fotos = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Images fvi=new Fragment_View_Images();
                final Bundle bundle = new Bundle();
                bundle.putString("tipo", "usuario");
                bundle.putString("username", username);
                fvi.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvi).commit();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        clickableSpan_num_seguidores = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                if(followers.length>0)
                {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment_View_Following_Followers fvp = new Fragment_View_Following_Followers();
                    final Bundle bundle = new Bundle();
                    bundle.putStringArray("data", followers);
                    fvp.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
                }
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        clickableSpan_num_siguiendo = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                if(following.length>0)
                {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment_View_Following_Followers fvp = new Fragment_View_Following_Followers();
                    final Bundle bundle = new Bundle();
                    bundle.putStringArray("data", following);
                    fvp.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
                }
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };



        return v;
    }



    //////////////////////////////////////////////////////////////////////////////

    private class AttemptFindIsMyFriend extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args)
        {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tu", ((MainActivity)getActivity()).username));
            params.add(new BasicNameValuePair("username", username));
            String json = jsonParser.makeHttpRequestString(URL5, "POST", params);

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
                    isMyFriend = obj.getString("existe");
                }
                amistad.setChecked(Boolean.parseBoolean(isMyFriend));

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }



    ////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindFollowers extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("following_followers", "origen"));
            String json = jsonParser.makeHttpRequestString(URL4, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);
                followers=new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    followers[i] = obj.getString("username");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }


    private class AttemptFindFollowing extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("following_followers", "destino"));
            String json = jsonParser.makeHttpRequestString(URL4, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);
                following=new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    following[i] = obj.getString("username");
                    /*if(!(username.equals(((MainActivity)getActivity()).username)))
                    {
                        //Toast.makeText(getActivity().getApplicationContext(),following[i]+" // "+username,Toast.LENGTH_LONG).show();

                        if(following[i].equals(username))
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"lllllllllllllllll",Toast.LENGTH_LONG).show();
                            //amistad.setChecked(false);
                        }
                        amistad.setChecked(true);
                    }*/
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }


    //////////////////////////////////////////////////////////////////////////////////

    private class AttemptDeleteFriendship extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String tu=args[0];
            String username=args[1];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tu", tu));
            params.add(new BasicNameValuePair("username", username));
            JSONObject json = jsonParser.makeHttpRequest(URL3, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                if(result != null)
                {
                    result.getString("seccess");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    //////////////////////////////////////////////////////////////////////////////////

    private class AttemptCreateFriendship extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String tu=args[0];
            String username=args[1];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tu", tu));
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
                    result.getString("seccess");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////

    private class AttemptFindUsernameData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
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
                    db_nombre = obj.getString("nombre");
                    db_apellido1 = obj.getString("apellido1");
                    db_apellido2 = obj.getString("apellido2");
                    db_username = obj.getString("username");
                    db_password = obj.getString("password");
                    db_date_año = obj.getString("anyo");
                    db_date_mes = obj.getString("mes");
                    db_date_dia = obj.getString("dia");
                    db_puntuacion = obj.getString("puntuacion");
                    db_handicap = obj.getString("handicap");
                    db_num_fotos = obj.getString("num_fotos");
                    db_url = obj.getString("url");
                    db_seguidores = obj.getString("seguidores");
                    db_siguiendo = obj.getString("siguiendo");
                }
                    float f=Float.parseFloat(db_handicap);
                    String h=String.format(String.format("%.1f", f));
                    tv_name.setText(db_nombre+" "+db_apellido1+" "+db_apellido2+" ("+h+")");
                    tv_username.setText("@"+db_username);
                    tv_fecha.append(db_date_dia+"/"+db_date_mes+"/"+db_date_año);
                    tv_puntuacion.append(db_puntuacion);

                    ss_num_fotos = new SpannableString(db_num_fotos);
                    ss_num_fotos.setSpan(clickableSpan_num_fotos, 0, db_num_fotos.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_fotos.setText(ss_num_fotos);
                    tv_fotos.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_fotos.setHighlightColor(Color.TRANSPARENT);

                    ss_num_seguidores = new SpannableString(db_seguidores);
                    ss_num_seguidores.setSpan(clickableSpan_num_seguidores, 0, db_seguidores.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_seguidores.setText(ss_num_seguidores);
                    tv_seguidores.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_seguidores.setHighlightColor(Color.TRANSPARENT);

                    ss_num_siguiendo = new SpannableString(db_siguiendo);
                    ss_num_siguiendo.setSpan(clickableSpan_num_siguiendo, 0, db_siguiendo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_siguiendo.setText(ss_num_siguiendo);
                    tv_siguiendo.setMovementMethod(LinkMovementMethod.getInstance());
                    tv_siguiendo.setHighlightColor(Color.TRANSPARENT);

                if(db_url!="null")
                {
                    db_url="http://"+ip_config+db_url;

                    //Toast.makeText(getActivity().getApplicationContext(),db_url,Toast.LENGTH_LONG).show();
                    //image.setBorderColor(Color.parseColor("#bdbdbd"));

                    Picasso.get().load(db_url).into(image);
                }


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}