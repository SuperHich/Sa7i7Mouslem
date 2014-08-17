package com.sa7i7mouslem;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sa7i7mouslem.adapters.FavouriteAhadithAdapter;
import com.sa7i7mouslem.adapters.IHadtihListener;
import com.sa7i7mouslem.entity.Hadith;
import com.sa7i7mouslem.externals.SABDataBase;
import com.sa7i7mouslem.externals.SABManager;
import com.sa7i7mouslem.utils.MySuperScaler;


public class FavouriteAhadithFragment extends ListFragment implements IHadtihListener{

	public static final String ARG_AHADITH = "ahadith_type";

	private FavouriteAhadithAdapter adapter;
	private ArrayList<Hadith> ahadith = new ArrayList<Hadith>();

	private SABDataBase sabDB;
	private TextView txv_emptyList;

	public FavouriteAhadithFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		sabDB = ((MainActivity)getActivity()).sabDB;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		SABManager.getInstance(getActivity()).setDownloadNotifier(null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

		txv_emptyList = (TextView) rootView.findViewById(R.id.txv_emptyList);
		
		if(!(MySuperScaler.scaled))
			MySuperScaler.scaleViewAndChildren(rootView, MySuperScaler.scale);


		adapter = new FavouriteAhadithAdapter(getActivity(), R.layout.favourite_list_item, ahadith, this);

		return rootView;
	}

	private void initAhadith(){
		ahadith.clear();
		ahadith.addAll(sabDB.getFavoriteHadiths());		
		adapter.notifyDataSetChanged();
		
		toggleEmptyMessage();
	}

	private void toggleEmptyMessage() {
		if(ahadith.size() == 0)
			txv_emptyList.setVisibility(View.VISIBLE);
		else
			txv_emptyList.setVisibility(View.GONE);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		getListView().setAdapter(adapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		getListView().setDividerHeight(1);

		initAhadith();

	}

	@Override
	public void onHadithShowMore(int position) {
		Hadith hadith = ahadith.get(position);
		ahadith.get(position).setShown(!hadith.isShown());

		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onHadithPause(int position) {
	}

	@Override
	public void onHadithListen(int position) {
	}

	@Override
	public void onHadithDownload(int position) {
	}

	@Override
	public void onHadithFavorite(int position) {

		Hadith hadith = ahadith.get(position);
		boolean newFavStatus = !hadith.isFavorite();
		if(sabDB.setFavoriteHadith(hadith.getId(), newFavStatus))
		{
		
			initAhadith();
			adapter.notifyDataSetChanged();

			if(newFavStatus)
				Toast.makeText(getActivity(), R.string.added_to_fav, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity(), R.string.removed_from_fav, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onHadithComment(int position) {
	}

	@Override
	public void onHadithShare(int position) {
	}

}
