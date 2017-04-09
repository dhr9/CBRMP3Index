package com.example.dhruv.cbrmp3index.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.dhruv.cbrmp3index.data.DataContract.DataEntry;
import com.example.dhruv.cbrmp3index.data.StoredDataModel;

/**
 * Created by dhruv on 31/3/17.
 */

public class DatabaseOperations {

    public static int getDatabaseSize(SQLiteDatabase db) {
        Cursor cursor = getFullDatabase(db);
        return cursor.getCount();
    }

    public static Cursor getFullDatabase(SQLiteDatabase db) {
        return db.query(DataEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    public static void addNewRow(@NonNull SQLiteDatabase db, StoredDataModel model){
        ContentValues cv = new ContentValues();
        cv.put(DataEntry.COLUMN_NAME,model.getName());
        cv.put(DataEntry.COLUMN_ID,model.getId());
        cv.put(DataEntry.COLUMN_DATA_TYPE,model.getType());
        cv.put(DataEntry.COLUMN_PARENT_ID,model.getParentId());
        db.insert(DataEntry.TABLE_NAME,null,cv);
    }

    public static Cursor getPageData(SQLiteDatabase db,String parent){
        return db.query(DataEntry.TABLE_NAME,null,
                DataEntry.COLUMN_PARENT_ID+"=?",new String[]{parent},null,null,
                DataEntry.COLUMN_DATA_TYPE+" DESC");
    }
    public static void updateRowById(SQLiteDatabase db, StoredDataModel model){
        ContentValues cv = new ContentValues();
        cv.put(DataEntry.COLUMN_NAME,model.getName());
        cv.put(DataEntry.COLUMN_ID,model.getId());
        cv.put(DataEntry.COLUMN_DATA_TYPE,model.getType());
        cv.put(DataEntry.COLUMN_PARENT_ID,model.getParentId());
        cv.put(DataEntry.COLUMN_EXTRA_INFO,model.getExtraInfo());
        cv.put(DataEntry.COLUMN_RENAMED_VALUE,model.getRenamedValue());
        cv.put(DataEntry.COLUMN_CHANGE_BOOL,model.isChanged());
        db.update(DataEntry.TABLE_NAME,cv,
                DataEntry.COLUMN_ID+"=?",new String[]{model.getId()});
    }

    public static StoredDataModel getRowById(SQLiteDatabase db, String id) {
        Cursor c = db.query(DataEntry.TABLE_NAME,null,
                DataEntry.COLUMN_ID+"=?", new String[]{id},null,null,null);
        if(c.getCount()!=1){
            return null;
        }
        c.moveToFirst();
        StoredDataModel model = new StoredDataModel();
        model.setId(c.getString(c.getColumnIndex(DataEntry.COLUMN_ID)));
        model.setType(c.getString(c.getColumnIndex(DataEntry.COLUMN_DATA_TYPE)));
        model.setRenamedValue(c.getString(c.getColumnIndex(DataEntry.COLUMN_RENAMED_VALUE)));
        model.setName(c.getString(c.getColumnIndex(DataEntry.COLUMN_NAME)));
        model.setParentId(c.getString(c.getColumnIndex(DataEntry.COLUMN_PARENT_ID)));
        model.setExtraInfo(c.getString(c.getColumnIndex(DataEntry.COLUMN_EXTRA_INFO)));
        model.setChangedInt(c.getInt(c.getColumnIndex(DataEntry.COLUMN_CHANGE_BOOL)));
        c.close();
        return model;
    }

    public static boolean checkIdPresence(SQLiteDatabase db,String id) {
        Cursor cursor = db.query(DataEntry.TABLE_NAME,
                new String[]{DataEntry.COLUMN_ID},
                DataEntry.COLUMN_ID+"=?", new String[]{id},null,null,null);

        boolean isPresent = cursor.getCount()==1;
        cursor.close();
        return isPresent;
    }
}
