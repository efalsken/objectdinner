package com.androidstartup.view;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidstartup.view.R;
import com.androidstartup.model.Dinner;
import com.androidstartup.provider.DinnerProvider;

public class DinnerList extends ListActivity {

	// Menu Item order
	public static final int DEL_BOOKMARK_INDEX = Menu.FIRST + 1;
	public static final int COUNT_BOOKMARK_INDEX = Menu.FIRST + 2;
	public static final int GOTO_BOOKMARK_INDEX = Menu.FIRST;

	private List<Dinner> entries;
	ArrayAdapter<Dinner> adapter;
	private DinnerProvider dinnerProvider = DinnerProvider.getInstance(this);

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
	 * Populates the Dinner ListView
	 */
	private void fillData() {
		entries = dinnerProvider.getAll();
		if (entries.isEmpty())
			notifyUser("No entries");
		adaptListView();
	}
	
	private void adaptListView(){
		adapter = new ArrayAdapter<Dinner>(this, 
				android.R.layout.simple_list_item_1, entries);
		setListAdapter(adapter);
	}

	private void delete(String name) {
		dinnerProvider.delete(name);
		fillData();
		adapter.notifyDataSetChanged();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
                super.onCreateOptionsMenu(menu);
                menu.add(0, DEL_BOOKMARK_INDEX, DEL_BOOKMARK_INDEX, R.string.bkm_delete);
                menu.add(0, COUNT_BOOKMARK_INDEX, COUNT_BOOKMARK_INDEX, R.string.bkm_count);
                menu.add(0, GOTO_BOOKMARK_INDEX, GOTO_BOOKMARK_INDEX, R.string.bkm_goto);
                return super.onCreateOptionsMenu(menu);
    }

	public boolean onOptionsItemSelected(MenuItem item) {
		int position = item.getItemId();
		int itemPosition = getSelectedItemPosition() < 0 ? 0
				: getSelectedItemPosition();
		if (entries.size() == 0) {
			position = COUNT_BOOKMARK_INDEX;
		}
		switch (position) {
			case DEL_BOOKMARK_INDEX:
				delete(entries.get(itemPosition).name);
				break;
			case GOTO_BOOKMARK_INDEX:
				view(itemPosition);
				break;
			case COUNT_BOOKMARK_INDEX:
				int count = dinnerProvider.size();
				notifyUser("Entries: " + Integer.toString(count));
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		view(position);
	}

	private void view(int position) {
		Intent i = new Intent(this, ViewDinner.class);
		Dinner dinner = entries.get(position);
		i.putExtra(AndroidND.NAME, dinner.name);
		AndroidND.bookmark = dinner;
		startActivity(i);
	}	

	protected void notifyUser(String msg) {
		Toast.makeText(DinnerList.this, msg, Toast.LENGTH_SHORT).show();
	}
}
