package songbook.klaydze.com.songbook.model;

import io.realm.RealmObject;

/**
 * Created by Jessie on 7/2/2016.
 */
public class SongListItem {

    private String songNumber,
            songTitle,
            songArtist;

    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(String songNumber) {
        this.songNumber = songNumber;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public SongListItem(String songNumber, String songTitle, String songArtist, boolean isFavorite) {
        this.songNumber = songNumber;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.isFavorite = isFavorite;
    }
}
