package com.kdg6.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kdg6.R;
import com.kdg6.cache.DataCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BaseActivity extends CheckPermissionsActivity {
    protected ProgressDialog progressDialog;
    protected Window window;
    protected AlertDialog dlg = null;
    protected String checkedtxet, checkedtxet2, selectedId2;
    protected DisplayMetrics dm;
    protected Animation translate_Animation;
    protected Button dlg_confirmb;
    protected Button dlg_cancelb;
    protected TextView dlg_title;
    protected boolean backboolean;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(this.dm);
        this.translate_Animation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.translate);
        dlg = new AlertDialog.Builder(this).create();
        this.window = dlg.getWindow();
        Intent backintent = getIntent();
        backboolean = backintent.getBooleanExtra("fanhui", false);
        initImageLoader();
    }

    protected void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        imageLoader.init(config);
    }

    // @Override
    // protected void onDestroy() {
    // super.onDestroy();
    // Log.e("dd", getClass().getSimpleName()+"    onDestroy");
    // // 结束Activity&从堆栈中移除
    // AppManager.getAppManager().finishActivity(this);
    // }

    /**
     * Toast显示消息
     *
     * @param text 需要显示的内容
     */
    protected void toastShowMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * 返回上一个activity
     *
     * @param cls 需要跳转的activity
     */
    protected void skipActivity2(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("fanhui", true);
        startActivity(intent);
        // finish();
    }

    /**
     * 跳转到下另外一个activity
     *
     * @param cls 需要跳转的activity
     */
    protected void skipActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        // finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("dd", "onNewIntent");
        setIntent(intent);
    }

    protected void Call(String tel) {
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + tel));
        startActivity(phoneIntent);
    }

    protected void dialogShowMessage_P(String message,
                                       OnClickListener confirmListener) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", confirmListener);
        builder.create().show();

    }

    /**
     * 对话框显示消息
     *
     * @param message         需要显示的消息
     * @param confirmListener 点击确认的事件处理
     *                        点击取消的事件处理
     */
    protected void dialogShowMessage(String message,
                                     OnClickListener confirmListener, OnClickListener canlListener) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", confirmListener);
        builder.setNegativeButton("取消",
                canlListener == null ? new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface face,
                                        int paramAnonymous2Int) {
                        face.dismiss();
                    }
                } : canlListener);
        builder.create().show();

    }

    protected void dialogShowMessage(String message, String positiveStr, String negativeStr,
                                     OnClickListener confirmListener, OnClickListener canlListener) {
        AlertDialog.Builder builder = new Builder(this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setTitle("提示");
        builder.setPositiveButton(positiveStr, confirmListener);
        builder.setNegativeButton(negativeStr,
                canlListener == null ? new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface face,
                                        int paramAnonymous2Int) {
                        face.dismiss();
                    }
                } : canlListener);
        builder.create().show();

    }

    protected void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "提示", "正在处理中，请稍后...");
    }

    private EditText searchC;
    protected AlertDialog inputdlg;
    protected Window inputwindow;

    /**
     * 带EditText的 AlertDialog
     *
     * @param view           选定内容输出的view
     * @param name           title
     * @param arrayradiotext 共选择的项（list）
     * @param edtext         当选中项内容为edtext，则显示输入框
     */
    protected void inputdialog(final TextView view, String name,
                               List<String> arrayradiotext, final String edtext) {
    }


}
