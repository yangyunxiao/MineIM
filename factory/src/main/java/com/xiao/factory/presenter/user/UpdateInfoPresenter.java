package com.xiao.factory.presenter.user;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.Factory;
import com.xiao.factory.R;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.api.user.UserUpdateModel;
import com.xiao.factory.model.card.UserCard;
import com.xiao.factory.model.db.User;
import com.xiao.factory.net.UploadHelper;
import com.xiao.common.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;


public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {

    private final static String TAG = "UpdateInfoPresenter";

    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }


    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {

        start();

        final UpdateInfoContract.View view = getView();

        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {

            view.showError(R.string.data_account_update_invalid_parameter);
        } else {

            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(photoFilePath);

                    if (TextUtils.isEmpty(url)) {

                        view.showError(R.string.data_upload_error);

                    } else {

                        UserUpdateModel model = new UserUpdateModel("", url, desc, isMan ? User.SEX_MAN : User.SEX_WOMAN);

                        UserHelper.updateUserInfo(model, UpdateInfoPresenter.this);

                    }
                }
            });
        }

    }

    @Override
    public void onDataLoadSuccess(UserCard userCard) {

        final UpdateInfoContract.View view = getView();

        if (view == null) {

            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                Log.e(TAG,"onDataLoadSuccess");
                view.updateSucceed();

            }
        });
    }

    @Override
    public void onDataLoadFailed(@StringRes final int failedMsg) {
        final UpdateInfoContract.View view = getView();

        if (view == null) {

            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                view.showError(failedMsg);
            }
        });
    }
}
