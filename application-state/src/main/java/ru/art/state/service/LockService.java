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

package ru.art.state.service;

import static java.util.Objects.*;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.*;
import static ru.art.state.module.ApplicationStateModule.*;
import java.util.*;
import java.util.concurrent.locks.*;

public interface LockService {
    static void lock() {
        lock(DEFAULT_LOCK);
    }

    static void unlock() {
        unlock(DEFAULT_LOCK);
    }

    static void lock(String name) {
        Map<String, ReentrantLock> clusterLocks = applicationState().getClusterLocks();
        if (clusterLocks.isEmpty()) {
            ReentrantLock reentrantLock = new ReentrantLock();
            clusterLocks.put(name, reentrantLock);
            reentrantLock.lock();
            return;
        }
        ReentrantLock reentrantLock = clusterLocks.get(name);
        if (isNull(reentrantLock)) {
            reentrantLock = new ReentrantLock();
            clusterLocks.put(name, reentrantLock);
            reentrantLock.lock();
            return;
        }
        reentrantLock.lock();
    }

    static void unlock(String name) {
        Map<String, ReentrantLock> clusterLocks = applicationState().getClusterLocks();
        if (clusterLocks.isEmpty()) {
            return;
        }
        ReentrantLock reentrantLock = clusterLocks.get(name);
        if (isNull(reentrantLock)) {
            return;
        }
        reentrantLock.unlock();
        clusterLocks.remove(name);
    }

    static Set<String> getCurrentLocks() {
        return applicationState().getClusterLocks().keySet();
    }
}
