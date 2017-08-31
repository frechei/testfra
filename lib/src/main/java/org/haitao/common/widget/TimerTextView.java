package org.haitao.common.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Description  倒计时textview
 * Author by wangHaitao(a758277560@gmail.com)
 * Created  on 2016/8/1 14:05
 * Version V1.0   
 */
public class TimerTextView extends TextView{

    private CountDownListener countDownListener;
    CountDownTimer  mTimer;
    public TimerTextView(Context context) {
        this(context, null);
    }
    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setCountDownListener(CountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }
    /**
     * 开始倒计时
     * @param time
     * @param unit
     * @param endText
     */
    public void startPlay(int time, final String unit, final String endText){
        mTimer = new CountDownTimer(time*1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (countDownListener==null){
                    setText(millisUntilFinished / 1000 + unit);
                }else {
                    countDownListener.onTimmerPlay(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                mTimer=null;
                if (countDownListener==null){
                    setText(endText);
                }else {
                    countDownListener.onCountDownEnd();
                }

            }
        };
        mTimer.start();
    }
    /**
     * true 正在计时 false 没有
     * @return
     */
    public  boolean isTiming(){
        return  mTimer!=null;
    }

    public  interface CountDownListener{
        public  void  onCountDownEnd();
        public  void  onTimmerPlay(long millisUntilFinished);
    }
}
