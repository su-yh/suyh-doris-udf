package com.suyh.doris.udf;

import com.suyh.doris.udf.util.DateUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.time.ZoneOffset;

/**
 * @author suyh
 * @since 2025-11-27
 */
public class DateFromTsUdf extends UDF {
    public Integer evaluate(Long tsSeconds, String zoneIdText) {
        if (tsSeconds == null) {
            return null;
        }
        if (zoneIdText == null) {
            return null;
        }

        return DateUtils.timestampToDate(tsSeconds * 1000L, ZoneOffset.of(zoneIdText));
    }
}
