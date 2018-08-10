package com.xiao.common.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.ArrayMap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.xiao.common.utils.StreamUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 表情工具类
 * Created by xiao on 2018/8/10.
 */

public class Face {

    /**
     * 全局的表情映射ArrayMap，更加轻量级
     */
    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();

    private static List<FaceTab> FACE_TABS = null;

    public static List<FaceTab> all(Context context) {
        init(context);
        return FACE_TABS;
    }


    private static void init(Context context) {

        if (FACE_TABS == null) {

            synchronized (Face.class) {
                if (FACE_TABS == null) {

                    ArrayList<FaceTab> faceTabs = new ArrayList<>();
                    FaceTab tab = initAssetsFace(context);

                    if (tab != null) {
                        faceTabs.add(tab);
                    }

                    tab = initResourceFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    }

                    for (FaceTab faceTab : faceTabs) {
                        faceTab.copyToMap(FACE_MAP);
                    }

                    //init list 不可变的集合
                    FACE_TABS = Collections.unmodifiableList(faceTabs);
                }
            }
        }

    }

    /**
     * 从face-t.zip 包解析我们的表情
     */
    private static FaceTab initAssetsFace(Context context) {

        String faceAsset = "face-t.zip";
        //data/data/包名/files/face/ft/*
        String faceCacheDir = String.format("%s/face/tf", context.getFilesDir());
        File faceFolder = new File(faceCacheDir);
        if (!faceFolder.exists()) {
            //不存在进行初始化
            if (faceFolder.mkdirs()) {

                try {
                    InputStream inputStream = context.getAssets().open(faceAsset);

                    File faceSource = new File(faceFolder, "source.zip");
                    StreamUtil.copy(inputStream, faceSource);
                    unZipFile(faceSource, faceFolder);

                    StreamUtil.delete(faceSource.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        File infoFile = new File(faceCacheDir, "info.json");
        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = gson.newJsonReader(new FileReader(infoFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        FaceTab tab = gson.fromJson(reader, FaceTab.class);

        for (Bean face : tab.faces) {
            face.preview = String.format("%s/%s", faceCacheDir, face.preview);
            face.source = String.format("%s/%s", faceCacheDir, face.source);

        }
        return tab;

    }

    /**
     * 解压
     */
    private static void unZipFile(File faceZip, File destDir) throws IOException {

        final String folderPath = destDir.getAbsolutePath();

        ZipFile zipFile = new ZipFile(faceZip);
        for (Enumeration<?> entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(".")) {
                continue;
            }

            InputStream in = zipFile.getInputStream(entry);
            String str = folderPath + File.separator + name;

            str = new String(str.getBytes("8859_1"), "GB2312");

            File destFile = new File(str);

            StreamUtil.copy(in, destFile);

        }
    }


    /**
     * 从drawable资源中加载数据并映射到对应的key
     */
    private static FaceTab initResourceFace(Context context) {

        final ArrayList<Bean> faces = new ArrayList<>();
        final Resources resources = context.getResources();
        String packageName = context.getApplicationInfo().packageName;

        for (int i = 1; i <= 142; i++) {

            String key = String.format(Locale.ENGLISH, "fb%03d", i);
            String resStr = String.format(Locale.ENGLISH, "face_base_%03d", i);

            int resId = resources.getIdentifier(resStr, "drawable", packageName);
            if (resId == 0) {
                continue;
            }

            faces.add(new Bean(key, resId));
        }

        if (faces.size() == 0) {
            return null;
        }

        return new FaceTab("NAME", faces.get(0).preview, faces);

    }


    /**
     * 输入表情到editable
     */
    public static void inputFace(@NonNull final Context context, final Editable editable,
                                 final Face.Bean bean, final int size) {

        Glide.with(context)
                .load(bean.preview)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Spannable spannable = new SpannableString(String.format("[%s]", bean.key));
                        ImageSpan span = new ImageSpan(context, resource, ImageSpan.ALIGN_BASELINE);
                        spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editable.append(spannable);
                    }
                });
    }

    /**
     * 拿一个Bean
     */
    public static Bean get(Context context, String key) {
        init(context);
        if (FACE_MAP.containsKey(key)) {
            return FACE_MAP.get(key);
        }
        return null;
    }


    /**
     * 每一个表情盘，含有很多表情
     */
    public static class FaceTab {
        public List<Bean> faces = new ArrayList<>();
        public String name;

        //预览图，包括了drawable下面的资源int类型
        public Object preview;

        public FaceTab(String name, Object preview, List<Bean> faces) {
            this.faces = faces;
            this.name = name;
            this.preview = preview;
        }


        void copyToMap(ArrayMap<String, Bean> faceMap) {
            for (Bean face : faces) {
                faceMap.put(face.key, face);
            }
        }
    }


    public static class Bean {
        public String key;
        public String desc;
        public Object source;
        public Object preview;

        public Bean(String key, int preview) {
            this.key = key;
            this.preview = preview;
        }
    }
}
