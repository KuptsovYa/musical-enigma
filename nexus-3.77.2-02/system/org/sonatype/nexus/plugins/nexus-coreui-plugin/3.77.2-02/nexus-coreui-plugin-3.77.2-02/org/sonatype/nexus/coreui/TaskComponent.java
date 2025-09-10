/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableMap
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Provider
 *  javax.inject.Singleton
 *  javax.validation.Valid
 *  javax.validation.Validator
 *  javax.validation.constraints.NotEmpty
 *  javax.validation.constraints.NotNull
 *  javax.validation.groups.Default
 *  javax.ws.rs.NotFoundException
 *  org.apache.shiro.authz.annotation.RequiresAuthentication
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.rapture.StateContributor
 *  org.sonatype.nexus.repository.date.TimeZoneUtils
 *  org.sonatype.nexus.scheduling.ExternalTaskState
 *  org.sonatype.nexus.scheduling.TaskConfiguration
 *  org.sonatype.nexus.scheduling.TaskDescriptor
 *  org.sonatype.nexus.scheduling.TaskInfo
 *  org.sonatype.nexus.scheduling.TaskScheduler
 *  org.sonatype.nexus.scheduling.TaskState
 *  org.sonatype.nexus.scheduling.schedule.Cron
 *  org.sonatype.nexus.scheduling.schedule.Daily
 *  org.sonatype.nexus.scheduling.schedule.Hourly
 *  org.sonatype.nexus.scheduling.schedule.Manual
 *  org.sonatype.nexus.scheduling.schedule.Monthly
 *  org.sonatype.nexus.scheduling.schedule.Monthly$CalendarDay
 *  org.sonatype.nexus.scheduling.schedule.Now
 *  org.sonatype.nexus.scheduling.schedule.Once
 *  org.sonatype.nexus.scheduling.schedule.Schedule
 *  org.sonatype.nexus.scheduling.schedule.Weekly
 *  org.sonatype.nexus.scheduling.schedule.Weekly$Weekday
 *  org.sonatype.nexus.validation.Validate
 *  org.sonatype.nexus.validation.group.Create
 *  org.sonatype.nexus.validation.group.Update
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import javax.ws.rs.NotFoundException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.coreui.FormFieldXO;
import org.sonatype.nexus.coreui.TaskTypeXO;
import org.sonatype.nexus.coreui.TaskXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.rapture.StateContributor;
import org.sonatype.nexus.repository.date.TimeZoneUtils;
import org.sonatype.nexus.scheduling.ExternalTaskState;
import org.sonatype.nexus.scheduling.TaskConfiguration;
import org.sonatype.nexus.scheduling.TaskDescriptor;
import org.sonatype.nexus.scheduling.TaskInfo;
import org.sonatype.nexus.scheduling.TaskScheduler;
import org.sonatype.nexus.scheduling.TaskState;
import org.sonatype.nexus.scheduling.schedule.Cron;
import org.sonatype.nexus.scheduling.schedule.Daily;
import org.sonatype.nexus.scheduling.schedule.Hourly;
import org.sonatype.nexus.scheduling.schedule.Manual;
import org.sonatype.nexus.scheduling.schedule.Monthly;
import org.sonatype.nexus.scheduling.schedule.Now;
import org.sonatype.nexus.scheduling.schedule.Once;
import org.sonatype.nexus.scheduling.schedule.Schedule;
import org.sonatype.nexus.scheduling.schedule.Weekly;
import org.sonatype.nexus.validation.Validate;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.Update;

