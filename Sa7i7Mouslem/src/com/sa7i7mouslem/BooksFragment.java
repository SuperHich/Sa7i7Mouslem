package com.sa7i7mouslem;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sa7i7mouslem.adapters.BooksAdapter;
import com.sa7i7mouslem.adapters.IFragmentNotifier;
import com.sa7i7mouslem.entity.Book;
import com.sa7i7mouslem.externals.SABDataBase;
import com.sa7i7mouslem.externals.SABManager;
import com.sa7i7mouslem.utils.MySuperScaler;


public class BooksFragment extends ListFragment implements IFragmentNotifier{

	private BooksAdapter adapter;
	private ArrayList<Book> books = new ArrayList<Book>();
	
	private SABDataBase sabDB;

	public BooksFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		sabDB = ((MainActivity)getActivity()).sabDB;
		SABManager.getInstance(getActivity()).setFragmentNotifier(this);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		SABManager.getInstance(getActivity()).setFragmentNotifier(null);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_abwab, container, false);

		if(!(MySuperScaler.scaled))
			MySuperScaler.scaleViewAndChildren(rootView, MySuperScaler.scale);


		adapter = new BooksAdapter(getActivity(), R.layout.bab_list_item, books);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setAdapter(adapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		
		books.clear();
		books.addAll(sabDB.getAllBooks());
		adapter.notifyDataSetChanged();
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				((MainActivity) getActivity()).onBookItemClicked(books.get(position));
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

}
