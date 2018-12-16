package arwinata.org.android_ruangkelas_admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.List;

import arwinata.org.android_ruangkelas_admin.Class.Gedung;
import arwinata.org.android_ruangkelas_admin.DaftarRuangActivity;
import arwinata.org.android_ruangkelas_admin.DetailGedungActivity;
import arwinata.org.android_ruangkelas_admin.R;

public class GedungAdapter extends RecyclerView.Adapter<GedungAdapter.GedungViewHolder> {

    private AdapterView.OnItemClickListener listener;
    private Context mContext;
    private List<Gedung> mGedung;

    public GedungAdapter(Context mContext, List<Gedung> mGedung) {
        this.mContext = mContext;
        this.mGedung = mGedung;
    }

    @NonNull
    @Override
    public GedungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gedung_item, parent, false);
        return new GedungViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GedungViewHolder holder, int position) {
        Gedung gdgItem = mGedung.get(position);
        holder.namaGedung.setText(gdgItem.getNama());
        Picasso.get().load(gdgItem.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.gambarGedung);

    }

    @Override
    public int getItemCount() {
        return mGedung.size();
    }

    //mengimplementasikan OnCreate agar bisa menampilkan menu ketika OnLongClik
    public class GedungViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{

        public TextView namaGedung;
        public ImageView gambarGedung;

        public GedungViewHolder(View itemView) {
            super(itemView);
            namaGedung = itemView.findViewById(R.id.item_namaGedung);
            gambarGedung = itemView.findViewById(R.id.item_gambarGedung);

            //mengklik panjang(LongClick), akan menjalankan perintah onCreateContextMenu
            gambarGedung.setOnCreateContextMenuListener(this);

            gambarGedung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iGedung = new Intent(mContext, DaftarRuangActivity.class);
                    iGedung.putExtra("namaGedung", namaGedung.getText());
                    mContext.startActivity(iGedung);
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {

            //memberi judul menu yang muncul
            contextMenu.setHeaderTitle("Memilih Gedung "+namaGedung.getText());

            //pilihan menu
            contextMenu.add(this.getAdapterPosition(), 121, 0, "Detail Gedung");
            contextMenu.add(this.getAdapterPosition(), 122, 1, "Hapus Gedung");
        }
    }

    public void hapusGedung(int position){
        //meng-get nama gedung untuk kemudian dihapus
        Gedung gdgItem = mGedung.get(position);
        String namaGedung = gdgItem.getNama();

        //letakkan koding hapus di Sini

        Toast.makeText(mContext, "menghapus gedung "+namaGedung, Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }

    public void keDetailGedung(int position){
        //meng-get nama gedung untuk kemudian dihapus
        Gedung gdgItem = mGedung.get(position);

        Intent i = new Intent(mContext, DetailGedungActivity.class);
        i.putExtra("namaGedung", gdgItem.getNama());
        i.putExtra("imageGedung", gdgItem.getImageUrl());
        mContext.startActivity(i);
    }
}