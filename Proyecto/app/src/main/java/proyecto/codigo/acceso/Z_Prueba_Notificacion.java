package proyecto.codigo.acceso;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.net.URL;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;


public class Z_Prueba_Notificacion extends Fragment {

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
            R.layout.z_custom_notification);

    View v;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);



        v=inflater.inflate(R.layout.z_main, container, false);



        mNotificationIntent = new Intent(getActivity().getApplicationContext(),
                ZZ.class);
        mContentIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0,
                mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        final Button button = (Button) v.findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Define the Notification's expanded message and Intent:

                mContentView.setTextViewText(R.id.text, contentText + " ("
                        + ++mNotificationCount + ")");

                // Build the Notification

                Notification.Builder notificationBuilder = new Notification.Builder(
                        getActivity().getApplicationContext())
                        .setTicker(tickerText)
                        .setSmallIcon(R.drawable.app_icon)
                        .setAutoCancel(true)
                        .setContentIntent(mContentIntent)
                        .setSound(soundURI)
                        .setVibrate(mVibratePattern)
                        .setContent(mContentView);

                // Pass the Notification to the NotificationManager:
                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(MY_NOTIFICATION_ID,
                        notificationBuilder.build());
            }
        });















       /* v=inflater.inflate(R.layout.fragment_find, container, false);
        textView_username=v.findViewById(R.id.autocomplete_username);
        textView_field_name=v.findViewById(R.id.autocomplete_field_name);

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/find-usernames.php";
        URL1="http://"+ip_config+"/TFG/BD/find-field-names.php";


        textView_username.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Profile fvp=new Fragment_View_Profile();
                final Bundle bundle = new Bundle();
                bundle.putString("username", (String) arg0.getAdapter().getItem(arg2));
                fvp.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
            }
        });

        textView_field_name.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                for (int i=0;i<field_names.length;i++)
                {
                    if(field_names[i]==(String) arg0.getAdapter().getItem(arg2))
                    {
                        id_campo=field_ids[i];
                    }
                }
                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Field fvf=new Fragment_View_Field();
                final Bundle bundle = new Bundle();
                bundle.putString("id", id_campo);
                fvf.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvf).commit();
            }
        });*/
        return v;
    }



}