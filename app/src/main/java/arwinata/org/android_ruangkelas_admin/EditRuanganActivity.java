package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditRuanganActivity extends AppCompatActivity {

    String urlGambar;

    File mFileUri, simpanGambarDir;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage mStorageRef = FirebaseStorage.getInstance();

    TextView tvEditNamaGedung;
    EditText etEditNamaRuang, etEditLantai;
    ImageView imgvJadwal;

    Button btnUploadJadwal, btnUploadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ruangan);

        etEditLantai= findViewById(R.id.etEditLantaiRuang);
        etEditNamaRuang = findViewById(R.id.etEditNamaRuangan);
        imgvJadwal = findViewById(R.id.imgvEditJadwalRuang);
        btnUploadData = findViewById(R.id.btnEditDataRuang);
        btnUploadJadwal = findViewById(R.id.btnGantiFotoJadwal);

        tvEditNamaGedung = findViewById(R.id.tvEditLokasi);
        tvEditNamaGedung.setText(getIntent().getStringExtra("namaGedung"));
        etEditNamaRuang.setText(getIntent().getStringExtra("namaRuang"));
        etEditLantai.setText(String.valueOf(getIntent().getIntExtra("lantaiRuang", 0)));

        Picasso.get().load(getIntent().getStringExtra("imageJadwal"))
                .rotate(90)
                .fit()
                .centerCrop()
                .into(imgvJadwal);

        btnUploadJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilGambar();
            }
        });

        btnUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });
    }

    private void uploadData(){
        db.collection("ruangan")
                .document(getIntent().getStringExtra("documentIdRuangan"))
                .update(
                        "nama", etEditNamaRuang.getText().toString(),
                        "lantai", Integer.parseInt(etEditLantai.getText().toString())
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Update Data berhasil!",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal Update Data",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ambilGambar(){
        //mengallow access kamera dan mengabaikan security issue yang terjadi
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //jika kamera mengambil gambar
        if(camera_intent.resolveActivity(getPackageManager())!=null){
            mFileUri = getNamaPhotoJadwal();
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileUri));
            startActivityForResult(camera_intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            hapusJadwalLama();
            Picasso.get().load(mFileUri).
                    memoryPolicy(MemoryPolicy.NO_CACHE).
                    networkPolicy(NetworkPolicy.NO_CACHE).
                    resize(130, 200).
                    centerCrop().
                    into(imgvJadwal);
            System.out.println(mFileUri.getPath());
        }
    }

    private void hapusJadwalLama() {
        StorageReference imageRef = mStorageRef
                .getReferenceFromUrl(getIntent().getStringExtra("imageJadwal"));

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Jadwal Lama dihapus!",
                        Toast.LENGTH_SHORT).show();

                //menambahkan gambar baru ke firestore dan mendapatkan URLnya
                tambahGambarBaru();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "gagal menghapus Jadwal lama!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tambahGambarBaru(){
        final StorageReference mRef = mStorageRef.getReference("fotoJadwal")
                .child(etEditNamaRuang.getText().toString()+".jpg");

        mRef.putFile(Uri.fromFile(mFileUri))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Berhasil Upload jadwal Baru!",
                                Toast.LENGTH_SHORT).show();
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlGambar = uri.toString();
                                Toast.makeText(getApplicationContext(), "Berhasil get URL jadwal!",
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

    private void ubahUrlGambar(String urlBaru) {
        db.collection("ruangan")
                .document(getIntent().getStringExtra("documentIdRuangan"))
                .update("imageJadwal", urlBaru).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private File getNamaPhotoJadwal(){
        //meng-get lokasi memori eksternal untuk dijadikan tempat menyimpan foto
        simpanGambarDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), "RuangKelasPolinema");

        //cek apakah sudah ada folder.
        if(!simpanGambarDir.exists()){
            //jika gagal membuat direktori, tampilkan toast
            if(!simpanGambarDir.mkdirs()){
                Toast.makeText(getApplicationContext(), "Gagal membuat Direktori!",
                        Toast.LENGTH_LONG).show();
            }
        }

        File mediaFile;
        //membuat file kosong pada direktori
        mediaFile = new File(simpanGambarDir.getPath() + File.separator + "Foto_Jadwal.jpg");
        return mediaFile;
    }
}
