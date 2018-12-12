package arwinata.org.android_ruangkelas_admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AddRuangKelas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ruang_kelas);

        Toast.makeText(getApplicationContext(),"Ini Add Ruang Kelas", Toast.LENGTH_LONG).show();
    }
}
