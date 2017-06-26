package com.cola.libs.cronjob.beans;

/**
 * Created by jiachen.shi on 6/15/2017.
 */
public enum CronJobResult {

    /** The CronJob was executed without running into any problems **/
    SUCCESS,

    /** The CronJob has completed its run on schedule, but it was not successful for some reason.
     Unlike the 'ERROR' result, this indicates a planned completion of the CronJob. Check the CronJob's
     log for details. **/
    FAILURE,

    /** There was a problem during the CronJob's run (for example, the Java Virtual Machine was
     terminated or there was a system exception). Unlike the {literal}FAILURE{literal} status, this
     status indicates an uncontrolled termination of the CronJob. Check the CronJob's log for details. **/
    ERROR,

    /** No result is available. Most probably the CronJob has never been started yet. **/
    UNKNOWN
}
