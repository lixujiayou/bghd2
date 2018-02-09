package com.bghd.express.utils.tools;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


/**
 * Created by wangwentao on 2017/1/25.
 * Toast统一管理类
 */

public class ToastUtil {
    private static boolean isShow = true;//默认显示
    private static Toast mToast = null;//全局唯一的Toast
    public static int TOAST_TYPE_SUCCESS = 1;
    public static int TOAST_TYPE_ERRO = 2;
    public static int TOAST_TYPE_WARNING = 3;
    public static int TOAST_TYPE_NOMAL = 4;

    /*private控制不应该被实例化*/
    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示Toast
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast){
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public void cancelToast() {
        if(isShow && mToast != null){
            mToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     * type : 1:success 2:erro 3:warning 4:nomal
     */
    public static void showToast(Context context, CharSequence message, int type) {
        if (isShow){
            if (mToast == null) {
                if(type == TOAST_TYPE_SUCCESS) {
                    mToast = Toasty.success(context, message, Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_ERRO){
                    mToast = Toasty.error(context, message, Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_WARNING){
                    mToast = Toasty.warning(context, message, Toast.LENGTH_SHORT, true);
                }else{
                    mToast = Toasty.normal(context, message, Toast.LENGTH_SHORT);
                }
            } else {
                mToast.cancel();
                if(type == TOAST_TYPE_SUCCESS) {
                    mToast = Toasty.success(context, message, Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_ERRO){
                    mToast = Toasty.error(context, message, Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_WARNING){
                    mToast = Toasty.warning(context, message, Toast.LENGTH_SHORT, true);
                }else{
                    mToast = Toasty.normal(context, message, Toast.LENGTH_SHORT);
                }
            }
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showToast(Context context, int resId, int type) {
        if (isShow){
            if (mToast == null) {
                if(type == TOAST_TYPE_SUCCESS) {
                    mToast = Toasty.success(context, context.getResources().getString(resId), Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_ERRO){
                    mToast = Toasty.error(context, context.getResources().getString(resId), Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_WARNING){
                    mToast = Toasty.success(context, context.getResources().getString(resId), Toast.LENGTH_SHORT, true);
                }else{
                    mToast = Toasty.normal(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
                }
            } else {
                mToast.cancel();
                if(type == TOAST_TYPE_SUCCESS) {
                    mToast = Toasty.success(context, context.getResources().getString(resId), Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_ERRO){
                    mToast = Toasty.error(context, context.getResources().getString(resId), Toast.LENGTH_SHORT, true);
                }else if(type == TOAST_TYPE_WARNING){
                    mToast = Toasty.success(context, context.getResources().getString(resId), Toast.LENGTH_SHORT, true);
                }else{
                    mToast = Toasty.normal(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
                }
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(Context context, int resId) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     * @param duration 单位:毫秒
     */
    public static void show(Context context, int resId, int duration) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, duration);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast的View
     * @param context
     * @param message
     * @param duration 单位:毫秒
     * @param view 显示自己的View
     */
    public static void customToastView(Context context, CharSequence message, int duration, View view) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            if(view != null){
                mToast.setView(view);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast的位置
     * @param context
     * @param message
     * @param duration 单位:毫秒
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void customToastGravity(Context context, CharSequence message, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            mToast.show();
        }
    }

    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     * @param context
     * @param message
     * @param iconResId 图片的资源id,如:R.drawable.icon
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastWithImageAndText(Context context, CharSequence message, int iconResId, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(iconResId);
            toastView.addView(imageView, 0);
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型CharSequence
     * @param context
     * @param message
     * @param duration
     * @param view
     * @param isGravity true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, CharSequence message, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            if(view != null){
                mToast.setView(view);
            }
            if(isMargin){
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if(isGravity){
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型resId
     * @param context
     * @param resId
     * @param duration
     * @param view :应该是一个布局，布局中包含了自己设置好的内容
     * @param isGravity true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, int resId, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, duration);
            } else {
                mToast.setText(resId);
            }
            if(view != null){
                mToast.setView(view);
            }
            if(isMargin){
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if(isGravity){
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }
}