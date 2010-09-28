package com.androidstartup.view;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidstartup.view.R;
import com.androidstartup.control.DinnerAdapter;
import com.androidstartup.model.Dinner;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class AddDinner extends MapActivity {

	private EditText titleText;
	public MapView mMapView;
	private EditText dateDinner;
	private EditText timeDinner;
	private EditText hostnameText;
	private EditText descText;
	private EditText dinnerCInfoText;
	private EditText dinnerAddressText;
	private Spinner dinnerCountry;
	private CenterOverlay centerPointer = null;
	private String message = "";
	private DinnerAdapter adapter;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_dinner);
		setUpViews();
		configureMapView();
		setCurrentDate();
		createAdapter();
	}
	
	public void setCurrentDate(){
		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// get the current time
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		updateDate();
		updateTime();
	}
	
	private void createAdapter() {
		adapter = new DinnerAdapter(this);
	}

	private void setUpViews() {
		mMapView = (MapView) findViewById(R.id.mapview2);
		mMapView.setClickable(true); //TODO
		mMapView.setBuiltInZoomControls(true); //TODO
		titleText = (EditText) findViewById(R.id.dinner_title);
		dateDinner = (EditText) findViewById(R.id.dinner_date);
		dateDinner.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		timeDinner = (EditText) findViewById(R.id.dinner_time);
		timeDinner.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
		descText = (EditText) findViewById(R.id.dinner_desc);
		hostnameText = (EditText) findViewById(R.id.dinner_hostname);
		dinnerCInfoText = (EditText) findViewById(R.id.dinner_cinfo);
		dinnerAddressText = (EditText) findViewById(R.id.dinner_address);
		dinnerAddressText.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				adapter.pointToEdit(dinnerAddressText.getText().toString());
			}
		});
		dinnerCountry = (Spinner) findViewById(R.id.dinner_country);
		ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
				R.layout.list_item, DinnerAdapter.COUNTRIES);
		countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dinnerCountry.setAdapter(countryAdapter);
		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (validate()) { // Validate UI data TODO validate dinner name is not duplicated
					Dinner dinner = adapter.finishEditActivity();
					// Return to parent activity passing dinner's address
					Intent result = new Intent();
					result.putExtra(AndroidND.ADDRESS, dinner.getAddress());
					setResult(RESULT_OK, result);
					finish();
				} else {
					notifyUser(AddDinner.this, message);
				}
			}
		});
	}

	private void configureMapView() {
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(false);
		centerPointer = new CenterOverlay(this, true);
		mMapView.getOverlays().add(centerPointer);
		mMapView.invalidate();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
						mDay);
			case TIME_DIALOG_ID:
				return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
						false);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDate();
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateTime();
		}
	};

	// Updates the date in the TextView
	private void updateDate() {
		dateDinner.setText(new StringBuilder().
		// Month is 0 based so add 1
				append(mMonth + 1).append("-").append(mDay).append("-").append(
						mYear).append(" "));
	}

	private void updateTime() {
		timeDinner.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}

	public void setCountry(String country) {
		int index = 0;
		for (String c : DinnerAdapter.COUNTRIES) {
			if (c.equalsIgnoreCase(country))
				dinnerCountry.setSelection(index);
			else
				index++;
		}
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private boolean validate() {
		if (TextUtils.isEmpty(titleText.getText().toString())) {
			message = "Title required";
			return false;
		}
		if (TextUtils.isEmpty(hostnameText.getText().toString())) {
			message = "Host name required";
			return false;
		}
		if (TextUtils.isEmpty(dinnerAddressText.getText().toString())) {
			message = "Address required";
			return false;
		}	
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
		return false;
	}

	public void animateTo(GeoPoint point) {
		mapController().animateTo(point);
	}

	private MapController mapController() {
		return mMapView.getController();
	}

	public EditText getTitleText() {
		return titleText;
	}

	public void setTitleText(EditText titleText) {
		this.titleText = titleText;
	}

	public EditText getDateDinner() {
		return dateDinner;
	}

	public void setDateDinner(EditText dateDinner) {
		this.dateDinner = dateDinner;
	}

	public EditText getTimeDinner() {
		return timeDinner;
	}

	public void setTimeDinner(EditText timeDinner) {
		this.timeDinner = timeDinner;
	}

	public EditText getHostnameText() {
		return hostnameText;
	}

	public void setHostnameText(EditText hostnameText) {
		this.hostnameText = hostnameText;
	}

	public EditText getDescText() {
		return descText;
	}

	public void setDescText(EditText descText) {
		this.descText = descText;
	}

	public EditText getDinnerCInfoText() {
		return dinnerCInfoText;
	}

	public void setDinnerCInfoText(EditText dinnerCInfoText) {
		this.dinnerCInfoText = dinnerCInfoText;
	}

	public EditText getDinnerAddressText() {
		return dinnerAddressText;
	}

	public void setDinnerAddressText(EditText dinnerAddressText) {
		this.dinnerAddressText = dinnerAddressText;
	}

	public Spinner getDinnerCountry() {
		return dinnerCountry;
	}

	public void setDinnerCountry(Spinner dinnerCountry) {
		this.dinnerCountry = dinnerCountry;
	}

	public int getmHour() {
		return mHour;
	}

	public void setmHour(int mHour) {
		this.mHour = mHour;
	}

	public int getmMinute() {
		return mMinute;
	}

	public void setmMinute(int mMinute) {
		this.mMinute = mMinute;
	}
	
	public void setDinnerAddressText(String text){
		dinnerAddressText.setText(text);
	}
}
