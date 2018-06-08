package com.xiao.mineim.fragment.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xiao.common.widget.GalleryView;
import com.xiao.mineim.App;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {

    private static final int PERMISSION_RETURN_CODE = 0x110;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_permission, container, false);

        rootView.findViewById(R.id.permission_button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPermissionState(getView());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new GalleryFragment.TransBottomSheetDialog(getContext());

    }

    /**
     * 刷新布局中权限的状态
     */
    private void refreshPermissionState(View rootView) {
        if (rootView == null) {
            return;
        }

        Context context = getContext();

        rootView.findViewById(R.id.permission_image_network)
                .setVisibility(hasNetworkPermission(context) ? View.VISIBLE : View.INVISIBLE);
        rootView.findViewById(R.id.permission_image_read)
                .setVisibility(hasReadPermission(context) ? View.VISIBLE : View.INVISIBLE);
        rootView.findViewById(R.id.permission_image_write)
                .setVisibility(hasWritePermission(context) ? View.VISIBLE : View.INVISIBLE);
        rootView.findViewById(R.id.permission_image_audio)
                .setVisibility(hasAudioPermission(context) ? View.VISIBLE : View.INVISIBLE);

    }

    /**
     * 检查是否有网络权限
     */
    private static boolean hasNetworkPermission(Context context) {

        //准备需要检查的网络权限
        String[] netWorkPermissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };


        return EasyPermissions.hasPermissions(context, netWorkPermissions);

    }

    /**
     * 检查是否有读取外部存储权限
     */
    private static boolean hasReadPermission(Context context) {

        //准备需要检查的读取权限
        String[] readPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };


        return EasyPermissions.hasPermissions(context, readPermissions);

    }

    /**
     * 检查是否有写入外部存储权限
     */
    private static boolean hasWritePermission(Context context) {

        //准备需要检查的写入外存储权限
        String[] writePermissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };


        return EasyPermissions.hasPermissions(context, writePermissions);

    }

    /**
     * 检查是否有录音权限
     */
    private static boolean hasAudioPermission(Context context) {

        //准备需要检查的录音权限权限
        String[] audioPermissions = new String[]{
                Manifest.permission.RECORD_AUDIO
        };


        return EasyPermissions.hasPermissions(context, audioPermissions);

    }


    private static void show(FragmentManager fragmentManager) {


        new PermissionFragment().show(fragmentManager, PermissionFragment.class.getName());
    }

    public static boolean hasAllPermissions(Context context, FragmentManager fragmentManager) {

        boolean hasAllPermission = hasNetworkPermission(context)
                && hasReadPermission(context)
                && hasWritePermission(context)
                && hasAudioPermission(context);

        //如果没有权限则申请权限
        if (!hasAllPermission) {

            show(fragmentManager);
        }

        return hasAllPermission;
    }

    /**
     * 申请权限
     */
    @AfterPermissionGranted(PERMISSION_RETURN_CODE)
    private void requestPermission() {

        String[] permissions = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        if (EasyPermissions.hasPermissions(getContext(), permissions)) {

            App.showToast(R.string.label_permission_ok);
            //getView在fragment得到跟布局  必须在onCreateView之后调用
            refreshPermissionState(getView());
        } else {

            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions),
                    PERMISSION_RETURN_CODE, permissions);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    //Android 中当一次权限申请被拒绝之后就只能去设置界面开启权限
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }
}