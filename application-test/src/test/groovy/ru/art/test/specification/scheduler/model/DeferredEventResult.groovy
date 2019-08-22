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

package ru.art.test.specification.scheduler.model

import java.time.LocalDateTime

class DeferredEventResult<T> {
    LocalDateTime triggeredTime
    int order
    T value

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        DeferredEventResult that = (DeferredEventResult) o

        if (order != that.order) return false
        if (that.triggeredTime.hour - triggeredTime.hour > 1
                || that.triggeredTime.minute - triggeredTime.minute > 1
                || that.triggeredTime.second - triggeredTime.second > 1) return false
        if (value != that.value) return false
        return true
    }
}