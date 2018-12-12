package arwinata.org.android_ruangkelas_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button btnLogin;
    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etusername);
        etPassword = findViewById(R.id.etpassword);

        btnLogin = findViewById(R.id.btnlogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                checkLogin(username, password);
            }
        });
    }

    public void checkLogin(String insertUsername, String insertPassword)
    {
        Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();

        Query query = db.collection("admin")
                .whereEqualTo("username", insertUsername).whereEqualTo("password", insertPassword);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Wrong username or Password", Toast.LENGTH_LONG).show();
                } else {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Admin adm = documentSnapshot.toObject(Admin.class);

                        String nip = adm.getNip();
                        String nama = adm.getNama();
                        String username = adm.getUsername();
                        String password = adm.getPassword();
                        String documentId = documentSnapshot.getId();

                        String data = "NIP: " + nip
                                +"\nNama: " + nama
                                +"\nUsername: "+username
                                +"\nPassword: "+password;

                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                        goToMain2Activity(documentId);
                    }
                }
            }
        });
    }
    public void goToMain2Activity(String documentId)
    {
        Intent i = new Intent(getApplicationContext(), Main2Activity.class);
        i.putExtra("documentId", documentId);
        startActivity(i);
    }
}
