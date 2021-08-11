/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.communicator.action;

import io.art.communicator.exception.*;
import io.art.communicator.model.*;
import io.art.core.managed.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommunicatorAction implements Managed {
    @Getter
    @EqualsAndHashCode.Include
    private final CommunicatorActionIdentifier id;

    @Getter
    private final MetaType<?> inputType;

    @Getter
    private final MetaType<?> outputType;

    @Singular("inputDecorator")
    private final List<UnaryOperator<Flux<Object>>> inputDecorators;

    @Singular("outputDecorator")
    private final List<UnaryOperator<Flux<Object>>> outputDecorators;

    @Getter
    private final Communication communication;

    @Getter(lazy = true, value = PRIVATE)
    private final Supplier<Object> producer = selectProducer();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Object, Object> handler = selectHandler();

    @Override
    public void initialize() {
        communication.initialize(this);
    }

    @Override
    public void dispose() {
        communication.dispose();
    }

    public <T> T communicate() {
        return cast(getProducer().get());
    }

    public <T> T communicate(Object input) {
        return cast(getHandler().apply(input));
    }

    private Supplier<Object> selectProducer() {
        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return () -> {
                try {
                    process(Flux.empty()).subscribe();
                    return null;
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        if (outputType.internalKind() == MONO) {
            return () -> {
                try {
                    return process(Flux.empty()).last();
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        if (outputType.internalKind() == FLUX) {
            return () -> {
                try {
                    return process(Flux.empty());
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        return () -> {
            try {
                return block(process(Flux.empty()).last());
            } catch (Throwable throwable) {
                throw new CommunicatorException(throwable);
            }
        };
    }

    private Function<Object, Object> selectHandler() {
        if (isNull(inputType)) {
            Supplier<Object> producer = selectProducer();
            return empty -> producer.get();
        }

        if (inputType.internalKind() == MONO) {
            if (isNull(outputType) || outputType.internalKind() == VOID) {
                return input -> {
                    try {
                        process(Flux.from(asMono(input))).subscribe();
                        return null;
                    } catch (Throwable throwable) {
                        throw new CommunicatorException(throwable);
                    }
                };
            }

            if (outputType.internalKind() == MONO) {
                return input -> {
                    try {
                        return process(Flux.from(asMono(input))).last();
                    } catch (Throwable throwable) {
                        throw new CommunicatorException(throwable);
                    }
                };
            }

            if (outputType.internalKind() == FLUX) {
                return input -> {
                    try {
                        return process(Flux.from(asMono(input)));
                    } catch (Throwable throwable) {
                        throw new CommunicatorException(throwable);
                    }
                };
            }

            return input -> {
                try {
                    return block(process(Flux.from(asMono(input))).last());
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        if (inputType.internalKind() == FLUX) {
            if (isNull(outputType) || outputType.internalKind() == VOID) {
                return input -> {
                    try {
                        process(asFlux(input)).subscribe();
                        return null;
                    } catch (Throwable throwable) {
                        throw new CommunicatorException(throwable);
                    }
                };
            }

            if (outputType.internalKind() == MONO) {
                return input -> {
                    try {
                        return process(asFlux(input)).last();
                    } catch (Throwable throwable) {
                        throw new CommunicatorException(throwable);
                    }
                };
            }

            if (outputType.internalKind() == FLUX) {
                return input -> {
                    try {
                        return process(asFlux(input));
                    } catch (Throwable throwable) {
                        throw new CommunicatorException(throwable);
                    }
                };
            }

            return input -> {
                try {
                    return block(process(asFlux(input)).last());
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        if (isNull(outputType) || outputType.internalKind() == VOID) {
            return input -> {
                try {
                    process(Flux.just(input)).subscribe();
                    return null;
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        if (outputType.internalKind() == MONO) {
            return input -> {
                try {
                    return process(Flux.just(input)).last();
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        if (outputType.internalKind() == FLUX) {
            return input -> {
                try {
                    return process(Flux.just(input));
                } catch (Throwable throwable) {
                    throw new CommunicatorException(throwable);
                }
            };
        }

        return input -> {
            try {
                return block(process(Flux.just(input)).last());
            } catch (Throwable throwable) {
                throw new CommunicatorException(throwable);
            }
        };
    }

    private Flux<Object> process(Flux<Object> input) {
        return decorateOutput(communication.communicate(decorateInput(input)));
    }

    private Flux<Object> decorateInput(Flux<Object> input) {
        Flux<Object> result = input;
        if (isNotEmpty(inputDecorators)) {
            for (UnaryOperator<Flux<Object>> decorator : inputDecorators) {
                result = decorator.apply(result);
            }
        }
        return result;
    }

    private Flux<Object> decorateOutput(Flux<Object> output) {
        Flux<Object> result = orElse(output, Flux::empty);
        if (isNotEmpty(outputDecorators)) {
            for (UnaryOperator<Flux<Object>> decorator : outputDecorators) {
                result = decorator.apply(result);
            }
        }
        return result;
    }
}
