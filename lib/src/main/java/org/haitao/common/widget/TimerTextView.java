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
     * 开始计时
     * @param millisInFuture
     * @param countDownInterval
     * @param countDownListener
     */
    public void startPlayMil(long millisInFuture, final long countDownInterval,CountDownListener countDownListener ){
        this.countDownListener = countDownListener;
        startPlayMil(millisInFuture,countDownInterval,null,null);
    }
    public void startPlay(long inFuture, final long countDownInterval, final String appendText, final String endText){
        startPlayMil(inFuture*1000,countDownInterval*1000,appendText,endText);
    }
    /**
     * 开始倒计时
     * @param millisInFuture
     * @param countDownInterval
     * @param appendText
     * @param endText
     */
    public void startPlayMil(long millisInFuture, final long countDownInterval, final String appendText, final String endText){
        mTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (countDownListener==null){
                    setText(millisUntilFinished / countDownInterval + appendText);
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

    /**
     * 取消计时
     */
    public void cancel(){
        if (null!=mTimer)
            mTimer.cancel();
    }

    public  interface CountDownListener{
        public  void  onCountDownEnd();
        public  void  onTimmerPlay(long millisUntilFinished);
    }
}
