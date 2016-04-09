package fr.loicleinot.imdb;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IMDbContract mDbHelper = new IMDbContract(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] projection = { IMDbContract.IMDbEntry.COLUMN_NAME_TITLE, IMDbContract.IMDbEntry.COLUMN_NAME_RELEASED, IMDbContract.IMDbEntry.COLUMN_NAME_PLOT, IMDbContract.IMDbEntry.COLUMN_NAME_POSTER, };

        Cursor c = db.query(IMDbContract.IMDbEntry.TABLE_NAME, projection, null, null, null, null, null);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        ArrayList<IMDbObject> object = new ArrayList<>();

        if(c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndexOrThrow(IMDbContract.IMDbEntry.COLUMN_NAME_TITLE));
                String year = c.getString(c.getColumnIndexOrThrow(IMDbContract.IMDbEntry.COLUMN_NAME_RELEASED));
                String plot = c.getString(c.getColumnIndexOrThrow(IMDbContract.IMDbEntry.COLUMN_NAME_PLOT));
                byte[] img = c.getBlob(c.getColumnIndexOrThrow(IMDbContract.IMDbEntry.COLUMN_NAME_POSTER));

                object.add(new IMDbObject(name, year, plot, img));
                Log.d("result", name);
            } while(c.moveToNext());
        }

        c.close();

        IMDbRecyclerViewAdapter mAddapter = new IMDbRecyclerViewAdapter(object);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAddapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        this.finish();
        return true;
    }

}
