package com.dsm.platform.util;

import android.graphics.Paint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ViewUtil {

    private ViewUtil() {
        throw new UnsupportedOperationException("Can not be instantiated");
    }

    /*
	 * 避免短时间内多处点击事件(消抖)
	 * 当响应了一次点击事件后，需得经过interval(毫秒)时间后才能再次响应点击事件
	 */
    public static void noMoreClick(final View view, long interval) {
        view.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, interval);
    }

    /**
     * 实现一次退格，则删除所有字符
     */
    public static class ClearTextWatcher implements TextWatcher {
        private final EditText mEditText;
        private boolean isReduce = false;

        public ClearTextWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            isReduce = after < count;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (isReduce) {
                mEditText.setText("");
            }
        }
    }

    /**
     * 简单的监听EditText文本变化之后
     */
    public abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public abstract void afterTextChanged(Editable s);
    }

    /**
     * 判断TextView中的文本是否溢出
     */
    public static boolean isOverFlowed(TextView tv) {
        int availableWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight();
        Paint textViewPaint = tv.getPaint();
        float textWidth = textViewPaint.measureText(tv.getText().toString());
        return textWidth > availableWidth * tv.getLineCount();
    }
}
