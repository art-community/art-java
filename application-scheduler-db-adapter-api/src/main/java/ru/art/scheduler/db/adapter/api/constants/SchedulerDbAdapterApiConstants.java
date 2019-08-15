/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.scheduler.db.adapter.api.constants;

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
