package org.haitao.common.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class BackKeyEditText extends EditText {

    public BackKeyEditText(Context context) {
        super(context);
    }

    public BackKeyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackKeyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnBackKeyListener {
        void back(TextView textView);
    }


    public void setOnBackKeyListener(OnBackKeyListener onBackKeyListener) {
        this.onBackKeyListener = onBackKeyListener;
    }

    private OnBackKeyListener onBackKeyListener;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBackKeyListener != null) {
                onBackKeyListener.back(this);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

}
