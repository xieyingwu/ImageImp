package com.ggtf.xieyingwu.demotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyingwu on 2017/3/20.
 */

public class DragGroup extends LinearLayout implements View.OnTouchListener {
    private final static String TAG = "DRAG";
    private final static int DEFAULT_X = 50;
    private final static int DEFAULT_Y = 50;
    private ViewGroup left;
    private ViewGroup right;
    private ImageView dragImg;
    private WindowManager wm;
    private WindowManager.LayoutParams lp;
    private boolean isAdded;
    private CountDownTimer cdt;
    private int rawX;
    private int rawY;
    private int x, y;
    private int dragX;
    private int dragY;
    private boolean isDown;
    private boolean isScale;

    public DragGroup(Context context) {
        this(context, null);
    }


    public DragGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragImg = new ImageView(context);
        lp = new WindowManager.LayoutParams();
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 2) {
            if (left == null) left = (ViewGroup) getChildAt(0);
            if (right == null) {
                right = (ViewGroup) getChildAt(1);
                right.setOnTouchListener(this);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.e(TAG, "onTouchEvent() action = " + action);
        switch (action) {
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.e(TAG, "onInterceptTouchEvent() action = " + action);

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        Log.e(TAG, "dispatchTouchEvent() action = " + action);

        return super.dispatchTouchEvent(ev);
    }

    private void addDragView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        dragImg.setBackgroundColor(0x88123456);
        dragImg.setImageBitmap(bitmap);
        lp.width = bitmap.getWidth() * 2;
        lp.height = bitmap.getHeight() * 2;
        Log.i(TAG, "width = " + lp.width + " ;height = " + lp.height);
        lp.gravity = Gravity.TOP | Gravity.START;
//        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        lp.format = PixelFormat.TRANSPARENT;
//        lp.windowAnimations=android.R.style.Animation_Translucent;/*入场出场动画*/
        dragX = (rawX - lp.width / 2);
        dragY = (rawY - lp.height / 2);
        Log.i(TAG, "dragX = " + dragX + " ;dragY = " + dragY);
        Log.i(TAG, "触摸点坐标: x = " + rawX + " ;y = " + rawY);
        lp.x = dragX;
        lp.y = dragY;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (!isAdded) {
            wm.addView(dragImg, lp);
            isAdded = true;
        } else {
            wm.updateViewLayout(dragImg, lp);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!v.equals(right)) return false;
        Log.e(TAG, "onTouch() ");
        if (isDown && event.getAction() != MotionEvent.ACTION_DOWN) {
            isDown = false;
            cdt.cancel();
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isDown = true;
            rawX = (int) event.getRawX();
            rawY = (int) event.getRawY();
            x = (int) event.getX();
            y = (int) event.getY();
            /*判断是否是长按效果；判断ACTION_DOWN的行为持续的时间长度*/
            cdt = new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Log.e(TAG, "进行了长按事件，产生拖动悬浮图");
                    int childCount = right.getChildCount();
                    boolean isSelected = false;
                    for (int i = 0; i < childCount; i++) {
                        Object tag = right.getChildAt(i).getTag();
                        isSelected = tag != null && tag.equals(true);
                        if (isSelected) break;
                    }
                    if (isSelected) {
                    /*有子控件被选中，长按进行移动分组处理，生成悬浮图片*/
                        addDragView();
                    /*生成移动动画*/
                        playAnim();
                    }
                }
            };
            cdt.start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isAdded) {
                /*判断当前悬浮界面的坐标位置*/
                getDragPosition();
//              移除悬浮拖拽界面
                wm.removeViewImmediate(dragImg);
                isAdded = false;
                /*还原控件效果*/
                resetStateRight();
            }
            if (isScale) {
                scaleChildHeight(left, 1.5f, true);
                isScale = false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int movedX = (int) (event.getRawX() - rawX);
            int movedY = (int) (event.getRawY() - rawY);
            changeDragView(movedX, movedY);
        }
        return false;
    }

    private void resetStateRight() {
        Log.e(TAG, "重置子控件显示效果");
        int childCount = right.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = right.getChildAt(i);
            if (childAt.getVisibility() != VISIBLE) {
                childAt.setVisibility(VISIBLE);
            }
        }
    }

    private void playAnim() {
        int childCount = right.getChildCount();
        List<View> animView = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View childAt = right.getChildAt(i);
            if (childAt.getTag() != null && childAt.getTag().equals(true)) {
                animView.add(childAt);
            }
        }
        for (View view : animView) {
            playMovedAnim(view);
        }
    }

    private void playMovedAnim(final View view) {
        boolean isTest = true;
        float startX = view.getX();
        float startY = view.getY();
        int width = view.getWidth();
        int height = view.getHeight();
        Log.w(TAG, "view.getX() = " + startX + " ;v.getY() = " + startY + " ;v.getWidth = " + width + " ;view.getHeight = " + height);
        Log.e(TAG, "悬浮图片在右边视图的相对位置 x = " + x + " ;y = " + y);
        float centerX = startX + width / 2;
        float centerY = startY + height / 2;
//        if (isTest) return;
        TranslateAnimation ta = new TranslateAnimation(0, x - centerX, 0, y - centerY);
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.3f);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(ta);
        as.addAnimation(aa);
        as.setDuration(500);
        as.setInterpolator(new LinearInterpolator());
        view.startAnimation(as);
    }

    private void getDragPosition() {
        Log.e(TAG, "lp.x = " + lp.x + " ;lp.y = " + lp.y);
        int childCount = left.getChildCount();
        int l = 0;
        int r = 0;
        int t = 0;
        int b = 0;
        for (int i = 0; i < childCount; i++) {
            View childAt = left.getChildAt(i);
            r = childAt.getWidth();
            if (i != 0) {
                t = t + childAt.getHeight();
            }
            b = b + childAt.getHeight();
            Log.e(TAG, "l = " + l + " ;t = " + t + " ;r = " + r + " ;b = " + b);
            Rect rect = new Rect(l, t, r, b);
            boolean contains = rect.contains(lp.x, lp.y);
            if (contains) {
                Toast.makeText(getContext(), "分组移动到左边第 " + i + " 个分组中", Toast.LENGTH_SHORT).show();
                /*更新右边控件的数据*/
                updateRightData();
                break;
            }
        }
    }

    private void updateRightData() {
        int childCount = right.getChildCount();
        if (right instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) right;
            Adapter adapter = adapterView.getAdapter();
            if (adapter instanceof AdapterGrid) {
                AdapterGrid grid = (AdapterGrid) adapter;
                List<Integer> resIds = grid.resIds;
                List<Integer> leaveResIds = new ArrayList<>();
                for (int i = 0; i < childCount; i++) {
                    View childAt = right.getChildAt(i);
                    if (childAt.getTag() == null) {
                        leaveResIds.add(resIds.get(i));
                    }
                }
                grid.resIds.clear();
                grid.resIds.addAll(leaveResIds);
                grid.notifyDataSetChanged();
            }
        }
    }

    private void changeDragView(int movedX, int movedY) {
        if (isAdded) {
            lp.x = dragX + movedX;
            lp.y = dragY + movedY;
            wm.updateViewLayout(dragImg, lp);
            /*判断是否进入左边控件范围；依据X坐标值判断*/
            if (lp.x < left.getWidth() && !isScale) {
                isScale = true;
                Log.e(TAG, "进入到左边分组控件范围;子控件高度扩大1.5倍");
                scaleChildHeight(left, 1.5f, false);
            } else if (lp.x > left.getWidth() && isScale) {
                Log.e(TAG, "还原左边控件高度");
                scaleChildHeight(left, 1.5f, true);
                isScale = false;
            }
        }
    }

    private void scaleChildHeight(ViewGroup left, float scale, boolean isReset) {
        int childCount = left.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = left.getChildAt(i);
            ViewGroup.LayoutParams lp = childAt.getLayoutParams();
            Log.e(TAG, "child = " + i + " ;lp.Height = " + lp.height);
            if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                lp.height = childAt.getHeight();
            }
            if (isReset) {
                lp.height = (int) (lp.height / scale);
            } else {
                lp.height = (int) (lp.height * scale);
            }
            childAt.setLayoutParams(lp);
        }
    }
}
