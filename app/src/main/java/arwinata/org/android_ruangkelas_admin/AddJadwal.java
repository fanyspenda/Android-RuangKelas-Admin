package arwinata.org.android_ruangkelas_admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AddJadwal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jadwal);

        Toast.makeText(getApplicationContext(),"Ini Add Jadwal", Toast.LENGTH_LONG).show();
    }
}
