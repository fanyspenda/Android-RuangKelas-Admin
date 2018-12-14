package arwinata.org.android_ruangkelas_admin.Class;

public class Gedung {

    private String nama, imageUrl;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Gedung() {
    }

    public Gedung(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Gedung(String nama, String imageUrl) {
        this.imageUrl = imageUrl;
        this.nama = nama;
    }
}
