package arwinata.org.android_ruangkelas_admin.Class;

public class Ruangan {

    private String imageJadwal, lokasi, nama;
    private int lantai;

    //konstruktor kosong untuk menginisiasi objek pertama kali
    public Ruangan() {
    }

    //konstruktor full untuk mengambil semua data dari firestore
    public Ruangan(String imageJadwal, int lantai, String lokasi, String nama) {
        this.imageJadwal = imageJadwal;
        this.lantai = lantai;
        this.lokasi = lokasi;
        this.nama = nama;
    }

    //konstruktor full hanya untuk mengambil alamat foto dari Firestore
    public Ruangan(String imageJadwal) {
        this.imageJadwal = imageJadwal;
    }

    public String getImageJadwal() {
        return imageJadwal;
    }

    public void setImageJadwal(String imageJadwal) {
        this.imageJadwal = imageJadwal;
    }

    public int getLantai() {
        return lantai;
    }

    public void setLantai(int lantai) {
        this.lantai = lantai;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}