package fr.loicleinot.imdb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_page);

        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDbContract mDbHelper = new IMDbContract(getApplicationContext());

                String title = ((TextView) findViewById(R.id.title)).getText().toString();
                String released = ((TextView) findViewById(R.id.year)).getText().toString();
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.buildDrawingCache();
                byte[] poster = DbBitmapUtility.getBytes(imageView.getDrawingCache());
                String plot = ((TextView) findViewById(R.id.textView3)).getText().toString();

                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_TITLE, title);
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_RELEASED, released);
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_POSTER, poster);
                values.put(IMDbContract.IMDbEntry.COLUMN_NAME_PLOT, plot);

                // Insert the new row, returning the primary key value of the new row
                db.insert(IMDbContract.IMDbEntry.TABLE_NAME, "null", values);
            }
        });

        Intent intent = getIntent();
        IMDbObject imdbinfo = intent.getParcelableExtra("data");

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(DbBitmapUtility.getImage(imdbinfo.getImage()));

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(imdbinfo.getTitle());

        TextView plot = (TextView) findViewById(R.id.textView3);
        plot.setText(imdbinfo.getPlot());

        TextView year = (TextView) findViewById(R.id.year);
        year.setText(imdbinfo.getReleased());
/*
        TextView time = (TextView) findViewById(R.id.time);
        time.setText(imdbinfo.getTime());

        TextView actors = (TextView) findViewById(R.id.actors);
        actors.setText(imdbinfo.getActors());

        TextView directors = (TextView) findViewById(R.id.director);
        directors.setText(imdbinfo.getDirectors());

        TextView genre = (TextView) findViewById(R.id.genre);
        genre.setText(imdbinfo.getGenre());*/
    }


}
