package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HapusRuanganActivity extends AppCompatActivity {

    TextView tvNamaRuangan;
    Button btnHapus, btnBatal;
    String documentId;
    String jadwalURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hapus_ruangan);

        tvNamaRuangan = findViewById(R.id.tvHapusNamaRuang);
        btnBatal = findViewById(R.id.btnBatalHapusRuang);
        btnHapus = findViewById(R.id.btnHapusRuang);

        documentId = getIntent().getStringExtra("documentIdRuangan");
        jadwalURL = getIntent().getStringExtra("imageJadwal");
        String namaRuangDenganMark = getIntent().getStringExtra("namaRuang") + " ?";
        tvNamaRuangan.setText(namaRuangDenganMark);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusGambar();
            }
        });
    }

    private void hapusGambar() {
        StorageReference mRef = FirebaseStorage.getInstance()
                .getReferenceFromUrl(jadwalURL);

        mRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Jadwal Dihapus...",
                        Toast.LENGTH_SHORT).show();
                hapusData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Jadwal Gagal Dihapus... /n hapus Ruang Gagal!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ruangan").document(documentId)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "hapus Ruang Berhasil!",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),
                        Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "hapus Ruang Gagal!/n"+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
