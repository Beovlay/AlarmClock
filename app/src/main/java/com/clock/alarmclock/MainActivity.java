    package com.clock.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_Manager;
    TimePicker alarm_timepicker;
    TextView update_time;
    Context context;
    PendingIntent pending_Intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        alarm_Manager =(AlarmManager) getSystemService(ALARM_SERVICE);

        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        update_time=(TextView) findViewById(R.id.update_time);

        final Calendar calendar = Calendar.getInstance();

        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        Button start_alarm = (Button) findViewById(R.id.alarm_on);

        start_alarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (hour > 12){
                    hour_string = String.valueOf(hour - 12);
                }
                if (minute < 10){
                    minute_string = "0" + String.valueOf(minute);
                }

                set_alarm_text("Будильник поставлений на: " + hour_string + ":" + minute_string);

                my_intent.putExtra("extra", "on");

                pending_Intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_Manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_Intent);



            }
        });

        Button stop_alarm = (Button) findViewById(R.id.alarm_off);

        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_alarm_text("Будильник виключений!");

                alarm_Manager.cancel(pending_Intent);

                my_intent.putExtra("extra", "off");

                sendBroadcast(my_intent);
            }
        });


    }

    private void set_alarm_text(String output) {
        update_time.setText(output);
    }
}
