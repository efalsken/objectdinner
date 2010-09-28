package com.androidstartup.control;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import com.androidstartup.model.Dinner;
import com.androidstartup.provider.DinnerProvider;
import com.androidstartup.view.AddDinner;
import com.androidstartup.view.AndroidND;
import com.androidstartup.view.ViewDinner;
import com.google.android.maps.GeoPoint;

public class DinnerAdapter {
	
	private Dinner dinner = new Dinner();;
	private ViewDinner viewActivity;
	private AddDinner editActivity;
	private DinnerProvider dinnerProvider;
	
	public DinnerAdapter(String dinnerName, ViewDinner activity) {
		super();
		this.viewActivity = activity;
		dinnerProvider = DinnerProvider.getInstance(activity);
		this.dinner = fetch(dinnerName);
		this.renderViewActivity();
	}
	
	public DinnerAdapter(Dinner dinner, ViewDinner activity) {
		super();
		this.dinner = dinner;
		this.viewActivity = activity;
		dinnerProvider = DinnerProvider.getInstance(activity);
		this.renderViewActivity();
	}
	
	public DinnerAdapter(AddDinner activity) {
		super();
		this.editActivity = activity;
		dinnerProvider = DinnerProvider.getInstance(activity);
		this.renderEditActivity();
	}

	private Dinner fetch(String name){
		return dinnerProvider.get(name);
	}
	
	public Dinner getDinner() {
		return dinner;
	}
	
	public void setDinner(Dinner dinner) {
		this.dinner = dinner;
		notifyModelChanged();
	}

	private void notifyModelChanged() {
		if(viewActivity != null)
			renderViewActivity();
		if(editActivity != null)
			renderEditActivity();
	}
	
