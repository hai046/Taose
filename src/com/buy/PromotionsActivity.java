package com.buy;

import android.os.Bundle;

import com.buy.bases.BaseActivity;
import com.buy.views.TopicDetailView;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class PromotionsActivity extends BaseActivity {
    public static final String ID = "topicId";
    private TopicDetailView mTopicDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        mTopicDetailView = new TopicDetailView( this ) {
            @Override
            public void stopRefresh() {
                stopRinghtBtn( );
//                MyLog.e( "test", "stopRefreshstopRefreshstopRefreshstopRefresh" );
            }
        };
        setContentView( mTopicDetailView.getView( ) );
        
        int topicId = getIntent( ).getIntExtra( ID, -1 );
        if (topicId < 0) {
            finish( );
            return;
        }
       
        mTopicDetailView.setTopicId( topicId, getIntent( ).getStringExtra( "icon" ) );
        setTitle( getIntent( ).getStringExtra( "name" ) );
        MobclickAgent.onEvent( this, "topic_gallery", "查看:"+ getIntent( ).getStringExtra( "name" ) );
        onRightBtnRota( );
    }

    @Override
    public boolean onClickLeftButton() {
        return false;
    }

    @Override
    public void onClickRightButton() {
        mTopicDetailView.onRefresh( );
        onRightBtnRota( );
    }

    @Override
    public void setCurrentTitleString() {
        setTitle( R.string.promotions );
        setTopBtnBackground( R.drawable.selector_back, R.drawable.selector_loading );
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        super.onResume( );
        MobclickAgent.onResume( this );
    }

    @Override
    protected void onPause() {
        super.onPause( );
        MobclickAgent.onPause( this );
    }
}
