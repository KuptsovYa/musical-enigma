/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.health.HealthCheck$Result
 */
package com.sonatype.nexus.pro.systemchecks.heartbeat;

import com.codahale.metrics.health.HealthCheck;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class ResultUtil {
    ResultUtil() {
    }

    static HealthCheck.Result deserialize(Map<String, Object> result) {
        return Optional.ofNullable(result.get("healthy")).filter(Boolean.class::isInstance).map(Boolean.class::cast).map(ResultUtil::method).map(ctr -> (HealthCheck.Result)ctr.apply((String)result.get("message"))).orElse(null);
    }

    private static Function<String, HealthCheck.Result> method(Boolean healthy) {
        if (healthy.booleanValue()) {
            return HealthCheck.Result::healthy;
        }
        return HealthCheck.Result::unhealthy;
    }
}

