package com.xiao.factory.utils;

import com.xiao.common.app.Application;
import com.xiao.common.utils.HashUtil;
import com.xiao.common.utils.StreamUtil;
import com.xiao.factory.net.Network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件缓存，实现文件的下载操作
 * 下载成功后回调响应方法
 * Created by xiao on 2018/8/16.
 */

public class FileCache<Holder> {

    private File baseDir;

    private String ext;

    private CacheListener<Holder> listener;

    /**
     * 最后一次目标
     */
    private SoftReference<Holder> holderSoftReference;

    public FileCache(String baseDir, String ext, CacheListener<Holder> listener) {
        this.baseDir = new File(Application.getCacheDirFile(), baseDir);
        this.ext = ext;
        this.listener = listener;
    }

    /**
     * 构建一个缓存文件，同一个网络路径对应本地的文件
     */
    private File buildCacheFile(String path) {
        String key = HashUtil.getMD5String(path);
        return new File(baseDir, key + "." + ext);
    }


    public void download(Holder holder, String path) {
        if (path.startsWith(Application.getCacheDirFile().getAbsolutePath())) {
            listener.onDownloadSucceed(holder, new File(path));
            return;
        }

        final File cacheFile = buildCacheFile(path);
        if (cacheFile.exists() && cacheFile.length() > 0) {
            listener.onDownloadSucceed(holder, cacheFile);
            return;
        }

        holderSoftReference = new SoftReference<Holder>(holder);

        OkHttpClient client = Network.getClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new NetCallback(holder, cacheFile));
    }

    /**
     * 拿最后的目标，只能使用一次
     */
    private Holder getLastHolderAndClear() {
        if (holderSoftReference == null) {
            return null;
        } else {
            Holder holder = holderSoftReference.get();
            holderSoftReference.clear();
            return holder;
        }
    }


    /**
     * 下载的回调
     */
    private class NetCallback implements Callback {

        private final SoftReference<Holder> holderSoftReference;
        private final File file;

        public NetCallback(Holder holder, File file) {
            this.holderSoftReference = new SoftReference<Holder>(holder);
            this.file = file;
        }

        @Override
        public void onFailure(Call call, IOException e) {

            Holder holder = holderSoftReference.get();
            if (holder != null && holder == getLastHolderAndClear()) {

                //仅仅最后一次才是有效的
                FileCache.this.listener.onDownloadFailed(holder);
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            InputStream inputStream = response.body().byteStream();

            if (inputStream != null && StreamUtil.copy(inputStream, file)) {
                Holder holder = holderSoftReference.get();
                if (holder != null && holder == getLastHolderAndClear()) {
                    FileCache.this.listener.onDownloadSucceed(holder, file);
                }
            } else {
                onFailure(call, null);
            }
        }


    }


    public interface CacheListener<Holder> {
        /**
         * 成功把目标返回
         */
        void onDownloadSucceed(Holder holder, File file);

        void onDownloadFailed(Holder holder);
    }

}
