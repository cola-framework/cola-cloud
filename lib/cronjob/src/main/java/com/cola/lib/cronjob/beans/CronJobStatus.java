package com.cola.lib.cronjob.beans;

/**
 * Created by jiachen.shi on 6/15/2017.
 */
public enum CronJobStatus {

    /** The CronJob is being executed right now **/
    RUNNING,

    /** The CronJob's execution has been interrupted in its execution, either automatically by a
     piece of business logic or manually by user intervention. You will need to resume the CronJob's
     execution if you want it to finish its run. **/
    PAUSED,

    /** The CronJob has completed its run **/
    FINISHED,

    /** The CronJob's execution has been cancelled - either due to an error or due to user
     intervention. Check the CronJob's log for details **/
    ABORTED,

    /** Most probably, a CronJob in this state has never been run before **/
    UNKNOWN

}
