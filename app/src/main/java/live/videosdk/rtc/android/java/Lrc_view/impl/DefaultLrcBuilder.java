package live.videosdk.rtc.android.java.Lrc_view.impl;

import android.util.Log;

import live.videosdk.rtc.android.java.Lrc_view.ILrcBuilder;
import live.videosdk.rtc.android.java.Lrc_view.LrcRowUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: tingyuzhang
 * @Time: 2022-07-15 2:48 afternoon
 * @Team: Raging Coders
 * @Description: Parse the lyrics
 */

/**
 * Parse the lyrics to get a set of LrcRow
 */
public class DefaultLrcBuilder implements ILrcBuilder {
    /**
     * The Tag.
     */
    static final String TAG = "DefaultLrcBuilder";

    /**
     *
     * @param rawLrc context of lrc
     * @return
     */
    public List<LrcRow> getLrcRows(String rawLrc) {
        Log.d(TAG, "getLrcRows by rawString");
        if (rawLrc == null || rawLrc.length() == 0) {
            Log.e(TAG, "getLrcRows rawLrc null or empty");
            return null;
        }
        StringReader reader = new StringReader(rawLrc);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        List<LrcRow> rows = new ArrayList<LrcRow>();
        try {
            //Read each line of the lyrics in a loop
            do {
                line = br.readLine();
                Log.d(TAG, "lrc raw line: " + line);
                if (line != null && line.length() > 0) {
                    //Parsing each line of lyrics gives a collection of each line of lyrics
                    //Since some lyrics repeat for more than one time
                    // So we can parse multiple lines of lyrics
                    List<LrcRow> lrcRows = LrcRowUtil.createRows(line);
                    if (lrcRows != null && lrcRows.size() > 0) {
                        for (LrcRow row : lrcRows) {
                            Log.d(TAG, "row = " + row);
                            rows.add(row);
                        }
                    }
                }
            } while (line != null);

            // Setting the end time of each row
            if (rows.size() > 0) {
                Collections.sort(rows);
                if (rows != null && rows.size() > 0) {
                    int size = rows.size();
                    for (int i = 0; i < size; i++) {
                        LrcRow lrcRow = rows.get(i);
                        if (i < size - 1) {
                            lrcRow.setEndTime(rows.get(i + 1).getStartTime());
                        } else {
                            lrcRow.setEndTime(lrcRow.getStartTime() + 10000);
                        }
                        Log.d(TAG, "lrcRow:" + lrcRow.toString());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "parse exceptioned:" + Log.getStackTraceString(e));
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader.close();
        }
        return rows;
    }
}
