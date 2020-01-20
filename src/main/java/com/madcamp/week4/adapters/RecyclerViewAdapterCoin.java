package com.madcamp.week4.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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
import com.madcamp.week4.fragments.FragmentCoin;

import java.util.List;

public class RecyclerViewAdapterCoin extends RecyclerView.Adapter<RecyclerViewAdapterCoin.ViewHolder> {
    private Context mContext;
    private List<ModelCoins> mListCoins;
    private LayoutInflater inflater;
    private DbOpenHelper mDbOpenHelper;


    public RecyclerViewAdapterCoin(Context context, List<ModelCoins> listCoins) {
        mListCoins = listCoins;
        mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_coin, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        mDbOpenHelper = new DbOpenHelper(mContext);
        mDbOpenHelper.open();
        mDbOpenHelper.create();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView coin, number;

        coin = holder.coin;
        number = holder.number;

        coin.setText(mListCoins.get(position).getCoin());
        number.setText(mListCoins.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return mListCoins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView coin, number;
        ImageView add, sub;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            coin = itemView.findViewById(R.id.coin);
            number= itemView.findViewById(R.id.number);
            add = itemView.findViewById(R.id.add);
            sub = itemView.findViewById(R.id.sub);
            cardView = itemView.findViewById(R.id.cardview);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Integer new_number = Integer.parseInt(mListCoins.get(position).getNumber());
                        new_number ++;
                        mDbOpenHelper.updateColumn(Long.parseLong(mListCoins.get(position).getId()),
                                mListCoins.get(position).getCoin(),
                                Integer.toString(new_number));
                        mListCoins.get(position).setNumber(Integer.toString(new_number));
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });
            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Integer new_number = Integer.parseInt(mListCoins.get(position).getNumber());
                        if(new_number >0) {
                            new_number--;
                            mDbOpenHelper.updateColumn(Long.parseLong(mListCoins.get(position).getId()),
                                    mListCoins.get(position).getCoin(),
                                    Integer.toString(new_number));
                            mListCoins.get(position).setNumber(Integer.toString(new_number));
                            notifyItemChanged(getAdapterPosition());
                        }
                    }
                }
            });
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle("데이터 삭제")
                                .setMessage("해당 화폐를 삭제 하시겠습니까?")
                                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(mContext, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                        mDbOpenHelper.deleteColumn(Long.parseLong(mListCoins.get(position).getId()));
                                        mListCoins.remove(getAdapterPosition());
                                        notifyItemChanged(getAdapterPosition());
                                        notifyItemRangeChanged(getAdapterPosition(), mListCoins.size());
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(mContext, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .create()
                                .show();
                    }
                }
            });
        }
    }
}
