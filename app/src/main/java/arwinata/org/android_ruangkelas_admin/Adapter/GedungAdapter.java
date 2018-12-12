package arwinata.org.android_ruangkelas_admin.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import java.util.List;

import arwinata.org.android_ruangkelas_admin.Gedung;
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

    public class GedungViewHolder extends RecyclerView.ViewHolder{

        public TextView namaGedung;
        public ImageView gambarGedung;

        public GedungViewHolder(View itemView) {
            super(itemView);
            namaGedung = itemView.findViewById(R.id.item_namaGedung);
            gambarGedung = itemView.findViewById(R.id.item_gambarGedung);
        }
    }
}