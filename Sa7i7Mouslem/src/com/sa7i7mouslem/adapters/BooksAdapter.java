package com.sa7i7mouslem.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sa7i7mouslem.R;
import com.sa7i7mouslem.entity.Book;
import com.sa7i7mouslem.utils.MySuperScaler;
import com.sa7i7mouslem.utils.SAMFonts;

public class BooksAdapter extends ArrayAdapter<Book> {

	Context mContext;
	int layoutResourceId;
	ArrayList<Book> data = null;
	LayoutInflater inflater;
	
	public BooksAdapter(Context mContext, int layoutResourceId, ArrayList<Book> data) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(layoutResourceId, parent, false);
			
			MySuperScaler.scaleViewAndChildren(convertView, MySuperScaler.scale);
			
			// get the elements in the layout
			holder.textview = (TextView) convertView.findViewById(R.id.txv_babTitle); 
			holder.textview.setTypeface(SAMFonts.getMOHANDFont());
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		int size = (int) MySuperScaler.screen_width / 21 ;
		holder.textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

		/*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 */
		Book book = data.get(position);

		holder.textview.setText(book.getName());

		return convertView;
	}

	class ViewHolder
	{
		TextView textview; 
	}

}
