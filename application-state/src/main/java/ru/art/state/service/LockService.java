package ru.art.state.service;

import static java.util.Objects.isNull;
import static ru.art.state.api.constants.StateApiConstants.LockServiceConstants.DEFAULT_LOCK;
import static ru.art.state.module.ApplicationStateModule.applicationState;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

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
