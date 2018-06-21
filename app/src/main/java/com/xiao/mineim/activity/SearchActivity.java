package com.xiao.mineim.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.xiao.common.app.ToolbarActivity;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.search.SearchContactFragment;
import com.xiao.mineim.fragment.search.SearchGroupFragment;

public class SearchActivity extends ToolbarActivity {

    private final static String EXTRA_TYPE = "EXTRA_TYPE";

    public final static int TYPE_CONTACT = 0x1;
    public final static int TYPE_GROUP = 0x2;

    private int mSearchType;

    private SearchFragment mSearchFragment;


    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_search;
    }

    public static void show(Context context, int type) {

        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle extras) {

        mSearchType = extras.getInt(EXTRA_TYPE);
        return mSearchType == TYPE_CONTACT || mSearchType == TYPE_GROUP;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Fragment fragment;
        if (mSearchType == TYPE_CONTACT) {

            SearchContactFragment contactFragment = new SearchContactFragment();
            fragment = contactFragment;
            mSearchFragment = contactFragment;
        } else {

            SearchGroupFragment groupFragment = new SearchGroupFragment();
            fragment = groupFragment;
            mSearchFragment = groupFragment;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.search_layout_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {

            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //当点击了提交按钮时候
                    search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //当文字改变的时候  不会及时搜索 只在为null的情况下开始搜索
                    if (TextUtils.isEmpty(newText)) {

                        search(newText);

                        return true;
                    }
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);

    }

    /**
     * 搜索
     */
    private void search(String query) {

//        if (mSearchType == )
        if (mSearchFragment != null) {

            mSearchFragment.search(query);

        }

    }

    public interface SearchFragment {

        void search(String content);
    }
}
