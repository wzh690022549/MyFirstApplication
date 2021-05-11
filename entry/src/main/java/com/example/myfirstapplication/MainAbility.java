package com.example.myfirstapplication;

import com.example.myfirstapplication.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

public class MainAbility extends Ability implements IAbilityConnection, Component.ClickedListener, Component.KeyEventListener {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        // 将UI资源文件加载到应用中
        super.setMainRoute(MainAbilitySlice.class.getName());
    }

    /**
     * 应用迁移: 应用迁移前需要得到用户的多设备协同权限的授权. 若应用已获得该授权, 则应用可以直接发生迁移.
     */
    private void migrateAbility() {
        if (verifySelfPermission(SystemPermission.DISTRIBUTED_DATASYNC) == IBundleManager.PERMISSION_GRANTED) {
            this.continueAbility();
        } else {
            requestPermission(SystemPermission.DISTRIBUTED_DATASYNC);
        }
    }

    /**
     * 主动向用户发起权限申请
     *
     * @param permission 要申请的权限
     */
    private void requestPermission(String permission) {
        if (canRequestPermission(permission)) {
            requestPermissionsFromUser(new String[]{permission}, 0x1001);
        }
    }

    /**
     * 当用户完成权限申请确认后
     *
     * @param requestCode 请求码
     * @param permissions 要申请的权限
     * @param grantResults 权限授予结果数组
     */
    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions == null || permissions.length == 0 || grantResults == null || grantResults.length == 0) {
            return;
        }
        if (requestCode == 0x1001 && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
            this.continueAbility();
        }
    }
}
