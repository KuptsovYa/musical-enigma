/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.Future
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 *  org.sonatype.nexus.scheduling.TaskNotificationCondition
 *  org.sonatype.nexus.scheduling.constraints.CronExpression
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.sonatype.nexus.scheduling.TaskNotificationCondition;
import org.sonatype.nexus.scheduling.constraints.CronExpression;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

public class TaskXO {
    @NotBlank(groups={Update.class, Schedule.class})
    private String id;
    @NotNull
    private Boolean enabled;
    @NotBlank(groups={Create.class, Update.class})
    private String name;
    @NotBlank(groups={Create.class})
    private String typeId;
    private String typeName;
    private String status;
    private String statusDescription;
    private Date nextRun;
    private Date lastRun;
    private String lastRunResult;
    private Boolean runnable;
    private Boolean stoppable;
    private String timeZoneOffset;
    private String alertEmail;
    private TaskNotificationCondition notificationCondition;
    private Map<String, String> properties;
    @NotBlank(groups={Create.class})
    private String schedule;
    @NotNull(groups={OnceToMonthlySchedule.class})
    @Future(groups={OnceSchedule.class})
    private Date startDate;
    private Integer[] recurringDays;
    @NotBlank(groups={AdvancedSchedule.class})
    @CronExpression(groups={AdvancedSchedule.class})
    private String cronExpression;
    private Boolean isReadOnlyUi;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return this.statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Date getNextRun() {
        return this.nextRun;
    }

    public void setNextRun(Date nextRun) {
        this.nextRun = nextRun;
    }

    public Date getLastRun() {
        return this.lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public String getLastRunResult() {
        return this.lastRunResult;
    }

    public void setLastRunResult(String lastRunResult) {
        this.lastRunResult = lastRunResult;
    }

    public Boolean getRunnable() {
        return this.runnable;
    }

    public void setRunnable(Boolean runnable) {
        this.runnable = runnable;
    }

    public Boolean getStoppable() {
        return this.stoppable;
    }

    public void setStoppable(Boolean stoppable) {
        this.stoppable = stoppable;
    }

    public String getTimeZoneOffset() {
        return this.timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public String getAlertEmail() {
        return this.alertEmail;
    }

    public void setAlertEmail(String alertEmail) {
        this.alertEmail = alertEmail;
    }

    public TaskNotificationCondition getNotificationCondition() {
        return this.notificationCondition;
    }

    public void setNotificationCondition(TaskNotificationCondition notificationCondition) {
        this.notificationCondition = notificationCondition;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getSchedule() {
        return this.schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer[] getRecurringDays() {
        return this.recurringDays;
    }

    public void setRecurringDays(Integer[] recurringDays) {
        this.recurringDays = recurringDays;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Boolean isReadOnlyUi() {
        return this.isReadOnlyUi;
    }

    public void setIsReadOnlyUi(Boolean isReadOnlyUi) {
        this.isReadOnlyUi = isReadOnlyUi;
    }

    public String toString() {
        return "TaskXO{id='" + this.id + "', enabled=" + this.enabled + ", name='" + this.name + "', typeId='" + this.typeId + "', typeName='" + this.typeName + "', status='" + this.status + "', statusDescription='" + this.statusDescription + "', nextRun=" + this.nextRun + ", lastRun=" + this.lastRun + ", lastRunResult='" + this.lastRunResult + "', runnable=" + this.runnable + ", stoppable=" + this.stoppable + ", timeZoneOffset='" + this.timeZoneOffset + "', alertEmail='" + this.alertEmail + "', notificationCondition=" + this.notificationCondition + ", properties=" + this.properties + ", schedule='" + this.schedule + "', startDate=" + this.startDate + ", recurringDays=" + Arrays.toString((Object[])this.recurringDays) + ", cronExpression='" + this.cronExpression + "', isReadOnlyUi=" + this.isReadOnlyUi + "}";
    }

    public static interface OnceToMonthlySchedule {
    }

    public static interface OnceSchedule {
    }

    public static interface AdvancedSchedule {
    }

    public static interface Schedule {
    }
}

