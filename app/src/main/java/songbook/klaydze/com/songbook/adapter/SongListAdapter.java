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
        TextView itemSongNumber;
        TextView itemSongTitle;
        TextView itemSongArtist;

        public ViewHolder(View view) {
            super(view);

            itemSongNumber = (TextView) view.findViewById(R.id.tvSongNumber);
            itemSongTitle = (TextView) view.findViewById(R.id.tvSongTitle);
            itemSongArtist = (TextView) view.findViewById(R.id.tvSongArtist);
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
        holder.itemSongNumber.setText(songItems.get(position).getSongNumber());
        holder.itemSongTitle.setText(songItems.get(position).getSongTitle());
        holder.itemSongArtist.setText(songItems.get(position).getSongArtist());
    }

    @Override
    public int getItemCount() {
        return songItems.size();
    }
}
