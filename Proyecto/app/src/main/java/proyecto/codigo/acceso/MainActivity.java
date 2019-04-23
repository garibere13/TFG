package proyecto.codigo.acceso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String username;
    public static Double latitud;
    public static Double longitud;

    private static final int LOGOUT_TAG = 0;
    private static final int PROGRESS_TAG = 1;
    private DialogFragment mDialog;
    TextView user;
    CircleImageView image;

    String home_user;


    JSONParser jsonParser=new JSONParser();
    String URL;
    String URL1;
    String ip_config;
    public String db_url;
    public String db_nuevos_seguidores;














/*
    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;

    // Notification Count
    private int mNotificationCount;

    // Notification Text Elements
    private final CharSequence tickerText = "This is a Really, Really, Super Long Notification Message!";
    private final CharSequence contentText = "You've Been Notified!";

    // Notification Action Elements
    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;

    // Notification Sound and Vibration on Arrival
    private Uri soundURI = Uri
            .parse("android.resource://proyecto.codigo.acceso/"
                    + R.raw.alarm_rooster);
    private long[] mVibratePattern = { 0, 200, 200, 300 };

    RemoteViews mContentView = new RemoteViews(
            "proyecto.codigo.acceso",
            R.layout.custom_notification);

*/





    protected void onCreate(Bundle savedInstanceState) {

        username=getIntent().getExtras().getString("username");
        home_user=username;
        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-username-data.php";
        URL1="http://"+ip_config+"/TFG/BD/find-friendship-request.php";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AttemptFindUsernameData attemptFindData=new AttemptFindUsernameData();
        attemptFindData.execute(username);

        AttemptFindFriendshipRequest attemptFindFriendshipRequest=new AttemptFindFriendshipRequest();
        attemptFindFriendshipRequest.execute(username);

       /* FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        image=headerView.findViewById(R.id.home_user_profile_image);
        user=headerView.findViewById(R.id.nombre_usuario_home);
        user.setText("¡Bienvenido @"+username+"!");
















/*
        mNotificationIntent = new Intent(this, ZZ.class);
        mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        */





















        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fm=getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.contenedor, new Fragment_View_Profile()).commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //ESTO ES LO DE SETTINGS QUE APARECE EN TODOS ARRIBA A LA DERECHA
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showDialogFragment(LOGOUT_TAG);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm=getSupportFragmentManager();

        if (id == R.id.nav_home)
        {
            Intent ma = new Intent(this, MainActivity.class);
            ma.putExtra("username", home_user);
            startActivity(ma);
            finish();
        }
        if (id == R.id.nav_ver_mapa)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment_View_Map()).commit();
        }
        if (id == R.id.nav_view_favourites)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment_View_Favourites()).commit();
        }
        else if (id == R.id.nav_create_field)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment_Create_Field()).commit();
        }
        else if (id == R.id.nav_find_friend)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment_Find()).commit();
        }
        else if (id == R.id.nav_view_profile)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment_View_Profile()).commit();
        }
     /*   else if (id == R.id.nav_logout)
        {
            showDialogFragment(LOGOUT_TAG);
        }


        else if (id == R.id.prueba_notificacion)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Z_Prueba_Notificacion()).commit();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void showDialogFragment(int dialogID) {

        switch (dialogID) {

            // Show AlertDialog
            case LOGOUT_TAG:

                // Create a new AlertDialogFragment
                mDialog = AlertDialogFragment.newInstance();

                // Show AlertDialogFragment
                mDialog.show(getFragmentManager(), "Alert");
                break;

            // Show ProgressDialog
            case PROGRESS_TAG:

                // Create a new ProgressDialogFragment
                mDialog = ProgressDialogFragment.newInstance();

                // Show new ProgressDialogFragment
                mDialog.show(getFragmentManager(), "Shutdown");
                break;
        }
    }


    // Abort or complete ShutDown based on value of shouldContinue
    private void continueShutdown(boolean shouldContinue) {
        if (shouldContinue) {

            // Prevent further interaction with the ShutDown Butotn
           // mShutdownButton.setEnabled(false);

            // Show ProgressDialog as shutdown process begins
            showDialogFragment(PROGRESS_TAG);

            // Finish the ShutDown process
            finishShutdown();

        } else {

            // Abort ShutDown and dismiss dialog
            mDialog.dismiss();
        }
    }



    private void finishShutdown() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // Pretend to do something before
                    // shutting down
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                } finally {
                    finish();
                }
            }
        }).start();
    }



    /////////////////////////////////////////////////////////////////////////////////////7


    // Class that creates the AlertDialog
    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.confirm_log_out)

                    // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)

                    // Set up Yes Button
                    .setPositiveButton(R.string.confirm_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    ((MainActivity) getActivity())
                                            .continueShutdown(true);
                                }
                            })

                    // Set up No Button
                    .setNegativeButton(R.string.confirm_no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    ((MainActivity) getActivity())
                                            .continueShutdown(false);
                                }
                            }).create();
        }
    }



    //////////////////////////////////////////////////////////////////////////////////////




    // Class that creates the ProgressDialog
    public static class ProgressDialogFragment extends DialogFragment {

        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        // Build ProgressDialog
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            //Create new ProgressDialog
            final ProgressDialog dialog = new ProgressDialog(getActivity());

            // Set Dialog message
            //dialog.setMessage(R.string.message_log_out);
            dialog.setMessage("¡Hasta pronto "+username+"!");

            // Dialog will be displayed for an unknown amount of time
            dialog.setIndeterminate(true);
            return dialog;
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////
    private class AttemptFindFriendshipRequest extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //@Override
        protected String doInBackground(String... args) {

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            String json = jsonParser.makeHttpRequestString(URL1, "POST", params);

            return json;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try
            {
                JSONArray jsonArray = new JSONArray(result);
                db_nuevos_seguidores="";
                String aux;

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    aux = obj.getString("origen");
                    //db_nuevos_seguidores=db_nuevos_seguidores+" // "+aux;



                    /*mContentView.setTextViewText(R.id.text, contentText + " ("
                            + ++mNotificationCount + ")");

                    // Build the Notification

                    Notification.Builder notificationBuilder = new Notification.Builder(
                            getApplicationContext())
                            .setTicker(tickerText)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setAutoCancel(true)
                            .setContentIntent(mContentIntent)
                            .setSound(soundURI)
                            .setVibrate(mVibratePattern)
                            .setContent(mContentView);

                    // Pass the Notification to the NotificationManager:
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(MY_NOTIFICATION_ID,
                            notificationBuilder.build());*/

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
                    db_url = obj.getString("url");
                }

                if(db_url!="null")
                {
                    db_url="http://"+ip_config+db_url;
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