package com.example.dhruv.cbrmp3index.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.dhruv.cbrmp3index.MainActivity;
import com.example.dhruv.cbrmp3index.data.DataContract;

/**
 * Created by dhruv on 26/3/17.
 */

public class ItemViewModel {

    private String songName;
    private String renamedName;

    private boolean isDirectory;
    private Intent nextScreen;
    private String uniqueID;
    private String extraInfo;
    private boolean isChanged;


    private boolean isMarked;

    public void mapFromCursor(Context context,Cursor cursor){
        setSongName(cursor.getString(cursor.getColumnIndex(DataContract.DataEntry.COLUMN_NAME)));
        setRenamedName(cursor.getString(cursor.getColumnIndex(DataContract.DataEntry.COLUMN_RENAMED_VALUE)));
        setDirectory(cursor.getString(cursor.getColumnIndex(DataContract.DataEntry.COLUMN_DATA_TYPE)).equals("folder"));
        String id = cursor.getString(cursor.getColumnIndex(DataContract.DataEntry.COLUMN_ID));
        setNextScreen(MainActivity.createStandardIntent(context, id));
        setExtraInfo(cursor.getString(cursor.getColumnIndex(DataContract.DataEntry.COLUMN_EXTRA_INFO)));
        setUniqueID(id);
        setChanged(cursor.getInt(cursor.getColumnIndex(DataContract.DataEntry.COLUMN_CHANGE_BOOL))==1);
    }

    public boolean isRenamed(){
        if(renamedName!=null){
            if(!(renamedName.equals("") || renamedName.equals(songName))){
                return true;
            }
        }
        return false;
    }

    public boolean hasExtraInfo(){
        if(extraInfo!=null){
            if(!extraInfo.equals("")){
                return true;
            }
        }
        return false;
    }

    public boolean checkForChange() {
        return isRenamed() || isMarked();
    }

//    ------------------------------


    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Intent getNextScreen() {
        return nextScreen;
    }

    public void setNextScreen(Intent nextScreen) {
        this.nextScreen = nextScreen;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getRenamedName() {
        return renamedName;
    }

    public void setRenamedName(String renamedName) {
        this.renamedName = renamedName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

}
