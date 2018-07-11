package com.uowee.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by GuoWee on 2018/7/11.
 */

public class StartActivity extends ListActivity {
    private String[] mTitles = new String[]{
            MainActivity.class.getSimpleName(),
            EffectFirstActivity.class.getSimpleName(),
            EffectSecondActivity.class.getSimpleName()
    };
    private Class[] mActivities = new Class[]{
            MainActivity.class,
            EffectFirstActivity.class,
            EffectSecondActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTitles));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(this, mActivities[position]));
    }
}
