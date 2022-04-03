package io.art.transport.configuration;

import io.art.core.annotation.*;
import io.art.core.exception.*;
import io.art.core.source.*;
import io.art.transport.constants.TransportModuleConstants.*;
import lombok.*;
import reactor.util.retry.*;
import static io.art.core.checker.NullityChecker.orElse;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.Defaults.*;
import static io.art.transport.constants.TransportModuleConstants.RetryPolicy.INDEFINITELY;
import static io.art.transport.constants.TransportModuleConstants.RetryPolicy.retryPolicy;
import static reactor.util.retry.Retry.*;
import java.time.*;

@Getter
@Public
@Builder(toBuilder = true)
public class TransportRetryConfiguration {
    private Duration minBackoff;
    private long backOffMaxAttempts;
    private long fixedDelayMaxAttempts;
    private RetryPolicy retryPolicy;
    private Duration fixedDelay;
    private int maxInRow;
    private int max;

    public Retry toRetry() {
        switch (retryPolicy) {
            case BACKOFF:
                return backoff(backOffMaxAttempts, minBackoff);
            case FIXED_DELAY:
                return fixedDelay(fixedDelayMaxAttempts, fixedDelay);
            case MAX:
                return max(max);
            case MAX_IN_A_ROW:
                return maxInARow(maxInRow);
            case INDEFINITELY:
                return indefinitely();
        }
        throw new ImpossibleSituationException();
    }

    public static TransportRetryConfiguration retry() {
        return TransportRetryConfiguration.builder()
                .retryPolicy(INDEFINITELY)
                .build();
    }

    public static TransportRetryConfiguration retry(ConfigurationSource source) {
        TransportRetryConfiguration configuration = TransportRetryConfiguration.builder().build();
        configuration.retryPolicy = retryPolicy(source.getString(POLICY_KEY), INDEFINITELY);
        configuration.backOffMaxAttempts = orElse(source.getLong(BACKOFF_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
        configuration.fixedDelayMaxAttempts = orElse(source.getLong(FIXED_DELAY_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
        configuration.minBackoff = orElse(source.getDuration(BACKOFF_MIN_BACKOFF_KEY), DEFAULT_RETRY_MIN_BACKOFF);
        configuration.fixedDelay = orElse(source.getDuration(FIXED_DELAY_KEY), DEFAULT_RETRY_FIXED_DELAY);
        configuration.max = orElse(source.getInteger(MAX_KEY), DEFAULT_RETRY_MAX);
        configuration.maxInRow = orElse(source.getInteger(MAX_IN_ROW_KEY), DEFAULT_RETRY_MAX_IN_ROW);
        return configuration;
    }

    public static TransportRetryConfiguration retry(ConfigurationSource source, TransportRetryConfiguration defaults) {
        TransportRetryConfiguration configuration = TransportRetryConfiguration.builder().build();
        configuration.retryPolicy = retryPolicy(source.getString(POLICY_KEY), defaults.retryPolicy);
        configuration.backOffMaxAttempts = orElse(source.getLong(BACKOFF_MAX_ATTEMPTS_KEY), defaults.backOffMaxAttempts);
        configuration.fixedDelayMaxAttempts = orElse(source.getLong(FIXED_DELAY_MAX_ATTEMPTS_KEY), defaults.fixedDelayMaxAttempts);
        configuration.minBackoff = orElse(source.getDuration(BACKOFF_MIN_BACKOFF_KEY), defaults.minBackoff);
        configuration.fixedDelay = orElse(source.getDuration(FIXED_DELAY_KEY), defaults.fixedDelay);
        configuration.max = orElse(source.getInteger(MAX_KEY), defaults.max);
        configuration.maxInRow = orElse(source.getInteger(MAX_IN_ROW_KEY), defaults.maxInRow);
        return configuration;
    }

}
