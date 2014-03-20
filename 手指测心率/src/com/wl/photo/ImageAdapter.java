package com.wl.photo;

import android.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context context;
	private Integer[] mThumbIds;
	
	public ImageAdapter(Context context,Integer[] mThumbIds){
		this.context = context;
		this.mThumbIds = mThumbIds;
	}
	
	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(context);
		i.setImageResource(mThumbIds[position]);
		i.setAdjustViewBounds(true);
		i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		i.setBackgroundResource(R.drawable.picture_frame);
		return i;
	}

}
