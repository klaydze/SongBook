package songbook.klaydze.com.songbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import songbook.klaydze.com.songbook.realm_model.SongModel;
import songbook.klaydze.com.songbook.R;

/**
 * Created by Jessie on 7/8/2016.
 */
public class SongRecyclerViewAdapter extends RealmRecyclerViewAdapter<SongModel, SongRecyclerViewAdapter.ViewHolder> {
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
        SongModel song = getData().get(position);

        TextView itemSongNumber = (TextView) holder.itemView.findViewById(R.id.tvSongNumber);
        TextView itemSongTitle = (TextView) holder.itemView.findViewById(R.id.tvSongTitle);
        TextView itemSongArtist = (TextView) holder.itemView.findViewById(R.id.tvSongArtist);

        itemSongNumber.setText(song.getSongNumber());
        itemSongTitle.setText(song.getSongTitle());
        itemSongArtist.setText(song.getSongArtist());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
