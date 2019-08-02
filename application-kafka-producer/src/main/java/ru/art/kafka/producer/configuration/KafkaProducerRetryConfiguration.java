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

package ru.art.kafka.producer.configuration;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaProducerRetryConfiguration {
    /**
     * @return Quantity resend any record whose send fails with a potentially temporary error
     */
    int getRetries();

    /**
     * @return Quantity resend any record whose send fails with a potentially temporary error for single connection
     */
    int getMaxAttemptPerSingleConnection();

    /**
     * @return timeout in milliseconds for an upper bound on the time to report success or
     * failure for method {@link org.apache.kafka.clients.producer.KafkaProducer#send(ProducerRecord)}
     */
    int getDeliveryTimeout();


}
