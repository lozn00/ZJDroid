package com.android.reverse.apimonitor;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.android.reverse.hook.HookParam;
import com.android.reverse.util.Logger;
import com.android.reverse.util.RefInvoke;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class ContextImplHook extends ApiMonitorHook {

    @Override
    public void startHook() {
        // TODO Auto-generated method stub
        Method registerReceivermethod = RefInvoke.findMethodExact(
                "android.app.ContextImpl", ClassLoader.getSystemClassLoader(),
                "registerReceiver", BroadcastReceiver.class, IntentFilter.class);
        hookhelper.hookMethod(registerReceivermethod, new AbstractBahaviorHookCallBack() {

            @Override
            public void descParam(HookParam param) {
                // TODO Auto-generated method stub
                Logger.log_behavior("Register BroatcastReceiver");
                if (param.args[0] != null) {
                    Logger.log_behavior("The BroatcastReceiver ClassName = " + param.args[0].getClass().toString());
                }
                if (param.args[1] != null) {
                    IntentFilter arg = (IntentFilter) param.args[1];
                    String intentstr = descIntentFilter(arg);
                    Logger.log_behavior("Intent Action = [" + intentstr + "]");
                    if (arg.hasAction(Intent.ACTION_SCREEN_OFF)) {
                        removeAction(arg, Intent.ACTION_SCREEN_OFF);
                    } else if (arg.hasAction(Intent.ACTION_TIME_TICK)) {
                        removeAction(arg, Intent.ACTION_TIME_TICK);//解锁USER_PRESENT
                        //删除
                    }
                }

            }
        });
    }

    private void removeAction(IntentFilter arg, String action) {
        try {
            Field mActions = arg.getClass().getDeclaredField("mActions");
            try {
                mActions.setAccessible(true);
                ArrayList<String> o = (ArrayList<String>) mActions.get(arg);
                boolean remove = o.remove(action);
                Logger.log_behavior("Remove  Action Result" + remove + "= [" + action + "]");
            } catch (IllegalAccessException e) {

                Logger.log_behavior("Remove  Action Fail= [" + action + "]" + e.toString());
            }
        } catch (NoSuchFieldException e) {
            Logger.log_behavior("Remove  Action Faiil1= [" + action + "]" + e.toString());
            e.printStackTrace();
        }
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
