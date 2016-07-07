package songbook.klaydze.com.songbook.model;

/**
 * Created by Jessie on 7/2/2016.
 */
public class SongListItem {

    private String songNumber,
            songTitle,
            songArtist;

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

    public SongListItem(String songNumber, String songTitle, String songArtist) {
        this.songNumber = songNumber;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
    }


}
