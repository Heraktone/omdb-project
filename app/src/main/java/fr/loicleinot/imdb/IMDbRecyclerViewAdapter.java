package fr.loicleinot.imdb;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Heraktone-PORTABLE on 09/04/2016.
 *
 */
public class IMDbRecyclerViewAdapter extends RecyclerView
        .Adapter<IMDbRecyclerViewAdapter
        .DataObjectHolder> {
    private ArrayList<IMDbObject> mDataset;

    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView year;
        ImageView image;
        TextView type;
        TextView actors;
        TextView directors;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.title_list);
            year = (TextView) itemView.findViewById(R.id.year);
            image = (ImageView) itemView.findViewById(R.id.imageView3);
            type = (TextView) itemView.findViewById(R.id.typeObject);
            directors = (TextView) itemView.findViewById(R.id.directors);
            actors = (TextView) itemView.findViewById(R.id.actors);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MovieActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("data", mDataset.get(getAdapterPosition()));
            context.startActivity(intent);
            //Toast.makeText(getApplicationContext(), mDataset.get(getAdapterPosition()).getPlot(), Toast.LENGTH_LONG).show();
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
        context = parent.getContext();

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        IMDbObject obj = mDataset.get(position);
        Resources res = context.getResources();
        holder.label.setText(obj.getTitle());
        holder.year.setText(String.format(res.getString(R.string.string_date), obj.getReleased()));
        holder.image.setImageBitmap(DbBitmapUtility.getImage(mDataset.get(position).getImage()));
        holder.type.setText(String.format(res.getString(R.string.string_other),obj.getType()));
        holder.actors.setText(String.format(res.getString(R.string.string_other), obj.getActors()));
        holder.directors.setText(String.format(res.getString(R.string.string_other),obj.getDirectors()));
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