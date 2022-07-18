package live.videosdk.rtc.android.java.Lrc_view;
import live.videosdk.rtc.android.java.Lrc_view.impl.LrcRow;

/**
 * 歌词拖动时候的监听类
 */
public interface ILrcViewListener {
    /**
     * 当歌词被用户上下拖动的时候回调该方法    (ps:seek的过去式和过去分词为sought)
     */
    void onLrcSought(int newPosition, LrcRow row);
}
