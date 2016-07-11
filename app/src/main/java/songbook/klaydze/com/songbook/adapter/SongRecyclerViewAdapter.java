package songbook.klaydze.com.songbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import songbook.klaydze.com.songbook.realm_model.SongModel;
import songbook.klaydze.com.songbook.R;

/**
 * Created by Jessie on 7/8/2016.
 */
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

        final SongModel song = getData().get(position);

        final TextView itemSongTitle = (TextView) holder.itemView.findViewById(R.id.tvSongTitle);
        final ToggleButton itemSongFavorite = (ToggleButton) holder.itemView.findViewById(R.id.toggleFavorite);
        TextView itemSongNumber = (TextView) holder.itemView.findViewById(R.id.tvSongNumber);
        TextView itemSongArtist = (TextView) holder.itemView.findViewById(R.id.tvSongArtist);

        itemSongNumber.setText(song.getSongNumber());
        itemSongTitle.setText(song.getSongTitle());
        itemSongArtist.setText(song.getSongArtist());
        itemSongFavorite.setChecked(song.isFavorite());

        itemSongFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemSongFavorite.isChecked()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            song.setFavorite(itemSongFavorite.isChecked());
                        }
                    });
                }
                else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            song.setFavorite(itemSongFavorite.isChecked());
                        }
                    });
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
