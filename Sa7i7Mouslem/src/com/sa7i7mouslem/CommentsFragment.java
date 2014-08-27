package com.sa7i7mouslem;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sa7i7mouslem.adapters.CommentsAdapter;
import com.sa7i7mouslem.adapters.IFragmentNotifier;
import com.sa7i7mouslem.entity.Comment;
import com.sa7i7mouslem.entity.Hadith;
import com.sa7i7mouslem.externals.SAMDataBase;
import com.sa7i7mouslem.externals.SAMManager;
import com.sa7i7mouslem.utils.MySuperScaler;
import com.sa7i7mouslem.utils.SAMFonts;


@SuppressLint("ValidFragment")
public class CommentsFragment extends ListFragment implements IFragmentNotifier, OnTouchListener{

	private CommentsAdapter adapter;
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	
	private TextView txv_text, txv_title;
	private Button btn_showMore, btn_add_comment, btn_back;
	
	private SAMDataBase sabDB;
	private Hadith hadith;

	public CommentsFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	public CommentsFragment(Hadith hadith) {
		this.hadith = hadith;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		SAMManager.getInstance(getActivity()).setFragmentNotifier2(this);
		sabDB = ((MainActivity)getActivity()).sabDB;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();

		SAMManager.getInstance(getActivity()).setFragmentNotifier2(null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

		if(!(MySuperScaler.scaled))
			MySuperScaler.scaleViewAndChildren(rootView, MySuperScaler.scale);

		txv_title = (TextView) rootView.findViewById(R.id.txv_title);
		txv_text = (TextView) rootView.findViewById(R.id.txv_text);
		btn_showMore = (Button) rootView.findViewById(R.id.btn_showMore);
		btn_add_comment = (Button) rootView.findViewById(R.id.btn_add_comment);
		btn_back = (Button) rootView.findViewById(R.id.btn_back);
		
		int sizeTitle = (int) MySuperScaler.screen_width / 16 ;
		txv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeTitle);
		
		int size = (int) MySuperScaler.screen_width / 23 ;
		txv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		
		txv_title.setTypeface(SAMFonts.getMOHANDFont());
		txv_text.setTypeface(SAMFonts.getMOHANDFont());
		
		btn_showMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hadith.setShown(!hadith.isShown());
				toggleShown(hadith.isShown());
			}
		});
		
		btn_add_comment.setOnTouchListener(this);
		btn_back.setOnTouchListener(this);

		toggleShown(hadith.isShown());
		
		adapter = new CommentsAdapter(getActivity(), R.layout.comment_list_item, comments);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setAdapter(adapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		
		refreshList();
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				showPopupMenu(position);
			}
		});
	}
	
	private void refreshList(){
		comments.clear();
		comments.addAll(sabDB.getCommentsWithHadithID(hadith.getId()));
		adapter.notifyDataSetChanged();
	}

	private void toggleShown(boolean isShown){
		if(isShown){
			txv_text.setMovementMethod(new ScrollingMovementMethod());
			txv_text.setMaxHeight(((MainActivity) getActivity()).screen_height / 2);
			txv_text.setText(Html.fromHtml(SAMDataBase.formatHadith(hadith.getText()).concat(".")));
			btn_showMore.setBackgroundResource(R.drawable.showless_selector);
		}
		else{
			txv_text.setMovementMethod(null);
			txv_text.setMaxLines(2);
			txv_text.setText(Html.fromHtml(SAMDataBase.formatHadith(hadith.getText()).concat("...")));
			btn_showMore.setBackgroundResource(R.drawable.showmore_selector);
		}
	}

	@Override
	public void requestRefrech() {
		refreshList();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		
		getListView().setEnabled(enabled);
		getListView().setClickable(enabled);
		
		btn_showMore.setEnabled(enabled);
		btn_add_comment.setEnabled(enabled);
		btn_back.setEnabled(enabled);
		
	}
	
	private void showPopupMenu(final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.modifications)
               .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   ((MainActivity) getActivity()).gotoAddEditCommentFragment(MainActivity.EDIT_COMMENT_FRAGMENT, comments.get(position), hadith.getId());
                   }
               })
               .setNegativeButton(R.string.remove, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       if(sabDB.removeComment(comments.get(position)))
                       {
                    	   comments.remove(position);
                    	   adapter.notifyDataSetChanged();
                    	   
                    	   SAMManager.getInstance(getActivity()).getFragmentNotifier().requestRefrech();
                       }
                   }
               })
               .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
        			public void onClick(DialogInterface dialog, int id) {
        				return;
        			}
        });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			Button view = (Button) v;
			view.getBackground().setColorFilter(0x77000000, 

					PorterDuff.Mode.SRC_ATOP);
			v.invalidate();
			break;
		}
		case MotionEvent.ACTION_UP: {
			
			Button view = (Button) v;
			view.getBackground().clearColorFilter();
			view.invalidate();

			switch (v.getId()) {
			case R.id.btn_add_comment:
				((MainActivity) getActivity()).gotoAddEditCommentFragment(MainActivity.ADD_COMMENT_FRAGMENT, null, hadith.getId());
				break;
			case R.id.btn_back:
				((MainActivity) getActivity()).onBackPressed();
				break;
			default:
				break;
			}

		}
		case MotionEvent.ACTION_CANCEL: {
			Button view = (Button) v;
			view.getBackground().clearColorFilter();
			view.invalidate();
			break;
		}
		}
		return true;
	}
}
