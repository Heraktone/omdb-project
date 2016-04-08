package fr.loicleinot.imdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Heraktone on 02/04/2016.
 *
 */
public final class IMDbContract extends SQLiteOpenHelper{
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + IMDbEntry.TABLE_NAME + " (" +
                    IMDbEntry._ID + " INTEGER PRIMARY KEY," +
                    IMDbEntry.COLUMN_NAME_IMDB_ID + TEXT_TYPE + COMMA_SEP +
                    IMDbEntry.COLUMN_NAME_POSTER + "BLOB" + COMMA_SEP +
                    IMDbEntry.COLUMN_NAME_PLOT + TEXT_TYPE + COMMA_SEP +
                    IMDbEntry.COLUMN_NAME_RELEASED + TEXT_TYPE + COMMA_SEP +
                    IMDbEntry.COLUMN_NAME_TITLE + TEXT_TYPE +" )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + IMDbEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public IMDbContract(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /* Inner class that defines the table contents */
    public static abstract class IMDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "imdb";
        public static final String COLUMN_NAME_IMDB_ID = "imdbentry";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_PLOT = "Plot";
        public static final String COLUMN_NAME_POSTER = "Poster";
        public static final String COLUMN_NAME_RELEASED = "Released";
    }
}
