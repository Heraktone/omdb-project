package fr.loicleinot.imdb;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * Created by Heraktone-PORTABLE on 08/04/2016.
 *
 */
public class IMDbObject implements Parcelable {
    private String COLUMN_NAME_TITLE = "Title";
    private String COLUMN_NAME_RELEASED = "Released";
    private String COLUMN_NAME_PLOT = "Released";
    private byte[] image;

    public IMDbObject(String title, String released, String plot, byte[] img) {
        COLUMN_NAME_RELEASED = released;
        COLUMN_NAME_TITLE = title;
        COLUMN_NAME_PLOT = plot;
        image = img;
    }

    public IMDbObject(String title, String released) {
        COLUMN_NAME_RELEASED = released;
        COLUMN_NAME_TITLE = title;
    }


    protected IMDbObject(Parcel in) {
        COLUMN_NAME_TITLE = in.readString();
        COLUMN_NAME_RELEASED = in.readString();
        COLUMN_NAME_PLOT = in.readString();
        image = in.createByteArray();
    }

    public static final Creator<IMDbObject> CREATOR = new Creator<IMDbObject>() {
        @Override
        public IMDbObject createFromParcel(Parcel in) {
            return new IMDbObject(in);
        }

        @Override
        public IMDbObject[] newArray(int size) {
            return new IMDbObject[size];
        }
    };

    public String getTitle() {
        return COLUMN_NAME_TITLE;
    }

    public String getReleased() {
        return COLUMN_NAME_RELEASED;
    }

    public String getPlot() {
        return COLUMN_NAME_PLOT;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(COLUMN_NAME_TITLE);
        dest.writeString(COLUMN_NAME_RELEASED);
        dest.writeString(COLUMN_NAME_PLOT);
        dest.writeByteArray(image);
    }


}