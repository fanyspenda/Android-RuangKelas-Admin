package arwinata.org.android_ruangkelas_admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import arwinata.org.android_ruangkelas_admin.Class.Ruangan;
import arwinata.org.android_ruangkelas_admin.DetailRuanganActivity;
import arwinata.org.android_ruangkelas_admin.R;

public class RuangAdapter extends RecyclerView.Adapter<RuangAdapter.RuangViewHolder> {

    private Context mContext;
    private List<Ruangan> mRuangan;

    public RuangAdapter(Context mContext, List<Ruangan> mRuangan) {
        this.mContext = mContext;
        this.mRuangan = mRuangan;
    }

    @NonNull
    @Override
    public RuangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ruang_item, parent, false);
        return new RuangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuangViewHolder holder, int position) {
        final Ruangan ruangItem = mRuangan.get(position);
        holder.namaRuang.setText(ruangItem.getNama());
        holder.namaRuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iKeRuangan = new Intent(mContext, DetailRuanganActivity.class);
                iKeRuangan.putExtra("lantai", ruangItem.getLantai());
                iKeRuangan.putExtra("namaRuangan", ruangItem.getNama());
                iKeRuangan.putExtra("imageJadwal", ruangItem.getImageJadwal());
                mContext.startActivity(iKeRuangan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRuangan.size();
    }

    public class RuangViewHolder extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener {

        TextView namaRuang;
        public RuangViewHolder(View itemView) {
            super(itemView);
            namaRuang = itemView.findViewById(R.id.item_namaRuang);
            namaRuang.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            //memberi judul menu yang muncul
            contextMenu.setHeaderTitle("Memilih Ruang "+namaRuang.getText());

            //pilihan menu
            contextMenu.add(this.getAdapterPosition(), 123, 0, "Edit Ruang");
            contextMenu.add(this.getAdapterPosition(), 124, 1, "Hapus Ruang");
        }
    }

    public void keEditRuangan(int position){
        Ruangan ruanganItem = mRuangan.get(position);
        Toast.makeText(mContext, "mengedit ruangan "+ruanganItem.getNama(),
                Toast.LENGTH_SHORT).show();
    }

    public void hapusRuangan(int position){
        Ruangan ruanganItem = mRuangan.get(position);
        Toast.makeText(mContext, "menghapus ruangan "+ruanganItem.getNama(),
                Toast.LENGTH_SHORT).show();
    }
}
