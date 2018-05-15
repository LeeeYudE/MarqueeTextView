package marqueetextview.charco.android.marqueetextview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import marqueetextview.charco.android.marqueetextview.R;


/**
 * Created 18/5/8 20:56
 * Author:charcolee
 * Version:V1.0
 * ----------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------
 */

public class MarqueeTextView extends View implements View.OnTouchListener {

    public static final int INTERVAL = 100;//文字与上下边缘的间隙
    public static final int SPEED_SLOW = 1;//慢速移动
    public static final int SPEED_NORMAL = 2;//正常移动
    public static final int SPEED_FAST = 3;//快速移动

    private String content;//文字内容
    private TextPaint mPaint;//绘制文字的paint
    private Rect mTextRect;//测量文字长度
    private float textX,textY;//绘制文字的X，Y坐标

    private int textColor ;
    private int backgroudColor ;
    private int speed ;
    private float mDownX;
    private boolean isStartScroll = true;//是否开始滚动
    private boolean isAutoScro = true;//是否自动滚动
    private int  heightPixels , widthPixels , textWidth ;
    private long mLastClick;


    public MarqueeTextView(Context context) {
        this(context,null);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView);
        content = ta.getString(R.styleable.MarqueeTextView_content);
        if (TextUtils.isEmpty(content)){
            content = "本人单身，欢迎来撩";
        }
        textColor = ta.getColor(R.styleable.MarqueeTextView_textColor,getResources().getColor(R.color.text_color));
        backgroudColor = ta.getColor(R.styleable.MarqueeTextView_backgroundColor,getResources().getColor(R.color.background_color));
        setSpeed(ta.getInt(R.styleable.MarqueeTextView_speed,SPEED_SLOW));
        ta.recycle();
        init();
    }

    private void init() {
        //获取屏幕宽高
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        heightPixels = dm.heightPixels;
        widthPixels = dm.widthPixels;

        mPaint = new TextPaint();
        mPaint.setColor(textColor);
        //将文字大小设定为屏幕高度减去上下间隙
        mPaint.setTextSize(heightPixels - INTERVAL*2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextRect = new Rect();
        setBackgroundColor(backgroudColor);
        setOnTouchListener(this);
        checkTextWidth(content);
    }

    //检查文字长度是否小于屏幕长度
    private void checkTextWidth(String text){
        //测量文字长度
        mPaint.getTextBounds(text,0,text.length(),mTextRect);
        textWidth = mTextRect.width();
        if (widthPixels > textWidth){//如果文字长度小于屏幕长度，自动补全，再重新设置
            checkTextWidth(text+"  "+text);
        }else {
            content = text;
            textWidth = mTextRect.width();//文字最终的长度
            textX = widthPixels;//将文字初始滚动坐标设定为右侧屏幕外部
            //文字垂直居中显示  参考 https://blog.csdn.net/sinat_26710701/article/details/70184252
            Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
            textY = heightPixels/2 - (fm.descent - (-fm.ascent + fm.descent)/2);
        }
    }

    public void setTextContent(String content){
        checkTextWidth(content);
    }

    //设置滚动速度
    public void setSpeed(int mode){
        switch (mode){
            case SPEED_SLOW:
                speed = 10;
                break;
            case SPEED_NORMAL:
                speed = 30;
                break;
            case SPEED_FAST:
                speed =  50;
                break;
            default:
                speed = 30;
                break;
        }
    }

    //设置文字颜色
    public void setTextColor(int color){
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //是否开始滚动，如果按住屏幕，则不自动滚动，直至手指抬起
        if (isStartScroll){
            if (textX < -textWidth){//如果文字滚动到负的文字长度，代表文字已经全部显示完成，重新设置X坐标为右屏幕外侧
                textX = widthPixels;
            }
            textX -= speed;
        }

        //绘制文字
        canvas.drawText(content,textX,textY,mPaint);

        //如果是自动滚动并且已经开始滚动，则重新进行下一轮绘制
        if (isStartScroll&&isAutoScro){
            invalidate();
        }

    }

    //根据手势判定操作
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = motionEvent.getX();
                //手指按住屏幕则停止滚动
                isStartScroll = false;
                //双击屏幕停止自动滚动
                if ((System.currentTimeMillis() - mLastClick)>500){
                    mLastClick = System.currentTimeMillis();
                }else {
                    switchAutoScroll();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                //根据手指滑动距离修改文字显示位置
                textX -= (mDownX - motionEvent.getX())*2;
                mDownX = motionEvent.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起重新开始滚动
                isStartScroll = true;
                invalidate();
                break;
        }
        return false;
    }

    public void switchAutoScroll(){
        isAutoScro = !isAutoScro;
    }

    //强制设置控件宽高为屏幕宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthPixels,heightPixels);
    }



}
