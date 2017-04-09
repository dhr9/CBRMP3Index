package com.example.dhruv.cbrmp3index;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dhruv.cbrmp3index.data.DataDbHelper;
import com.example.dhruv.cbrmp3index.data.StoredDataModel;
import com.example.dhruv.cbrmp3index.utils.DatabaseOperations;
import com.example.dhruv.cbrmp3index.viewmodel.ActivityViewModel;
import com.example.dhruv.cbrmp3index.viewmodel.ItemViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static String ACTIVITY_KEY = "key";
    public static String DATA_KEY = "data";

    @BindView(R.id.menu_list_view)
    RecyclerView menuListView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    MenuAdapter mAdapter = new MenuAdapter();
    ActivityViewModel activityViewModel = new ActivityViewModel();

    String PARENT_TAG;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        showLoading();

        final Intent intent = getIntent();
        PARENT_TAG = intent.getStringExtra(ACTIVITY_KEY);

        DataDbHelper dbHelper = new DataDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        menuListView.setLayoutManager(new LinearLayoutManager(this));
        menuListView.setAdapter(mAdapter);
        mAdapter.setFm(getSupportFragmentManager());
        mAdapter.setmContext(this);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);

                ItemViewModel item = mAdapter.getItems().get(positionStart);
                StoredDataModel model = DatabaseOperations.getRowById(mDb, item.getUniqueID());
                model.setRenamedValue(item.getRenamedName());
                model.setExtraInfo(item.getExtraInfo());
                model.setChanged(item.isChanged());
                DatabaseOperations.updateRowById(mDb, model);
            }
        });

        getData();
        setData();
        stopLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem mGlobal = menu.findItem(R.id.search_global);
        if(PARENT_TAG.equals(LoadingActivity.PARENT_TAG_VALUE)){
            mGlobal.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.get_marked:
                Toast.makeText(this,"Get Marked",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.search_local:
                Toast.makeText(this,"Search Local",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.search_global:
                Toast.makeText(this,"Search Global",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDb.close();
    }

    public void showLoading() {
        menuListView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        loadingIndicator.setVisibility(View.INVISIBLE);
        menuListView.setVisibility(View.VISIBLE);
    }

    private void getData() {
        Cursor cursor = DatabaseOperations.getPageData(mDb, PARENT_TAG);

        Log.v("MainActivity", PARENT_TAG + "\t\t" + cursor.getCount());
        ArrayList<ItemViewModel> itemViewModels = new ArrayList<>(0);
        for (int i = 0; i < cursor.getCount(); i++) {
            ItemViewModel model = new ItemViewModel();

            cursor.moveToPosition(i);
            model.mapFromCursor(this, cursor);
            itemViewModels.add(model);
        }
        cursor.close();
        activityViewModel.setItemViewModels(itemViewModels);
    }

    public static Intent createStandardIntent(Context context, String parent) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ACTIVITY_KEY, parent);

        return intent;
    }

    private void setData() {
        mAdapter.setItems(activityViewModel.getItemViewModels());
    }

}
