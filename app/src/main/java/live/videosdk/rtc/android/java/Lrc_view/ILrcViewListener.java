package live.videosdk.rtc.android.java.Lrc_view;
import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

/**
 * listener when dragging the lyrics
 */
public interface ILrcViewListener {
    /**
     * After dragging the lyrics, revert to playing to the lyrics line
     */
    void onLrcSought(int newPosition, LrcRow row);
}
