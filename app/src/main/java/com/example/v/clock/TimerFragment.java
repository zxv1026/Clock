package com.example.v.clock;

import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by v on 2017/8/24.
 */

public class TimerFragment extends Fragment {
    private TimerTask timerTask = null;
    private TextView tvHour, tvMinute, tvSecond, tvMSecond;
    private int timeUsedInMsec;
    private Button btn_start_pause, btn_timing_reset;
    private static final int MSG_WHAT_SHOW_TIME = 1;
    private int Button_Status;
    private static final int STATUS_ONE =0;
    private static final int STATUS_TWO =1;
    private static final int STATUS_THREE =2;
    private Timer timer = new Timer();
    private Drawable drawable_start,drawable_pause,drawable_reset,drawable_record;
    //选择时间Dialog
    private TimePickerDialog timePickerDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dview = inflater.inflate(R.layout.ft_timer, container, false);

        tvHour = dview.findViewById(R.id.tvHour);
        tvMinute = dview.findViewById(R.id.tvMinute);
        tvSecond = dview.findViewById(R.id.tvSecond);
        tvMSecond = dview.findViewById(R.id.tvMSecond);

        btn_timing_reset = dview.findViewById(R.id.time);
        btn_start_pause = dview.findViewById(R.id.Start);


        drawable_start = ContextCompat.getDrawable(getContext(),R.drawable.kaishi);
        drawable_pause = ContextCompat.getDrawable(getContext(),R.drawable.zanting);
        drawable_reset = ContextCompat.getDrawable(getContext(),R.drawable.chongshe);
        drawable_record = ContextCompat.getDrawable(getContext(),R.drawable.jici);
        //开始和暂停
        btn_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Button_Status==STATUS_ONE){
                    //开始，但是没有定时过
                    Toast.makeText(getContext(),R.string.output,Toast.LENGTH_SHORT).show();
                }
                else if(Button_Status==STATUS_TWO){
                    //开始，定过时了
                    startTime();
                    btn_start_pause.setText(R.string.pause);
                    btn_start_pause.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_pause,null);
                    Button_Status=STATUS_THREE;

                }
                else{
                    //暂停
                    stopTimer();
                    btn_start_pause.setText(R.string.start);
                    btn_start_pause.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_start,null);
                    Button_Status=STATUS_TWO;
                }
            }
        });
        //定时和重置
        btn_timing_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Button_Status==STATUS_ONE){
                    //定时
                    showTime();
//                    if(timeUsedInMsec>0){
                    btn_timing_reset.setText(R.string.reset);
                    btn_timing_reset.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_reset,null);
                    Button_Status=STATUS_TWO;
//                    }
                }
                else{
                    //重置
                    timeUsedInMsec=0;
                    stopTimer();
                    btn_timing_reset.setText(R.string.timing);
                    btn_timing_reset.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_record,null);
                    btn_start_pause.setText(R.string.start);
                    btn_start_pause.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_start,null);
                    Button_Status=STATUS_ONE;
                }
            }
        });
        return dview;
    }
    private void showTime() {
        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvHour.setText(String.format("%02d",hourOfDay));
                tvMinute.setText(String.format("%02d",minute));
                timeUsedInMsec=hourOfDay*60*60*100+minute*60*100;
            }
        }, 0, 0, true);
        timePickerDialog.show();
//        timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void startTime(){
        if(timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(timeUsedInMsec>0){
                        timeUsedInMsec-- ;
                        handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
                    }
                    else {
                        handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
                        stopTimer();
//                        btn_timing_reset.performClick();
                    }
                }
            };
            timer.schedule(timerTask,9,9);
        }
    }
    private void stopTimer(){
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                // 如果消息匹配，则将相应时间计算后显示在相应TextView上
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(String.format("%02d",timeUsedInMsec/100/60/60));
                    tvMinute.setText(String.format("%02d",timeUsedInMsec/100/60%60));
                    tvSecond.setText(String.format("%02d",timeUsedInMsec/100%60));
                    tvMSecond.setText(String.format("%02d",timeUsedInMsec%100));
                    break;
                default:
                    break;
            }
        }
    };
}
