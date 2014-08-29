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

public class AboutAppFragment extends ListFragment implements OnTouchListener, OnClickListener {

	final static String SITE_WEB = "http://www.tech.islaamlight.com";
	final static String EMAIL = "noorapps@hotmail.com";
	final static String TWITTER = "https://twitter.com/noorapps";
	final static String WHATSAPP = "00966556118113";
	final static String PHONE = "00112303099";
	
	private TextView title , whatsapp, email, website, twitter, call;
	private ImageView img_whatsapp, img_email, img_site, img_twitter, img_call;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.about_links, container, false);
		if(!(MySuperScaler.scaled))
			MySuperScaler.scaleViewAndChildren(view, MySuperScaler.scale);
		
		
		title = (TextView) view.findViewById(R.id.title);
		
		img_whatsapp = (ImageView) view.findViewById(R.id.img_whatsapp);
		img_email = (ImageView) view.findViewById(R.id.img_email);
		img_site = (ImageView) view.findViewById(R.id.img_site);
		img_twitter = (ImageView) view.findViewById(R.id.img_twitter);
		img_call = (ImageView) view.findViewById(R.id.img_call);
		
		whatsapp = (TextView) view.findViewById(R.id.whatsapp);
		email = (TextView) view.findViewById(R.id.email);
		website = (TextView) view.findViewById(R.id.site);
		twitter = (TextView) view.findViewById(R.id.twitter);
		call = (TextView) view.findViewById(R.id.call);
	
		int size_title = (int) MySuperScaler.screen_width / 16 ;
		int size = (int) MySuperScaler.screen_width / 23 ;
		int size_web = (int) MySuperScaler.screen_width / 24 ;
		
		title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size_title);
		
		whatsapp.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		email.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		website.setTextSize(TypedValue.COMPLEX_UNIT_PX, size_web);
		twitter.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		call.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		
		title.setTypeface(SAMFonts.getMOHANDFont());
		whatsapp.setTypeface(SAMFonts.getMOHANDFont());
		email.setTypeface(SAMFonts.getMOHANDFont());
		website.setTypeface(SAMFonts.getMOHANDFont());
		twitter.setTypeface(SAMFonts.getMOHANDFont());
		call.setTypeface(SAMFonts.getMOHANDFont());
		
		img_whatsapp.setOnTouchListener(this);
		img_email.setOnTouchListener(this);
		img_site.setOnTouchListener(this);
		img_twitter.setOnTouchListener(this);
		img_call.setOnTouchListener(this);
		
		whatsapp.setOnClickListener(this);
		email.setOnClickListener(this);
		website.setOnClickListener(this);
		twitter.setOnClickListener(this);
		call.setOnClickListener(this);
	
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
			case R.id.img_email:
				sendMailTo(EMAIL);
				break;
			case R.id.img_site:
				gotoLink(SITE_WEB);
				break;
			case R.id.img_twitter:
				gotoLink(TWITTER);
				break;
			case R.id.img_call:
				makeCall(PHONE);
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.whatsapp:
			openWhatsappContact(WHATSAPP);
			break;
		case R.id.email:
			sendMailTo(EMAIL);
			break;
		case R.id.site:
			gotoLink(SITE_WEB);
			break;
		case R.id.twitter:
			gotoLink(TWITTER);
			break;
		case R.id.call:
			makeCall(PHONE);
			break;
		default:
			break;
		}
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

	private void sendMailTo(String email) {
		Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
		sendMailIntent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { email });
		sendMailIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendMailIntent, "Envoyer le mail"));
	}
	
	private void makeCall(String numberTel) {
		Uri number = Uri.parse("tel:" + numberTel);
		Intent dial = new Intent(Intent.ACTION_CALL, number);
		startActivity(dial);
	}
}
