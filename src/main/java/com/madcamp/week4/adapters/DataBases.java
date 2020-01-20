package com.madcamp.week4.adapters;

import android.provider.BaseColumns;

public class DataBases {
    public static final class CreateDB implements BaseColumns{
        public static final String COIN = "coin";
        public static final String NUMBER = "number";
        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +COIN+" integer not null , "
                +NUMBER+" integer not null );";
    }
}