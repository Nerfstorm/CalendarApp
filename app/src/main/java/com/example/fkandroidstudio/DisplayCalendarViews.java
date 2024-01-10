package com.example.fkandroidstudio;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

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

        float rTimeRange = 0f,rEndTime = 0f;
        try {
            rTimeRange = ((Number) timeRangeDiv).floatValue();
        } catch (ClassCastException e) {
            System.out.println("Error DisplayCalendarViews: timeRangeDiv does not contain numeric value");
        }
        try {
            rEndTime = ((Number) endTimeDiv).floatValue();
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
            float finalReceivedEndTime = rEndTime;
            float finalReceivedTimeRange = rTimeRange;
            targetLayout.post(() -> {

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = (int) (finalReceivedEndTime * MainActivity.weekLayoutHeight);
                layoutParams.height = (int) (finalReceivedTimeRange * MainActivity.weekLayoutHeight);
                layoutParams.topMargin = 0;
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                Log.i("displayEvents",Integer.toString(layoutParams.bottomMargin));

                customView.setLayoutParams(layoutParams);
                targetLayout.addView(customView);
            });
        } else Log.e("DisplayCalendarViews", "Target layout is null for week day: " + weekDay);

    }

}