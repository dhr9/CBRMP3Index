package com.example.dhruv.cbrmp3index.data;

import android.provider.BaseColumns;

/**
 * Created by dhruv on 27/3/17.
 */

public class DataContract {

    public static final class DataEntry implements BaseColumns {

        public static final String TABLE_NAME = "mp3_index";
        public static final String COLUMN_ID = "unique_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PARENT_ID = "parent_id";
        public static final String COLUMN_DATA_TYPE = "data_type";
        public static final String COLUMN_RENAMED_VALUE = "renamed_value";
        public static final String COLUMN_EXTRA_INFO = "extra_info";
        public static final String COLUMN_CHANGE_BOOL = "change_bool";
    }
}
