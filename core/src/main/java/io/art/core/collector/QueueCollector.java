/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.core.collector;

import io.art.core.factory.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.stream.*;

public class QueueCollector {
    public static <T> Collector<T, ?, Queue<T>> queueCollector() {
        return toCollection(QueueFactory::queue);
    }

    public static <T> Collector<T, ?, Deque<T>> dequeCollector() {
        return toCollection(QueueFactory::deque);
    }
}
