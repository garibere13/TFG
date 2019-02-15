package proyecto.codigo.acceso;

import android.content.Intent;
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


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String username;
    private static final int LOGOUT_TAG = 0;
    private static final int PROGRESS_TAG = 1;
    private DialogFragment mDialog;
    //TextView user;

    protected void onCreate(Bundle savedInstanceState) {

        username=getIntent().getExtras().getString("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm=getSupportFragmentManager();

        if (id == R.id.nav_camera)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment1()).commit();
        }
        else if (id == R.id.nav_gallery)
        {
        }
        else if (id == R.id.nav_slideshow)
        {
        }
        else if (id == R.id.nav_create_field)
        {
            Intent i=new Intent(MainActivity.this, Maps.class);
            startActivity(i);
        }
        else if (id == R.id.nav_find_friend)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment_Find_Friend()).commit();
        }
        else if (id == R.id.nav_view_profile)
        {
            fm.beginTransaction().replace(R.id.contenedor, new Fragment_View_Profile()).commit();
        }
        else if (id == R.id.nav_logout)
        {
            showDialogFragment(LOGOUT_TAG);
        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

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

   /* public void sendDataToViewProfile(String data boolean){
        Fragment_View_Profile.setData(data);
    }*/


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

                    // Set up No Button
                    .setNegativeButton(R.string.confirm_no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    ((MainActivity) getActivity())
                                            .continueShutdown(false);
                                }
                            })

                    // Set up Yes Button
                    .setPositiveButton(R.string.confirm_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    ((MainActivity) getActivity())
                                            .continueShutdown(true);
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


    /*public static String return_username()
    {
        return username;
    }*/

}