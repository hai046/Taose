package com.buy.games.dice;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.view.KeyEvent;

import com.buy.util.BitmapUtil;
import com.brunjoy.taose.R;

public class GLRender implements Renderer {
    boolean key = true;
    float xrot, yrot;
    float xspeed, yspeed;
    float z = -5.0f;
    int one = 0x10000;

    // ���߲���
    FloatBuffer lightAmbient = FloatBuffer.wrap( new float[] { 0.5f, 0.5f, 0.5f, 1.0f } );
    FloatBuffer lightDiffuse = FloatBuffer.wrap( new float[] { 1.0f, 1.0f, 1.0f, 1.0f } );
    FloatBuffer lightPosition = FloatBuffer.wrap( new float[] { 0.0f, 0.0f, 2.0f, 1.0f } );

    int filter = 1;

    int[] texture;

    IntBuffer vertices = IntBuffer.wrap( new int[] { -one, -one, one, one, -one, one, one, one, one, -one, one, one,

    -one, -one, -one, -one, one, -one, one, one, -one, one, -one, -one,

    -one, one, -one, -one, one, one, one, one, one, one, one, -one,

    -one, -one, -one, one, -one, -one, one, -one, one, -one, -one, one,

    one, -one, -one, one, one, -one, one, one, one, one, -one, one,

    -one, -one, -one, -one, -one, one, -one, one, one, -one, one, -one,

    } );

    IntBuffer normals = IntBuffer.wrap( new int[] { 0, 0, one, 0, 0, one, 0, 0, one, 0, 0, one,

    0, 0, one, 0, 0, one, 0, 0, one, 0, 0, one,

    0, one, 0, 0, one, 0, 0, one, 0, 0, one, 0,

    0, -one, 0, 0, -one, 0, 0, -one, 0, 0, -one, 0,

    one, 0, 0, one, 0, 0, one, 0, 0, one, 0, 0,

    -one, 0, 0, -one, 0, 0, -one, 0, 0, -one, 0, 0, } );

    IntBuffer texCoords = IntBuffer.wrap( new int[] { one, 0, 0, 0, 0, one, one, one, 0, 0, 0, one, one, one, one, 0, one, one, one, 0, 0, 0, 0, one, 0, one, one, one, one, 0, 0,
            0, 0, 0, 0, one, one, one, one, 0, one, 0, 0, 0, 0, one, one, one, } );

    ByteBuffer indices = ByteBuffer.wrap( new byte[] { 0, 1, 3, 2, 4, 5, 7, 6, 8, 9, 11, 10, 12, 13, 15, 14, 16, 17, 19, 18, 20, 21, 23, 22, } );

    @Override
    public void onDrawFrame(GL10 gl) {
        // �����Ļ����Ȼ���
        gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );

        gl.glMatrixMode( GL10.GL_MODELVIEW );
        // ���õ�ǰ��ģ�͹۲����
        gl.glLoadIdentity( );

        gl.glEnable( GL10.GL_LIGHTING );

        // //////////////
        gl.glTranslatef( 0.0f, 0.0f, z );

        // ������ת
        gl.glRotatef( xrot, 1.0f, 0.0f, 0.0f );
        gl.glRotatef( yrot, 0.0f, 1.0f, 0.0f );

        // ��������
        gl.glBindTexture( GL10.GL_TEXTURE_2D, texture[filter] );

        gl.glNormalPointer( GL10.GL_FIXED, 0, normals );
        gl.glVertexPointer( 3, GL10.GL_FIXED, 0, vertices );
        gl.glTexCoordPointer( 2, GL10.GL_FIXED, 0, texCoords );

        gl.glEnableClientState( GL10.GL_NORMAL_ARRAY );
        gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );

        // �����ı���
        gl.glDrawElements( GL10.GL_TRIANGLE_STRIP, 24, GL10.GL_UNSIGNED_BYTE, indices );

        gl.glDisableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
        gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glDisableClientState( GL10.GL_NORMAL_ARRAY );
        // �޸���ת�Ƕ�
        xrot += 0.3f;
        yrot += 0.2f;

        // ��Ͽ���
        if (key) {
            gl.glEnable( GL10.GL_BLEND ); // �򿪻��
            gl.glDisable( GL10.GL_DEPTH_TEST ); // �ر���Ȳ���
        } else {
            gl.glDisable( GL10.GL_BLEND ); // �رջ��
            gl.glEnable( GL10.GL_DEPTH_TEST ); // ����Ȳ���
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        // ����OpenGL�����Ĵ�С
        gl.glViewport( 0, 0, width, height );
        // ����ͶӰ����
        gl.glMatrixMode( GL10.GL_PROJECTION );
        // ����ͶӰ����
        gl.glLoadIdentity( );
        // �����ӿڵĴ�С
        gl.glFrustumf( -ratio, ratio, -1, 1, 1, 10 );
        // ѡ��ģ�͹۲����
        gl.glMatrixMode( GL10.GL_MODELVIEW );
        // ����ģ�͹۲����
        gl.glLoadIdentity( );
    }

    private Context mContext;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable( GL10.GL_DITHER );

        // ����ϵͳ��͸�ӽ�������
        gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST );
        // ��ɫ����
        gl.glClearColor( 0, 0, 0, 0 );

        gl.glEnable( GL10.GL_CULL_FACE );
        // ������Ӱƽ��
        gl.glShadeModel( GL10.GL_SMOOTH );
        // ������Ȳ���
        gl.glEnable( GL10.GL_DEPTH_TEST );

        // ���ù���,,1.0fΪȫ���ߣ�a=50%
        gl.glColor4f( 1.0f, 1.0f, 1.0f, 0.5f );
        // ����Դ����alphaͨ��ֵ�İ�͸����Ϻ���
        gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );

        // �������
        IntBuffer textureBuffer = IntBuffer.allocate( 3 );
        gl.glGenTextures( 3, textureBuffer );
        texture = textureBuffer.array( );

        Bitmap mBitmap=null;
        try {
            mBitmap = BitmapFactory.decodeStream( mContext.getAssets( ).open( "/dice/demo2.png", AssetManager.ACCESS_STREAMING ) );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            mBitmap=BitmapFactory.decodeResource( mContext.getResources( ), R.drawable.zwt1 );
            e.printStackTrace();
        }
        
        mBitmap=BitmapUtil.formatScaleBitmap( mBitmap, 64, 64 );
        
        
        gl.glBindTexture( GL10.GL_TEXTURE_2D, texture[0] );
        gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST ); // ( NEW )
        gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST ); // ( NEW )
        GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap, 0 );

        gl.glBindTexture( GL10.GL_TEXTURE_2D, texture[1] );
        gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR ); // ( NEW )
        gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR ); // ( NEW )
        GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap, 0 );

        gl.glBindTexture( GL10.GL_TEXTURE_2D, texture[2] );
        gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR ); // ( NEW )
        gl.glTexParameterx( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR ); // ( NEW )
        GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, mBitmap, 0 );

        // ��Ȳ������
        gl.glClearDepthf( 1.0f );
        gl.glDepthFunc( GL10.GL_LEQUAL );
        gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST );
        gl.glEnable( GL10.GL_TEXTURE_2D );

        // ���û�����
        gl.glLightfv( GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient );

        // ���������
        gl.glLightfv( GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse );

        // ���ù�Դλ��
        gl.glLightfv( GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition );

        // ����һ�Ź�Դ
        gl.glEnable( GL10.GL_LIGHT1 );

        // �������
        gl.glEnable( GL10.GL_BLEND );
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        key = !key;
        return false;
    }
}