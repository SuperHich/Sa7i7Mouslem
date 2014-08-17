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
import com.sa7i7mouslem.entity.Comment;
import com.sa7i7mouslem.utils.MySuperScaler;

public class CommentsAdapter extends ArrayAdapter<Comment> {

	Context mContext;
	int layoutResourceId;
	ArrayList<Comment> data = null;
	LayoutInflater inflater;
	
	public CommentsAdapter(Context mContext, int layoutResourceId, ArrayList<Comment> data) {

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
			holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title_comment); 
			holder.txv_text = (TextView) convertView.findViewById(R.id.txv_text_comment);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}

		/*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 */
		
		int size = (int) MySuperScaler.screen_width / 23 ;
		holder.txv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		holder.txv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		
		Comment comment = data.get(position);

		holder.txv_title.setText((position+1) + "- " + comment.getTitle());
		holder.txv_text.setText(comment.getText());

		return convertView;
	}

	class ViewHolder
	{
		TextView txv_title;
		TextView txv_text;
	}

}
