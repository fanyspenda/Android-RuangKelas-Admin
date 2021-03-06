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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import arwinata.org.android_ruangkelas_admin.Adapter.GedungAdapter;
import arwinata.org.android_ruangkelas_admin.Class.Gedung;

public class Main2Activity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnaddgedung;
    private RecyclerView rvGedung;
    private GedungAdapter gedAdapter;

    private CollectionReference dbGedung;
    private List<Gedung> mGedung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnaddgedung = findViewById(R.id.btnAddGedung);

        btnaddgedung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddGedung.class);
                startActivity(i);
            }
        });
        rvGedung = findViewById(R.id.rvGedung);
        rvGedung.setHasFixedSize(true);
        rvGedung.setLayoutManager(new LinearLayoutManager(this));

        loadGedung(dbGedung);
    }

    private void loadGedung(CollectionReference db){
        mGedung = new ArrayList<>();
        db = FirebaseFirestore.getInstance().collection("gedung");

        db.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Gedung gedung = documentSnapshot.toObject(Gedung.class);
                        gedung.setDocumentId(documentSnapshot.getId());
                        mGedung.add(gedung);
                    }
                    gedAdapter = new GedungAdapter(getApplicationContext(), mGedung);
                    rvGedung.setAdapter(gedAdapter);

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
            case 121:
                gedAdapter.keEditGedung(item.getGroupId());
                return true;

            case 122:
                gedAdapter.hapusGedung(item.getGroupId());
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
