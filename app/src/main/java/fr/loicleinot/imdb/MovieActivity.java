package fr.loicleinot.imdb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_page);

        Intent intent = getIntent();
        final IMDbObject imdbinfo = intent.getParcelableExtra("data");
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDbContract mDbHelper = new IMDbContract(getApplicationContext());

                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_TITLE, imdbinfo.getTitle());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_RELEASED, imdbinfo.getReleased());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_POSTER, imdbinfo.getImage());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_PLOT, imdbinfo.getPlot());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_ACTORS, imdbinfo.getActors());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_DIRECTORS, imdbinfo.getDirectors());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_GENRE, imdbinfo.getGenre());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_TYPE, imdbinfo.getType());
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_RUNTIME, imdbinfo.getRuntime());

                // Insert the new row, returning the primary key value of the new row
                db.insert(IMDbContract.IMDbEntry.TABLE_NAME, "null", values);
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(DbBitmapUtility.getImage(imdbinfo.getImage()));

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(imdbinfo.getTitle());

        TextView plot = (TextView) findViewById(R.id.textView3);
        plot.setText(imdbinfo.getPlot());

        TextView year = (TextView) findViewById(R.id.year);
        year.setText(imdbinfo.getReleased());

        TextView type = (TextView) findViewById(R.id.genre);
        type.setText(imdbinfo.getGenre());

        TextView time = (TextView) findViewById(R.id.time);
        time.setText(imdbinfo.getRuntime());

        TextView actors = (TextView) findViewById(R.id.actors);
        actors.setText(imdbinfo.getActors());

        TextView directors = (TextView) findViewById(R.id.director);
        directors.setText(imdbinfo.getDirectors());

    }


}