	private void renderEditActivity() {
		Geocoder geocoder = new Geocoder(editActivity.getApplicationContext(), Locale
				.getDefault());
		StringBuffer address = new StringBuffer();
		try {
			List<Address> foundAddresses = geocoder.getFromLocation(
					AndroidND.bookmark.latitude / 1E6,
					AndroidND.bookmark.longitude / 1E6, 1);
			if (!foundAddresses.isEmpty()) {
				Address addr = foundAddresses.get(0);
				GeoPoint p = new GeoPoint(((int) (1E6 * addr.getLatitude())),
						((int) (1E6 * addr.getLongitude())));
				editActivity.animateTo(p);
				address.append(addr.getFeatureName()).append(" ").append(
						addr.getThoroughfare()).append(", ").append(
						addr.getLocality()).append(", ").append(
						addr.getPostalCode()).append(", ").append(
						addr.getAdminArea());
				editActivity.setCountry(addr.getCountryName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		editActivity.setDinnerAddressText(address.toString());
	}
	
	public Dinner finishEditActivity(){
		dinner.setName(editActivity.getTitleText().getText().toString());
		Date date = makeDate(editActivity.getDateDinner().getText().toString(),
				editActivity.getTimeDinner().getText().toString());
		dinner.setEventDate(date);
		dinner.setDescription(editActivity.getDescText().getText().toString());
		dinner.setHostedName(editActivity.getHostnameText().getText().toString());
		dinner.setContactInfo(editActivity.getDinnerCInfoText().getText().toString());
		dinner.setAddress(editActivity.getDinnerAddressText().getText().toString());
		dinner.setCountry(getCountry(editActivity.getDinnerCountry().getSelectedItemPosition()));
		dinner.setLatitude(AndroidND.bookmark.getLatitude());
		dinner.setLongitude(AndroidND.bookmark.getLongitude());
		dinner.setSatellite(AndroidND.bookmark.isSatellite());
		dinner.setTraffic(AndroidND.bookmark.isTraffic());
		dinner.setZoomLevel(AndroidND.bookmark.getZoomLevel());
		// Store dinner object
		dinnerProvider.store(dinner);
		return dinner;
	}
	
	private void renderViewActivity() {
		viewActivity.getmTitle().setText(dinner.getName());
		viewActivity.getmWhen().setText(dateToString());
		if (!TextUtils.isEmpty(dinner.getAddress())) {
			viewActivity.getmWhere().setText(dinner.getAddress());
			pointToView(dinner.getAddress());
		} else {
			viewActivity.getmWhere().setText(getTextLocation());
			pointToView(dinner.latitude, dinner.longitude);
		}
		viewActivity.getmDescription().setText(dinner.getDescription());
		viewActivity.getmOrganizer().setText(dinner.getHostedName());
	}
	
	private String getTextLocation() {
		Geocoder geocoder = new Geocoder(viewActivity.getApplicationContext(), Locale
				.getDefault());
		StringBuffer address = new StringBuffer();
		try {
			List<Address> foundAddresses = geocoder.getFromLocation(dinner.latitude / 1E6,
					dinner.longitude / 1E6, 1);
			if (!foundAddresses.isEmpty()) {
				Address addr = foundAddresses.get(0);
				address.append(addr.getFeatureName()).append(" ").append(
						addr.getThoroughfare()).append(", ").append(
						addr.getLocality()).append(", ").append(
						addr.getPostalCode()).append(", ").append(
						addr.getAdminArea());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return address.toString();
	}
	
	private void pointToView(final String text) {
		Geocoder geocoder = new Geocoder(viewActivity.getApplicationContext(), Locale
				.getDefault());
		try {
			List<Address> foundAddresses = geocoder.getFromLocationName(text, 5);
			if(foundAddresses != null && foundAddresses.size() > 0){
				Address addr = foundAddresses.get(0);
				GeoPoint p = new GeoPoint(((int) (1E6 * addr.getLatitude())),
						((int) (1E6 * addr.getLongitude())));
				viewActivity.animateTo(p);
			}
		} catch (IOException ioe) {
			viewActivity.notifyUser("Search failed: " + ioe.getMessage());
		}
	}
	
	public void pointToEdit(final String text) {
		Geocoder geocoder = new Geocoder(editActivity.getApplicationContext(), Locale
				.getDefault());
		try {
			List<Address> foundAddresses = geocoder.getFromLocationName(text, 1);
			if(foundAddresses != null && foundAddresses.size() > 0){
				Address addr = foundAddresses.get(0);
				GeoPoint p = new GeoPoint(((int) (1E6 * addr.getLatitude())),
						((int) (1E6 * addr.getLongitude())));
				AndroidND.bookmark.latitude = p.getLatitudeE6();
				AndroidND.bookmark.longitude = p.getLongitudeE6();
				editActivity.animateTo(p);
			}
		} catch (IOException ioe) {
			editActivity.notifyUser("Search failed: " + ioe.getMessage());
		}
	}

	private void pointToView(int latitud, int longitud) {
		GeoPoint p = new GeoPoint(latitud, longitud);
		viewActivity.animateTo(p);
	}
	
	private String dateToString(){
		SimpleDateFormat postFormater = new SimpleDateFormat("MM-dd-yyyy HH:mm"); 
		String newDateStr = postFormater.format(dinner.getEventDate()); 
		return newDateStr;
	}
	
	private Date makeDate(String date, String time) {
		Date dateObj = null;
		SimpleDateFormat curFormater = new SimpleDateFormat("MM-dd-yyyy");
		try {
			dateObj = curFormater.parse(date);
			dateObj.setHours(editActivity.getmHour());
			dateObj.setMinutes(editActivity.getmMinute());
		} catch (ParseException pe) {
			editActivity.notifyUser("Parsing of Date failed: " + pe.getMessage());
		}
		return dateObj;
	}
	
	private String getCountry(int position) {
		return COUNTRIES[position];
	}
	
	public final static String[] COUNTRIES = new String[] { "Afghanistan", "Albania",
		"Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
		"Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
		"Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
		"Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
		"Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
		"Botswana", "Bouvet Island", "Brazil",
		"British Indian Ocean Territory", "British Virgin Islands",
		"Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire",
		"Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
		"Central African Republic", "Chad", "Chile", "China",
		"Christmas Island", "Cocos (Keeling) Islands", "Colombia",
		"Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
		"Cuba", "Cyprus", "Czech Republic",
		"Democratic Republic of the Congo", "Denmark", "Djibouti",
		"Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt",
		"El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
		"Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji",
		"Finland", "Former Yugoslav Republic of Macedonia", "France",
		"French Guiana", "French Polynesia", "French Southern Territories",
		"Gabon", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
		"Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
		"Guinea", "Guinea-Bissau", "Guyana", "Haiti",
		"Heard Island and McDonald Islands", "Honduras", "Hong Kong",
		"Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
		"Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
		"Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
		"Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
		"Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar",
		"Malawi", "Malaysia", "Maldives", "Mali", "Malta",
		"Marshall Islands", "Martinique", "Mauritania", "Mauritius",
		"Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
		"Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
		"Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
		"New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
		"Niue", "Norfolk Island", "North Korea", "Northern Marianas",
		"Norway", "Oman", "Pakistan", "Palau", "Panama",
		"Papua New Guinea", "Paraguay", "Peru", "Philippines",
		"Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
		"Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe",
		"Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
		"Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
		"Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",
		"Sierra Leone", "Singapore", "Slovakia", "Slovenia",
		"Solomon Islands", "Somalia", "South Africa",
		"South Georgia and the South Sandwich Islands", "South Korea",
		"Spain", "Sri Lanka", "Sudan", "Suriname",
		"Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
		"Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
		"The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
		"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
		"Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
		"Ukraine", "United Arab Emirates", "United Kingdom",
		"United States", "United States Minor Outlying Islands", "Uruguay",
		"Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
		"Wallis and Futuna", "Western Sahara", "Yemen", "Yugoslavia",
		"Zambia", "Zimbabwe" };
}

