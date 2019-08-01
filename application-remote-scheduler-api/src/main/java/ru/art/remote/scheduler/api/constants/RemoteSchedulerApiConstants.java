package ru.art.remote.scheduler.api.constants;

public interface RemoteSchedulerApiConstants {
    String REMOTE_SCHEDULER_SERVICE_ID = "REMOTE_SCHEDULER_SERVICE";

    enum TaskStatus {
        NEW,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        FINISHED
    }

    interface Methods {
        String ADD_DEFERRED_TASK = "ADD_DEFERRED_TASK";
        String ADD_PERIODIC_TASK = "ADD_PERIODIC_TASK";
        String ADD_INFINITY_PROCESS = "ADD_INFINITY_PROCESS";
        String GET_DEFERRED_TASK_BY_ID = "GET_DEFERRED_TASK_BY_ID";
        String GET_PERIODIC_TASK_BY_ID = "GET_PERIODIC_TASK_BY_ID";
        String GET_ALL_DEFERRED_TASKS = "GET_ALL_DEFERRED_TASKS";
        String GET_ALL_PERIODIC_TASKS = "GET_ALL_PERIODIC_TASKS";
        String GET_ALL_INFINITY_PROCESSES = "GET_ALL_INFINITY_PROCESSES";
        String CANCEL_PERIODIC_TASK = "CANCEL_PERIODIC_TASK";
    }

    interface Fields {
        String ID = "id";
        String STATUS = "status";
        String SERVLET_PATH = "executableServletPath";
        String SERVICE_ID = "executableServiceId";
        String METHOD_ID = "executableMethodId";
        String EXECUTION_COUNT = "executionCount";
        String MAX_EXECUTION_COUNT = "maxExecutionCount";
        String INTERVAL_IN_SECONDS = "executionPeriodSeconds";
        String REQUEST = "executableRequest";

        String TASKS = "tasks";
        String PROCESSES = "processes";

        String EXECUTION_DATE = "executionDateTime";
        String CREATION_DATE = "creationDateTime";
        String EXECUTION_PERIOD_SECONDS = "executionPeriodSeconds";
        String EXECUTION_DELAY = "executionDelay";

        String FINISH_AFTER_COMPLETION = "finishAfterCompletion";
    }
}
