package com.xiao.factory;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.xiao.common.utils.HashUtil;

import java.io.File;
import java.util.Date;

/**
 * 上传帮助类
 * Created by xiao on 2018/6/8.
 */

public class UploadHelper {

    private static final String TAG = UploadHelper.class.getName();

    private static final String ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com";

    private static final String BUCKET_NAME = "xiao-talker";

    private static OSS getClient() {

        OSSCredentialProvider ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(
                "LTAICoNdbNleZYW8", "GKzQa2K5CizSGTo2fTNGqboPB4sxcx"
        );

        return new OSSClient(Factory.application(), ENDPOINT, ossCredentialProvider);

    }


    /**
     * 上传文件
     */
    private static String upload(String objKey, String path) {

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objKey, path);

        OSS ossClient = getClient();

        try {
            // 开启同步上传
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);

            // 得到一个外网可访问的URL
            String url = ossClient.presignPublicObjectURL(BUCKET_NAME, objKey);

            Log.d(TAG, String.format("Media Upload Url returned is %s", url));

            return url;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String getDateString() {

        return DateFormat.format("yyyyMM", new Date()).toString();

    }

    private static String getImageObjKey(String path) {

        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDateString();

        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }


    private static String getPortraitObjKey(String path) {

        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDateString();

        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }


    private static String getAudioObjKey(String path) {

        String fileMd5 = HashUtil.getMD5String(new File(path));

        String dateString = getDateString();

        return String.format("audio/%s/%s.mp3", dateString, fileMd5);

    }

    public static String uploadImage(String path) {

        String objKey = getImageObjKey(path);
        return upload(objKey, path);
    }

    public static String uploadPortrait(String path) {
        String objKey = getPortraitObjKey(path);
        return upload(objKey, path);
    }

    public static String uploadAudio(String path) {
        String objKey = getAudioObjKey(path);
        return upload(objKey, path);
    }
}
