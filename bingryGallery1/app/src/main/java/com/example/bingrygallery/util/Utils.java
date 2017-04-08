package com.example.bingrygallery.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;

import com.example.bingrygallery.ui.ImageDetailActivity;
import com.example.bingrygallery.ui.GalleryMainActivity;
public class Utils {
    private Utils() {};


    @TargetApi(VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode() {
        if (Utils.hasGingerBread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(GalleryMainActivity.class, 1)
                        .setClassInstanceLimit(ImageDetailActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

	public static boolean hasFroyo() {
		// TODO Auto-generated method stub
		return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
	}

	public static boolean hasGingerBread(){
		return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
	}
	public static boolean hasHoneycomb() {
		// TODO Auto-generated method stub
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		// TODO Auto-generated method stub
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		// TODO Auto-generated method stub
		return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
	}

	public static boolean hasKitKat() {
		// TODO Auto-generated method stub
		return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
	}

}
