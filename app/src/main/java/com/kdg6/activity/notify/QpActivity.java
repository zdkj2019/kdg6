package com.kdg6.activity.notify;

import java.io.IOException;

import com.kdg6.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class QpActivity extends Activity{

	private MediaPlayer mMediaPlayer;
	private TextView tv_qp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_qp);
		tv_qp = (TextView) findViewById(R.id.tv_qp);
		tv_qp.setText(getIntent().getStringExtra("message"));
        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
	}
	
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = null; 
	}
}
