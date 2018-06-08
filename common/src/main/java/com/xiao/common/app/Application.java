package com.xiao.common.app;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    /**
     * 获取缓存文件夹地址
     */
    public static File getCacheDirFile() {

        return instance.getCacheDir();
    }

    /**
     * 获取头像的临时存储地址
     */
    public static File getPortraitTmpFile() {

        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");

        dir.mkdirs();

        // 删除旧的缓存文件
        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }


        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");

        return path.getAbsoluteFile();
    }


    /**
     * 获取音频文件的临时存储地址
     *
     * @param isTmp 是否缓存文件 true 每次返回的文件地址是相同的
     * @return
     */
    public static File getAudioTmpFile(boolean isTmp) {

        File dir = new File(getCacheDirFile(), "audio");

        dir.mkdirs();

        File[] files = dir.listFiles();

        if (files != null && files.length > 0) {

            for (File tmpFile : files) {
                tmpFile.delete();
            }
        }

        //TODO 此处有疑问  加入此行是否有意义，原本目的是为了减少缓存文件  但是事实是每次都会删除此目录下的所有文件,每次调用也都是新文件  个人觉得没什么用
        File path = new File(dir, isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");

        return path.getAbsoluteFile();

    }

    public static void showToast(final String msg) {

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public static void showToast(@StringRes int msgId) {

        showToast(instance.getString(msgId));

    }


}
