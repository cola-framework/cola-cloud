package com.cola.lib.cronjob.beans;

/**
 * Created by jiachen.shi on 6/15/2017.
 */
public class PerformResult {

    private final CronJobResult result;
    private final CronJobStatus status;

    public PerformResult(CronJobResult result, CronJobStatus status) {
        this.result = result;
        this.status = status;
    }

    public CronJobResult getResult() {
        return this.result;
    }

    public CronJobStatus getStatus() {
        return this.status;
    }

    public String toString() {
        return "PerformResult[Cronjob Result: " + this.getResult() + " Cronjob Status: " + this.getStatus() + "]";
    }

}
