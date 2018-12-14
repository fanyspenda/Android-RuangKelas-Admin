package arwinata.org.android_ruangkelas_admin.Class;

import android.widget.Adapter;

public class Admin {
    private String nama, nip, username, password;

    public Admin(String nama, String nip, String username, String password) {
        this.nama = nama;
        this.nip = nip;
        this.username = username;
        this.password = password;
    }

    public Admin(){}

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
