package arwinata.org.android_ruangkelas_admin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import arwinata.org.android_ruangkelas_admin.Class.Ruangan;
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
        Ruangan ruangItem = mRuangan.get(position);

        holder.namaRuang.setText(ruangItem.getNama());
    }

    @Override
    public int getItemCount() {
        return mRuangan.size();
    }

    public class RuangViewHolder extends RecyclerView.ViewHolder {

        TextView namaRuang;
        public RuangViewHolder(View itemView) {
            super(itemView);
            namaRuang = itemView.findViewById(R.id.item_namaRuang);

            namaRuang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "menklik Ruang "+namaRuang.getText(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
