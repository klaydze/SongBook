package songbook.klaydze.com.songbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import songbook.klaydze.com.songbook.realm_model.SongModel;
import songbook.klaydze.com.songbook.R;

public class SongRecyclerViewAdapter extends RealmRecyclerViewAdapter<SongModel, SongRecyclerViewAdapter.ViewHolder> {

    Realm realm;

    public SongRecyclerViewAdapter(Context context, OrderedRealmCollection<SongModel> data) {
        super(context, data, true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        realm = Realm.getDefaultInstance();

        try {
            final SongModel song = getData().get(position);
            final ToggleButton toggleButton;

            holder.tvSongNumber.setText(String.format(Locale.getDefault(), "%05d", Integer.parseInt(song.getSongNumber())));
            holder.tvSongTitle.setText(song.getSongTitle());
            holder.tvSongArtist.setText(song.getSongArtist());
            holder.tbFavorite.setChecked(song.isFavorite());

            if (song.getHasSongChorus()) {
                holder.imgChorus.setVisibility(View.VISIBLE);
            } else {
                holder.imgChorus.setVisibility(View.GONE);
            }

            song.setSongVolume("Vol 10");
            if (song.getSongVolume() == null || song.getSongVolume().isEmpty()) {
                holder.tvSongVolume.setText("");
                holder.tvSongVolume.setVisibility(View.INVISIBLE);
            } else {
                holder.tvSongVolume.setText(song.getSongVolume());
                holder.tvSongVolume.setVisibility(View.VISIBLE);
            }

            toggleButton = holder.tbFavorite;
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toggleButton.isChecked()) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                song.setFavorite(true);
                            }
                        });
                    } else {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                song.setFavorite(false);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            String err;
            err = e.getStackTrace().toString();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongTitle;
        ToggleButton tbFavorite;
        TextView tvSongNumber;
        TextView tvSongArtist;
        TextView tvSongVolume;
        ImageView imgChorus;

        public ViewHolder(View itemView) {
            super(itemView);

            tvSongTitle = (TextView) itemView.findViewById(R.id.tvSongTitle);
            tbFavorite = (ToggleButton) itemView.findViewById(R.id.toggleFavorite);
            tvSongNumber = (TextView) itemView.findViewById(R.id.tvSongNumber);
            tvSongArtist = (TextView) itemView.findViewById(R.id.tvSongArtist);
            tvSongVolume = (TextView) itemView.findViewById(R.id.tvSongVolume);
            imgChorus = (ImageView) itemView.findViewById(R.id.imgChorus);
        }
    }
}
