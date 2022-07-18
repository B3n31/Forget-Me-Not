package live.videosdk.rtc.android.java.Lrc_view;

import java.util.List;

import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

/**
 * 解析歌词，得到LrcRow的集合
 */
public interface ILrcBuilder {
    /**
     * 解析歌词，得到LrcRow的集合
     * @param rawLrc lrc内容
     * @return LrcRow的集合
     */
    List<LrcRow> getLrcRows(String rawLrc);
}
