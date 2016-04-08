package fr.loicleinot.imdb;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                Bitmap img = DbBitmapUtility.getImage(c.getBlob(c.getColumnIndexOrThrow(IMDbContract.IMDbEntry.COLUMN_NAME_POSTER)));

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

    public class IMDbRecyclerViewAdapter extends RecyclerView
            .Adapter<IMDbRecyclerViewAdapter
            .DataObjectHolder> {
        private String LOG_TAG = "IMDbRecyclerViewAdapter";
        private ArrayList<IMDbObject> mDataset;

        public class DataObjectHolder extends RecyclerView.ViewHolder
                implements View
                .OnClickListener {
            TextView label;
            TextView subtitle;
            ImageView image;

            public DataObjectHolder(View itemView) {
                super(itemView);
                label = (TextView) itemView.findViewById(R.id.title_list);
                subtitle = (TextView) itemView.findViewById(R.id.subtitle);
                image = (ImageView) itemView.findViewById(R.id.imageView3);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), mDataset.get(getAdapterPosition()).getPlot(), Toast.LENGTH_LONG).show();
            }
        }

        public IMDbRecyclerViewAdapter(ArrayList<IMDbObject> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout, parent, false);

            return new DataObjectHolder(view);
        }

        @Override
        public void onBindViewHolder(DataObjectHolder holder, int position) {
            holder.label.setText(mDataset.get(position).getTitle());
            holder.subtitle.setText(mDataset.get(position).getReleased());
            holder.image.setImageBitmap(mDataset.get(position).getImage());
        }
/*
        public void addItem(IMDbObject dataObj, int index) {
            mDataset.add(dataObj);
            notifyItemInserted(index);
        }

        public void deleteItem(int index) {
            mDataset.remove(index);
            notifyItemRemoved(index);
        }*/

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}
