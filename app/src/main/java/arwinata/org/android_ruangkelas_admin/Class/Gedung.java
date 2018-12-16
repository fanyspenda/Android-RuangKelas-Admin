package arwinata.org.android_ruangkelas_admin.Class;

public class Gedung {

    private String nama, imageUrl, documentId;

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

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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
