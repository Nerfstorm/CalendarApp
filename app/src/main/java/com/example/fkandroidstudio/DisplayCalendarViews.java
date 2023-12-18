package com.example.fkandroidstudio;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import java.util.Date;

public class DisplayCalendarViews extends RelativeLayout {
    int[] idArray;
    int[] colorArray;
    Context context;

    public DisplayCalendarViews(Context recvContext) {
        super(recvContext);
        this.context = recvContext;
        this.idArray = new int[]{0, R.id.monLayout, R.id.tueLayout, R.id.wedLayout, R.id.thuLayout, R.id.friLayout, R.id.satLayout, R.id.sunLayout};
        this.colorArray = new int[]{R.color.cal_purple,R.color.cal_green,R.color.cal_magenta,R.color.cal_red,R.color.cal_yellow};
    }

    public void createCustomView(Object weekDay, Object timeRangeDiv, Object endTimeDiv,int colorIndex) {

        float recvTimeRange = 0f,recvEndTime = 0f;
        try {
            recvTimeRange = ((Number) timeRangeDiv).floatValue();
        } catch (ClassCastException e) {
            System.out.println("Error DisplayCalendarViews: timeRangeDiv does not contain numeric value");
        }
        try {
            recvEndTime = ((Number) endTimeDiv).floatValue();
        } catch (ClassCastException e) {
            System.out.println("Error DisplayCalendarViews: endTimeDiv does not contain numeric value");
        }



        // Create a simple view programmatically
        View customView = new View(context);
        customView.setBackgroundColor(ContextCompat.getColor(context, colorArray[colorIndex]));
        customView.setAlpha(0.3f);

        // Get the target layout using the provided weekDay parameter
        ViewGroup targetLayout = ((MainActivity) context).findViewById(idArray[(int) weekDay]);

        // Null check for the target layout
        if (targetLayout != null) {
            float finalRecvEndTime = recvEndTime;
            float finalRecvTimeRange = recvTimeRange;
            targetLayout.post(() -> {

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = (int) (finalRecvEndTime * MainActivity.weekLayoutHeight);
                layoutParams.height = (int) (finalRecvTimeRange * MainActivity.weekLayoutHeight);
                layoutParams.topMargin = 0;
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                Log.i("displayev",Integer.toString(layoutParams.bottomMargin));

                customView.setLayoutParams(layoutParams);
                targetLayout.addView(customView);
            });
        } else Log.e("DisplayCalendarViews", "Target layout is null for week day: " + weekDay);

    }

}