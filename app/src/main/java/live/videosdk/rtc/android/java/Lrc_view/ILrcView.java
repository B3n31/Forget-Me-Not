package live.videosdk.rtc.android.java.Lrc_view;

import java.util.List;
import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

/**
 * interface of lyrics
 */
public interface ILrcView {

    /**
     * LrcRow sets
     */
    void setLrc(List<LrcRow> lrcRows);

    /**
     * Highlight the row which is playing
     */
    void seekLrcToTime(long time);
    /**
     * listener when dragging the lyrics
     */
    void setListener(ILrcViewListener listener);
}
