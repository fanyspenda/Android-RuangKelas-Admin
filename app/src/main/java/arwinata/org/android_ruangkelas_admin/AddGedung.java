package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddGedung extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgGedung;
    EditText txtNamaGdg, txtDescGdg;
    Button btnSimpan, btnKembali;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gedung);

        imgGedung = findViewById(R.id.gambarGedung);
        imgGedung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        btnKembali = findViewById(R.id.btnKembali);
        btnSimpan = findViewById(R.id.btnSimpanG);
        txtDescGdg = findViewById(R.id.edtDescGedung);
        txtNamaGdg = findViewById(R.id.edtNamaGedung);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGedung();
            }
        });


        Toast.makeText(getApplicationContext(),"Ini Add Gedung", Toast.LENGTH_LONG).show();
    }

    public void imageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    public void addGedung()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("nama", txtNamaGdg.getText().toString());
        data.put("deskripsi", txtDescGdg.getText().toString());

        db.collection("gedung")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Berhasil menambahkan data!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! Tidak dapat menambahkan data!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
