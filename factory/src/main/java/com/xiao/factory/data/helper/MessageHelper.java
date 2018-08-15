package com.xiao.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.common.Common;
import com.xiao.common.app.Application;
import com.xiao.common.utils.PicturesCompressor;
import com.xiao.common.utils.StreamUtil;
import com.xiao.factory.Factory;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.message.MsgCreateModel;
import com.xiao.factory.model.card.MessageCard;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.Message_Table;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;
import com.xiao.factory.net.UploadHelper;

import java.io.File;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息处理工具类
 */

public class MessageHelper {

    /**
     * 从本地查询消息
     */
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    public static void push(final MsgCreateModel model) {

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                Message message = findFromLocal(model.getId());

                //成功状态，如果是一个已经发送过得消息，则不能重新发送
                //正在发送的状态： 如果是一个消息正在发送，则不能重新发送
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {

                    return;
                }

                //发送是需要通知界面更新状态
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                //发送文件消息分两步，上传到云服务器，消息push到我们自己的服务器

                //如果是文件类型的(语音，图片，文件)  需要先上传后才发送
                if (card.getType() != Message.TYPE_STR) {
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)) {

                        String content;
                        switch (card.getType()) {
                            case Message.TYPE_PIC:
                                content = uploadPicture(card.getContent());
                                break;
                            case Message.TYPE_AUDIO:
                                content = uploadAudio(card.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }

                        if (TextUtils.isEmpty(content)) {
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                        }

                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);

                        //因为卡片的内容改变了，而我们上传到服务器是使用的model
                        //因此model跟着刷新
                        model.refreshByCard();
                    }
                }

                //直接发送， 进行网络调度
                RemoteService service = Network.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {

                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {

                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //检查是否是账户异常
                            Factory.decodeRspCode(rspModel, null);
                            //走失败的流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {

                        //通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    /**
     * 上传图片
     */
    private static String uploadPicture(String path) {
        File file = null;

        //通过Glide的缓存区间解决了图片外部权限的问题
        try {
            file = Glide.with(Factory.application())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (file != null) {

            //进行压缩
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png", cacheDir, SystemClock.uptimeMillis());

            try {

                if (PicturesCompressor.compressImage(file.getAbsolutePath(), tempFile,
                        Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {

                    String ossPath = UploadHelper.uploadImage(tempFile);
                    StreamUtil.delete(tempFile);
                    return ossPath;
                }
            } catch (Exception exception) {

                exception.printStackTrace();
            }
        }

        return null;
    }

    private static String uploadAudio(String content) {
        return null;
    }

    /**
     * 查询一个消息，这个消息是一个群中的最后一条信息
     */
    public static Message findLastWithGroup(String groupId) {

        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause().and(Message_Table.sender_id.eq(userId)).and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }
}
