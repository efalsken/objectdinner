package com.androidstartup.view;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidstartup.view.R;
import com.androidstartup.model.Dinner;
import com.androidstartup.provider.DinnerProvider;

public class SearchList extends ListActivity {

	private static final int RESULT_GOTO_MAP = 2;
	private List<Dinner> entries;
	protected DinnerProvider dinnerProvider = DinnerProvider.getInstance(this);

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.bkm_list);
		getListView().setTextFilterEnabled(true);
		getListView().setFastScrollEnabled(true);
		fillData();
	}

	/**
	 * Populates the dinner ListView
	 */
	private void fillData() {
		entries = dinnerProvider.getByKeyword(AndroidND.keyword);
		notifyUser("Found: "+Integer.toString(entries.size()));
		if (entries.isEmpty()){
			finish();
		}
		ArrayAdapter<Dinner> adapter = new ArrayAdapter<Dinner>(this,
				android.R.layout.simple_list_item_1, entries);
		setListAdapter(adapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		AndroidND.bookmark = entries.get(position);
		setResult(RESULT_GOTO_MAP);
		finish();
	}

	protected void notifyUser(String msg) {
		Toast.makeText(SearchList.this, msg, Toast.LENGTH_SHORT).show();
	}
}
