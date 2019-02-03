package proyecto.codigo.acceso;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloAndroid extends Activity {

    String username;
    TextView q;
    public void onCreate(Bundle savedInstanceState)
    {
        username=getIntent().getExtras().getString("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.helloandroid);

        q=findViewById(R.id.label);
        q.setText(username );
    }
}