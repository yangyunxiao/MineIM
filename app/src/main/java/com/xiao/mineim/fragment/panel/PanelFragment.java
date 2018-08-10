package com.xiao.mineim.fragment.panel;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xiao.common.app.BaseFragment;
import com.xiao.common.face.Face;
import com.xiao.common.utils.UiTool;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.message.ChatFragment;

import net.qiujuer.genius.ui.Ui;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanelFragment extends BaseFragment {

    private PanelCallback mPanelCallback;


    public PanelFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_panel;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        initFace(rootView);
        initRecord(rootView);
        initGallery(rootView);
    }


    private void initFace(View rootView) {
        View facePanel = rootView.findViewById(R.id.lay_panel_face);

        View backspace = facePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PanelCallback callback = mPanelCallback;

                if (callback == null) {
                    return;
                }

                //模拟一个键盘删除点击
                KeyEvent backEvent = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL,
                        0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);

                callback.getInputEditText().dispatchKeyEvent(backEvent);
            }
        });

        TabLayout tabLayout = facePanel.findViewById(R.id.tab);
        ViewPager viewPager = facePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);


        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int totalScreen = UiTool.getScreenWidth(getActivity());
        final int spanCount = totalScreen / minFaceSize;

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;

                FaceAdapter adapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListenerImpl<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Face.Bean bean) {
                        if (mPanelCallback == null) {
                            return;
                        }

                        //表情添加到输入框
                        EditText editText = mPanelCallback.getInputEditText();
                        Face.inputFace(getContext(), editText.getText(), bean,
                                (int) (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));
                    }
                });

                recyclerView.setAdapter(adapter);
                container.addView(recyclerView);
                return recyclerView;

            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return Face.all(getContext()).get(position).name;
            }
        });


    }

    private void initRecord(View rootView) {

    }

    private void initGallery(View rootView) {

    }

    public void showFace() {

    }

    public void showRecord() {

    }

    public void showGallery() {

    }

    public void setup(PanelCallback callback) {

        mPanelCallback = callback;

    }

    public interface PanelCallback {

        EditText getInputEditText();
    }
}
