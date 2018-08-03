package com.xiao.factory.presenter.group;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.presenter.BaseRecyclerPresenter;
import com.xiao.factory.Factory;
import com.xiao.factory.R;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.api.group.GroupCreateModel;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.model.db.view.UserSampleModel;
import com.xiao.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群创建界面的Presenter
 * Created by xiao on 2018/8/2.
 */

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View>
        implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {

    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        Factory.runOnAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateContract.View view = getView();
        view.showLoading();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)
                || TextUtils.isEmpty(picture) || users.size() == 0) {

            view.showError(R.string.label_group_create_invalid);

            return;
        }

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url)) {
                    return;
                }

                //进行网络请求
                GroupCreateModel model = new GroupCreateModel(name, desc, url, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });
    }

    private String uploadPicture(String path) {
        String url = UploadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(url)) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }


    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();

            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = sampleModel;
                viewModel.isSelected = false;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {

    }

    @Override
    public void onDataLoadSuccess(GroupCard groupCard) {

    }

    @Override
    public void onDataLoadFailed(@StringRes int failedMsg) {

    }
}
