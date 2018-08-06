package com.xiao.mineim.fragment.group;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiao.mineim.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMemberAddFragment extends BottomSheetDialogFragment {


    public GroupMemberAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_member_add, container, false);
    }

}
