package com.example.dhruv.cbrmp3index;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dhruv.cbrmp3index.data.DataDbHelper;
import com.example.dhruv.cbrmp3index.data.RecvdDataModel;
import com.example.dhruv.cbrmp3index.data.StoredDataModel;
import com.example.dhruv.cbrmp3index.utils.DatabaseOperations;
import com.example.dhruv.cbrmp3index.utils.JsonBaseType;
import com.example.dhruv.cbrmp3index.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhruv on 26/3/17.
 */

public class LoadingActivity extends AppCompatActivity {

    public static String PARENT_TAG_VALUE = "CBR 00000";

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.result_text_view)
    TextView resultTextView;

    String gitHubResponse;
    boolean isDatabasePresent = false;
    SQLiteDatabase mDb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading_activity);
        ButterKnife.bind(this);

        DataDbHelper dbHelper = new DataDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        if (getStoredVersion() == -1) {
            setStoredVersion(0);
            // Means no database stored found!
        } else if (DatabaseOperations.getDatabaseSize(mDb) > 0) {
            isDatabasePresent = true;
            // Means database found!
        }

        new GitHubQuery().execute();
    }

    public void startMainActivity() {
        mDb.close();

        startActivity(MainActivity.createStandardIntent(this,PARENT_TAG_VALUE));
        finish();
    }

    public void showLoading() {
        resultTextView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    public void showResults() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        resultTextView.setVisibility(View.VISIBLE);
    }

    public int getStoredVersion() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("value_key", -1);
    }

    public void setStoredVersion(int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("value_key", newValue);
        editor.apply();
    }

    private boolean recursiveSave(String parentTag, JSONArray baseData) {
        boolean success = true;
        try {
            for (int i = 0; i < baseData.length(); i++) {
                RecvdDataModel recvdDataModel = new RecvdDataModel();
                recvdDataModel.mapAll(baseData.getJSONObject(i));
                StoredDataModel storedDataModel;
                if (DatabaseOperations.checkIdPresence(mDb, recvdDataModel.getId())) {
                    storedDataModel = DatabaseOperations
                            .getRowById(mDb, recvdDataModel.getId());
                    storedDataModel.setName(recvdDataModel.getName());
                    storedDataModel.setParentId(parentTag);
                    DatabaseOperations.updateRowById(mDb, storedDataModel);
                } else {
                    storedDataModel = new StoredDataModel();
                    storedDataModel.mapReceived(recvdDataModel, parentTag);
                    DatabaseOperations.addNewRow(mDb, storedDataModel);
                }
                if (storedDataModel.getType().equals("folder")) {
//                    Log.v("Folder",String.valueOf(i)+"\t\t"+storedDataModel.getId());
                    success &= recursiveSave(storedDataModel.getId(), recvdDataModel.getData());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultTextView.setText("INTERNAL ERROR");
            showResults();
            success = false;
        }
        return success;
    }

    private void checkJSON(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            int jsonVersion = JsonBaseType.getVersion(jsonObject);
            if (jsonVersion == getStoredVersion()) {
                Log.v("JsonParse","Already have this Data, skipping JSON parse");
            } else {
                JSONArray baseData = JsonBaseType.getJsonArray(jsonObject);
                boolean sucess = recursiveSave(PARENT_TAG_VALUE,baseData);
                if (sucess) {
                    Log.v("JsonParse","JsonParse Successful");
                    setStoredVersion(jsonVersion);
                } else {
                    Log.v("JsonParse","JsonParse FAILED !");
                }
            }
            startMainActivity();
        } catch (JSONException e) {
            e.printStackTrace();
            resultTextView.setText("INTERNAL ERROR");
            showResults();
        }
    }

    private class GitHubQuery extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            gitHubResponse = s;

            if (s == null) {
                if (!isDatabasePresent) {
                    s = "PLEASE CONNECT TO THE INTERNET OR TRY AGAIN";
                    resultTextView.setText(s);
                    showResults();
                } else {
                    Log.v("Http","Proceeding without connecting to the internet");
                    startMainActivity();
                }
            } else {
                checkJSON(s);
            }
        }
    }

}



