package com.android.reverse.apimonitor;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.reverse.hook.HookParam;
import com.android.reverse.util.Logger;
import com.android.reverse.util.RefInvoke;

import java.lang.reflect.Method;
import java.util.Iterator;

public class ActivitySkipHook extends ApiMonitorHook {

    @Override
    public void startHook() {
        // TODO Auto-generated method stub
            /*    Intent intent=new Intent();

        public void startActivity(Intent intent) {*/
        Method registerReceivermethod = RefInvoke.findMethodExact(
                "android.content.ContextWrapper", ClassLoader.getSystemClassLoader(),
                "startActivity", Intent.class);
        registerReceivermethod.setAccessible(true);
        hookhelper.hookMethod(registerReceivermethod, new AbstractBahaviorHookCallBack() {

            @Override
            public void descParam(HookParam param) {
                // TODO Auto-generated method stub
                Logger.log_behavior("Register startActivity");
                if (param.args[0] != null) {
                    Logger.log_behavior("The startActivity ClassName = " + param.args[0].getClass().toString());
                    Intent intent = (Intent) param.args[0];
                    Context context = null;
                    Logger.log_behavior("The startActivity Action = " + intent.getAction());
                    Logger.log_behavior("The startActivity packageName = " + intent.getPackage());
                    Logger.log_behavior("The startActivity class = " + intent.getComponent() != null ? intent.getComponent().getClassName() : "Empty");
                }
            }
        });
        registerReceivermethod = RefInvoke.findMethodExact(
                "android.content.ContextWrapper", ClassLoader.getSystemClassLoader(),
                "startService", Intent.class);
        registerReceivermethod.setAccessible(true);
        hookhelper.hookMethod(registerReceivermethod, new AbstractBahaviorHookCallBack() {

            @Override
            public void descParam(HookParam param) {
                // TODO Auto-generated method stub
                Logger.log_behavior("Register startService");
                if (param.args[0] != null) {
                    Logger.log_behavior("The startService ClassName = " + param.args[0].getClass().toString());
                    Intent intent = (Intent) param.args[0];
                    Context context = null;
                    Logger.log_behavior("The startService Action = " + intent.getAction());
                    Logger.log_behavior("The startService packageName = " + intent.getPackage());
                    Logger.log_behavior("The startService class = " + intent.getComponent() != null ? intent.getComponent().getClassName() : "Empty");
                }
            }
        });
    }

    public String descIntentFilter(IntentFilter intentFilter) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> actions = intentFilter.actionsIterator();
        String action = null;
        while (actions.hasNext()) {
            action = actions.next();
            sb.append(action + ",");
        }
        return sb.toString();

    }

}
