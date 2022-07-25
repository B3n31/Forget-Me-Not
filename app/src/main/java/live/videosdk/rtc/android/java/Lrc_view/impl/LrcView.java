package live.videosdk.rtc.android.java.Lrc_view.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import live.videosdk.rtc.android.java.Lrc_view.ILrcView;
import live.videosdk.rtc.android.java.Lrc_view.ILrcViewListener;

import java.util.List;

/**
 * Design for LrcView
 */
public class LrcView extends View implements ILrcView {

    public final static String TAG = "LrcView";
    /**
     * Regular lyrics mode
     */
    public final static int DISPLAY_MODE_NORMAL = 0;
    /**
     * Dragging lyrics mode
     */
    public final static int DISPLAY_MODE_SEEK = 1;
    /**
     * Zoom in/out lyrics
     */
    public final static int DISPLAY_MODE_SCALE = 2;
    /**
     * Display lyrics
     */
    private int mDisplayMode = DISPLAY_MODE_NORMAL;

    /**
     * lyrics row
     */
    private List<LrcRow> mLrcRows;
    /**
     * Min movement for dragging
     */
    private int mMinSeekFiredOffset = 10;

    /**
     * Numbers of highlight rows
     */
    private int mHighLightRow = 0;
    /**
     * Highlight color
     */
    private int mHighLightRowColor = Color.YELLOW;
    /**
     * non-Highlight color
     */
    private int mNormalRowColor = Color.WHITE;

    /**
     * Color of the line below
     **/
    private int mSeekLineColor = Color.CYAN;
    /**
     * Highlight color when dragging
     **/
    private int mSeekLineTextColor = Color.CYAN;
    /**
     * Highlight row text size
     **/
    private int mSeekLineTextSize = 30;
    /**
     * Highlight row text min size
     **/
    private int mMinSeekLineTextSize = 15;
    /**
     * Highlight row text max size
     **/
    private int mMaxSeekLineTextSize = 35;

    /**
     * default text size
     **/
    private int mLrcFontSize = 130;    // font size of lrc
    /**
     * text min size
     **/
    private int mMinLrcFontSize = 110;
    /**
     * text max size
     **/
    private int mMaxLrcFontSize = 150;

    /**
     * The spacing between two lines of lyrics
     **/
    private int mPaddingY = 100;
    /**
     * The starting position of a line under the highlight when dragging
     **/
    private int mSeekLinePaddingX = 0;

    private ILrcViewListener mLrcViewListener;

    /**
     * When there's no lyrics
     **/
    private String mLoadingLrcTip = "Waiting for the song";

    private Paint mPaint;

    /**
     * Current playing time
     */
    long currentMillis;

    /**
     * Regular Highlight
     */
    private int MODE_HIGH_LIGHT_NORMAL = 0;
    /**
     * Karaoke mode
     */
    private int MODE_HIGH_LIGHT_KARAOKE = 1;

    private int mode = MODE_HIGH_LIGHT_KARAOKE;


    public LrcView(Context context, AttributeSet attr) {
        super(context, attr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);
    }

    public void setListener(ILrcViewListener listener) {
        mLrcViewListener = listener;
    }

