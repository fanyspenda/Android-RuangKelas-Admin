package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import arwinata.org.android_ruangkelas_admin.Adapter.RuangAdapter;
import arwinata.org.android_ruangkelas_admin.Class.Ruangan;

public class DaftarRuangActivity extends AppCompatActivity {

    String namaGedung;
    Button btnTambahRuang;
    CollectionReference dbRuang;

    RecyclerView rvRuang;
    RuangAdapter ruanganAdapter;
    List<Ruangan> mRuangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_ruang);

        namaGedung = getIntent().getStringExtra("namaGedung");

        dbRuang  = FirebaseFirestore.getInstance().collection("ruangan");
        btnTambahRuang = findViewById(R.id.btnAddRuang);

        rvRuang = findViewById(R.id.rvRuang);
        rvRuang.setHasFixedSize(true);
        rvRuang.setLayoutManager(new LinearLayoutManager(this));

        loadRuangan(dbRuang, namaGedung);

        btnTambahRuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iKeRuangan = new Intent(getApplicationContext(), AddRuanganActivity.class);
                iKeRuangan.putExtra("namaGedung", namaGedung);
                startActivity(iKeRuangan);
            }
        });
    }

    private void loadRuangan(CollectionReference db, String namaGedung){
        mRuangan = new ArrayList<>();

        db.whereEqualTo("lokasi", namaGedung)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Ruangan ruang = documentSnapshot.toObject(Ruangan.class);
                        ruang.setDocumentId(documentSnapshot.getId());
                        mRuangan.add(ruang);

                        Toast.makeText(getApplicationContext(), ruang.getImageJadwal(), Toast.LENGTH_LONG).show();
                    }
                    ruanganAdapter = new RuangAdapter(getApplicationContext(), mRuangan);
                    rvRuang.setAdapter(ruanganAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "Tidak Ada Data", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error Mendapatkan Data: " + e, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 123:
                ruanganAdapter.keEditRuangan(item.getGroupId());
                return true;

            case 124:
                ruanganAdapter.hapusRuangan(item.getGroupId());
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}