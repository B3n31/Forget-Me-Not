package live.videosdk.rtc.android.java.Lrc_view;

import java.util.List;

import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

/**
 * Parsing lyrics and then return to LrcRow
 */
public interface ILrcBuilder {
    /**
     * @param rawLrc context of lrc
     * @return sets of LrcRow
     */
    List<LrcRow> getLrcRows(String rawLrc);
}
