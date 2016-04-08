package fr.loicleinot.imdb;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

/**
 * Created by Heraktone-PORTABLE on 08/04/2016.
 *
 */
public class IMDbObject implements BaseColumns {
    private String COLUMN_NAME_TITLE = "Title";
    private String COLUMN_NAME_RELEASED = "Released";
    private String COLUMN_NAME_PLOT = "Released";
    private Bitmap image;

    public IMDbObject(String title, String released, String plot, Bitmap img) {
        COLUMN_NAME_RELEASED = released;
        COLUMN_NAME_TITLE = title;
        COLUMN_NAME_PLOT = plot;
        image = img;
    }

    public String getTitle() {
        return COLUMN_NAME_TITLE;
    }

    public String getReleased() {
        return COLUMN_NAME_RELEASED;
    }

    public String getPlot() {
        return COLUMN_NAME_PLOT;
    }

    public Bitmap getImage() {
        return image;
    }
}