/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.eventbus.AllowConcurrentEvents
 *  com.google.common.eventbus.Subscribe
 *  com.google.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.licensing.product.ProductLicenseKey
 *  org.sonatype.nexus.audit.AuditData
 *  org.sonatype.nexus.audit.AuditorSupport
 *  org.sonatype.nexus.common.event.EventAware
 *  org.sonatype.nexus.common.event.EventManager
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import com.sonatype.nexus.licensing.ext.LicenseChangedWrapperEvent;
import com.sonatype.nexus.licensing.ext.LicenseExpirationEvent;
import com.sonatype.nexus.licensing.ext.LicenseSource;
import com.sonatype.nexus.licensing.ext.LicenseUninstallEvent;
import com.sonatype.nexus.licensing.ext.LicenseUpdateFailedEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.licensing.product.ProductLicenseKey;
import org.sonatype.nexus.audit.AuditData;
import org.sonatype.nexus.audit.AuditorSupport;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.common.event.EventManager;

@Named
@Singleton
public class LicenseAuditor
extends AuditorSupport
implements EventAware {
    public static final String DOMAIN = "license";
    private static final String EXPIRATION_TYPE = "expiration";
    private static final String UPDATE_FAILED_TYPE = "updateFailed";
    private static final String UNINSTALL_TYPE = "uninstall";
    private static final String CHANGED_TYPE = "changed";
    private static final int EXPIRY_WARNING_DAYS = 30;
    private static final Duration DAILY_CHECK = Duration.ofDays(1L);
    private final EventManager eventManager;
    private LocalDateTime lastExpirationEventPostTime;

    @Inject
    public LicenseAuditor(EventManager eventManager) {
        this.eventManager = Objects.requireNonNull(eventManager);
        this.registerType(LicenseChangedEvent.class, CHANGED_TYPE);
        this.registerType(LicenseExpirationEvent.class, EXPIRATION_TYPE);
        this.registerType(LicenseUpdateFailedEvent.class, UPDATE_FAILED_TYPE);
        this.registerType(LicenseUninstallEvent.class, UNINSTALL_TYPE);
        this.lastExpirationEventPostTime = LocalDateTime.MIN;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(LicenseExpirationEvent event) {
        this.recordExpirationEvent(event);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(LicenseUpdateFailedEvent event) {
        this.recordUpdateFailedEvent(event);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(LicenseChangedWrapperEvent event) {
        this.recordChangedEvent(event);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(LicenseUninstallEvent event) {
        this.recordUninstallEvent(event);
    }

    private void recordExpirationEvent(LicenseExpirationEvent event) {
        if (this.isRecording()) {
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(event.isExpired() ? "EXPIRED" : EXPIRATION_TYPE);
            data.setContext("system");
            Map attributes = data.getAttributes();
            attributes.put("expirationDate", LicenseAuditor.string((Object)event.getExpirationDate()));
            attributes.put("daysUntilExpiration", LicenseAuditor.string((Object)event.getDaysUntilExpiration()));
            attributes.put("features", LicenseAuditor.string((Iterable)event.getLicenseKey().getRawFeatures()));
            this.record(data);
        }
    }

    private void recordUpdateFailedEvent(LicenseUpdateFailedEvent event) {
        if (this.isRecording()) {
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(UPDATE_FAILED_TYPE);
            data.setContext("system");
            Map attributes = data.getAttributes();
            attributes.put("reason", event.getReason());
            this.record(data);
        }
    }

    private void recordChangedEvent(LicenseChangedWrapperEvent event) {
        if (this.isRecording()) {
            LicenseChangedEvent licenseChangedEvent = event.getLicenseChangedEvent();
            LicenseSource source = event.getLicenseSource();
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(CHANGED_TYPE);
            data.setContext("system");
            Map attributes = data.getAttributes();
            attributes.put("installed", LicenseAuditor.string((Object)licenseChangedEvent.isLicenseInstalled()));
            attributes.put("valid", LicenseAuditor.string((Object)licenseChangedEvent.isLicenseValid()));
            attributes.put("location", LicenseAuditor.string((Object)source.getDescription()));
            ProductLicenseKey licenseKey = licenseChangedEvent.getLicenseKey();
            if (licenseKey != null) {
                attributes.put("free", LicenseAuditor.string((Object)licenseKey.isFreeLicense()));
                attributes.put("evaluation", LicenseAuditor.string((Object)licenseKey.isEvaluation()));
                attributes.put("licensedUsers", LicenseAuditor.string((Object)licenseKey.getLicensedUsers()));
                attributes.put("effectiveDate", LicenseAuditor.string((Object)licenseKey.getEffectiveDate()));
                attributes.put("expirationDate", LicenseAuditor.string((Object)licenseKey.getExpirationDate()));
                attributes.put("features", LicenseAuditor.string((Iterable)licenseKey.getRawFeatures()));
                this.postExpirationEventIfNeeded(licenseKey);
            }
            this.record(data);
        }
    }

    private void recordUninstallEvent(LicenseUninstallEvent event) {
        if (this.isRecording()) {
            LicenseSource source = event.getLicenseSource();
            AuditData data = new AuditData();
            data.setDomain(DOMAIN);
            data.setType(UNINSTALL_TYPE);
            data.setContext("system");
            Map attributes = data.getAttributes();
            attributes.put("installed", "false");
            attributes.put("valid", LicenseAuditor.string((Object)event.isLicenseValid()));
            attributes.put("location", LicenseAuditor.string((Object)source.getDescription()));
            this.record(data);
        }
    }

    private void postExpirationEventIfNeeded(ProductLicenseKey licenseKey) {
        boolean shouldPost;
        if (licenseKey == null) {
            return;
        }
        LocalDateTime expirationDate = this.convertToLocalDateTime(licenseKey.getExpirationDate());
        if (expirationDate == null) {
            return;
        }
        int daysUntilExpiration = this.getDaysUntilExpiration(expirationDate);
        boolean bl = shouldPost = daysUntilExpiration <= 30 || this.isExpired(expirationDate);
        if (shouldPost && this.shouldPostExpirationEvent()) {
            boolean isExpired = this.isExpired(expirationDate);
            this.eventManager.post((Object)new LicenseExpirationEvent(licenseKey, this.convertToDate(expirationDate), daysUntilExpiration, isExpired));
            this.updateLastExpirationEventPostTime();
        }
    }

    private int getDaysUntilExpiration(LocalDateTime expirationDate) {
        return (int)Duration.between(LocalDateTime.now(), expirationDate).toDays();
    }

    private boolean isExpired(LocalDateTime expirationDate) {
        return expirationDate.compareTo(LocalDateTime.now()) <= 0;
    }

    private boolean shouldPostExpirationEvent() {
        return Duration.between(this.lastExpirationEventPostTime, LocalDateTime.now()).compareTo(DAILY_CHECK) >= 0;
    }

    private void updateLastExpirationEventPostTime() {
        this.lastExpirationEventPostTime = LocalDateTime.now();
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Date convertToDate(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}

