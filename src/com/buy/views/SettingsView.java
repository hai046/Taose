package com.buy.views;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buy.bases.MyBaseAdapter;
import com.buy.data.CheckUpdataManager;
import com.buy.dialogs.InputPassWordDialog;
import com.buy.dialogs.InputPassWordDialog.CallBack;
import com.buy.dialogs.SelectDialog;
import com.buy.stores.Settings;
import com.buy.util.MyToast;
import com.buy.util.Util;
import com.brunjoy.taose.R;
import com.umeng.analytics.MobclickAgent;

public class SettingsView extends BaseView implements OnItemClickListener {

    private ListView mListView;

    public SettingsView(Context mContext) {
        super( mContext );
        // DianJinPlatform.initialize( mContext, 16665, "fd0e987d53118877b112afb72a0eb8c2" );// 点金广告初始化
        mListView = new ListView( mContext );
        mListView.addHeaderView( getHeardView( ) );
        mListView.addFooterView( getFooterView( ) );
        mListView.setDivider( mContext.getResources( ).getDrawable( R.drawable.hbfgx ) );
        initData( );
        mListView.setAdapter( adapter );
        mListView.setBackgroundColor( mContext.getResources( ).getColor( R.color.contentbg ) );
        mListView.setCacheColorHint( Color.TRANSPARENT );
        setContentView( mListView, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT ) );
        mListView.setOnItemClickListener( this );
    }

    private View getHeardView() {
        View view = getLayoutInflater( ).inflate( R.layout.settings_top, null );
        return view;
    }

    private View getFooterView() {
        View view = getLayoutInflater( ).inflate( R.layout.settings_bottom, null );
        view.findViewById( R.id.setting_webchat ).setOnClickListener( new OnClickListener( ) {

            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse( "http://weixin.qq.com/r/gHSMg7zEE4Jsh63gnyGk" );
                mContext.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );

            }
        } );
        return view;
    }

    private ArrayList<MyHoder> list = new ArrayList<SettingsView.MyHoder>( );

    @Override
    public void onStart() {
        adapter.notifyDataSetChanged( );
    }

    private void initData() {
        String[] array = mContext.getResources( ).getStringArray( R.array.settings );
        for (int i = 0; i < array.length; i++) {
            list.add( new MyHoder( array[i], getImage( i ) ) );
        }
        adapter.addAll( list );

    }

    InputPassWordDialog input;
    private boolean isFirstInput = true;
    private String pwd = null;

    private int getImage(int i) {
        switch (i) {
        case 0:
            return R.drawable.sybz;

        case 1:

            return R.drawable.szmm;
        case 2:

            return R.drawable.mzsm;
        case 3:

            return R.drawable.xxfk;
        case 4:

            return R.drawable.jcgx;
        case 5:

            return R.drawable.gywm;
            // case 5:
            //
            // return R.drawable.tjcp;
            // case 6:
            //
            // return R.drawable.gywm;
        case 7:

            MyToast.showMsgShort( mContext, "weichat" );
            break;
        default:
            break;
        }
        return R.drawable.zwt1;
    }

    private void inputPwd() {
        if (input == null) {
            input = new InputPassWordDialog( mContext );
            input.setOnCallBack( new CallBack( ) {

                @Override
                public void cancle() {
                    isFirstInput = true;
                }

                @Override
                public void bakeResult(String result) {
                    if (isFirstInput) {
                        isFirstInput = false;
                        inputPwd( );
                        pwd = result;
                    } else {
                        isFirstInput = true;
                        if (result.equals( pwd )) {
                            com.buy.stores.Settings.getInSettings( mContext ).setPassWord( Util.getMD5Str( result ) );
                            Toast.makeText( mContext, "密码设置成功，下次启动应用生效", Toast.LENGTH_LONG ).show( );
                            MobclickAgent.onEvent( mContext, "setPassword" );
                        } else {
                            Toast.makeText( mContext, "两次输入密码不一致，请重新设置", Toast.LENGTH_LONG ).show( );
                            inputPwd( );
                            pwd = null;
                        }
                    }

                }
            } );
        }

        if (isFirstInput) {
            input.setTitle( R.string.prompt_input_psd );
        } else {
            input.setTitle( R.string.prompt_input_psd_again );
        }
        input.show( );
    }

    private MyBaseAdapter<MyHoder> adapter = new MyBaseAdapter<SettingsView.MyHoder>( ) {

        @Override
        public View bindView(int position, MyHoder t, View view) {
            if (view == null)
                view = getLayoutInflater( ).inflate( R.layout.settings_item, null );
            TextView name = (TextView) view.findViewById( R.id.name );
            ImageView image = (ImageView) view.findViewById( R.id.image );
            TextView tvContent = (TextView) view.findViewById( R.id.content );
            if (position == 4) {// 比较当前的服务器版本信息
                int vcode = Settings.getInSettings( mContext ).getInt( "serverversionCode", 1 );
                int cVersion = Settings.getInSettings( mContext ).getInt( "currentversionCode", 1 );
                if (vcode > cVersion) {
                    tvContent.setText( "New Ver:" + vcode );
                    tvContent.setTextColor( mContext.getResources( ).getColor( R.color.red ) );
                } else {
                    tvContent.setText( "Ver:" + vcode );
                    tvContent.setTextColor( mContext.getResources( ).getColor( R.color.black ) );
                }

            } else {
                tvContent.setText( null );
            }
            name.setText( t.getName( ) );
            image.setImageResource( t.getImageResId( ) );
            return view;
        }
    };

    private class MyHoder {
        private String name;
        private int imageResId;

        public MyHoder(String name, int imageResId) {
            this.name = name;
            this.imageResId = imageResId;
        }

        public String getName() {
            return name;
        }

        // public void setName(String name) {
        // this.name = name;
        // }

        public int getImageResId() {
            return imageResId;
        }

        // public void setImageResId(int imageResId) {
        // this.imageResId = imageResId;
        // }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg2 - 1) {
        // case 0:
        // Intent intent = new Intent( );
        // intent.setClass( mContext, CreditSecurityActivity.class );
        // mContext.startActivity( intent );
        // break;
        case 0:
            Util.gotoBuyHelp( mContext );
            break;

        case 1:
            if (TextUtils.isEmpty( com.buy.stores.Settings.getInSettings( mContext ).getPassWord( ) )) {
                inputPwd( );
            } else {
                if (mSelectDialog == null) {
                    mSelectDialog = new SelectDialog( mContext ) {

                        @Override
                        public void onSelected(int position, String msg) {
                            if (position == 1) {
                                InputPassWordDialog mInputPassWordDialog = new InputPassWordDialog( mContext );
                                mInputPassWordDialog.setTitle( "请输入应用密码" );
                                mInputPassWordDialog.setOnCallBack( new CallBack( ) {

                                    @Override
                                    public void cancle() {

                                    }

                                    @Override
                                    public void bakeResult(String result) {
                                        if (Util.getMD5Str( result ).equals( com.buy.stores.Settings.getInSettings( mContext ).getPassWord( ) )) {
                                            com.buy.stores.Settings.getInSettings( mContext ).setPassWord( null );

                                            Toast.makeText( mContext, "已经为您删除了密码", 1 ).show( );
                                            MobclickAgent.onEvent( mContext, "deletePwd" );
                                        } else {
                                            Toast.makeText( mContext, "输入密码错误!", 1 ).show( );
                                        }

                                    }
                                } );
                                mInputPassWordDialog.show( );
                            } else {
                                inputPwd( );
                            }

                        }
                    };
                    mSelectDialog.addItem( "修改密码" );
                    mSelectDialog.addItem( "删除密码" );
                }
                mSelectDialog.show( );
            }
            break;
        case 2:
            Util.gotoDisclaimer( mContext );
            break;
        case 3:
            Util.gotoSuggest( mContext );
            break;
        case 4:
            new CheckUpdataManager( mContext ).checkAndUpdata( true );
            break;
        case 5:
            Util.gotoAboutMe( mContext );
            break;
        // case 5:
        // DianJinPlatform.showOfferWall( mContext, DianJinPlatform.Oriention.SENSOR, OfferWallStyle.BROWN );
        // break;
        // case 6:
        // Util.gotoAboutMe( mContext );
        // break;
        default:
            break;
        }
    }

    SelectDialog mSelectDialog;
}
