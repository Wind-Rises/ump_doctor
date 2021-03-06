package it.swiftelink.com.vcs_doctor.videoChat.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;


/**
 * Created by chentao on 2018/1/14.
 * 自由拖动的悬浮窗
 */

public class DraggableFloatWindow {

    private static final String TAG = DraggableFloatWindow.class.getSimpleName();
    private static DraggableFloatWindow mDraggableFloatWindow;
    private static WindowManager.LayoutParams mParams = null;
    private static WindowManager mWindowManager = null;
    private static DraggableFloatView mDraggableFloatView;


    public DraggableFloatWindow(Context context) {

        initDraggableFloatView(context);
    }

    /**
     * 第一种得到弹窗的方法
     *
     * @param context，上下文对象
     * @return
     */
    public static DraggableFloatWindow getDraggableFloatWindow(Context context) {
        if (mDraggableFloatWindow == null) {
            synchronized (DraggableFloatWindow.class) {
                if (mDraggableFloatWindow == null) {
                    initDraggableFloatViewWindow(context);
                    mDraggableFloatWindow = new DraggableFloatWindow(context);
                }
            }
        }
        return mDraggableFloatWindow;
    }

    public void setOnTouchButtonListener(DraggableFloatView.OnTouchButtonClickListener touchButtonListener) {
        mDraggableFloatView.setTouchButtonClickListener(touchButtonListener);
    }


    public void show() {

        attachFloatViewToWindow();
    }

    public void dismiss() {
        detachFloatViewFromWindow();
    }

    private boolean showState = false;

    /**
     * attach floatView to window
     */
    private void attachFloatViewToWindow() {
        showState = true;
        if (mDraggableFloatView == null)
            throw new IllegalStateException("DraggableFloatView can not be null");
        if (mParams == null)
            throw new IllegalStateException("WindowManager.LayoutParams can not be null");
        try {
            mWindowManager.updateViewLayout(mDraggableFloatView, mParams);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
            //if floatView not attached to window,addView
            mWindowManager.addView(mDraggableFloatView, mParams);
        }
    }

    public void addVideoView(View view){

        if(mDraggableFloatView != null){
            mDraggableFloatView.addView(view);
        }
    }

    /**
     * detach floatView from window
     */
    private void detachFloatViewFromWindow() {
        if (showState) {
            mWindowManager.removeView(mDraggableFloatView);
            showState = false;
        }
    }

    public boolean isShowing() {
        return showState;
    }

    /**
     * 初始化initFloatViewWindow参数
     *
     * @param context，上下文对象
     */
    private static void initDraggableFloatViewWindow(Context context) {
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.packageName = context.getPackageName();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //The default position is vertically to the right
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = DeviceUtils.getScreenWidth(context) - dip2px(context, 20);
        mParams.y = dip2px(context, 60);

        mParams.format = PixelFormat.RGBA_8888;
    }


    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 初始化touch按钮所在window
     *
     * @param context，上下文对象
     */
    private void initDraggableFloatView(Context context) {
        mDraggableFloatView = new DraggableFloatView(context, new OnFlingListener() {
            @Override
            public void onMove(float moveX, float moveY) {
                //  mParams.gravity = Gravity.NO_GRAVITY;
                mParams.x = (int) (mParams.x + moveX);
                mParams.y = (int) (mParams.y + moveY);
                mWindowManager.updateViewLayout(mDraggableFloatView, mParams);
            }
        });
    }
}
