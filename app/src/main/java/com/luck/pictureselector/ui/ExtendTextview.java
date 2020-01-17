package com.luck.pictureselector.ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatTextView;

import com.luck.pictureselector.R;



public class ExtendTextview extends AppCompatTextView {

    // 最大行数
    private static final int MAX_LINES_ON_SHRINK = 2;
    // 收起状态
    public static final int STATE_SHRINK = 0;
    // 展开状态
    public static final int STATE_EXPAND = 1;
    // 当前状态
    private int mCurrState = STATE_SHRINK;
    private BufferType mBufferType = BufferType.NORMAL;
    // 文本布局
    private Layout mLayout;
    // 可展示文本的宽度
    private int mLayoutWidth = 0;


    private String mToShrinkHint = "收起";

    private int maxLines;
    // 文本内容
    private CharSequence mOrigText;
    // 白色，粗体的展开收起样式
    private StyleSpan whiteStyleSpan = new StyleSpan(Typeface.BOLD);



    public ExtendTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExtendTextview);
        maxLines = typedArray.getInt(R.styleable.ExtendTextview_max_lines, MAX_LINES_ON_SHRINK);
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        whiteStyleSpan.updateMeasureState(textPaint);
        setOnClickListener(new ExpandableClickListener());
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getLayout() != null && getLayout().getWidth() != 0) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    // 第一次展示的时候更新文本
                    setTextInternal(getNewTextByConfig(), mBufferType);
                }
            }
        });
    }

    private Layout getValidLayout() {
        return mLayout != null ? mLayout : getLayout();
    }

    /**
     * 展示和收起事件处理
     */
    private void toggle() {
        switch (mCurrState) {
            case STATE_SHRINK:
                mCurrState = STATE_EXPAND;
                break;
            case STATE_EXPAND:
                mCurrState = STATE_SHRINK;
                break;
        }
        setTextInternal(getNewTextByConfig(), mBufferType);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mOrigText = text;
        mBufferType = type;
        setTextInternal(getNewTextByConfig(), type);
    }



    private void setTextInternal(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    /**
     * 通过此方法计算展示的宽度，通过不同状态来加后缀的文本
     *
     * @return 所有要展示的文本
     */
    private CharSequence getNewTextByConfig() {
        // 最终要展示的内容（包括前缀 + 文本）
        CharSequence mTargetText = mOrigText;

        mLayout = getLayout();
        if (mLayout != null) {
            mLayoutWidth = mLayout.getWidth();
        }

        if (mLayoutWidth <= 0) {
            if (getWidth() == 0) {
                return mTargetText;
            } else {
                mLayoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            }
        }

        TextPaint mTextPaint = getPaint();

        int mTextLineCount;
        int mMaxLinesOnShrink = maxLines;
        switch (mCurrState) {
            case STATE_SHRINK: {
                //通过DynamicLayout来确定位置
                mLayout = new DynamicLayout(mTargetText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL,
                        1.0f, 0.0f, false);
                mTextLineCount = mLayout.getLineCount();
                if (mTextLineCount <= mMaxLinesOnShrink) {
                    return getLessTwoLineText(mTargetText);
                }
                int indexEnd = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);
                int indexStart = getValidLayout().getLineStart(mMaxLinesOnShrink - 1);
                String mEllipsisHint = "...";
                String mToExpandHint = "展开";
                int indexEndTrimmed = indexEnd - mEllipsisHint.length() - mToExpandHint.length();
                if (indexEndTrimmed <= indexStart) {
                    indexEndTrimmed = indexEnd;
                }
                int remainWidth =
                        getValidLayout().getWidth() - (int) (mTextPaint.measureText(mTargetText.subSequence(indexStart, indexEndTrimmed).toString()) + 0.5);
                float widthTailReplaced = mTextPaint.measureText(mEllipsisHint + mToExpandHint);
                int indexEndTrimmedRevised = indexEndTrimmed;
                //确定 除去...展开剩余文本的位置
                if (remainWidth > widthTailReplaced) {
                    int extraOffset = 0;
                    int extraWidth = 0;
                    while (remainWidth > widthTailReplaced + extraWidth) {
                        extraOffset++;
                        if (indexEndTrimmed + extraOffset <= mTargetText.length()) {
                            extraWidth = (int) (mTextPaint.measureText(mTargetText.subSequence(indexEndTrimmed,
                                    indexEndTrimmed + extraOffset).toString()) + 0.5);
                        } else {
                            break;
                        }
                    }
                    indexEndTrimmedRevised += extraOffset - 2;
                } else {
                    int extraOffset = 0;
                    int extraWidth = 0;
                    while (remainWidth + extraWidth < widthTailReplaced) {
                        extraOffset--;
                        if (indexEndTrimmed + extraOffset > indexStart) {
                            extraWidth =
                                    (int) (mTextPaint.measureText(mTargetText.subSequence(indexEndTrimmed + extraOffset, indexEndTrimmed).toString()) + 0.5);
                        } else {
                            break;
                        }
                    }
                    indexEndTrimmedRevised += extraOffset;
                }

                String fixText = removeEndLineBreak(mTargetText.subSequence(0, indexEndTrimmedRevised));
                SpannableStringBuilder ssbShrink = new SpannableStringBuilder(fixText);
                ssbShrink.append(mEllipsisHint);
                ssbShrink.append(mToExpandHint);

                // 展开文本
                int start = ssbShrink.length() - mToExpandHint.length();
                ssbShrink.setSpan(whiteStyleSpan, start <= 0 ? 0 : start, ssbShrink.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                setOnTouchListener(new TouchTextView(ssbShrink));
                return ssbShrink;
            }
            case STATE_EXPAND: {
                mLayout = new DynamicLayout(mTargetText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL,
                        1.0f, 0.0f, false);
                mTextLineCount = mLayout.getLineCount();
                if (mTextLineCount <= mMaxLinesOnShrink) {
                    return getLessTwoLineText(mTargetText);
                }

                SpannableStringBuilder ssbExpand =
                        new SpannableStringBuilder(mTargetText);
                if (!TextUtils.isEmpty(mToShrinkHint)) {
                    ssbExpand.append(" ").append(mToShrinkHint);
                }
                // 收起文本
                ssbExpand.setSpan(whiteStyleSpan, ssbExpand.length() - mToShrinkHint.length(), ssbExpand.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                setOnTouchListener(new TouchTextView(ssbExpand));
                return ssbExpand;
            }
        }
        return mTargetText;
    }

    /**
     * 少于给定的行数是所需要做的处理
     */
    private CharSequence getLessTwoLineText(CharSequence mTargetText) {
        SpannableStringBuilder ssbShrink = new SpannableStringBuilder(mTargetText);
        setOnTouchListener(new TouchTextView(ssbShrink));
        return ssbShrink;
    }

    private String removeEndLineBreak(CharSequence text) {
        String str = text.toString();
        while (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 展开和收起事件
     */
    private class ExpandableClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            toggle();
        }
    }

    /**
     * 可以区分整体的点击和前缀的点击
     */
    public static class TouchTextView implements View.OnTouchListener {
        Spannable spannable;

        public TouchTextView(Spannable spannable) {
            this.spannable = spannable;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (!(v instanceof AppCompatTextView)) {
                return false;
            }
            AppCompatTextView textView = (AppCompatTextView) v;
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= textView.getTotalPaddingLeft();
                y -= textView.getTotalPaddingTop();

                x += textView.getScrollX();
                y += textView.getScrollY();

                Layout layout = textView.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    /*if (action == MotionEvent.ACTION_UP) {
                        // 前缀事件ClickableSpan的点击处理
                        link[0].onClick(textView);
                    } else*/
                    if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(spannable, spannable.getSpanStart(link[0]),
                                spannable.getSpanEnd(link[0]));

                    }
                    return true;
                } else {
                    Selection.removeSelection(spannable);
                    return false;
                }
            }
            return false;
        }
    }

    public void setToShrinkHint(String toShrinkHint) {
        this.mToShrinkHint = toShrinkHint;
    }

    public BufferType getBufferType() {
        return mBufferType;
    }
}
