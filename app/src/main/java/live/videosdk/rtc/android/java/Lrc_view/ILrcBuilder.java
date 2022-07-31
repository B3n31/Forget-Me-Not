package live.videosdk.rtc.android.java.Lrc_view;

import java.util.List;

import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

/**
 * @Author: tingyuzhang
 * @Time: 2022-07-15 2:51 afternoon
 * @Team: Raging Coders
 * @Description: Return the lyrics into lrcRow
 */

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
