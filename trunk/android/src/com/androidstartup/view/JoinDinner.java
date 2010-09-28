package com.androidstartup.view;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstartup.view.R;
import com.androidstartup.model.Dinner;
import com.androidstartup.model.RSVP;
import com.androidstartup.provider.DinnerProvider;
import com.androidstartup.provider.RSVPProvider;

public class JoinDinner extends ListActivity {

	private ArrayAdapter<RSVP> mAdapter;
	private String dinnerName;
	private DinnerProvider dinnerProvider = DinnerProvider.getInstance(this);
	private RSVPProvider rsvpProvider = RSVPProvider.getInstance(this);
	private TextView mName;
	private List<RSVP> mAttendees;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Use a custom layout file
		setContentView(R.layout.join_dinner);
		Bundle extras = getIntent().getExtras();
		dinnerName = extras.getString(AndroidND.NAME);
		TextView mTitle = (TextView) findViewById(R.id.title);
		mName = (TextView) findViewById(R.id.name);
		mTitle.setText(dinnerName);
		// Tell the list view which view to display when the list is empty
		getListView().setEmptyView(findViewById(R.id.empty));
		getListView().setTextFilterEnabled(true);
		getListView().setFastScrollEnabled(true);		
		fillData();
		// Wire up the add button to add a new photo
		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				join();
			}
		});
	}

	private void getData() {
		mAttendees = rsvpProvider.getByDinner(dinnerName);
	}
	
	private void fillData(){
		getData();
		mAdapter = new ArrayAdapter<RSVP>(this,
				android.R.layout.simple_list_item_1, mAttendees);
		setListAdapter(mAdapter);
	}

	private void join() {
		Dinner dinner = dinnerProvider.get(dinnerName);
		RSVP rsvp = new RSVP(dinner);
		if (TextUtils.isEmpty(mName.getText().toString())) {
			Toast.makeText(this, "Name required", Toast.LENGTH_LONG).show();
		} else {
			rsvp.setName(mName.getText().toString());
			mName.setText("");
			mName.clearFocus();
			rsvpProvider.store(rsvp);
			fillData();
			mName.clearFocus();
		}
	}
}
