package com.common.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.common.library.R;

import java.lang.reflect.Field;


public class StrokeTextView extends AppCompatTextView {
    TextPaint m_TextPaint;
    int mInnerColor;
    int mOuterColor;
    float outerBorderSize;
//    private Typeface tf;

    public StrokeTextView(Context context, int outerColor, int innnerColor) {
        super(context);
        AssetManager mgr = context.getAssets();//得到AssetManager
//        tf = Typeface.createFromAsset(mgr, "ttfmain.ttf");//根据路径得到Typeface

        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
        float outerBorderSize = 5f;
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_TextPaint = this.getPaint();
        AssetManager mgr = context.getAssets();//得到AssetManager
//        tf = Typeface.createFromAsset(mgr, "ttfmain.ttf");//根据路径得到Typeface
        //获取自定义的XML属性名称
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        //获取对应的属性值
        this.mInnerColor = a.getColor(R.styleable.StrokeTextView_innnerColor, 0xffffff);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor, 0xffffff);
        this.outerBorderSize = a.getFloat(R.styleable.StrokeTextView_outerBorderSize, 5f);

    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle, int outerColor, int innnerColor) {
        super(context, attrs, defStyle);
        AssetManager mgr = context.getAssets();//得到AssetManager
//        tf = Typeface.createFromAsset(mgr, "ttfmain.ttf");//根据路径得到Typeface
        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
        float outerBorderSize = 5f;
    }

    private boolean m_bDrawSideLine = true; // 默认采用描边

    @Override
    public void setText(CharSequence text, BufferType type) {
//        setTypeface(tf);
        super.setText(text, type);
    }

    /**
     *
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (m_bDrawSideLine) {
            // 描外层
            // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
            setTextColorUseReflection(mOuterColor);
            m_TextPaint.setStrokeWidth(outerBorderSize); // 描边宽度
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE); // 描边种类
            m_TextPaint.setFakeBoldText(true); // 外层text采用粗体
            m_TextPaint.setShadowLayer(1, 0, 0, 0); // 字体的阴影效果，可以忽略
            super.onDraw(canvas);

            // 描内层，恢复原先的画笔

            // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
            setTextColorUseReflection(mInnerColor);
            m_TextPaint.setStrokeWidth(0);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            m_TextPaint.setFakeBoldText(false);
            m_TextPaint.setShadowLayer(0, 0, 0, 0);

        }
        super.onDraw(canvas);
    }

    /**
     * 使用反射的方法进行字体颜色的设置
     */
    @SuppressLint("SoonBlockedPrivateApi")
    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }


}
