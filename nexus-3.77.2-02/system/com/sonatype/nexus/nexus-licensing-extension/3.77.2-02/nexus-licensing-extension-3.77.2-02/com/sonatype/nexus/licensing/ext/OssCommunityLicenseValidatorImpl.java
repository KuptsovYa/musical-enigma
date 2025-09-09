/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.nexus.common.app.InternalLicenseValidator
 *  org.sonatype.nexus.common.time.DateHelper
 */
package com.sonatype.nexus.licensing.ext;

import com.google.common.base.Preconditions;
import com.sonatype.nexus.licensing.ext.MultiProductPreferenceFactory;
import java.util.Arrays;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.nexus.common.app.InternalLicenseValidator;
import org.sonatype.nexus.common.time.DateHelper;

@Named
@Singleton
public class OssCommunityLicenseValidatorImpl
implements InternalLicenseValidator {
    public static final String PEAK_REQUESTS_PER_DAY_EXCEEDED = "peak_requests_per_day_exceeded";
    public static final String COMPONENT_TOTAL_COUNT_EXCEEDED = "component_total_count_exceeded";
    public static final int TRIAL_PERIOD_DAYS = 30;
    private final MultiProductPreferenceFactory productPreferenceFactory;

    @Inject
    public OssCommunityLicenseValidatorImpl(MultiProductPreferenceFactory productPreferenceFactory) {
        this.productPreferenceFactory = (MultiProductPreferenceFactory)Preconditions.checkNotNull((Object)productPreferenceFactory);
    }

    public boolean isRequired() {
        return this.trialPeriodHasEnded();
    }

    private boolean trialPeriodHasEnded() {
        this.productPreferenceFactory.setProduct("CORE");
        Preferences preferences = this.productPreferenceFactory.nodeForPath("");
        Long componentTotalCountExceeded = preferences.getLong(COMPONENT_TOTAL_COUNT_EXCEEDED, 0L);
        Long peakRequestsPerDayExceeded = preferences.getLong(PEAK_REQUESTS_PER_DAY_EXCEEDED, 0L);
        Date oldestDate = DateHelper.oldestDateFromLongs(Arrays.asList(componentTotalCountExceeded, peakRequestsPerDayExceeded));
        if (oldestDate == null) {
            return false;
        }
        return DateHelper.daysElapsed((Date)oldestDate, (Date)new Date()) > 30;
    }
}

