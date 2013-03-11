package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;

public class MyViewPager extends ViewPager {
//    private String TAG = "MyViewPager";
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        //MyLog.i( TAG, "dispatchKeyEvent   MotionEvent=" + event );
        return super.dispatchKeyEvent( event );
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        //MyLog.i( TAG, "onTouchEvent   MotionEvent=" + ev );
        return super.onTouchEvent( ev );
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    public MyViewPager(Context context) {
        super( context );

    }

    @Override
    public void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        super.setCurrentItemInternal( item, smoothScroll, always, 600 );
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        //MyLog.i( TAG, "onInterceptTouchEvent   MotionEvent=" + ev );

        return super.onInterceptTouchEvent( ev );
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
//        //MyLog.i( TAG, "dispatchPopulateAccessibilityEvent   event=" + event );
        return super.dispatchPopulateAccessibilityEvent( event );
    }

}
