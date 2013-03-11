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

package com.brunjoy.Zxing.decoding;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.brunjoy.Zxing.activity.CaptureActivity;
import com.brunjoy.Zxing.camera.CameraManager;
import com.brunjoy.Zxing.utils.Const;
import com.brunjoy.Zxing.utils.ZxingDataManager;
import com.brunjoy.Zxing.view.ViewfinderResultPointCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler extends Handler {

    private static final String TAG = CaptureActivityHandler.class.getSimpleName( );

    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureActivity activity, Vector<BarcodeFormat> decodeFormats, String characterSet) {
        this.activity = activity;
        decodeThread = new DecodeThread( activity, decodeFormats, characterSet, new ViewfinderResultPointCallback( activity.getViewfinderView( ) ) );
        decodeThread.start( );
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        CameraManager.get( ).startPreview( );
        restartPreviewAndDecode( );
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
        case Const.AUTO_FOCUS:
            // Log.d(TAG, "Got auto-focus message");
            // When one auto focus pass finishes, start another. This is the closest thing to
            // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
            if (state == State.PREVIEW) {
                CameraManager.get( ).requestAutoFocus( this, Const.AUTO_FOCUS );
            }
            break;
        case Const.RESTART_PREVIEW:
            // Log.d( TAG, "Got restart preview message" );
            restartPreviewAndDecode( );
            break;
        case Const.DECODE_SUCCEEDED:
            Log.d( TAG, "Got decode succeeded message" );
            state = State.SUCCESS;
            Bundle bundle = message.getData( );
            Bitmap barcode = bundle == null ? null : (Bitmap) bundle.getParcelable( DecodeThread.BARCODE_BITMAP );
            String str_result = ((Result) message.obj).getText( );
            activity.handleDecode( (Result) message.obj, barcode );
            activity.playBeepSoundAndVibrate( );
            // Intent intent = new Intent( activity, MainActivity.class );
            // intent.putExtra( "numid", activity.getIntent( ).getStringExtra( "numid" ) );
            // intent.putExtra( "code", str_result );
            // activity.startActivity( intent );
            Log.e( TAG, "================================" );
            Log.e( TAG, "str_result=" + str_result );
            Log.e( TAG, "================================" );

            ZxingDataManager.getInsance( ).callBack( str_result );
//            if (str_result != null && str_result.startsWith( "connect_id:" )) {
                activity.finish( );
//            } /*else {
//                CameraManager.get( ).requestPreviewFrame( decodeThread.getHandler( ), Const.DECODE );
//            }*/
            break;
        case Const.DECODE_FAILED:
            // We're decoding as fast as possible, so when one decode fails, start another.
            state = State.PREVIEW;
            CameraManager.get( ).requestPreviewFrame( decodeThread.getHandler( ), Const.DECODE );
            break;
        case Const.RETURN_SCAN_RESULT:
            // Log.d( TAG, "Got return scan result message" );
            activity.setResult( Activity.RESULT_OK, (Intent) message.obj );
            activity.finish( );
            break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get( ).stopPreview( );
        Message quit = Message.obtain( decodeThread.getHandler( ), Const.QUIT );
        quit.sendToTarget( );
        try {
            decodeThread.join( );
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages( Const.DECODE_SUCCEEDED );
        // removeMessages(R.id.return_scan_result);
        removeMessages( Const.DECODE_FAILED );
    }
    
    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get( ).requestPreviewFrame( decodeThread.getHandler( ), Const.DECODE );
            CameraManager.get( ).requestAutoFocus( this, Const.AUTO_FOCUS );
            activity.drawViewfinder( );
        }
    }

}
