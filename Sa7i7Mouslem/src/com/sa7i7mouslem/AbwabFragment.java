package com.sa7i7mouslem;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sa7i7mouslem.adapters.AbwabAdapter;
import com.sa7i7mouslem.adapters.IFragmentNotifier;
import com.sa7i7mouslem.entity.Chapter;
import com.sa7i7mouslem.externals.SAMDataBase;
import com.sa7i7mouslem.externals.SAMManager;
import com.sa7i7mouslem.utils.LoadMoreListView;
import com.sa7i7mouslem.utils.LoadMoreListView.OnLoadMoreListener;
import com.sa7i7mouslem.utils.MySuperScaler;


public class AbwabFragment extends ListFragment implements IFragmentNotifier{

	public static final String ARG_BOOKID = "arg_bookid";
	
	private AbwabAdapter adapter;
	private ArrayList<Chapter> abwab = new ArrayList<Chapter>();
	
	private SAMDataBase sabDB;
	
	private int bookId = -1;
	private int pageId = 0;

	public AbwabFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		sabDB = ((MainActivity)getActivity()).sabDB;
		SAMManager.getInstance(getActivity()).setFragmentNotifier(this);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
//		SAMManager.getInstance(getActivity()).setFragmentNotifier(null);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(getArguments() != null)
			bookId = getArguments().getInt(ARG_BOOKID);
		
		View rootView = inflater.inflate(R.layout.fragment_abwab, container, false);

		if(!(MySuperScaler.scaled))
			MySuperScaler.scaleViewAndChildren(rootView, MySuperScaler.scale);


		adapter = new AbwabAdapter(getActivity(), R.layout.bab_list_item, abwab);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setAdapter(adapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		
		abwab.clear();
		if(bookId > -1)
			abwab.addAll(sabDB.getAllBabsFromBook(bookId));
		else
			abwab.addAll(sabDB.getAllBabsWithPage(pageId));
		
		adapter.notifyDataSetChanged();
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				((MainActivity) getActivity()).onBabItemClicked(abwab.get(position));
			}
		});
		
		((LoadMoreListView) getListView()).setFooterDividersEnabled(false);
		
		if(bookId == -1)
			((LoadMoreListView) getListView()).setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					// Do the work to load more items at the end of list
					// here
					new LoadDataTask().execute();
				}
			});
	}

	@Override
	public void requestRefrech() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnabled(boolean isEnabled) {
		getListView().setEnabled(isEnabled);
		getListView().setClickable(isEnabled);
	}
	
	private class LoadDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			
			try{
				Thread.sleep(1000);
			}catch(Exception e){}
			
			pageId += 1;
			abwab.addAll(sabDB.getAllBabsWithPage(pageId));

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// We need notify the adapter that the data have been changed
			adapter.notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		}
	}

}
