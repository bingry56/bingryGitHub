package com.example.bingrygallery.ui;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bingrygallery.R;
import com.example.bingrygallery.util.ImageFetcher;
import com.example.bingrygallery.util.ImageWorker;

import android.os.Bundle;

public class ImageDetailFragment extends Fragment{
	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
	private String mImageUrl;
	private ImageView mImageView;
	private ImageFetcher mImageFetcher;
	
	
	public static ImageDetailFragment newInstance(String imageUrl){
		final ImageDetailFragment f= new ImageDetailFragment();
		
		final Bundle args = new Bundle();
		args.putString(IMAGE_DATA_EXTRA, imageUrl);
		f.setArguments(args);
		return f;
	}
	
	public ImageDetailFragment(){}
	
	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView)v.findViewById(R.id.imageView);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		if(ImageDetailActivity.class.isInstance(getActivity())){
			mImageFetcher = ((ImageDetailActivity)getActivity()).getImageFetcher();
			mImageFetcher.loadImage(mImageUrl, mImageView);
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mImageView !=null){
			ImageWorker.cancelWork(mImageView);
			mImageView.setImageDrawable(null);
		}
	}
}
