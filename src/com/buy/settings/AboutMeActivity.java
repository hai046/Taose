package com.buy.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.buy.bases.BaseActivity;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class AboutMeActivity extends BaseActivity {
    TextView tvEmali;
    String email = "bd@taose69.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        setContentView( R.layout.setting_about_layout );
        setTitle( R.string.menu_aboutme );
        TextView tvVersion = (TextView) findViewById( R.id.setting_about_versionTV );
        tvEmali = (TextView) findViewById( R.id.setting_about_emailTV );

        tvEmali.setText( email  );
        PackageManager pm = getPackageManager( );
        try {
            PackageInfo pi = pm.getPackageInfo( getPackageName( ), 0 );
            tvVersion.setText( "版本：" + pi.versionName );
        } catch (NameNotFoundException e) {

        }
        tvEmali.setOnClickListener( new OnClickListener( ) {

            @Override
            public void onClick(View v) {
                Uri emailUri = Uri.parse( "mailto:" + email );
                Intent intent = new Intent( Intent.ACTION_SENDTO, emailUri );
                String extraInfo = Build.MANUFACTURER + " " + Build.MODEL + ",\tandroid:" + Build.VERSION.RELEASE + ",\t" + getWindowManager( ).getDefaultDisplay( ).getWidth( )
                        + "*" + getWindowManager( ).getDefaultDisplay( ).getHeight( );
                intent.putExtra( Intent.EXTRA_TEXT, "\n\n\n\n\n\n\n\n\n\n\n\n\n" + "\t如果是应用的问题，为了更好的为您服务，请保留下面的额外信息\n" + "\t额外信息:" + extraInfo );
                intent.putExtra( Intent.EXTRA_SUBJECT, "来自【" + getString( R.string.app_name ) + "】用户的邮件" );
                startActivity( Intent.createChooser( intent, "发送" ) );
            }
        } );
    }

    @Override
    public boolean onClickLeftButton() {
        return false;
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume( this );
        super.onResume( );
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause( this );
        super.onPause( );
    }

    @Override
    public void onClickRightButton() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCurrentTitleString() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

}
