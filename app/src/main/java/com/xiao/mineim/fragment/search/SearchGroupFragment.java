package com.xiao.mineim.fragment.search;


import com.xiao.common.app.BaseFragment;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.SearchActivity;

/**
 * 群搜索
 */
public class SearchGroupFragment extends BaseFragment implements SearchActivity.SearchFragment {


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
