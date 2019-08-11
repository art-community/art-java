/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.remote.scheduler.constants;

public interface RemoteSchedulerModuleConstants {
    String REMOTE_SCHEDULER_MODULE_ID = "REMOTE_SCHEDULER_MODULE";
    String REFRESH_DEFERRED_TASK_KEY = "REFRESH_DEFERRED_TASK_KEY";
    String REFRESH_PERIODIC_TASK_KEY = "REFRESH_PERIODIC_TASK_KEY";
    int ZERO = 0;

    String ADD_DEFERRED_PATH = "/addDeferred";
    String ADD_PERIODIC_PATH = "/addPeriodic";
    String ADD_PROCESS_PATH = "/addProcess";
    String GET_DEFERRED_PATH = "/getDeferred";
    String GET_PERIODIC_PATH = "/getPeriodic";
    String GET_ALL_DEFERRED_PATH = "/getAllDeferred";
    String GET_ALL_PERIODIC_PATH = "/getAllPeriodic";
    String GET_ALL_PROCESSES_PATH = "/getAllProcesses";
    String cancel_PATH = "/cancel";

    interface LoggingMessages {
        String TASK_STARTED_MESSAGE = "Task started. Task: ''{0}''";
        String PROCESS_STARTED_MESSAGE = "Process was ras. Process: ''{0}''";
        String TASK_COMPLETED_MESSAGE = "Task completed. Task: ''{0}'' completed with response: ''{1}''";
        String TASK_FAILED_MESSAGE = "Task failed. Task: ''{0}''";
        String PROCESS_COMPLETED_MESSAGE = "ProcessExecution completed. Task: ''{0}'' completed with response: ''{1}''";
        String PROCESS_FAILED_MESSAGE = "Process execution failed. Task: ''{0}''";
    }

    interface SchedulerConfigKeys {
        String BALANCER_SECTION_ID = "balancer";
        String BALANCER_HOST = "host";
        String BALANCER_PORT = "port";

        String SCHEDULER_SECTION_ID = "scheduler";
        String REFRESH_DEFERRED_PERIOD_MINUTES = "refreshDeferredPeriodMinutes";
        String REFRESH_PERIODIC_PERIOD_MINUTES = "refreshPeriodicPeriodMinutes";

        String DB_ADAPTER_SECTION_ID = "db";
        String SERVICE_ID = "adapter.serviceId";
    }

    interface ExceptionMessages {
        String ID_IS_EMPTY = "Id is empty";
    }

    interface Defaults {
        String DEFAULT_DB_ADAPTER_SERVICE_ID = "SCHEDULER_DB_ADAPTER_SERVICE";
        int DEFAULT_REFRESH_DEFERRED_PERIOD_MINUTES = 1;
        int DEFAULT_REFRESH_PERIODIC_PERIOD_MINUTES = 1;
    }
}