@Named
@Singleton
@DirectAction(action={"coreui_Task"})
public class TaskComponent
extends DirectComponentSupport
implements StateContributor {
    private static final String TASK_RESULT_OK = "Ok";
    private static final String TASK_RESULT_CANCELED = "Canceled";
    private static final String TASK_RESULT_ERROR = "Error";
    private static final String TASK_RESULT_INTERRUPTED = "Interrupted";
    public static final String PLAN_RECONCILIATION_TASK_ID = "blobstore.planReconciliation";
    public static final String PLAN_RECONCILIATION_TASK_OK_TEXT = " - Plan(s) is ready to run";
    private final TaskScheduler taskScheduler;
    private final Provider<Validator> validatorProvider;
    private final boolean allowCreation;

    @Inject
    public TaskComponent(TaskScheduler taskScheduler, Provider<Validator> validatorProvider, @Named(value="${nexus.scripts.allowCreation:-false}") boolean allowCreation) {
        this.taskScheduler = (TaskScheduler)Preconditions.checkNotNull((Object)taskScheduler);
        this.validatorProvider = (Provider)Preconditions.checkNotNull(validatorProvider);
        this.allowCreation = allowCreation;
    }

    @Nullable
    public Map<String, Object> getState() {
        return ImmutableMap.of((Object)"allowScriptCreation", (Object)this.allowCreation);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:tasks:read"})
    public List<TaskXO> read() {
        return this.taskScheduler.listsTasks().stream().filter(taskInfo -> taskInfo.getConfiguration().isVisible()).map(this::asTaskXO).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:tasks:read"})
    public List<TaskTypeXO> readTypes() {
        return this.taskScheduler.getTaskFactory().getDescriptors().stream().map(TaskComponent::asTaskTypeXO).collect(Collectors.toList());
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:tasks:create"})
    @Validate(groups={Create.class, Default.class})
    public TaskXO create(@NotNull @Valid TaskXO taskXO) throws Exception {
        Schedule schedule = this.asSchedule(taskXO);
        TaskConfiguration taskConfiguration = this.taskScheduler.createTaskConfigurationInstance(taskXO.getTypeId());
        Preconditions.checkState((boolean)taskConfiguration.isExposed(), (Object)"This task is not allowed to be created");
        taskXO.getProperties().forEach((arg_0, arg_1) -> ((TaskConfiguration)taskConfiguration).setString(arg_0, arg_1));
        taskConfiguration.setAlertEmail(taskXO.getAlertEmail());
        taskConfiguration.setNotificationCondition(taskXO.getNotificationCondition());
        taskConfiguration.setName(taskXO.getName());
        taskConfiguration.setEnabled(taskXO.getEnabled().booleanValue());
        TaskInfo task = this.scheduleTask(() -> this.taskScheduler.scheduleTask(taskConfiguration, schedule));
        this.log.debug("Created task with type '{}': {} {}", new Object[]{taskConfiguration.getClass(), taskConfiguration.getName(), taskConfiguration.getId()});
        return this.asTaskXO(task);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:tasks:update"})
    @Validate(groups={Update.class, Default.class})
    public TaskXO update(@NotNull @Valid TaskXO taskXO) throws Exception {
        TaskInfo task = this.taskScheduler.getTaskById(taskXO.getId());
        this.validateState(taskXO.getId(), task);
        if ("script".equals(task.getTypeId())) {
            this.validateScriptUpdate(task, taskXO);
        }
        Schedule schedule = this.asSchedule(taskXO);
        TaskConfiguration taskConfiguration = this.taskScheduler.createTaskConfigurationInstance(taskXO.getTypeId());
        taskConfiguration.apply(task.getConfiguration());
        taskConfiguration.setEnabled(taskXO.getEnabled().booleanValue());
        taskConfiguration.setName(taskXO.getName());
        taskConfiguration.setAlertEmail(taskXO.getAlertEmail());
        taskConfiguration.setNotificationCondition(taskXO.getNotificationCondition());
        taskXO.getProperties().forEach((arg_0, arg_1) -> ((TaskConfiguration)taskConfiguration).setString(arg_0, arg_1));
        task = this.scheduleTask(() -> this.taskScheduler.scheduleTask(taskConfiguration, schedule));
        return this.asTaskXO(task);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:tasks:delete"})
    @Validate
    public void remove(@NotEmpty String id) {
        TaskInfo taskInfo = this.taskScheduler.getTaskById(id);
        if (taskInfo != null) {
            taskInfo.remove();
        }
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:tasks:start"})
    @Validate
    public void run(@NotEmpty String id) throws Exception {
        TaskInfo taskInfo = this.taskScheduler.getTaskById(id);
        if (taskInfo != null) {
            taskInfo.runNow();
        }
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresAuthentication
    @RequiresPermissions(value={"nexus:tasks:stop"})
    @Validate
    public void stop(@NotEmpty String id) {
        this.taskScheduler.cancel(id, false);
    }

    private TaskXO asTaskXO(TaskInfo taskInfo) {
        Object statusDescription;
        ExternalTaskState externalTaskState = this.taskScheduler.toExternalTaskState(taskInfo);
        TaskState taskState = externalTaskState.getState();
        TaskState endTaskState = externalTaskState.getLastEndState();
        Date lastRun = externalTaskState.getLastRunStarted();
        Long runDuration = externalTaskState.getLastRunDuration();
        TaskConfiguration configuration = taskInfo.getConfiguration();
        TaskXO result = new TaskXO();
        result.setId(taskInfo.getId());
        result.setEnabled(configuration.isEnabled());
        result.setName(taskInfo.getName());
        result.setTypeId(configuration.getTypeId());
        result.setTypeName(configuration.getTypeName());
        result.setStatus(taskState.name());
        Object object = statusDescription = configuration.isEnabled() ? taskState.getDescription() : "Disabled";
        if (taskInfo.getCurrentState().getState().isRunning() && configuration.getProgress() != null) {
            statusDescription = (String)statusDescription + ": " + configuration.getProgress();
        }
        result.setStatusDescription((String)statusDescription);
        result.setSchedule(TaskComponent.getSchedule(taskInfo.getSchedule()));
        result.setLastRun(lastRun);
        result.setLastRunResult(TaskComponent.getLastRunResult(taskInfo, endTaskState, runDuration));
        result.setNextRun(externalTaskState.getNextFireTime());
        result.setRunnable(taskState.isWaiting());
        result.setStoppable(taskState.isRunning());
        result.setAlertEmail(configuration.getAlertEmail());
        result.setNotificationCondition(configuration.getNotificationCondition());
        result.setProperties(configuration.asMap());
        Schedule schedule = taskInfo.getSchedule();
        if (schedule instanceof Once) {
            result.setStartDate(((Once)schedule).getStartAt());
        } else if (schedule instanceof Hourly) {
            result.setStartDate(((Hourly)schedule).getStartAt());
        } else if (schedule instanceof Daily) {
            result.setStartDate(((Daily)schedule).getStartAt());
        } else if (schedule instanceof Weekly) {
            result.setStartDate(((Weekly)schedule).getStartAt());
            result.setRecurringDays(((Weekly)schedule).getDaysToRun().stream().map(dayToRun -> dayToRun.ordinal() + 1).collect(Collectors.toList()).toArray(new Integer[0]));
        } else if (schedule instanceof Monthly) {
            result.setStartDate(((Monthly)schedule).getStartAt());
            result.setRecurringDays(((Monthly)schedule).getDaysToRun().stream().map(dayToRun -> dayToRun.isLastDayOfMonth() ? 999 : dayToRun.getDay()).collect(Collectors.toList()).toArray(new Integer[0]));
        } else if (schedule instanceof Cron) {
            result.setStartDate(((Cron)schedule).getStartAt());
            result.setCronExpression(((Cron)schedule).getCronExpression());
        }
        result.setIsReadOnlyUi(configuration.getBoolean(".readOnlyUi", false));
        return result;
    }

    private Schedule asSchedule(TaskXO taskXO) {
        if ("advanced".equals(taskXO.getSchedule())) {
            ZoneOffset clientZoneOffset = ZoneOffset.of(taskXO.getTimeZoneOffset());
            ((Validator)this.validatorProvider.get()).validate((Object)taskXO, new Class[]{TaskXO.AdvancedSchedule.class});
            return this.taskScheduler.getScheduleFactory().cron(new Date(), taskXO.getCronExpression(), clientZoneOffset.getId());
        }
        if (!"manual".equals(taskXO.getSchedule())) {
            if (taskXO.getStartDate() == null) {
                ((Validator)this.validatorProvider.get()).validate((Object)taskXO, new Class[]{TaskXO.OnceToMonthlySchedule.class});
            }
            ZoneOffset clientZoneOffset = ZoneOffset.of(taskXO.getTimeZoneOffset());
            LocalDateTime startDateClient = LocalDateTime.ofInstant(taskXO.getStartDate().toInstant(), ZoneId.of(clientZoneOffset.getId()));
            LocalDateTime startDateServer = LocalDateTime.ofInstant(taskXO.getStartDate().toInstant(), ZoneId.systemDefault());
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(taskXO.getStartDate().getTime());
            date.set(13, 0);
            date.set(14, 0);
            switch (taskXO.getSchedule()) {
                case "once": {
                    ((Validator)this.validatorProvider.get()).validate((Object)taskXO, new Class[]{TaskXO.OnceSchedule.class});
                    return this.taskScheduler.getScheduleFactory().once(date.getTime());
                }
                case "hourly": {
                    return this.taskScheduler.getScheduleFactory().hourly(date.getTime());
                }
                case "daily": {
                    return this.taskScheduler.getScheduleFactory().daily(date.getTime());
                }
                case "weekly": {
                    return this.taskScheduler.getScheduleFactory().weekly(date.getTime(), Arrays.stream(taskXO.getRecurringDays()).map(recurringDay -> Weekly.Weekday.values()[TimeZoneUtils.shiftWeekDay((int)(recurringDay - 1), (LocalDateTime)startDateClient, (LocalDateTime)startDateServer)]).collect(Collectors.toSet()));
                }
                case "monthly": {
                    return this.taskScheduler.getScheduleFactory().monthly(date.getTime(), Arrays.stream(taskXO.getRecurringDays()).map(recurringDay -> recurringDay == 999 ? Monthly.CalendarDay.lastDay() : Monthly.CalendarDay.day((int)TimeZoneUtils.shiftMonthDay((int)recurringDay, (LocalDateTime)startDateClient, (LocalDateTime)startDateServer))).collect(Collectors.toSet()));
                }
            }
        }
        return this.taskScheduler.getScheduleFactory().manual();
    }

    @VisibleForTesting
    void validateState(String taskId, TaskInfo taskInfo) {
        if (taskInfo == null) {
            throw new NotFoundException(String.format("Task with id '%s' not found", taskId));
        }
        ExternalTaskState externalTaskState = this.taskScheduler.toExternalTaskState(taskInfo);
        if (externalTaskState.getState().isRunning()) {
            throw new IllegalStateException("Task can not be edited while it is being executed or it is in line to be executed");
        }
    }

    @VisibleForTesting
    void validateScriptUpdate(TaskInfo task, TaskXO update) {
        String originalSource = task.getConfiguration().getString("source");
        String updateSource = update.getProperties().get("source");
        if (!this.allowCreation && originalSource != null && !originalSource.equals(updateSource)) {
            throw new IllegalStateException("Script source updates are not allowed");
        }
    }

    private TaskInfo scheduleTask(Callable<TaskInfo> callable) throws Exception {
        try {
            return callable.call();
        }
        catch (Exception e) {
            this.log.error("Failed to schedule task", (Throwable)e);
            throw e;
        }
    }

    private static String getSchedule(Schedule schedule) {
        if (schedule instanceof Manual) {
            return "manual";
        }
        if (schedule instanceof Now) {
            return "internal";
        }
        if (schedule instanceof Once) {
            return "once";
        }
        if (schedule instanceof Hourly) {
            return "hourly";
        }
        if (schedule instanceof Daily) {
            return "daily";
        }
        if (schedule instanceof Weekly) {
            return "weekly";
        }
        if (schedule instanceof Monthly) {
            return "monthly";
        }
        if (schedule instanceof Cron) {
            return "advanced";
        }
        return schedule.getClass().getName();
    }

    private static String getLastRunResult(TaskInfo taskInfo, TaskState endState, Long runDuration) {
        StringBuilder lastRunResult = new StringBuilder();
        if (endState != null) {
            if (TaskState.OK.equals((Object)endState)) {
                lastRunResult.append(TASK_RESULT_OK);
            } else if (TaskState.CANCELED.equals((Object)endState)) {
                lastRunResult.append(TASK_RESULT_CANCELED);
            } else if (TaskState.FAILED.equals((Object)endState)) {
                lastRunResult.append(TASK_RESULT_ERROR);
            } else if (TaskState.INTERRUPTED.equals((Object)endState)) {
                lastRunResult.append(TASK_RESULT_INTERRUPTED);
            } else {
                lastRunResult.append(endState.name());
            }
            if (runDuration != null) {
                long milliseconds = runDuration;
                int hours = (int)(milliseconds / 1000L / 3600L);
                int minutes = (int)(milliseconds / 1000L / 60L - (long)(hours * 60));
                int seconds = (int)(milliseconds / 1000L % 60L);
                lastRunResult.append(" [");
                if (hours != 0) {
                    lastRunResult.append(hours).append("h");
                }
                if (minutes != 0 || hours != 0) {
                    lastRunResult.append(minutes).append("m");
                }
                lastRunResult.append(seconds).append("s]");
            }
            TaskComponent.appendPlanReconciliationText(lastRunResult, endState, taskInfo);
        }
        return lastRunResult.toString();
    }

    private static void appendPlanReconciliationText(StringBuilder lastRunResult, TaskState endState, TaskInfo taskInfo) {
        if (TaskState.OK.equals((Object)endState) && taskInfo.getTypeId().equals(PLAN_RECONCILIATION_TASK_ID)) {
            lastRunResult.append(PLAN_RECONCILIATION_TASK_OK_TEXT);
        }
    }

    private static TaskTypeXO asTaskTypeXO(TaskDescriptor taskDescriptor) {
        TaskTypeXO taskTypeXO = new TaskTypeXO();
        taskTypeXO.setId(taskDescriptor.getId());
        taskTypeXO.setName(taskDescriptor.getName());
        taskTypeXO.setExposed(taskDescriptor.isExposed());
        taskTypeXO.setConcurrentRun(taskDescriptor.allowConcurrentRun());
        if (taskDescriptor.getFormFields() != null) {
            taskTypeXO.setFormFields(taskDescriptor.getFormFields().stream().map(FormFieldXO::create).collect(Collectors.toList()));
        }
        return taskTypeXO;
    }
}

