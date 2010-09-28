package com.androidstartup.view;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.androidstartup.view.R;
import com.androidstartup.model.Dinner;
import com.androidstartup.model.MapBookmark;
import com.androidstartup.provider.DinnerProvider;
import com.androidstartup.provider.RSVPProvider;
import com.db4o.android.Db4oHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class AndroidND extends MapActivity implements LocationListener {
	protected MapView mMapView;
	protected Db4oHelper<Dinner> db4oHelper;
	private MyLocationOverlay myLocationOverlay;
	private BookmarkOverlay bookmarkOverlay;

	private static final int GET_SEARCH_TEXT = 0;
	private static final int SET_DINNER_INFO = 1;
	private static final int RESULT_GOTO_MAP = 2;
	private static final int SEARCH_RESULTS = 3;
	private static final int DB4O_CLIENT_RESULT = 4;

	public static int MAX_RESULTS = 5;
	public static final String NAME = "name";
	public static final String DESC = "desc";
	public static final String DATE = "date";
	public static final String HOST = "host";
	public static final String CONTACT = "contact";
	public static final String ADDRESS = "address";
	public static final String COUNTRY = "country";
	public static final String DB4OSERVICE = "LOCAL";
	
	private boolean ONSEARCH = false;

	Geocoder mGeocoder = null;
	static List<Dinner> foundDinners;
	static String keyword;

	// Menu Item order
	public static final int ZOOM_IN_INDEX = Menu.FIRST;
	public static final int ZOOM_OUT_INDEX = Menu.FIRST + 1;
	public static final int FIND_INDEX = Menu.FIRST + 2;
	public static final int SAVE_INDEX = Menu.FIRST + 3;
	public static final int EDIT_INDEX = Menu.FIRST + 4;
	public static final int MAP_MODE_INDEX = Menu.FIRST + 5;
	public static final int SATELLITE_INDEX = Menu.FIRST + 6;
	public static final int TRAFFIC_INDEX = Menu.FIRST + 7;
	public static final int STREETVIEW_INDEX = Menu.FIRST + 8;
	public static final int BOOKMARK_VIEW_INDEX = Menu.FIRST + 9;
	public static final int COMPASS_INDEX = Menu.FIRST + 10;
	public static final int CENTER_LOCATION_INDEX = Menu.FIRST + 11;
	public static final int TRACK_LOCATION_INDEX = Menu.FIRST + 12;
	public static final int SETTINGS_INDEX = Menu.FIRST + 13;
	public static final int ADD_DINNER = Menu.FIRST + 14;
	public static final int DB4O_CLIENT = Menu.FIRST + 15;

	protected static boolean TRACKING_MODE = false;
	protected static boolean COMPASS_MODE = false;
	protected static boolean BOOKMARK_MODE = true;

	public static MapBookmark bookmark;
	protected DinnerProvider dinnerProvider = DinnerProvider.getInstance(this);
	protected RSVPProvider rsvpProvider = RSVPProvider.getInstance(this);

	protected static final GeoPoint HOME_POINT = new GeoPoint(
			(int) (37.799800872802734 * 1E6), (int) (-122.40699768066406 * 1E6));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(true);
		// Initialize db4o
		dbHelper();
		configureBookmarkOverlay();
		// Adds a MyLocationOverlay
		configureTracking();
		configureCompass();
		resetToHomePoint();
	}

	private void configureTracking() {
		myLocationOverlay = new MyLocationOverlay(this, mMapView);
		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapController().animateTo(myLocationOverlay.getMyLocation());
			}
		});
		mapOverlays().add(myLocationOverlay);
		performTrackLocation(TRACKING_MODE);
	}

	private void configureCompass() {
		performCompassMode(COMPASS_MODE);
	}

	private void configureBookmarkOverlay() {
		bookmarkOverlay = new BookmarkOverlay(this);
		performBookmarkView(BOOKMARK_MODE);
	}

	protected List<Overlay> mapOverlays() {
		return mMapView.getOverlays();
	}

	protected void animateTo(GeoPoint point) {
		mapController().animateTo(point);
	}

	protected void animateTo(GeoPoint point, Message msg) {
		mapController().animateTo(point, msg);
	}

	protected void resetToHomePoint() {
		mMapView.setSatellite(false);
		mMapView.setTraffic(false);
		mMapView.setStreetView(false);
		animateTo(HOME_POINT);
		mapController().setZoom(15);
	}

	protected MapController mapController() {
		return mMapView.getController();
	}

	public Projection getProjection() {
		return mMapView.getProjection();
	}

	protected Db4oHelper<Dinner> dbHelper() {
		if (db4oHelper == null) {
			db4oHelper = new Db4oHelper<Dinner>(this);
			db4oHelper.db();
		}
		return db4oHelper;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, FIND_INDEX, FIND_INDEX, R.string.find).setIcon(
				R.drawable.findonmap);
		menu.add(0, SAVE_INDEX, SAVE_INDEX, R.string.add_dinner).setIcon(
				R.drawable.save);
		menu.add(0, EDIT_INDEX, EDIT_INDEX, R.string.dinners).setIcon(
				R.drawable.bookmarks);
		menu.add(0, DB4O_CLIENT, DB4O_CLIENT, "DB Service").setIcon(
				R.drawable.stat_sample);
		// Map Mode submenu
		SubMenu subMenu = menu.addSubMenu(0, MAP_MODE_INDEX, MAP_MODE_INDEX,
				R.string.map_mode).setIcon(R.drawable.mapmode);
		subMenu.add(0, SATELLITE_INDEX, SATELLITE_INDEX, R.string.satellite);
		subMenu.add(0, TRAFFIC_INDEX, TRAFFIC_INDEX, R.string.traffic);
		subMenu.add(0, STREETVIEW_INDEX, STREETVIEW_INDEX, R.string.streetview);
		subMenu.setGroupCheckable(0, true, true);
		// Settings submenu
		SubMenu subMenu2 = menu.addSubMenu(0, SETTINGS_INDEX, SETTINGS_INDEX,
				R.string.settings).setIcon(R.drawable.settings);
		subMenu2.add(0, BOOKMARK_VIEW_INDEX, BOOKMARK_VIEW_INDEX,
				R.string.bookmarkview).setChecked(BOOKMARK_MODE);
		subMenu2.add(0, TRACK_LOCATION_INDEX, TRACK_LOCATION_INDEX,
				R.string.track_gps).setIcon(R.drawable.trackme).setChecked(
				TRACKING_MODE);
		subMenu2.add(0, COMPASS_INDEX, COMPASS_INDEX, R.string.compass)
				.setChecked(COMPASS_MODE);
		subMenu.setGroupCheckable(0, true, true);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ZOOM_IN_INDEX:
			return performZoomIn();
		case ZOOM_OUT_INDEX:
			return performZoomOut();
		case SATELLITE_INDEX:
			item.setChecked(!item.isChecked());
			return performToggleSatellite();
		case TRAFFIC_INDEX:
			item.setChecked(!item.isChecked());
			return performToggleTraffic();
		case STREETVIEW_INDEX:
			item.setChecked(!item.isChecked());
			return performToggleStreetView();
		case FIND_INDEX:
			return performFindLocation();
		case CENTER_LOCATION_INDEX:
			return performCenterOnLocation();
		case BOOKMARK_VIEW_INDEX:
			item.setChecked(!item.isChecked());
			return performBookmarkView(item.isChecked());
		case TRACK_LOCATION_INDEX:
			item.setChecked(!item.isChecked());
			return performTrackLocation(item.isChecked());
		case COMPASS_INDEX:
			item.setChecked(!item.isChecked());
			performCompassMode(item.isChecked());
			return true;
		case SAVE_INDEX:
			return performAddDinner(null);
		case EDIT_INDEX:
			return performViewDinners();
		case DB4O_CLIENT:
			return performDb4oClient();
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_I) {
			return performZoomIn();
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			return performZoomOut();
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			return performToggleSatellite();
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			return performToggleTraffic();
		} else if (keyCode == KeyEvent.KEYCODE_V) {
			return performToggleStreetView();
		} else if (keyCode == KeyEvent.KEYCODE_F) {
			return performFindLocation();
		} else if (keyCode == KeyEvent.KEYCODE_G) {
			return performCenterOnLocation();
		} else if (keyCode == KeyEvent.KEYCODE_C) {
			return performAddDinner(null);
		} else if (keyCode == KeyEvent.KEYCODE_E) {
			return performViewDinners();
		} else if (keyCode == KeyEvent.KEYCODE_P) {
			return performFindPizza();
		} else if (keyCode >= KeyEvent.KEYCODE_1
				&& keyCode <= KeyEvent.KEYCODE_9) {
		}
		return super.onKeyDown(keyCode, event);
	}

	public List<Dinner> getDinners() {
		return foundDinners;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return true;
	}

	public void addContextMenu() {
		// mMapView.setLongClickable(true);
		registerForContextMenu(mMapView);
		mMapView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						MenuInflater cntmenu = getMenuInflater();
						cntmenu.inflate(R.menu.map_menu, menu);
					}
				});
		handleLongPress();
	}

	private void handleLongPress() {
		final GestureDetector gd = new GestureDetector(
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public void onLongPress(MotionEvent e) {
						openContextMenu(mMapView);
					}
				});
		gd.setIsLongpressEnabled(true);
		mMapView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent ev) {
				return gd.onTouchEvent(ev);
			}
		});
	}

	public boolean performZoomIn() {
		int level = mMapView.getZoomLevel();
		mapController().setZoom(level + 1);
		return true;
	}

	public boolean performZoomOut() {
		int level = mMapView.getZoomLevel();
		mapController().setZoom(level - 1);
		return true;
	}

	public boolean performToggleSatellite() {
		mMapView.setSatellite(!mMapView.isSatellite());
		return true;
	}

	public boolean performToggleTraffic() {
		mMapView.setTraffic(!mMapView.isTraffic());
		return true;
	}

	public boolean performToggleStreetView() {
		mMapView.setStreetView(!mMapView.isStreetView());
		return true;
	}

	public boolean performFindLocation() {
		Intent intent = new Intent(AndroidND.this,
				com.androidstartup.view.Search.class);
		startActivityForResult(intent, GET_SEARCH_TEXT);
		return true;
	}

	// Get last known location and center map on it
	public boolean performCenterOnLocation() {
		// Get location manager
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		// Register this as location listener
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, 1, this);// 3 secs, 10m
		// Select best provider (will try GPS first, then network triangulation)
		String provider = locationManager.getBestProvider(new Criteria(), true);
		if (provider == null) {
			notifyUser("No location provider found");
			return false;
		}
		if (!locationManager.isProviderEnabled(provider)) {
			notifyUser(provider + " is disabled");
			return false;
		}
		Location currentLocation = locationManager
				.getLastKnownLocation(provider);
		if (currentLocation == null) {
			notifyUser("Unknown location");// Usual response in the emulator if
			// there's no mock location
			return false;
		}
		// Get point
		GeoPoint point = new GeoPoint(
				(int) (currentLocation.getLatitude() * 1E6),
				(int) (currentLocation.getLongitude() * 1E6));
		// Center on map
		animateTo(point);
		return true;
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lon = (int) (location.getLongitude() * 1E6);
			GeoPoint p = new GeoPoint(lat, lon);
			animateTo(p);
		}
	}

	public boolean performCompassMode(boolean isChecked) {
		COMPASS_MODE = isChecked;
		if (isChecked)
			myLocationOverlay.enableCompass();
		else
			myLocationOverlay.disableCompass();
		mMapView.invalidate();
		return true;
	}

	public boolean performTrackLocation(boolean isChecked) {
		TRACKING_MODE = isChecked;
		if (isChecked)
			myLocationOverlay.enableMyLocation();
		else {
			myLocationOverlay.disableMyLocation();
			// Get location manager
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			locationManager.removeUpdates(this);
		}
		mMapView.invalidate();
		return true;
	}

	public boolean performBookmarkView(boolean isChecked) {
		BOOKMARK_MODE = isChecked;
		if (isChecked)
			mapOverlays().add(bookmarkOverlay);
		else
			mapOverlays().remove(bookmarkOverlay);
		mMapView.invalidate();
		return true;
	}

	public boolean performAddDinner(GeoPoint point) {
		Intent intent = new Intent(AndroidND.this,
				com.androidstartup.view.AddDinner.class);
		setBookmark(point);
		startActivityForResult(intent, SET_DINNER_INFO);
		return true;
	}
	
	public boolean performDb4oClient() {
		Intent intent = new Intent(AndroidND.this,
				com.db4o.android.Db4oClient.class);
		startActivityForResult(intent, DB4O_CLIENT_RESULT);
		return true;
	}

	private void setBookmark(GeoPoint point) {
		if (point == null) {
			point = mMapView.getMapCenter();
		}
		MapBookmark mb = new MapBookmark();
		mb.setLatitude(point.getLatitudeE6());
		mb.setLongitude(point.getLongitudeE6());
		mb.setZoomLevel(mMapView.getZoomLevel());
		mb.setSatellite(mMapView.isSatellite());
		mb.setTraffic(mMapView.isTraffic());
		AndroidND.bookmark = mb;
	}

	public boolean performViewDinners() {
		Intent intent = new Intent(AndroidND.this,
				com.androidstartup.view.DinnerList.class);
		startActivity(intent);
		return true;
	}

	public boolean performEditSearchResults() {
		Intent intent = new Intent(AndroidND.this,
				com.androidstartup.view.SearchList.class);
		startActivityForResult(intent, SEARCH_RESULTS);
		return true;
	}

	public boolean performViewDinner(Dinner dinner) {
		Intent intent = new Intent(AndroidND.this,
				com.androidstartup.view.ViewDinner.class);
		intent.putExtra(NAME, dinner.getName());
		startActivity(intent);
		return true;
	}

	// Example
	public boolean performFindPizza() {
		startSearch("Pizza");
		return true;
	}

	private void startSearch(final String text) {
			keyword = text;
			performEditSearchResults();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_SEARCH_TEXT) {
			if (data != null) {
				String searchString = data.getStringExtra("searchString");
				startSearch(searchString);
			}
		} else if (requestCode == SET_DINNER_INFO) {
			if (data != null) {
				String address = data.getStringExtra(ADDRESS);
				refreshPointTo(address);
			}
		} else if (requestCode == SEARCH_RESULTS) {
			if (resultCode == RESULT_GOTO_MAP) {
				ONSEARCH = true;
				if (mMapView.isSatellite() != AndroidND.bookmark.isSatellite())
					mMapView.setSatellite(true);
				if (mMapView.isTraffic() != AndroidND.bookmark.isTraffic())
					mMapView.setTraffic(true);
				// Add permanent map overlays
				// mapOverlays().add(new SearchOverlay(this));
				// Navigate to point
				GeoPoint p = new GeoPoint(AndroidND.bookmark.getLatitude(),
						AndroidND.bookmark.getLongitude());
				animateTo(p);
				mapController().setZoom(AndroidND.bookmark.getZoomLevel());
			}
		} else if (requestCode == DB4O_CLIENT_RESULT) {
			notifyUser(DB4OSERVICE);
			//db4oHelper.setDb(Db4oClient.objectContainer);
		}
	}

	private void refreshPointTo(String address) {
		mGeocoder = new Geocoder(this.getApplicationContext(), Locale
				.getDefault());
		try {
			List<Address> foundAddresses = mGeocoder.getFromLocationName(address, 1);
			Address addr = foundAddresses.get(0);
			GeoPoint p = new GeoPoint(((int) (1E6 * addr.getLatitude())),
					((int) (1E6 * addr.getLongitude())));
			setBookmark(p);
			animateTo(p);
		} catch (IOException e) {
			notifyUser("Search failed: " + e.getMessage());
		}
	}

	/* Utility methods */

	// Popup window to quickly pass user notifications
	void notifyUser(Context ctx, String message) {
		Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
	}

	void notifyUser(String message) {
		notifyUser(AndroidND.this, message);
	}

	/* Lifecycle methods */

	@Override
	protected void onPause() {
		super.onPause();
		if (TRACKING_MODE)
			myLocationOverlay.disableMyLocation();
		if (COMPASS_MODE)
			myLocationOverlay.disableCompass();
		dbHelper().close();
		db4oHelper = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (TRACKING_MODE && !myLocationOverlay.isMyLocationEnabled())
			myLocationOverlay.enableMyLocation();
		if (COMPASS_MODE && !myLocationOverlay.isCompassEnabled())
			myLocationOverlay.enableCompass();
		//mMapView.invalidate();
		if(!ONSEARCH)
			resetToHomePoint();
		else
			ONSEARCH = false;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

}
