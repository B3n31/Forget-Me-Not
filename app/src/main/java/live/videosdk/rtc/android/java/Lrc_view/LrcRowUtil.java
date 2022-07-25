package live.videosdk.rtc.android.java.Lrc_view;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

public class LrcRowUtil {
    public final static String TAG = "LrcRow";

     //load all lyrics into lyrics builder
    public static List<LrcRow> createRows(String standardLrcLine) {
        try {
            if (standardLrcLine.indexOf("[") != 0 || standardLrcLine.indexOf("]") != 9) {
                return null;
            }
            int lastIndexOfRightBracket = standardLrcLine.lastIndexOf("]");
            String content = standardLrcLine.substring(lastIndexOfRightBracket + 1, standardLrcLine.length());

             //read time
            String times = standardLrcLine.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
            //use ‘-’ separate mins and seconds
            String arrTimes[] = times.split("-");
            List<LrcRow> listTimes = new ArrayList<LrcRow>();
            for (int i = 0; i < arrTimes.length; i++) {
                String temp = arrTimes[i];

                if (temp.trim().length() == 0) {
                    continue;
                }

                LrcRow lrcRow = new LrcRow();
                lrcRow.setContent(content);
                lrcRow.setStartTimeString(temp);
                long startTime = timeConvert(temp);
                lrcRow.setStartTime(startTime);
                listTimes.add(lrcRow);
            }
            return listTimes;
        } catch (Exception e) {
            Log.e(TAG, "createRows exception:" + Log.getStackTraceString(e));
            return null;
        }
    }

    private static long timeConvert(String timeString) {
        timeString = timeString.replace('.', ':');
        String[] times = timeString.split(":");
        return Integer.valueOf(times[0]) * 60 * 1000 +//minutes
                Integer.valueOf(times[1]) * 1000 +//seconds
                Integer.valueOf(times[2]);//millisecond
    }
}