    public void setLoadingTipText(String text) {
        mLoadingLrcTip = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight(); // height of this view
        final int width = getWidth(); // width of this view
        //When no lyrics
        if (mLrcRows == null || mLrcRows.size() == 0) {
            if (mLoadingLrcTip != null) {
                // draw tip when no lrc.
                mPaint.setColor(mHighLightRowColor);
                mPaint.setTextSize(mLrcFontSize);
                mPaint.setTextAlign(Align.CENTER);
                canvas.drawText(mLoadingLrcTip, width / 2, height / 2 - mLrcFontSize, mPaint);
            }
            return;
        }

        int rowY = 0; // vertical point of each row.
        final int rowX = width / 2;
        int rowNum = 0;


        // Highlight the lyric that is being highlighted
        int highlightRowY = height / 2 - mLrcFontSize;

        if (mode == MODE_HIGH_LIGHT_KARAOKE){
            // Karaoke mode is highlighted word for word
            drawKaraokeHighLightLrcRow(canvas, width, rowX, highlightRowY);
        } else {
            // Normal highlight
            drawHighLrcRow(canvas, height, rowX, highlightRowY);
        }

        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            // Draw a line below the highlighted line
            mPaint.setColor(mSeekLineColor);
            //The line x coordinates from 0 to the screen width y coordinates are between the highlight lyrics and the next line of lyrics
            canvas.drawLine(mSeekLinePaddingX, highlightRowY + mPaddingY, width - mSeekLinePaddingX, highlightRowY + mPaddingY, mPaint);

            // The time to highlight the lyrics
            mPaint.setColor(mSeekLineTextColor);
            mPaint.setTextSize(mSeekLineTextSize);
            mPaint.setTextAlign(Align.LEFT);
            canvas.drawText(mLrcRows.get(mHighLightRow).startTimeString, 0, highlightRowY, mPaint);
        }

        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Align.CENTER);
        rowNum = mHighLightRow - 1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;

        //Draw all the words above the line that is playing
        while (rowY > -mLrcFontSize && rowNum >= 0) {
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY -= (mPaddingY + mLrcFontSize);
            rowNum--;
        }

        rowNum = mHighLightRow + 1;
        rowY = highlightRowY + mPaddingY + mLrcFontSize;

        //Draw all of the lyrics that can be displayed below the line that is being played
        while (rowY < height && rowNum < mLrcRows.size()) {
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum++;
        }

    }

    private void drawKaraokeHighLightLrcRow(Canvas canvas, int width, int rowX, int highlightRowY) {
        LrcRow highLrcRow = mLrcRows.get(mHighLightRow);
        String highlightText = highLrcRow.content;

        // Regular color
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Align.CENTER);
        canvas.drawText(highlightText, rowX, highlightRowY, mPaint);

        // Highlight color
        int highLineWidth = (int) mPaint.measureText(highlightText);
        int leftOffset = (width - highLineWidth) / 2;
        long start = highLrcRow.getStartTime();
        long end = highLrcRow.getEndTime();
        // Highlight color hight
        int highWidth = (int) ((currentMillis - start) * 1.0f / (end - start) * highLineWidth);
        if (highWidth > 0) {
            //bitmap for highlight
            mPaint.setColor(mHighLightRowColor);
            Bitmap textBitmap = Bitmap.createBitmap(highWidth, highlightRowY + mPaddingY, Bitmap.Config.ARGB_8888);
            Canvas textCanvas = new Canvas(textBitmap);
            textCanvas.drawText(highlightText, highLineWidth / 2, highlightRowY, mPaint);
            canvas.drawBitmap(textBitmap, leftOffset, 0, mPaint);
        }
    }

    private void drawHighLrcRow(Canvas canvas, int height, int rowX, int highlightRowY) {
        String highlightText = mLrcRows.get(mHighLightRow).content;
        mPaint.setColor(mHighLightRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Align.CENTER);
        canvas.drawText(highlightText, rowX, highlightRowY, mPaint);
    }

    /**
     *Set the number of line of lyrics to be highlighted
     **/
    public void seekLrc(int position, boolean cb) {
        if (mLrcRows == null || position < 0 || position > mLrcRows.size()) {
            return;
        }
        LrcRow lrcRow = mLrcRows.get(position);
        mHighLightRow = position;
        invalidate();
        //After dragging
        if (mLrcViewListener != null && cb) {
            //Call the onLrcSeeked method to move the music player to the position where the lyrics are highlighted
            mLrcViewListener.onLrcSought(position, lrcRow);
        }
    }

    private float mLastMotionY;
    /**
     * The coordinates of the first finger
     **/
    private PointF mPointerOneLastMotion = new PointF();
    /**
     * The coordinates of the second finger
     **/
    private PointF mPointerTwoLastMotion = new PointF();

    private boolean mIsFirstMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            //finger down
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "down,mLastMotionY:" + mLastMotionY);
                mLastMotionY = event.getY();
                mIsFirstMove = true;
                invalidate();
                break;
            //finger moving
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    Log.d(TAG, "two move");
                    doScale(event);
                    return true;
                }
                Log.d(TAG, "one move");
                // single pointer mode ,seek
                //If you press both fingers at the same time, zoom in and out, lift one finger and move the other finger without leaving the screen, nothing is done
                if (mDisplayMode == DISPLAY_MODE_SCALE) {
                    //if scaling but pointer become not two ,do nothing.
                    return true;
                }
                //One finger moving up/down, dragging
                doSeek(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mDisplayMode == DISPLAY_MODE_SEEK) {
                    //Highlight the lyrics as your finger is lifted and play from there
                    seekLrc(mHighLightRow, true);
                }
                mDisplayMode = DISPLAY_MODE_NORMAL;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * Zoom in/out part
     */
    private void doScale(MotionEvent event) {
        //If Dragging mode:
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            //If you press with one finger, scroll the lyrics up and down, and then press the other finger to change the lyrics mode from drag to zoom
            mDisplayMode = DISPLAY_MODE_SCALE;
            Log.d(TAG, "change mode from DISPLAY_MODE_SEEK to DISPLAY_MODE_SCALE");
            return;
        }
        // two pointer mode , scale font
        if (mIsFirstMove) {
            mDisplayMode = DISPLAY_MODE_SCALE;
            invalidate();
            mIsFirstMove = false;
            //The x and y coordinates of the two fingers
            setTwoPointerLocation(event);
        }
        //Gets the scale to which the size of the lyrics should be scaled
        int scaleSize = getScale(event);
        Log.d(TAG, "scaleSize:" + scaleSize);
        //if not equal 0, re-draw the lrcView
        if (scaleSize != 0) {
            setNewFontSize(scaleSize);
            invalidate();
        }
        setTwoPointerLocation(event);
    }

    /**
     * Dragging part
     */
    private void doSeek(MotionEvent event) {
        float y = event.getY();
        float offsetY = y - mLastMotionY; // The difference between the y coordinate of the first press and the y coordinate of the current moving finger position
        // If difference < 10, do nothing
        if (Math.abs(offsetY) < mMinSeekFiredOffset) {
            return;
        }
        //change the mode to dragging
        mDisplayMode = DISPLAY_MODE_SEEK;
        int rowOffset = Math.abs((int) offsetY / mLrcFontSize); //The number of lines to scroll

        Log.d(TAG, "move to new hightlightrow : " + mHighLightRow + " offsetY: " + offsetY + " rowOffset:" + rowOffset);

        if (offsetY < 0) {
            //Fingers move up, lyrics scroll down
            mHighLightRow += rowOffset;
        } else if (offsetY > 0) {
            //Fingers move down, lyrics scroll up
            mHighLightRow -= rowOffset;
        }
        mHighLightRow = Math.max(0, mHighLightRow);
        mHighLightRow = Math.min(mHighLightRow, mLrcRows.size() - 1);
        // If the number of lines to scroll is greater than 0, redraw LrcView
        if (rowOffset > 0) {
            mLastMotionY = y;
            invalidate();
        }
    }

    /**
     * Sets the x and y coordinates of the current two fingers
     */
    private void setTwoPointerLocation(MotionEvent event) {
        mPointerOneLastMotion.x = event.getX(0);
        mPointerOneLastMotion.y = event.getY(0);
        mPointerTwoLastMotion.x = event.getX(1);
        mPointerTwoLastMotion.y = event.getY(1);
    }

    /**
     * Set the font size after scaling
     */
    private void setNewFontSize(int scaleSize) {
        mLrcFontSize += scaleSize;
        mLrcFontSize = Math.max(mLrcFontSize, mMinLrcFontSize);
        mLrcFontSize = Math.min(mLrcFontSize, mMaxLrcFontSize);

        mSeekLineTextSize += scaleSize;
        mSeekLineTextSize = Math.max(mSeekLineTextSize, mMinSeekLineTextSize);
        mSeekLineTextSize = Math.min(mSeekLineTextSize, mMaxSeekLineTextSize);
    }

    /**
     * Gets the scale to which the size of the lyrics should be scaled
     */
    private int getScale(MotionEvent event) {
        Log.d(TAG, "scaleSize getScale");
        float x0 = event.getX(0);
        float y0 = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);

        float maxOffset = 0; // max offset between x or y axis,used to decide scale size

        boolean zooMin = false;
        //The x-coordinate difference between the first two fingers
        float oldXOffset = Math.abs(mPointerOneLastMotion.x - mPointerTwoLastMotion.x);
        //The difference between the x-coordinates of the second two fingers
        float newXOffset = Math.abs(x1 - x0);

        //The y-coordinate difference between the first two fingers
        float oldYOffset = Math.abs(mPointerOneLastMotion.y - mPointerTwoLastMotion.y);
        //The difference between the y-coordinates of the second two fingers
        float newYOffset = Math.abs(y1 - y0);

        //After the two fingers move, judge the maximum difference between the two fingers
        maxOffset = Math.max(Math.abs(newXOffset - oldXOffset), Math.abs(newYOffset - oldYOffset));
        //If move the x coordinate a little bit more
        if (maxOffset == Math.abs(newXOffset - oldXOffset)) {
            zooMin = newXOffset > oldXOffset ? true : false;
        }
        //If move the y coordinate a little bit more
        else {
            zooMin = newYOffset > oldYOffset ? true : false;
        }
        Log.d(TAG, "scaleSize maxOffset:" + maxOffset);
        if (zooMin) {
            return (int) (maxOffset / 10);//Magnify the maxi distance between fingers by 1/10
        } else {
            return -(int) (maxOffset / 10);//Magnify the min distance between fingers by 1/10
        }
    }

    /**
     * Setting the lrcRow
     */
    public void setLrc(List<LrcRow> lrcRows) {
        mLrcRows = lrcRows;
        invalidate();
    }

    /**
     * Call this method while playing to scroll the lyrics and highlight the current lyrics
     */
    public void seekLrcToTime(long time) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return;
        }
        if (mDisplayMode != DISPLAY_MODE_NORMAL) {
            return;
        }

        currentMillis = time;
        Log.d(TAG, "seekLrcToTime:" + time);

        for (int i = 0; i < mLrcRows.size(); i++) {
            LrcRow current = mLrcRows.get(i);
            LrcRow next = i + 1 == mLrcRows.size() ? null : mLrcRows.get(i + 1);
            /**
             * Set the behavior of the current line to be highlighted if the time of the current line is longer than the time of the next line
             * Set the current line to be highlighted when the current line is playing for longer than the last line of the current line
             */
            if ((time >= current.startTime && next != null && time < next.startTime)
                    || (time > current.startTime && next == null)) {
                seekLrc(i, false);
                return;
            }
        }
    }
}
