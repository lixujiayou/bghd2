package com.bghd.express.utils.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bghd.express.R;


/**
 * Created by ${wj} on 2015/4/8 0008.
 */
public class SideBar extends View {

    //触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    //26个字母
    public static String[] b={ "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };

    //选中
    private int choose=-1;

    private Paint paint=new Paint();
    //点击提示框
    private TextView mTextDialog;

    /**
     * 设置点击字体提示框
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog){
        this.mTextDialog=mTextDialog;
    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height=getHeight();//获取对应的高度
        int width=getWidth();//获取对应的宽度
        int singleHeight=height/b.length;//获取每一个字母的高度

        for(int i=0;i<b.length;i++){
            paint.setColor(Color.rgb(33,65,98));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(20);
            //选中
            if(i==choose)
            {
                paint.setColor(Color.RED);//设置选中状态颜色
                paint.setFakeBoldText(true);
            }
            //x坐标等于中间-字符串宽度的一办(????????)
            float xPos=width/2-paint.measureText(b[i])/2;
            float yPos=singleHeight*i+singleHeight;
            canvas.drawText(b[i],xPos,yPos,paint);
            paint.reset();//重置画笔
        }
    }

    /**
     * 重写
     * @param event
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int action=event.getAction();
        float y=event.getY();//点击Y坐标
        int oldChoose=choose;
        OnTouchingLetterChangedListener listener=onTouchingLetterChangedListener;
        int c=(int)(y/getHeight()*b.length);//点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数

        switch (action){
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(0*00000000));
                choose=-1;//
                invalidate();
                if(mTextDialog!=null)
                {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundResource(R.drawable.sidebar_background);
                if(oldChoose!=c)
                {
                    if(c>=0 && c<b.length)
                    {
                        if(listener!=null)
                        {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if(mTextDialog!=null)
                        {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose=c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener){
        this.onTouchingLetterChangedListener=onTouchingLetterChangedListener;
    }

    /**
     * 接口
     */
    public interface OnTouchingLetterChangedListener{
        public void onTouchingLetterChanged(String s);
    }
}
