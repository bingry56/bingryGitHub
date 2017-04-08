package com.example.bingrygallery.ui;

import com.example.bingrygallery.BuildConfig;
import com.example.bingrygallery.util.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class GalleryMainActivity extends FragmentActivity {
	private static final String TAG = "ImageGridActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_gallery_main);
		if(getSupportFragmentManager().findFragmentByTag(TAG) == null){
			final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, new ImageGridFragment());
			ft.commit();
		}
	}
	
}
