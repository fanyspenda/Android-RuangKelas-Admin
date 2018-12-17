package arwinata.org.android_ruangkelas_admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

public class DetailRuanganActivity extends AppCompatActivity {

    PhotoView phtvJadwal;
    TextView tvLantai, tvNamaRuang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ruangan);

        tvLantai = findViewById(R.id.tvDetailLantaiRuang);
        tvNamaRuang = findViewById(R.id.tvDetailNamaRuang);
        phtvJadwal = findViewById(R.id.phtvDetailJadwalRuang);

        tvLantai.setText(getIntent().getStringExtra("lantai"));
        tvNamaRuang.setText(getIntent().getStringExtra("namaRuangan"));

        Picasso.get().load(getIntent().getStringExtra("imageJadwal"))
                .rotate(90).into(phtvJadwal);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(phtvJadwal);
        photoViewAttacher.update();
    }
}
