/*
 * Decompiled with CFR 0.152.
 */
package com.sonatype.nexus.licensing.internal.rest;

import java.util.Date;

public class ApiLicenseDetailsXO {
    private String contactEmail;
    private String contactCompany;
    private String contactName;
    private Date effectiveDate;
    private Date expirationDate;
    private String licenseType;
    private String licensedUsers;
    private String fingerprint;
    private String features;

    public String getContactEmail() {
        return this.contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactCompany() {
        return this.contactCompany;
    }

    public void setContactCompany(String contactCompany) {
        this.contactCompany = contactCompany;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLicenseType() {
        return this.licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicensedUsers() {
        return this.licensedUsers;
    }

    public void setLicensedUsers(String licensedUsers) {
        this.licensedUsers = licensedUsers;
    }

    public String getFingerprint() {
        return this.fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFeatures() {
        return this.features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String toString() {
        return "ApiLicenseDetailsXO{contactEmail='" + this.contactEmail + '\'' + ", contactCompany='" + this.contactCompany + '\'' + ", contactName='" + this.contactName + '\'' + ", effectiveDate=" + this.effectiveDate + ", expirationDate=" + this.expirationDate + ", licenseType='" + this.licenseType + '\'' + ", licensedUsers='" + this.licensedUsers + '\'' + ", fingerprint='" + this.fingerprint + '\'' + ", features='" + this.features + '\'' + '}';
    }
}

