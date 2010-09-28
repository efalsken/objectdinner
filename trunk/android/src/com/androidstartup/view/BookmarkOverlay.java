package com.androidstartup.view;

import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.androidstartup.view.R;
import com.androidstartup.model.Dinner;
import com.androidstartup.model.MapBookmark;
import com.androidstartup.model.RSVP;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class BookmarkOverlay extends Overlay {

	private static final int BALLOON_HEIGHT = 34;
	// private static final int BALLOON_WIDTH = 37;
	private static final int BOOKMARK_CLICK_TOLERANCE = 1000;

	private AndroidND mMap;
	private MapView mView;
	private Paint paint = new Paint();
	private static Paint innerPaint, borderPaint, textPaint, numberPaint;

	public BookmarkOverlay(AndroidND map) {
		mMap = map;
		mView = map.mMapView;
	}

	public void draw(Canvas canvas, MapView map, boolean shadow) {
		super.draw(canvas, map, shadow);
		if (!shadow) {
			GeoPoint mapCenter = mView.getMapCenter();
			int latitudeSpan = mView.getLatitudeSpan();
			int longitudeSpan = mView.getLongitudeSpan();
			List<Dinner> dinners = mMap.dinnerProvider.getNearby(
					mapCenter, latitudeSpan, longitudeSpan);
			Iterator<Dinner> iterator = dinners.iterator();
			while (iterator.hasNext()) {
				Dinner dinner = iterator.next();
				GeoPoint point = new GeoPoint(dinner.getLatitude(), dinner
						.getLongitude());
				Point screenCoords = mView.getProjection()
						.toPixels(point, null);
				// Draw balloons
				Bitmap bmp = BitmapFactory.decodeResource(mMap.getResources(),
						R.drawable.pin);
				canvas.drawBitmap(bmp, 2 + screenCoords.x - BALLOON_HEIGHT / 3,
						screenCoords.y - BALLOON_HEIGHT, paint);
				drawGuestNumber(canvas, map, dinner);
				drawInfoWindow(canvas, map, dinner);
			}
		}
	}

	private void drawInfoWindow(Canvas canvas, MapView mapView,
			MapBookmark selectedMapLocation) {
		if (selectedMapLocation != null) {
			// First determine the screen coordinates of the selected
			// MapLocation
			Point selDestinationOffset = new Point();
			mapView.getProjection().toPixels(selectedMapLocation.getPoint(),
					selDestinationOffset);
			// Setup the info window with the right size & location
			//int INFO_WINDOW_WIDTH = selectedMapLocation.getName().length() * 8;
			int INFO_WINDOW_WIDTH = (int)getTextPaint().measureText(selectedMapLocation.getName() + 4);
			int INFO_WINDOW_HEIGHT = 23;
			RectF infoWindowRect = new RectF(0, 0, INFO_WINDOW_WIDTH,
					INFO_WINDOW_HEIGHT);
			int infoWindowOffsetX = selDestinationOffset.x - INFO_WINDOW_WIDTH
					/ 2;
			int infoWindowOffsetY = selDestinationOffset.y - INFO_WINDOW_HEIGHT
					- BALLOON_HEIGHT - 2;
			infoWindowRect.offset(infoWindowOffsetX, infoWindowOffsetY);
			// Draw inner info window
			canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
			// Draw border for info window
			canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());
			// Draw the MapLocation's name
			int TEXT_OFFSET_X = INFO_WINDOW_WIDTH / 2;
			int TEXT_OFFSET_Y = 15;
			String windowText = selectedMapLocation.getName();
			canvas.drawText(windowText, infoWindowOffsetX + TEXT_OFFSET_X,
					infoWindowOffsetY + TEXT_OFFSET_Y, getTextPaint());
		}
	}
	
	private void drawGuestNumber(Canvas canvas, MapView mapView,
			Dinner selectedMapLocation) {
		if (selectedMapLocation != null) {
			Point selDestinationOffset = new Point();
			mapView.getProjection().toPixels(selectedMapLocation.getPoint(), selDestinationOffset);
			List<RSVP> rsvps = mMap.rsvpProvider.getByDinner(selectedMapLocation.getName());
			String number = Integer.toString(rsvps.size());
			canvas.drawText(number, selDestinationOffset.x,
					selDestinationOffset.y - (int)(BALLOON_HEIGHT/1.75), getNumberPaint());
		}
	}

	@Override
	public boolean onTap(GeoPoint point, MapView view) {
		List<Dinner> dinners = mMap.dinnerProvider.getNearby(point,
				BOOKMARK_CLICK_TOLERANCE);
		if (dinners.size() > 0) {
			Iterator<Dinner> iterator = dinners.iterator();
			if (iterator.hasNext()) {
				Dinner dinner = iterator.next();
				mMap.performViewDinner(dinner);
			}
		} else 
			mMap.performAddDinner(point);
		return super.onTap(point, view);
	}

	public Paint getInnerPaint() {
		if (innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); // grey
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}

	public Paint getBorderPaint() {
		if (borderPaint == null) {
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 255, 255);
			borderPaint.setAntiAlias(true);
			borderPaint.setStyle(Style.STROKE);
			borderPaint.setStrokeWidth(2);
		}
		return borderPaint;
	}

	public Paint getTextPaint() {
		if (textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
			textPaint.setTextAlign(Align.CENTER);
		}
		return textPaint;
	}
	
	public Paint getNumberPaint() {
		if (numberPaint == null) {
			numberPaint = new Paint();
			numberPaint.setARGB(225, 25, 25, 25);
			numberPaint.setAntiAlias(true);
			numberPaint.setTextAlign(Align.CENTER);
		}
		return numberPaint;
	}
}
