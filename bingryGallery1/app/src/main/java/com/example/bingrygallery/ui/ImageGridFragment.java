package com.example.bingrygallery.ui;




import com.example.bingrygallery.BuildConfig;
import com.example.bingrygallery.R;
import com.example.bingrygallery.util.ImageCache;
import com.example.bingrygallery.util.ImageFetcher;
import com.example.bingrygallery.util.Utils;
import com.example.bingrygallery.provider.Images;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class ImageGridFragment extends Fragment implements AdapterView.OnItemClickListener{
	private static final String TAG = "ImageGridFragment";
	private static final String IMAGE_CACHE_DIR = "thumbs";
	
	private int mImageThumbSize;
	private int mImageThumbSpacing;
	private ImageAdapter mAdapter;
	private ImageFetcher mImageFetcher;
	
	public ImageGridFragment(){}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
		
		mAdapter = new ImageAdapter(getActivity());
		
		ImageCache.ImageCacheParams cacheParams = 
				new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
		
		mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
		
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState){
		
		final View v = inflator.inflate(R.layout.image_grid_fragment, container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView absListView, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING){
					if(!Utils.hasHoneycomb()){
						mImageFetcher.setPauseWork(true);
					}
				} else {
					mImageFetcher.setPauseWork(false);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, 
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener(){
					 @TargetApi(VERSION_CODES.JELLY_BEAN)
	                 @Override
	                 public void onGlobalLayout() {
						 if(mAdapter.getNumColumns() ==0){
							 final int numColumns = (int) Math.floor(
									 mGridView.getWidth()/(mImageThumbSize + mImageThumbSpacing));
							 
							 if(numColumns >0){
								 final int columnWidth = (mGridView.getWidth()/numColumns) - mImageThumbSpacing;
								 mAdapter.setNumColumns(numColumns);
								 mAdapter.setItemHeight(columnWidth);
								 if(BuildConfig.DEBUG){
									 Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
								 }
								 if(Utils.hasJellyBean()){
									 mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
								 } else {
									 mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
								 }
								 
							 }
						 }
					 }
				}
				);
		return v;
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }
	@TargetApi(VERSION_CODES.JELLY_BEAN)
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id){
		final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
		i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int)id);
		
		if(Utils.hasJellyBean()){
			ActivityOptions options =
					ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
			getActivity().startActivity(i, options.toBundle());
		} else {
			startActivity(i);
		}
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.main_menu, menu);
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageFetcher.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private class ImageAdapter extends BaseAdapter{
		
		private final Context mContext;
		private int mItemHeight = 0;
		private int mNumColumns =0;
		private int mActionBarHeight =0;
		private GridView.LayoutParams mImageViewLayoutParams;
		
		public ImageAdapter(Context context){
			super();
			mContext = context;
			mImageViewLayoutParams = new GridView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			TypedValue tv= new TypedValue();
			
			if(context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
				mActionBarHeight = TypedValue.complexToDimensionPixelSize(
						tv.data, context.getResources().getDisplayMetrics());
			}
		}

		public void setNumColumns(int numColumns) {
			// TODO Auto-generated method stub
			mNumColumns = numColumns;
		}

		public void setItemHeight(int height) {
			// TODO Auto-generated method stub
			if(height == mItemHeight){
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
			mImageFetcher.setImageSize(height);
			notifyDataSetChanged();
		}

		public int getNumColumns() {
			// TODO Auto-generated method stub
			return mNumColumns;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(getNumColumns() == 0){
				return 0;
			}
			
			return Images.imageThumbUrls.length + mNumColumns;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position < mNumColumns ? null:Images.imageThumbUrls[position - mNumColumns];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position < mNumColumns ? 0: position - mNumColumns;
		}
		@Override
		public int getViewTypeCount(){
			return 2;
		}
		
		@Override
		public int getItemViewType(int position){
			return (position < mNumColumns ? 1: 0);
		}
		
		@Override
		public boolean hasStableIds(){return true;}
		
		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			// TODO Auto-generated method stub
			if(position < mNumColumns){
				if(convertView == null){
					convertView = new View(mContext);
				}
				
				convertView.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.MATCH_PARENT, mActionBarHeight));
				return convertView;
			}
			
			ImageView imageView;
			if(convertView == null){
				imageView = new RecyclingImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(mImageViewLayoutParams);
			} else {
				imageView = (ImageView) convertView;
			}
			
			if(imageView.getLayoutParams().height != mItemHeight){
				imageView.setLayoutParams(mImageViewLayoutParams);
			}
			
			
			mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
			return imageView;
		}
		
	}
	
}
