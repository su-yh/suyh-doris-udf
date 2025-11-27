package com.suyh.doris.udf;

import com.suyh.doris.udf.util.DateUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.time.ZoneOffset;

/**
 * @author suyh
 * @since 2025-11-27
 */
public class DateMidnightTimestampUdf extends UDF {
    public Long evaluate(Integer dateValue, String zoneIdText) {
        if (dateValue == null) {
            return null;
        }
        if (zoneIdText == null || zoneIdText.isEmpty()) {
            return null;
        }

        ZoneOffset zoneOffset = ZoneOffset.of(zoneIdText);
        return DateUtils.convertMidnightTimestamp(dateValue, zoneOffset) / 1000L;
    }
}
