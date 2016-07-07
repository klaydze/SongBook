package songbook.klaydze.com.songbook.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Jessie on 6/29/2016.
 */
public class NavDrawerItem {

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title, Drawable iconItem) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    private boolean showNotify;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
