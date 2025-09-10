/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.health.HealthCheck
 *  com.codahale.metrics.health.HealthCheck$Result
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.sonatype.nexus.common.app.ApplicationVersion
 */
package com.sonatype.nexus.pro.systemchecks.internal.status;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Preconditions;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.app.ApplicationVersion;

@Named(value="Recent version")
@Singleton
public class RecentVersionHealthCheck
extends HealthCheck {
    public static final String HEALTH_CHECK_NAME = "Recent version";
    private static final String UNHEALTHY_MESSAGE = "This instance is running version released on %s. Please update to a more recent release.";
    private static final Logger log = LoggerFactory.getLogger(RecentVersionHealthCheck.class);
    private final ApplicationVersion applicationVersion;
    private static final long RECENT_RELEASE_MONTHS = 12L;

    @Inject
    public RecentVersionHealthCheck(ApplicationVersion applicationVersion) {
        this.applicationVersion = (ApplicationVersion)Preconditions.checkNotNull((Object)applicationVersion);
    }

    private boolean isHealthy() {
        Period sinceBuild = Period.between(this.getBuildDate(), LocalDate.now());
        return 12L >= sinceBuild.toTotalMonths();
    }

    private LocalDate getBuildDate() {
        String buildTimestamp = this.applicationVersion.getBuildTimestamp();
        if (buildTimestamp.length() >= 10) {
            try {
                return LocalDate.parse(buildTimestamp.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
            }
            catch (DateTimeParseException dateTimeParseException) {}
        }
        log.warn("Unable to parse build date: '{}'", (Object)buildTimestamp);
        return LocalDate.now();
    }

    private String reason() {
        return String.format(UNHEALTHY_MESSAGE, this.getBuildDate().toString());
    }

    protected HealthCheck.Result check() {
        return this.isHealthy() ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy((String)this.reason());
    }
}

