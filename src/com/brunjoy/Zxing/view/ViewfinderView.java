/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brunjoy.Zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.brunjoy.Zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;
import com.brunjoy.taose.R;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial transparency outside it, as well as the laser scanner animation and result
 * points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192, 128, 64 };
    private static final long ANIMATION_DELAY = 100L;
    private static final int OPAQUE = 0xFF;
    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int frameColor;
    private final int laserColor;
    private final int resultPointColor;
    private int scannerAlpha;
    private int vOffsetHeight = 0;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private Bitmap mBitmap;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super( context, attrs );

        // Initialize these once for performance rather than calling them every
        // time in onDraw().
        paint = new Paint( );
        Resources resources = getResources( );
        maskColor = resources.getColor( R.color.viewfinder_mask );
        resultColor = resources.getColor( R.color.result_view );
        frameColor = resources.getColor( R.color.viewfinder_frame );
        laserColor = resources.getColor( R.color.viewfinder_laser );
        resultPointColor = resources.getColor( R.color.possible_result_points );
        scannerAlpha = 0;
        possibleResultPoints = new HashSet<ResultPoint>( 5 );
        mBitmap = BitmapFactory.decodeResource( getResources( ), R.drawable.qrcode_scan_line );
    }

    private int offsetPadding = 0;
    private  Rect rectRrc = new Rect( );
    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get( ).getFramingRect( );
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth( );
        int height = canvas.getHeight( );

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor( resultBitmap != null ? resultColor : maskColor );
        canvas.drawRect( 0, 0, width, frame.top + 7, paint );//
        canvas.drawRect( 0, frame.top + 7, frame.left + 7, frame.bottom + 1 - 7, paint );
        canvas.drawRect( frame.right + 1 - 7, frame.top + 7, width, frame.bottom + 1 - 7, paint );
        canvas.drawRect( 0, frame.bottom + 1 - 7, width, height, paint );
        
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha( OPAQUE );
            canvas.drawBitmap( resultBitmap, frame.left, frame.top, paint );
        } else {
            int linewidht = 10;
            paint.setColor( frameColor );

            canvas.drawRect( offsetPadding + frame.left, offsetPadding + frame.top, offsetPadding + (linewidht + frame.left), offsetPadding + (50 + frame.top), paint );
            canvas.drawRect( offsetPadding + frame.left, offsetPadding + frame.top, offsetPadding + (50 + frame.left), offsetPadding + (linewidht + frame.top), paint );
            canvas.drawRect( -offsetPadding + ((0 - linewidht) + frame.right), offsetPadding + frame.top, -offsetPadding + (1 + frame.right), offsetPadding + (50 + frame.top),
                    paint );
            canvas.drawRect( -offsetPadding + (-50 + frame.right), offsetPadding + frame.top, -offsetPadding + frame.right, offsetPadding + (linewidht + frame.top), paint );
            canvas.drawRect( offsetPadding + frame.left, -offsetPadding + (-49 + frame.bottom), offsetPadding + (linewidht + frame.left), -offsetPadding + (1 + frame.bottom),
                    paint );
            canvas.drawRect( offsetPadding + frame.left, -offsetPadding + ((0 - linewidht) + frame.bottom), offsetPadding + (50 + frame.left), -offsetPadding + (1 + frame.bottom),
                    paint );
            canvas.drawRect( -offsetPadding + ((0 - linewidht) + frame.right), -offsetPadding + (-49 + frame.bottom), -offsetPadding + (1 + frame.right), -offsetPadding
                    + (1 + frame.bottom), paint );
            canvas.drawRect( -offsetPadding + (-50 + frame.right), -offsetPadding + ((0 - linewidht) + frame.bottom), -offsetPadding + frame.right, -offsetPadding
                    + (linewidht - (linewidht - 1) + frame.bottom), paint );

            paint.setColor( laserColor );
            paint.setAlpha( SCANNER_ALPHA[scannerAlpha] );
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int vmiddle = frame.height( ) / 2 + frame.top;
            vOffsetHeight += 3;
            if (vOffsetHeight >= (frame.height( ) * 0.5) - 25) {
                vOffsetHeight = (int) (-frame.height( ) * 0.5) + 25;
            }

//            Rect rectRrc = new Rect( frame.left, vmiddle + vOffsetHeight, frame.right, vmiddle + mBitmap.getHeight( ) + vOffsetHeight );
            rectRrc.left= frame.left;
            rectRrc.top= vmiddle + vOffsetHeight;
            rectRrc.right=frame.right;
            rectRrc.bottom=vmiddle + mBitmap.getHeight( ) + vOffsetHeight;
            // canvas.drawBitmap( mBitmap, left, top, paint );
            canvas.drawBitmap( mBitmap, null, rectRrc, null );
            // canvas.drawRect( frame.left + 25, vmiddle - 1+vOffsetHeight, frame.right - 25, vmiddle + 2+vOffsetHeight, paint );
            // int hmiddle = frame.width( ) / 2 + frame.left;

            // canvas.drawRect( hmiddle - 1, frame.top + 2, hmiddle + 2, frame.bottom - 1, paint );

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty( )) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>( 5 );
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha( OPAQUE );
                paint.setColor( resultPointColor );
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle( frame.left + point.getX( ), frame.top + point.getY( ), 6.0f, paint );
                }
            }
            if (currentLast != null) {
                paint.setAlpha( OPAQUE / 2 );
                paint.setColor( resultPointColor );
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle( frame.left + point.getX( ), frame.top + point.getY( ), 3.0f, paint );
                }
            }
            // Request another update at the animation interval, but only
            // repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed( ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom );
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate( );
    }
    
    public void setResultBitmat(Bitmap mBitmap)
    {
        this.resultBitmap=mBitmap;
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add( point );
    }

}
