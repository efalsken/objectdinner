package com.androidstartup.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstartup.view.R;
import com.androidstartup.control.DinnerAdapter;
import com.androidstartup.model.Dinner;
import com.androidstartup.provider.RSVPProvider;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class ViewDinner extends MapActivity {

	private TextView mTitle;
	public MapView mMapView;
	private TextView mWhen;
	private TextView mWhere;
	private TextView mDescription;
	private TextView mOrganizer;
	protected Dinner mDinner;
	protected String name;
	private CenterOverlay centerPointer = null;
	private ProgressDialog progressDialog;

	protected RSVPProvider rsvpProvider = RSVPProvider.getInstance(this);
	private DinnerAdapter adapter;

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			progressDialog.dismiss();
			createAdapter();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_dinner);
		Bundle extras = getIntent().getExtras();
		name = extras.getString(AndroidND.NAME);
		setUpViews();
		configureMapView();
		progressDialog = ProgressDialog.show(this, " Loading...",
				" Dinner Data", true, false);
		new Thread() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	private void setUpViews() {
		mTitle = (TextView) findViewById(R.id.title);
		mMapView = (MapView) findViewById(R.id.mapview2);
		mWhen = (TextView) findViewById(R.id.when);
		mWhere = (TextView) findViewById(R.id.where);
		mDescription = (TextView) findViewById(R.id.description);
		mOrganizer = (TextView) findViewById(R.id.organizer);
		findViewById(R.id.attendees).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				performJoin();
			}
		});
	}
	
	private void createAdapter() {
		adapter = new DinnerAdapter(name, this);
	}

	private void configureMapView() {
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(false);
		centerPointer = new CenterOverlay(this, false);
		mMapView.getOverlays().add(centerPointer);
		mMapView.invalidate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (centerPointer != null) {
			mMapView.getOverlays().remove(centerPointer);
			mMapView.invalidate();
		}
	}

	private boolean performJoin() {
		Intent intent = new Intent(this, JoinDinner.class);
		intent.putExtra(AndroidND.NAME, name);
		startActivity(intent);
		return true;
	}

	void notifyUser(Context ctx, String message) {
		Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
	}

	public void notifyUser(String message) {
		notifyUser(this, message);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void animateTo(GeoPoint point) {
		mapController().animateTo(point);
	}

	private MapController mapController() {
		return mMapView.getController();
	}

	public TextView getmTitle() {
		return mTitle;
	}

	public void setmTitle(TextView mTitle) {
		this.mTitle = mTitle;
	}

	public TextView getmWhen() {
		return mWhen;
	}

	public void setmWhen(TextView mWhen) {
		this.mWhen = mWhen;
	}

	public TextView getmWhere() {
		return mWhere;
	}

	public void setmWhere(TextView mWhere) {
		this.mWhere = mWhere;
	}

	public TextView getmDescription() {
		return mDescription;
	}

	public void setmDescription(TextView mDescription) {
		this.mDescription = mDescription;
	}

	public TextView getmOrganizer() {
		return mOrganizer;
	}

	public void setmOrganizer(TextView mOrganizer) {
		this.mOrganizer = mOrganizer;
	}

	public DinnerAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(DinnerAdapter adapter) {
		this.adapter = adapter;
	}

}
