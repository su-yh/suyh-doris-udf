package com.suyh.doris.udf;

import com.suyh.doris.util.DateUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author suyh
 * @since 2025-11-27
 */
public class DatePlusDaysUdf extends UDF {
    public Integer evaluate(Integer sourceDate, Integer daysToAdd) {
        if (sourceDate == null) {
            return null;
        }
        if (daysToAdd == null) {
            return null;
        }

        return DateUtils.plusDays(sourceDate, daysToAdd);
    }
}
