package com.example.dhruv.cbrmp3index.data;

import android.support.annotation.NonNull;

import com.example.dhruv.cbrmp3index.viewmodel.ItemViewModel;

/**
 * Created by dhruv on 31/3/17.
 */

public class StoredDataModel {

    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String type;
    @NonNull
    private String parentId;
    private String renamedValue;
    private String extraInfo;
    private boolean isChanged = false;

    public void mapReceived(RecvdDataModel recvdDataModel, String parent){
        setId(recvdDataModel.getId());
        setName(recvdDataModel.getName());
        setType(recvdDataModel.getType());
        setParentId(parent);
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public void setChangedInt(int bool) {
        isChanged = bool==1;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getParentId() {
        return parentId;
    }

    public void setParentId(@NonNull String parentId) {
        this.parentId = parentId;
    }

    public String getRenamedValue() {
        return renamedValue;
    }

    public void setRenamedValue(String renamedValue) {
        this.renamedValue = renamedValue;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
