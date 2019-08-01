package ru.adk.scheduler.db.adapter.api.constants;

public interface SchedulerDbAdapterApiConstants {

    interface Methods {
        String PUT_DEFERRED_TASK = "PUT_DEFERRED_TASK";
        String PUT_PERIODIC_TASK = "PUT_PERIODIC_TASK";
        String PUT_INFINITY_PROCESS = "PUT_INFINITY_PROCESS";
        String GET_DEFERRED_TASK = "GET_DEFERRED_TASK";
        String GET_PERIODIC_TASK = "GET_PERIODIC_TASK";
        String UPDATE_DEFERRED_TASK_STATUS = "UPDATE_DEFERRED_TASK_STATUS";
        String UPDATE_PERIODIC_TASK_STATUS = "UPDATE_PERIODIC_TASK_STATUS";
        String UPDATE_TASK_EXECUTION_TIME = "UPDATE_TASK_EXECUTION_TIME";
        String INC_EXECUTION_COUNT = "INC_EXECUTION_COUNT";
        String GET_ALL_DEFERRED_TASKS = "GET_ALL_DEFERRED_TASKS";
        String GET_ALL_PERIODIC_TASKS = "GET_ALL_PERIODIC_TASKS";
        String GET_ALL_INFINITY_PROCESSES = "GET_ALL_INFINITY_PROCESSES";
    }

    interface Fields {
        String TASK_ID = "taskId";
        String TASK_EXECUTION_PERIOD = "taskExecutionPeriod";
        String TASK_STATUS = "taskStatus";
    }
}
