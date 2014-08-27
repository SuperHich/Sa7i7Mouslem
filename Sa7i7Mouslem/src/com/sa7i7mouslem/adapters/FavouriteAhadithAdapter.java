package com.sa7i7mouslem.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sa7i7mouslem.R;
import com.sa7i7mouslem.entity.Hadith;
import com.sa7i7mouslem.externals.SAMDataBase;
import com.sa7i7mouslem.utils.MySuperScaler;

public class FavouriteAhadithAdapter extends ArrayAdapter<Hadith> {

	Context mContext;
	IHadtihListener listener;
	int layoutResourceId;
	ArrayList<Hadith> data = null;
	LayoutInflater inflater;
	
	public FavouriteAhadithAdapter(Context mContext, int layoutResourceId, ArrayList<Hadith> data, IHadtihListener listener) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
		this.listener = listener;
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
			holder.textview = (TextView) convertView.findViewById(R.id.text); 
			holder.btn_showMore = (Button) convertView.findViewById(R.id.btn_showMore);
			holder.btn_removeFav = (Button) convertView.findViewById(R.id.btn_removeFav);
			
			holder.btn_showMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer)(v.getTag());					
					listener.onHadithShowMore(position);
				}
			});
			
			holder.btn_removeFav.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer)(v.getTag());					
					listener.onHadithFavorite(position);
				}
			});
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}

		holder.btn_showMore.setTag(position);
		holder.btn_removeFav.setTag(position);
		
		int size = (int) MySuperScaler.screen_width / 23 ;
		holder.textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		
		
		/*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 */
		Hadith hadith = data.get(position);

		if(hadith.isShown()){
			holder.textview.setMaxLines(Integer.MAX_VALUE);
			holder.textview.setText(Html.fromHtml(SAMDataBase.formatHadith(hadith.getText()).concat(".")));
			holder.btn_showMore.setBackgroundResource(R.drawable.showless_selector);
		}
		else{
			holder.textview.setMaxLines(2);
			holder.textview.setText(Html.fromHtml(SAMDataBase.formatHadith(hadith.getText()).concat(" ... ")));
			holder.btn_showMore.setBackgroundResource(R.drawable.showmore_selector);
		}

		return convertView;
	}

	class ViewHolder
	{
		TextView textview; 
		Button btn_showMore;
		Button btn_removeFav;
	}

}
