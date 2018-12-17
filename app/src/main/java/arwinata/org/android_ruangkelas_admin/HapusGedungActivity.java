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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import arwinata.org.android_ruangkelas_admin.Class.Ruangan;

public class HapusGedungActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String namaGedungWithMark, documentId, gambarURL, namaGedung;
    TextView tvNamaGedung;
    Button btnBatal, btnHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hapus_gedung);
        namaGedung = getIntent().getStringExtra("namaGedung");
        namaGedungWithMark = namaGedung+" ?";
        documentId = getIntent().getStringExtra("documentIdGedung");
        gambarURL = getIntent().getStringExtra("imageGedung");

        tvNamaGedung = findViewById(R.id.tvHapusNamaGedung);
        btnBatal = findViewById(R.id.btnBatalHapusGedung);
        btnHapus = findViewById(R.id.btnHapusGedung);

        tvNamaGedung.setText(namaGedungWithMark);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJadwalRuang();
            }
        });
    }

    private void getJadwalRuang() {
        db.collection("ruangan").whereEqualTo("lokasi", namaGedung)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Ruangan ruang = documentSnapshot.toObject(Ruangan.class);
                        ruang.setDocumentId(documentSnapshot.getId());

                        hapusGambarRuang(ruang.getImageJadwal());
                        hapusRuangan(ruang.getDocumentId());
                        Toast.makeText(getApplicationContext(), "menghapus Ruang..", Toast.LENGTH_LONG).show();
                    }
                    hapusGambarGedung();
                    hapusGedung();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Tidak ada Data Ruang! " +
                        "\n Langsung Menhapus Gedung..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusGambarRuang(String imageJadwal) {
        StorageReference mRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageJadwal);
        mRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal Hapus Gambar Ruang",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusRuangan(String docId){
        db.collection("ruangan").document(docId)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Ruangan Dihapus", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal hapus Ruangan",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusGambarGedung() {
        StorageReference mRef = FirebaseStorage.getInstance()
                .getReferenceFromUrl(gambarURL);
        mRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "gambar Gedung Dihapus!",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal hapus gambar Gedung",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusGedung() {
        db.collection("gedung").document(documentId)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Delete Berhasil!",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Delete Gagal!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
