package com.sa7i7mouslem;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sa7i7mouslem.utils.MySuperScaler;
import com.sa7i7mouslem.utils.SAMFonts;

public class AboutFragment extends ListFragment implements OnTouchListener, OnClickListener {

	final static String SITE_WEB = "http://www.alssunnah.com";
	final static String FACEBOOK = "https://www.facebook.com/Alssunah";
	final static String TWITTER = "https://twitter.com/alssunnah";
	final static String YOUTUBE = "https://youtube.com/Alssunnah1428";
	final static String INSTAGRAM = "http://instagram.com/alsunnah";
	final static String WHATSAPP = "0591155650";
	
	private TextView title , whatsapp, youtube, website, twitter, instagram, facebook;
	private ImageView img_whatsapp, img_youtube, img_siteweb, img_twitter, img_instagram, img_facebook;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.about, container, false);
		if(!(MySuperScaler.scaled))
			MySuperScaler.scaleViewAndChildren(view, MySuperScaler.scale);



		title = (TextView) view.findViewById(R.id.title);

		img_whatsapp = (ImageView) view.findViewById(R.id.img_whatsapp);
		img_youtube = (ImageView) view.findViewById(R.id.img_youtube);
		img_siteweb = (ImageView) view.findViewById(R.id.img_siteweb);
		img_twitter = (ImageView) view.findViewById(R.id.img_twitter);
		img_instagram = (ImageView) view.findViewById(R.id.img_instagram);
		img_facebook = (ImageView) view.findViewById(R.id.img_facebook);
		
		whatsapp = (TextView) view.findViewById(R.id.whatsapp);
		youtube = (TextView) view.findViewById(R.id.youtube);
		website = (TextView) view.findViewById(R.id.site);
		twitter = (TextView) view.findViewById(R.id.twitter);
		instagram = (TextView) view.findViewById(R.id.instagram);
		facebook = (TextView) view.findViewById(R.id.facebook);

		int size_title = (int) MySuperScaler.screen_width / 16 ;
		int size = (int) MySuperScaler.screen_width / 23 ;
		int size_web = (int) MySuperScaler.screen_width / 24 ;
			
		title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size_title);

		whatsapp.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		facebook.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		website.setTextSize(TypedValue.COMPLEX_UNIT_PX, size_web);
		twitter.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		instagram.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		youtube.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

		title.setTypeface(SAMFonts.getMOHANDFont());
		whatsapp.setTypeface(SAMFonts.getMOHANDFont());
		youtube.setTypeface(SAMFonts.getMOHANDFont());
		website.setTypeface(SAMFonts.getMOHANDFont());
		twitter.setTypeface(SAMFonts.getMOHANDFont());
		instagram.setTypeface(SAMFonts.getMOHANDFont());
		facebook.setTypeface(SAMFonts.getMOHANDFont());


		img_whatsapp.setOnTouchListener(this);
		img_youtube.setOnTouchListener(this);
		img_siteweb.setOnTouchListener(this);
		img_twitter.setOnTouchListener(this);
		img_instagram.setOnTouchListener(this);
		img_facebook.setOnTouchListener(this);

		whatsapp.setOnClickListener(this);
		facebook.setOnClickListener(this);
		website.setOnClickListener(this);
		twitter.setOnClickListener(this);
		instagram.setOnClickListener(this);
		youtube.setOnClickListener(this);

		return view ;

	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			ImageView view = (ImageView) v;
			view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
			v.invalidate();
			break;
		}
		case MotionEvent.ACTION_UP: {

			switch (v.getId()) {
			case R.id.img_whatsapp:
				openWhatsappContact(WHATSAPP);
				break;
			case R.id.img_youtube:
				gotoLink(YOUTUBE);
				break;
			case R.id.img_siteweb:
				gotoLink(SITE_WEB);
				break;
			case R.id.img_twitter:
				gotoLink(TWITTER);
				break;
			case R.id.img_instagram:
				gotoLink(INSTAGRAM);
				break;
			case R.id.img_facebook:
				gotoLink(FACEBOOK);
				break;

			default:
				break;
			}

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
	
	private void gotoLink(String link){
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
	}

	private void openWhatsappContact(String number) {
	    Uri uri = Uri.parse("smsto:" + number);
	    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
	    i.setPackage("com.whatsapp");  
	    startActivity(Intent.createChooser(i, ""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.whatsapp:
			openWhatsappContact(WHATSAPP);
			break;
		case R.id.youtube:
			gotoLink(YOUTUBE);
			break;
		case R.id.site:
			gotoLink(SITE_WEB);
			break;
		case R.id.twitter:
			gotoLink(TWITTER);
			break;
		case R.id.instagram:
			gotoLink(INSTAGRAM);
			break;
		case R.id.facebook:
			gotoLink(FACEBOOK);
			break;

		default:
			break;
		}
	}
}
