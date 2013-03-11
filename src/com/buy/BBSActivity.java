package com.buy;

import android.os.Bundle;

import com.buy.bases.BaseActivity;
import com.buy.views.TiebaListView;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 帖子列表
 * 
 * @author 邓海柱<br>
 *         E-mail:denghaizhu@brunjoy.com
 */
public class BBSActivity extends BaseActivity {
    private TiebaListView mTiebaListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        onRightBtnRota( );
        mTiebaListView = new TiebaListView( this ) {
            @Override
            public void onRefresh() {
                super.onRefresh( );
                onRightBtnRota( );
            }

            @Override
            public void onCompile() {
                stopRinghtBtn( );

                super.onCompile( );
            }
        };
        setContentView( mTiebaListView.getView( ) );
    }

    @Override
    public boolean onClickLeftButton() {

        return false;
    }

    @Override
    public void onClickRightButton() {
        mTiebaListView.onRefresh( );
    }

    @Override
    public void setCurrentTitleString() {
        setTitle( "两性交流" );
        setTopBtnBackground( R.drawable.selector_back, R.drawable.selector_loading );
    }

    @Override
    public void onRefresh() {

    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume( this );
        super.onResume( );
    }

    @Override
    protected void onPause() {
        MobclickAgent.onResume( this );
        super.onPause( );
    }
}
