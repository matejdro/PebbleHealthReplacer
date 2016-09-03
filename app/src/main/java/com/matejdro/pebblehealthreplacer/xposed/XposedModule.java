package com.matejdro.pebblehealthreplacer.xposed;

import android.annotation.SuppressLint;

import java.util.UUID;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class XposedModule implements IXposedHookLoadPackage {

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		switch (lpparam.packageName)
		{
			case "com.getpebble.android.basalt":
				hookPebbleTimeApp(lpparam);
				break;
		}


	}
    private void hookPebbleTimeApp(final XC_LoadPackage.LoadPackageParam lpparam)
    {
		// Developer connection fix
		findAndHookMethod("com.getpebble.android.framework.e.s", lpparam.classLoader, "f", new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				//Prevent timeout from starting
				param.setResult(null);

			}
		});
		findAndHookMethod("com.getpebble.android.PebbleApplication", lpparam.classLoader, "onCreate", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				// Kickstart developer connection on startup
				Object pebbleFrameworkObject = XposedHelpers.getObjectField(param.thisObject, "f");
				XposedHelpers.callMethod(pebbleFrameworkObject, "d");
			}
		});
    }
}