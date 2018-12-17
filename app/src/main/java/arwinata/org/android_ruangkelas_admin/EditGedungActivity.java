package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditGedungActivity extends AppCompatActivity {

    EditText namaGedung;
    ImageView imageGedung;
    Button btnAmbilFoto, btnEditData;

    Uri mImageUri;
    String urlGambar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage mStorageRef = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gedung);

        namaGedung = findViewById(R.id.detailNamaGedung);
        imageGedung = findViewById(R.id.detailGambarGedung);
        btnAmbilFoto = findViewById(R.id.btnGantiFotoGedung);
        btnEditData = findViewById(R.id.btnEditDataGedung);

        namaGedung.setText(getIntent().getStringExtra("namaGedung"));
        Picasso.get().load(getIntent().getStringExtra("imageGedung"))
                .fit().centerCrop().into(imageGedung);

        btnAmbilFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //memilih gambar baru
                ambilGambar();
            }
        });

        btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubahData();
            }
        });
    }

    private void ubahData(){
        db.collection("gedung")
                .document(getIntent().getStringExtra("documentIdGedung"))
                .update(
                        "nama", namaGedung.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Berhasil ubah Nama!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal ubah Nama!"
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ambilGambar(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data.getData()!=null){
            mImageUri = data.getData();

            //menghapus gambar lama agar tidak memenuhi resource
            hapusGambarLama();
            Picasso.get().load(mImageUri)
                    .fit()
                    .centerCrop()
                    .into(imageGedung);
        }
    }

    private void hapusGambarLama(){
        StorageReference imageRef = mStorageRef
                .getReferenceFromUrl(getIntent().getStringExtra("imageGedung"));

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "gambar Lama dihapus!",
                        Toast.LENGTH_SHORT).show();

                //menambahkan gambar baru ke firestore dan mendapatkan URLnya
                tambahGambarBaru();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "gagal menghapus gambar lama!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tambahGambarBaru(){
        final StorageReference mRef = mStorageRef.getReference("fotoGedung")
                .child(namaGedung.getText()+".jpg");

        mRef.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Berhasil Upload gambar Baru!",
                        Toast.LENGTH_SHORT).show();
                mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urlGambar = uri.toString();
                        Toast.makeText(getApplicationContext(), "Berhasil get URL Gambar!",
                                Toast.LENGTH_SHORT).show();

                        //mengubah URL gambar yang tersimpan di firestore
                        ubahUrlGambar(urlGambar);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal get URL Gambar!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal mengupload gambar baru!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ubahUrlGambar(String urlBaru){
        db.collection("gedung")
                .document(getIntent().getStringExtra("documentIdGedung"))
                .update("imageUrl", urlBaru).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "URL Gambar Diubah!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "gagal mengubah URL Gambar!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableButton(){
        btnAmbilFoto.setEnabled(false);
        int abuAbu = Color.parseColor("#D3D3D3");
        btnAmbilFoto.setBackgroundColor(abuAbu);
    }

    private void enableButton(){
        btnAmbilFoto.setEnabled(true);
        int biru = Color.parseColor("#3F51B5");
        btnAmbilFoto.setBackgroundColor(biru);
    }
}
