package com.brunjoy.buy.sound;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class SoundManager {
    private MediaPlayer mPlayer;
    private static SoundManager mSoundManager;

    // private Context mContext;

    // public String DiceSound = "file:///android_asset/sounds/dicing.wav";

    public synchronized static SoundManager getInstance(/* Context mContext */) {
        if (mSoundManager == null) {
            mSoundManager = new SoundManager( /* mContext */);
        }
        return mSoundManager;
    }

    private SoundManager(/* Context mContext */) {
        mPlayer = new MediaPlayer( );
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition( );
    }

    public int getDuration() {
        return mPlayer.getDuration( );
    }

    public boolean isLooping() {
        return mPlayer.isLooping( );
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying( );
    }

    public void pause() {
        try {
            if (mPlayer != null && mPlayer.isPlaying( ))
                mPlayer.pause( );
        } catch (Exception e) {
        }
    }

    public void start(AssetFileDescriptor fileDescriptor) {

        try {
            if (mPlayer.isPlaying( ))
                mPlayer.stop( );
        } catch (Exception e) {
            e.printStackTrace( );
        }
        try {
            mPlayer.reset( );
            mPlayer.setDataSource( fileDescriptor.getFileDescriptor( ), fileDescriptor.getStartOffset( ), fileDescriptor.getLength( ) );
            mPlayer.setLooping( false );
            mPlayer.prepare( );
            mPlayer.start( );

        } catch (IllegalArgumentException e) {
            e.printStackTrace( );
        } catch (IllegalStateException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }

    }

    public void stop() {
        try {
            if (mPlayer.isPlaying( ))
                mPlayer.pause( );
            mPlayer.stop( );
            mPlayer.release( );
            
        } catch (Exception e) {
        }
        mPlayer = null;
        mSoundManager=null;
    }

}
