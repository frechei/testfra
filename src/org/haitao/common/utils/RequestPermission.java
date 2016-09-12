package org.haitao.common.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

/**
 * Description 申请权限  必须引入 v7 example compile 'com.android.support:appcompat-v7:23.4.0'
 * Author by wangHaitao(a758277560@gmail.com)
 * Created  on 2016/7/29 19:00
 * Version V1.0
 */
public abstract class RequestPermission {

    private int requestCode;
    private Activity activity;
    private String[] permissionList;

    protected RequestPermission(Activity activity, String[] permissionList, int requestCode) {
        this.requestCode = requestCode;
        this.activity = activity;
        this.permissionList = permissionList;

    }
    public void request(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionList = getShouldRequestList();
            if(permissionList.length != 0){
                ActivityCompat.requestPermissions(activity, permissionList, requestCode);
            }
            else{
                onSuccess();
            }
        } else{
            onSuccess();
        }
    }
    protected RequestPermission(Activity activity, String permission, int requestCode) {
        this.requestCode = requestCode;
        this.activity = activity;
        this.permissionList = new String[]{permission};
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionList = getShouldRequestList();
            if(permissionList.length != 0)
                ActivityCompat.requestPermissions(activity, permissionList, requestCode);
            else onSuccess();
        } else onSuccess();
    }

    private String[] getShouldRequestList() {
        List<String> permissions = new ArrayList<String>();
        for(String permission : permissionList) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permission);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) permissions.add(permission);
        }
        return permissions.toArray(new String[]{});
    }

    private String[] getFailList(String[] permissionList,int[] grantResults) {
        List<String> failList = new ArrayList<String>();
        for(int i = 0;i<grantResults.length;++i) {
            if(grantResults[i] == -1) failList.add(permissionList[i]);
        }
        return failList.toArray(new String[]{});
    }

    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        if(requestCode == this.requestCode ) {
            String[] failList = getFailList(permissions,grantResults);
            if(failList.length == 0){
                this.onSuccess();
            }
            else{
                this.onFail(failList);
            }
        }else {
        }

    }

    public abstract void onSuccess();

    public abstract void onFail(String[] failList);
}
