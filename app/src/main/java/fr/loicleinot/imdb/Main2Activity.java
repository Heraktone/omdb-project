package fr.loicleinot.imdb;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        Intent serviceIntent = new Intent(getApplicationContext(),fr.loicleinot.imdb.IMDbService.class);
        getApplicationContext().startService(serviceIntent);
        //stopService(new Intent(this, IMDbService.class));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Associate searchable configuration with the SearchView
        if(getSupportActionBar() != null)
            searchView = new SearchView( getSupportActionBar().getThemedContext());

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(true);

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

        ViewFlipper vf = (ViewFlipper)findViewById(R.id.ViewFlipper01);
        vf.setDisplayedChild(3);

        Button buttonFav = (Button) findViewById(R.id.button);

        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.add(android.R.string.search_go);

        searchItem.setIcon(R.drawable.ic_search_white_36dp);

        MenuItemCompat.setActionView(searchItem, searchView);

        MenuItemCompat.setShowAsAction(searchItem,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);


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
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public class RequestOMDB extends AsyncTask<String, Void, Void> {
        private boolean response = false;
        private ArrayList<IMDbObject> object;

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
            JSONObject json1, jsonObjectFiche;

            try {
                json1 = readURLJSON("http://www.omdbapi.com/?s=" + title.replaceAll(" ", "+"));
//                response = (json1.getString("Response").equals("True"));
//                Log.d("reponse", "json " + json1.getString("Response") + " / response " + response);
/*
                if(response)
                    imdbinfo = new IMDbPage(json1.getString("Title"), json1.getString("Plot"), getBitmapFromURL(json1.getString("Poster")), json1.getString("Released"), json1.getString("Runtime"), json1.getString("Actors"), json1.getString("Director"), json1.getString("Genre"));*/

                object = new ArrayList<>();

                JSONArray jsonResult = json1.getJSONArray("Search");
                for (int i = 0; i < jsonResult.length(); i++) {
                    response = true;
                    JSONObject row = jsonResult.getJSONObject(i);
                    jsonObjectFiche = readURLJSON("http://www.omdbapi.com/?i=" + row.getString("imdbID").replaceAll(" ", "+") + "&plot=full&r=json");

                    Bitmap img = getBitmapFromURL(row.getString("Poster"));
                    object.add(new IMDbObject(row.getString("Title"), row.getString("Year"), jsonObjectFiche.getString("Plot"), DbBitmapUtility.getBytes(img), row.getString("Type"), jsonObjectFiche.getString("Actors"), jsonObjectFiche.getString("Director"), jsonObjectFiche.getString("Runtime"), jsonObjectFiche.getString("Genre")));
                }


            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        private JSONObject readURLJSON(String urlLink) throws JSONException, IOException {
            int len = 50000;
            URL url = new URL(urlLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();

            String contentAsString = readIt(is, len);
            Log.d("content", "xml " + contentAsString);

            return new JSONObject(contentAsString);
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

            if(response) {
                vf.setDisplayedChild(2);

                IMDbRecyclerViewAdapter mAddapter = new IMDbRecyclerViewAdapter(object);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAddapter);

                Log.d("event", "post execution");
            }
            else {
                vf.setDisplayedChild(1);
            }
        }

    }
}
