package com.madcamp.week4.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.madcamp.week4.R;

import java.util.List;

public class RecyclerViewAdapterMap extends RecyclerView.Adapter<RecyclerViewAdapterMap.ViewHolder> {
    private Context mContext;
    private List<ModelMaps> mListMaps;
    private LayoutInflater inflater;
    private MapListRecyclerClickListener mClickListener;

    public RecyclerViewAdapterMap(Context context, List<ModelMaps> listMaps, MapListRecyclerClickListener clickListener) {
        mListMaps = listMaps;
        mContext = context;
        mClickListener = clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_map, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view,mClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView place, rating, dist;

            place = holder.place;
            rating = holder.rating;
            dist = holder.dist;

            place.setText(mListMaps.get(position).getPlace());
            rating.setText(mListMaps.get(position).getRating());
            dist.setText(mListMaps.get(position).getDist()+"m");
    }

    @Override
    public int getItemCount() {
        return mListMaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        CardView cardView;
        TextView place, rating, dist;
        MapListRecyclerClickListener mClickListener;

        public ViewHolder(View itemView, MapListRecyclerClickListener clickListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            place = itemView.findViewById(R.id.place);
            rating = itemView.findViewById(R.id.rating);
            dist = itemView.findViewById(R.id.dist);

            mClickListener = clickListener;

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            mClickListener.onMapListClicked(getAdapterPosition());
        }
    }

    public interface MapListRecyclerClickListener{
        void onMapListClicked(int position);
    }
}
