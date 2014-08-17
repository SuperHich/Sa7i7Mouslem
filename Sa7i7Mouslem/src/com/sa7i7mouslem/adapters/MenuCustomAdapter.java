package com.sa7i7mouslem.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sa7i7mouslem.R;
import com.sa7i7mouslem.utils.MySuperScaler;

public class MenuCustomAdapter extends BaseAdapter implements OnTouchListener {

	IMenuListener listener;
	TypedArray images;
	LayoutInflater inflater;
	public MenuCustomAdapter(Context context, TypedArray lListIcone)
	{
		images=lListIcone;
		inflater= LayoutInflater.from(context);
		listener = (IMenuListener) context;

	}
	@Override
	public int getCount() {
		return images.length();
	}
	@Override
	public Object getItem(int arg0) {
		return arg0;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.rowlv_module, null);
			
	//		MySuperScaler.scaleViewAndChildren(convertView, MySuperScaler.scale);
			
			holder.iv= (ImageView) convertView.findViewById(R.id.trim1);
			holder.iv.setOnTouchListener(this);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.iv.setTag(position);
		holder.iv.setBackgroundDrawable(images.getDrawable(position));
		return convertView;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			ImageView view = (ImageView) v;
			view.getBackground().setColorFilter(0x77ffffff, PorterDuff.Mode.SRC_ATOP);
			v.invalidate();
			break;
		}
		case MotionEvent.ACTION_UP: {
			int position = (Integer) v.getTag();
			listener.onMenuItemClicked(position);

		}
		case MotionEvent.ACTION_CANCEL: {
			ImageView view = (ImageView) v;
			view.getBackground().clearColorFilter();
			view.invalidate();
			break;
		}
		}
		return true;
	}

	class ViewHolder
	{
		ImageView iv;
	}


}