package songbook.klaydze.com.songbook.realm_model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Jessie on 7/8/2016.
 */
public class SongModel extends RealmObject {

    @Required
    private String songNumber;
    @Required
    private String songTitle;
    @Required
    private String songArtist;

    private String songCountry;
    private boolean isFavorite;

    @Ignore
    private String songVolume;

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

    public String getSongCountry() {
        return songCountry;
    }

    public void setSongCountry(String songCountry) {
        this.songCountry = songCountry;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public String getSongVolume() {
        return songVolume;
    }

    public void setSongVolume(String songVolume) {
        this.songVolume = songVolume;
    }
}
