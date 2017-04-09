package com.example.dhruv.cbrmp3index.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.dhruv.cbrmp3index.data.DataContract.DataEntry;

/**
 * Created by dhruv on 27/3/17.
 */

public class DataDbHelper extends SQLiteOpenHelper {

    Context mContext;

    private static final String DATABASE_NAME = "cbr_index.db";

    private static final int DATABASE_VERSION = 1;

    private boolean isNew = false;

    public boolean isNew() {
        return isNew;
    }

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
    }

    public void removeTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+ DataEntry.TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + DataEntry.TABLE_NAME + " (" +
                DataEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                DataEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DataEntry.COLUMN_PARENT_ID + " TEXT NOT NULL, " +
                DataEntry.COLUMN_DATA_TYPE + " TEXT NOT NULL, " +
                DataEntry.COLUMN_RENAMED_VALUE + " TEXT, " +
                DataEntry.COLUMN_CHANGE_BOOL + " INTEGER DEFAULT 0, " +
                DataEntry.COLUMN_EXTRA_INFO + " TEXT " + ");";

        db.execSQL(SQL_CREATE_TABLE);
        isNew = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        removeTable(db);
        Toast.makeText(mContext,"Table Updated",Toast.LENGTH_LONG).show();
        onCreate(db);
    }
}
