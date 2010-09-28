package com.androidstartup.view;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Align;

import com.androidstartup.model.Dinner;
import com.androidstartup.model.RSVP;
import com.androidstartup.view.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class CenterOverlay extends Overlay {

	private static final int TARGET_HEIGHT = 32;
	private static final int TARGET_WIDTH = 32;
	private static final int BALLOON_HEIGHT = 34;
	
	private MapActivity mMap;
	private MapView mView;
    private Paint paint = new Paint();
	private static Paint numberPaint;
    private boolean isCrossHair;
   
    public CenterOverlay(MapActivity map, boolean isCrossHair) {
        mMap = map;
        mView = (MapView)map.findViewById(R.id.mapview2);
        this.isCrossHair = isCrossHair;
    }

    public void draw(Canvas canvas, MapView map, boolean shadow) {
        super.draw(canvas, map, shadow);
        if(!shadow){
	        GeoPoint mapCenter = mView.getMapCenter();
        	Point screenCoords = mView.getProjection().toPixels(mapCenter, null);     
        	//Draw image
        	if (isCrossHair){
	            Bitmap bmp = BitmapFactory.decodeResource(mMap.getResources(), R.drawable.target);            
	            canvas.drawBitmap(bmp, screenCoords.x - TARGET_WIDTH/2, screenCoords.y - TARGET_HEIGHT/2, paint);
        	} else{
        		Bitmap bmp = BitmapFactory.decodeResource(mMap.getResources(),
						R.drawable.pin2);
				canvas.drawBitmap(bmp, 2 + screenCoords.x - BALLOON_HEIGHT / 3,
						screenCoords.y - BALLOON_HEIGHT, paint);
				drawGuestNumber(canvas, map, ((ViewDinner)mMap).getAdapter().getDinner());
        	}
        }
    }
    
    private void drawGuestNumber(Canvas canvas, MapView mapView,
			Dinner selectedMapLocation) {
		if (selectedMapLocation != null) {
			Point selDestinationOffset = new Point();
			mapView.getProjection().toPixels(selectedMapLocation.getPoint(), selDestinationOffset);
			List<RSVP> rsvps = ((ViewDinner)mMap).rsvpProvider.getByDinner(selectedMapLocation.getName());
			String number = Integer.toString(rsvps.size());
			canvas.drawText(number, selDestinationOffset.x,
					selDestinationOffset.y - (int)(BALLOON_HEIGHT/1.75), getNumberPaint());
		}
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
