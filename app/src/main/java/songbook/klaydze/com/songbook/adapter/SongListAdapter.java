package songbook.klaydze.com.songbook.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import songbook.klaydze.com.songbook.R;
import songbook.klaydze.com.songbook.model.SongListItem;

/**
 * Created by Jessie on 7/2/2016.
 */
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private ArrayList<SongListItem> songItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public SongListAdapter(ArrayList<SongListItem> items) {
        this.songItems = items;
    }

    @Override
    public SongListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TextView itemSongNumber = (TextView) holder.itemView.findViewById(R.id.tvSongNumber);
        TextView itemSongTitle = (TextView) holder.itemView.findViewById(R.id.tvSongTitle);
        TextView itemSongArtist = (TextView) holder.itemView.findViewById(R.id.tvSongArtist);

        itemSongNumber.setText(songItems.get(position).getSongNumber());
        itemSongTitle.setText(songItems.get(position).getSongTitle());
        itemSongArtist.setText(songItems.get(position).getSongArtist());
    }

    @Override
    public int getItemCount() {
        return songItems.size();
    }
}
