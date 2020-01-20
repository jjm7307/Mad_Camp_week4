package com.madcamp.week4.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madcamp.week4.MainActivity;
import com.madcamp.week4.R;
import com.madcamp.week4.adapters.DbOpenHelper;
import com.madcamp.week4.adapters.ModelCoins;
import com.madcamp.week4.adapters.RecyclerViewAdapterCoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentCoin extends Fragment {
    View v;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterCoin adapter;
    private TextView total_coins;
    private LinearLayout refresh_coin;

    private String coin;
    private String number;
    private String sort = "coin";
    private Integer total_money = 0;

    private List<ModelCoins> coin_list = new ArrayList<>();
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;

    private ImageView insert;

    private TimerTask second;
    private final Handler handler = new Handler();

    public FragmentCoin(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_coin, container, false);
        recyclerView = v.findViewById(R.id.rv_coins);
        insert = v.findViewById(R.id.insert);
        total_coins = v.findViewById(R.id.total_coins);
        refresh_coin = v.findViewById(R.id.refresh_coin);

        mDbOpenHelper = new DbOpenHelper(getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapterCoin(getContext(),coin_list);
        recyclerView.setAdapter(adapter);

        insert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final View add_money = inflater.inflate(R.layout.add_money, container, false);
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("데이터 추가")
                        .setView(add_money)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edt_coin = add_money.findViewById(R.id.edt_coin);
                                EditText edt_number = add_money.findViewById(R.id.edt_number);
                                if(TextUtils.isEmpty(edt_coin.getText().toString())){
                                    //Toast.makeText(getContext(), "화폐 종류를 입력하세요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                //Toast.makeText(getContext(), "추가가 완료되었습니다", Toast.LENGTH_SHORT).show();
                                if(TextUtils.isEmpty(edt_number.getText().toString())){
                                    mDbOpenHelper.insertColumn(edt_coin.getText().toString(), "0");
                                    showDatabase(sort);
                                    return;
                                }
                                else {
                                    mDbOpenHelper.insertColumn(edt_coin.getText().toString(), edt_number.getText().toString());
                                    showDatabase(sort);
                                    return;
                                }
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getContext(), "추가를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
            }});
        refresh_coin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDatabase(sort);
            }});
        //showDatabase(sort);
        testStart();
        return v;
    }

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        total_money = 0;
        coin_list.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempCoin = iCursor.getString(iCursor.getColumnIndex("coin"));
            String tempNumber = iCursor.getString(iCursor.getColumnIndex("number"));

            arrayIndex.add(tempIndex);
            total_money += Integer.parseInt(tempCoin) * Integer.parseInt(tempNumber);
            coin_list.add(new ModelCoins(tempIndex, tempCoin, tempNumber));
        }
        total_coins.setText(Integer.toString(total_money));
        if(coin_list.size() > 0) adapter.notifyDataSetChanged();
    }

    public void testStart(){
        second = new TimerTask() {
            @Override
            public void run() {
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }
    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                showDatabase(sort);
            }
        };
        handler.post(updater);
    }
}
