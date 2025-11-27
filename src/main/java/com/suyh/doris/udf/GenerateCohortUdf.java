package com.suyh.doris.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.time.Duration;

/**
 * @author suyh
 * @since 2025-11-27
 */
public class GenerateCohortUdf extends UDF {
    public Integer evaluate(Long behaviorTsSeconds, Long regTsSeconds) {
        if (behaviorTsSeconds == null) {
            return -1;
        }
        if (regTsSeconds == null) {
            return -1;
        }

        long diffSeconds = behaviorTsSeconds - regTsSeconds;
        long cohort = diffSeconds / Duration.ofDays(1L).getSeconds();
        return (int) cohort;
    }
}
