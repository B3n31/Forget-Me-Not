package live.videosdk.rtc.android.java.Lrc_view;
import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

/**
 * @Author: tingyuzhang
 * @Time: 2022-07-15 7:52 afternoon
 * @Team: Raging Coders
 * @Description: Lyrics view listener
 */


/**
 * listener when dragging the lyrics
 */
public interface ILrcViewListener {
    /**
     * After dragging the lyrics, revert to playing to the lyrics line
     */
    void onLrcSought(int newPosition, LrcRow row);
}
