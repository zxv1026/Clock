package com.example.v.clock;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by v on 2017/8/24.
 */

public class StopwatchFragment extends Fragment {
    private Button btn_start_pause, btn_reset_record;
    private ListView lst_cal;
    private Timer timer = new Timer();
    private TimerTask timerTask =null;
    private TextView tvHour, tvMinute, tvSecond, tvMSecond;
    private int timeUsedInMsec;
    private static final int MSG_WHAT_SHOW_TIME = 1;
    private int Button_Status;
    private static final int STATUS_START=0;
    private static final int STATUS_RESET=0;
    private static final int STATUS_PAUSE=1;
    private static final int STATUS_RECORD=1;
    private ImageView imageView;
    private int num;
    private String runningtime,runningtime_msecond,runningtime_hour,runningtime_minute,runningtime_second;
    private Drawable drawable_start,drawable_pause,drawable_reset,drawable_record;
    private TimeAdapter adapter;
    private List<HashMap<String,Object>> list = new ArrayList<>();


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ft_stopwatch, container, false);
        tvHour = view.findViewById(R.id.tvHour);
        tvMinute = view.findViewById(R.id.tvMinute);
        tvSecond = view.findViewById(R.id.tvSecond);
        tvMSecond = view.findViewById(R.id.tvMSecond);

        btn_start_pause = view.findViewById(R.id.btn_start_pause);
        btn_reset_record = view.findViewById(R.id.btn_reset_record);

        imageView = view.findViewById(R.id.image_sz);

        drawable_start = ContextCompat.getDrawable(getContext(),R.drawable.kaishi);
        drawable_pause = ContextCompat.getDrawable(getContext(),R.drawable.zanting);
        drawable_reset = ContextCompat.getDrawable(getContext(),R.drawable.chongshe);
        drawable_record = ContextCompat.getDrawable(getContext(),R.drawable.jici);

        lst_cal = view.findViewById(R.id.lvWatchTimeList);
        adapter = new TimeAdapter(getContext(), list);

        //重制和记录
        btn_reset_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Button_Status==STATUS_RESET){
                    stopTimer();
                    timeUsedInMsec=0;
                    list.clear();
                    num=0;
                    imageView.setVisibility(View.VISIBLE);
                } else if(Button_Status==STATUS_RECORD){
                    num++;
                    adapter.getrunningtime();
                    Map<String, Object> map = new HashMap<>();
                    map.put("tv_time",runningtime);
                    map.put("tv_number","NO:"+num);
                    list.add(0,(HashMap<String, Object>) map);//1在最下面
//                    list.add((HashMap<String, Object>) map);//1在最上面
                    adapter.notifyDataSetChanged();
//                    adapter = new TimeAdapter(getApplicationContext(), list);
                    lst_cal.setAdapter(adapter);
                    imageView.setVisibility(View.GONE);
                }

            }

        });
        //开始和暂停
        btn_start_pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Button_Status==STATUS_START){
                    startTimer();
                    btn_start_pause.setText(R.string.pause);
                    btn_reset_record.setText(R.string.record);
                    btn_start_pause.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_pause,null);
                    btn_reset_record.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_record,null);
                    Button_Status=STATUS_PAUSE;
                } else if(Button_Status==STATUS_PAUSE){
                    stopTimer();
                    btn_start_pause.setText(R.string.start);
                    btn_reset_record.setText(R.string.reset);
                    btn_start_pause.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_start,null);
                    btn_reset_record.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_reset,null);
                    Button_Status=STATUS_START;
                }
            }
        });
        return view;
    }

    // 开始
    private void startTimer() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    timeUsedInMsec++;
                    // 向Handler发送消息
                    handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
                }
            };
            timer.schedule(timerTask, 8, 10);
        }
    }
    // 结束
    private void stopTimer(){
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        // 向Handler发送消息
        handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
    }

    public class TimeAdapter extends BaseAdapter {

        private List<HashMap<String,Object>> list = null;
        private Context mContext;

        public TimeAdapter(Context mContext, List<HashMap<String,Object>> list) {
            this.mContext = mContext;
            this.list = list;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int position) {
            return this.list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_litem, null);
                viewHolder.tv_number = convertView.findViewById(R.id.itemnum);
                viewHolder.tv_time = convertView.findViewById(R.id.itemtime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_number.setText((String)list.get(position).get("tv_number"));
            viewHolder.tv_time.setText((String)list.get(position).get("tv_time"));

            return convertView;
        }
        public String getrunningtime(){
            if(timeUsedInMsec/100/60/60<10){
                runningtime_hour="0"+timeUsedInMsec/100/60/60;
            }else{
                runningtime_hour=timeUsedInMsec/100/60/60+"";
            }
            if(timeUsedInMsec/100/60%60<10){
                runningtime_minute="0"+timeUsedInMsec/100/60%60;
            }else{
                runningtime_minute=timeUsedInMsec/100/60%60+"";
            }
            if(timeUsedInMsec/100%60<10){
                runningtime_second="0"+timeUsedInMsec/100%60;
            }else{
                runningtime_second=timeUsedInMsec/100%60+"";
            }
            if(timeUsedInMsec%100<10){
                runningtime_msecond="0"+timeUsedInMsec%100;
            }else{
                runningtime_msecond=timeUsedInMsec%100+"";
            }
            runningtime=runningtime_hour+":"+runningtime_minute+ ":"+runningtime_second+"."+runningtime_msecond;
            return runningtime;
        }
        class ViewHolder {
            TextView tv_number;
            TextView tv_time;
        }

    }

}
