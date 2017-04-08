package com.example.bingrygallery.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.bingrygallery.util.RecyclingBitmapDrawable;

public class RecyclingImageView extends ImageView {
	public RecyclingImageView(Context context){
		super(context);
	}
	
	public RecyclingImageView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	protected void onDetachedFromWindoww() {
		setImageDrawable(null);
		super.onDetachedFromWindow();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable){
		final Drawable previousDrawable = getDrawable();
		
		super.setImageDrawable(drawable);
		notifyDrawable(drawable, true);
		notifyDrawable(previousDrawable, false);
	}
	
	private static void notifyDrawable(Drawable drawable, final boolean isDisplayed){
		if(drawable instanceof RecyclingBitmapDrawable){
			((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
		}
	}
}
