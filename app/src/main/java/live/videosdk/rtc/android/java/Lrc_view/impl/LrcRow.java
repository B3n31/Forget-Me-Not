package live.videosdk.rtc.android.java.Lrc_view.impl;

import android.util.Log;

/**
 * @Author: tingyuzhang
 * @Time: 2022-07-15 2:56 afternoon
 * @Team: Raging Coders
 * @Description: Contain content of each line of lyrics
 */

/**
 * the lyrics
 * Include the time and content of the lyrics
 */
public class LrcRow implements Comparable<LrcRow>{
    /**
     * The constant TAG.
     */
    public final static String TAG = "LrcRow";

    /**
     * The Start time string.
     */
    public String startTimeString;

    /**
     * change the time of the lyrics to long
     */
    public long startTime;
    /**
     * The End time.
     */
    public long endTime;

    /**
     * The content of this line of lyrics
     */
    public String content;


    /**
     * Instantiates a new Lrc row.
     */
    public LrcRow(){}

    /**
     * Gets start time string.
     *
     * @return the start time string
     */
    public String getStartTimeString() {
        return startTimeString;
    }

    /**
     * Sets start time string.
     *
     * @param startTimeString the start time string
     */
    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
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