package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailGedung extends AppCompatActivity {

    private TextView txtGedung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gedung);

        txtGedung = findViewById(R.id.txtGedung);
        Intent i = getIntent();
        String idnyaGedung = i.getStringExtra("idGedung");
        txtGedung.setText(idnyaGedung);
    }
}
