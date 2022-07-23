package live.videosdk.rtc.android.java.Lrc_view.impl;

import android.util.Log;

/**
 * the lyrics
 * Include the time and content of the lyrics
 */
public class LrcRow implements Comparable<LrcRow>{
    public final static String TAG = "LrcRow";

    public String startTimeString;

    /**
     * change the time of the lyrics to long
     */
    public long startTime;
    public long endTime;

    /** The content of this line of lyrics */
    public String content;

    
    public LrcRow(){}

    public String getStartTimeString() {
        return startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LrcRow{" +
                "startTimeString='" + startTimeString + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", content='" + content + '\'' +
                '}';
    }

    /**
     * When sorting, sort by the date of the lyrics
     */
    public int compareTo(LrcRow another) {
        return (int)(this.startTime - another.startTime);
    }
}