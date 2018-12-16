package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import arwinata.org.android_ruangkelas_admin.Class.Gedung;

public class AddGedung extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgGedung;
    EditText txtNamaGdg;
    Button btnSimpan;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("fotoGedung");
    Uri mImageGedungUri;
    Gedung fotoGedung;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gedung);

        imgGedung = findViewById(R.id.gambarGedung);
        btnSimpan = findViewById(R.id.btnSimpanG);
        txtNamaGdg = findViewById(R.id.edtNamaGedung);

        imgGedung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSimpan.setEnabled(false);
                int abuAbu = Color.parseColor("#D3D3D3");
                btnSimpan.setBackgroundColor(abuAbu);
                String namaGedungBaru = txtNamaGdg.getText().toString().trim().toUpperCase();
                checkNamaGedung(namaGedungBaru);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null){
            mImageGedungUri = data.getData();
            Picasso.get().load(mImageGedungUri)
                    .fit()
                    .centerCrop().into(imgGedung);
        }
    }

    public void addGedung(String namaGedungBaru, String linkFoto)
    {
        Map<String, Object> data = new HashMap<>();
        data.put("nama", namaGedungBaru);
        data.put("imageUrl", linkFoto);

        db.collection("gedung")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),
                                "Berhasil menambahkan data!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error! Tidak dapat menambahkan data!", Toast.LENGTH_LONG).show();
                    }
                });
        int hijau = Color.parseColor("#00bc00");
        btnSimpan.setBackgroundColor(hijau);
        btnSimpan.setEnabled(true);
    }

    private void checkNamaGedung(final String namaGedungBaru){
        if(namaGedungBaru.equals("")){
            Toast.makeText(getApplicationContext(), "Nama harus karakter Alfabet!",
                    Toast.LENGTH_LONG).show();
        }else {
            db.collection("gedung").whereEqualTo("nama", namaGedungBaru).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if(!queryDocumentSnapshots.isEmpty()){
                                Toast.makeText(getApplicationContext(), "Nama sudah dipakai!",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                if(mImageGedungUri!=null){
                                    addFoto(namaGedungBaru, mImageGedungUri);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Error check nama gedung: \n"+e.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
    }


    public void addFoto(final String gedungNama, Uri mImageGedungUri){
        //menamai gambar
        final StorageReference fileRef = mStorageRef.child(gedungNama+".jpg");

        //mengupload gambar
        fileRef.putFile(mImageGedungUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "gambar Berhasil Diupload!",
                        Toast.LENGTH_LONG).show();

                //mengget URL gambar
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        fotoGedung = new Gedung(uri.toString());
                        url = fotoGedung.getImageUrl();
                        addGedung(gedungNama, url);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fotoGedung = new Gedung("kosong");
                        url = fotoGedung.getImageUrl();
                        Toast.makeText(getApplicationContext(), "gagal get download Url!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fotoGedung = new Gedung("kosong");
                url = fotoGedung.getImageUrl();
                Toast.makeText(getApplicationContext(), "gagal upload!\n"+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}