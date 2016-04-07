package fr.loicleinot.imdb;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        ViewFlipper vf = (ViewFlipper)findViewById(R.id.ViewFlipper01);
        vf.setDisplayedChild(3);

        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMDbContract mDbHelper = new IMDbContract(getApplicationContext());

                String title = ((TextView)findViewById(R.id.title)).getText().toString();
                String titles[] = { title };

                SQLiteDatabase dbr = mDbHelper.getReadableDatabase();

                // Define a projection that specifies which columns from the database
                // you will actually use after this query.
                String[] projection = { IMDbContract.IMDbEntry._ID, IMDbContract.IMDbEntry.COLUMN_NAME_TITLE };

                Cursor c = dbr.query(IMDbContract.IMDbEntry.TABLE_NAME, projection, "Title", titles, null, null, "");
                String itemId = "";

                if(!c.moveToFirst()) {

                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(IMDbContract.IMDbEntry.COLUMN_NAME_IMDB_ID, "");
                    values.put(IMDbContract.IMDbEntry.COLUMN_NAME_TITLE, title);

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId;
                    newRowId = db.insert(
                            IMDbContract.IMDbEntry.TABLE_NAME,
                            "null",
                            values);
                }
                else
                    itemId = c.getString(c.getColumnIndexOrThrow(IMDbContract.IMDbEntry._ID));

                Log.d("title ?", itemId);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AsyncTask<String, Void, Void> asyncTask = new RequestOMDB();
                asyncTask.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        favoriteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), FavActivity.class);
                startActivity(intent);
                return true;
            }
        });

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fr.loicleinot.imdb/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://fr.loicleinot.imdb/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class RequestOMDB extends AsyncTask<String, Void, Void> {
        IMDbPage imdbinfo;
        private boolean response = false;

        @Override
        protected Void doInBackground(String... params) {
            try {
                urlRequest(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("event", "finished");
            return null;
        }

        private void urlRequest(String title) throws IOException {
            URL url;
            HttpURLConnection conn;
            InputStream is = null;
            String contentAsString;
            int len = 50000;
            JSONObject json1;

            try {
                url = new URL("http://www.omdbapi.com/?t=" + title.replaceAll(" ", "+") + "=&plot=full&r=json");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                is = conn.getInputStream();

                contentAsString = readIt(is, len);
                Log.d("content", "xml " + contentAsString);

                json1 = new JSONObject(contentAsString);
                response = (json1.getString("Response").equals("True"));
                Log.d("reponse", "json " + json1.getString("Response") + " / response " + response);

                if(response == true)
                    imdbinfo = new IMDbPage(json1.getString("Title"), json1.getString("Plot"), getBitmapFromURL(json1.getString("Poster")), json1.getString("Released"), json1.getString("Runtime"), json1.getString("Actors"), json1.getString("Director"), json1.getString("Genre"));

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        private String readIt(InputStream is, int len) throws IOException {
            String str = "";
            int count;
            Reader reader = new InputStreamReader(is);
            char[] buffer = new char[len];
            while ((count = reader.read(buffer, 0, len)) > 0) {
                str += new String(buffer, 0, count);
            }
            return str;
        }

        public Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void a) {
            super.onPostExecute(a);
            ViewFlipper vf = (ViewFlipper)findViewById(R.id.ViewFlipper01);

            if(response == true) {
                vf.setDisplayedChild(2);

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(imdbinfo.getPoster());

                TextView title = (TextView) findViewById(R.id.title);
                title.setText(imdbinfo.getTitle());

                TextView plot = (TextView) findViewById(R.id.textView3);
                plot.setText(imdbinfo.getPlot());

                TextView year = (TextView) findViewById(R.id.year);
                year.setText(imdbinfo.getYear());

                TextView time = (TextView) findViewById(R.id.time);
                time.setText(imdbinfo.getTime());

                TextView actors = (TextView) findViewById(R.id.actors);
                actors.setText(imdbinfo.getActors());

                TextView directors = (TextView) findViewById(R.id.director);
                directors.setText(imdbinfo.getDirectors());

                TextView genre = (TextView) findViewById(R.id.genre);
                genre.setText(imdbinfo.getGenre());

                Log.d("event", "post execution");
            }
            else {
                vf.setDisplayedChild(1);

                TextView plot = (TextView) findViewById(R.id.textView3);
                plot.setText("N/A");
            }
        }

    }
}
