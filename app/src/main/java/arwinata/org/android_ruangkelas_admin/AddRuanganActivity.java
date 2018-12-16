package arwinata.org.android_ruangkelas_admin;



import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import arwinata.org.android_ruangkelas_admin.Class.Ruangan;

public class AddRuanganActivity extends AppCompatActivity {

    TextView tvNamaGedung;
    EditText etNamaRuang, etLantai;
    Button btnSimpanRuang;
    ImageView imgvJadwal;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("fotoJadwal");
    File mFileUri;
    File simpanGambarDir = null;
    Ruangan fotoJadwalRuangan;
    String urlJadwal;

    String namaRuang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ruangan);

        btnSimpanRuang = findViewById(R.id.btnSimpanRuang);
        tvNamaGedung = findViewById(R.id.tvLokasi);
        etNamaRuang = findViewById(R.id.etNamaRuangan);
        etLantai = findViewById(R.id.etLantaiRuang);
        imgvJadwal = findViewById(R.id.imgvJadwalRuang);
        tvNamaGedung.setText(getIntent().getStringExtra("namaGedung"));

        imgvJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilFoto();
            }
        });

        btnSimpanRuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSimpanRuang.setEnabled(false);
                disableButtonTambahRuang();
                validasiDataRuang();
            }
        });
    }

    private void ambilFoto(){
        //mengallow access kamera dan mengabaikan security issue yang terjadi
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //jika kamera mengambil gambar
        if(camera_intent.resolveActivity(getPackageManager())!=null){
            mFileUri = getNamaPhoto();
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileUri));
            startActivityForResult(camera_intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Picasso.get().load(mFileUri).
                    memoryPolicy(MemoryPolicy.NO_CACHE).
                    networkPolicy(NetworkPolicy.NO_CACHE).
                    resize(130, 200).
                    centerCrop().
                    into(imgvJadwal);

            System.out.println(mFileUri.getPath());
        }
    }

    private File getNamaPhoto(){
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
        mediaFile = new File(simpanGambarDir.getPath() + File.separator + "Foto_Gedung.jpg");
        return mediaFile;
    }

    private void validasiDataRuang(){
        namaRuang = etNamaRuang.getText().toString().trim().toUpperCase();
        if (namaRuang.equals("")){
            Toast.makeText(getApplicationContext(), "Nama Ruang Salah!",
                    Toast.LENGTH_SHORT).show();
        }else if (Uri.fromFile(mFileUri)==null){
            Toast.makeText(getApplicationContext(), "Foto Jadwal harus diisi!",
                    Toast.LENGTH_SHORT).show();
        }else if (etLantai.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Lantai Ruang Salah!",
                    Toast.LENGTH_SHORT).show();
        }else {
            db.collection("ruangan").whereEqualTo("nama", namaRuang)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Nama Ruang Sudah dipakai!",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        simpanGambar(namaRuang);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Gagal Memvalidasi Nama Ruang!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void simpanGambar(String namaRuang){
        final StorageReference mRef = mStorageRef.child(namaRuang+".jpg");

        mRef.putFile(Uri.fromFile(mFileUri))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "gambar Berhasil Diupload!",
                        Toast.LENGTH_LONG).show();

                mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        fotoJadwalRuangan = new Ruangan(uri.toString());
                        urlJadwal = fotoJadwalRuangan.getImageJadwal();
                        tambahDataRuang(urlJadwal);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "gagal mendapatkan URL gambar!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "gambar Gagal Diupload! \n"+e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void tambahDataRuang(String urlJadwal){
        Map<String, Object> data = new HashMap<>();
        data.put("nama", namaRuang);
        data.put("imageJadwal", urlJadwal);
        data.put("lantai",Integer.parseInt(etLantai.getText().toString()));
        data.put("lokasi",tvNamaGedung.getText());

        db.collection("ruangan")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),
                                "Berhasil menambahkan data!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Gagal menambahkan data! \n"+e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
        });

        enableButtonTambahRuang();
    }

    private void disableButtonTambahRuang(){
        btnSimpanRuang.setEnabled(false);
        int abuAbu = Color.parseColor("#D3D3D3");
        btnSimpanRuang.setBackgroundColor(abuAbu);
    }

    private void enableButtonTambahRuang(){
        int hijau = Color.parseColor("#00bc00");
        btnSimpanRuang.setBackgroundColor(hijau);
        btnSimpanRuang.setEnabled(true);
    }
}
