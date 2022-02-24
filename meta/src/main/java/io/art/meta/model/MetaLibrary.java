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

package io.art.meta.model;

import io.art.core.annotation.*;
import io.art.core.caster.*;
import io.art.core.collection.*;
import io.art.meta.configuration.*;
import io.art.meta.exception.*;
import io.art.meta.validator.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.model.MetaClass.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.meta.state.MetaComputationState.*;
import static java.lang.String.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@ToString
@Generation
@EqualsAndHashCode
public abstract class MetaLibrary {
    private ImmutableMap<Class<?>, MetaClass<?>> classes;
    private final Map<String, MetaPackage> packages = map();
    private final Set<MetaClass<?>> rootClasses = set();
    private final List<MetaLibrary> dependencies = linkedList();
    private final AtomicBoolean computed = new AtomicBoolean(false);

    protected MetaLibrary(MetaLibrary[] dependencies) {
        this.dependencies.addAll(linkedListOf(dependencies));
    }

    protected <Meta extends MetaPackage> Meta register(Meta metaPackage) {
        packages.put(metaPackage.name(), metaPackage);
        return metaPackage;
    }

    protected <Meta extends MetaClass<?>> Meta register(Meta metaClass) {
        rootClasses.add(metaClass);
        return metaClass;
    }

    public ImmutableMap<String, MetaPackage> packages() {
        return immutableMapOf(packages);
    }

    public ImmutableSet<MetaClass<?>> rootClasses() {
        return immutableSetOf(rootClasses);
    }

    public ImmutableMap<Class<?>, MetaClass<?>> classes() {
        return classes;
    }

    public <Meta extends MetaPackage> Optional<Meta> packageOf(String name) {
        String[] parts = name.split(ESCAPED_DOT);
        if (isEmpty(parts)) return empty();
        MetaPackage root = packages.get(parts[0]);
        if (isNull(root)) return empty();
        if (parts.length == 1) return of(cast(root));
        Optional<MetaPackage> found = root.packageOf(join(DOT, skip(1, parts)));
        if (found.isPresent()) return found.map(Caster::cast);
        return empty();
    }

    public void compute(MetaModuleConfiguration configuration) {
        if (computed.get()) return;
        List<MetaLibrary> registryDependencies = configuration.getDependencies()
                .get()
                .stream()
                .collect(listCollector());
        for (MetaLibrary dependency : registryDependencies) {
            dependency.computeLibrary();
        }
        computeLibrary();
        initializeBuiltinMetaTypes();
        clearClassMutableRegistry();
    }

    private void computeLibrary() {
        if (computed.compareAndSet(false, true)) {
            for (MetaLibrary dependency : this.dependencies) {
                dependency.computeLibrary();
            }

            rootClasses.forEach(MetaClass::beginComputation);
            packages.values().forEach(MetaPackage::beginComputation);

            ImmutableArray<ValidationResult> validationErrors = getValidationErrors();
            if (validationErrors.isEmpty()) {
                rootClasses.forEach(MetaClass::completeComputation);
                packages.values().forEach(MetaPackage::completeComputation);
                classes = getClassMutableRegistry();
                return;
            }

            StringBuilder validationErrorMessage = new StringBuilder(META_COMPUTATION_FAILED);
            validationErrors.forEach(error -> validationErrorMessage.append(error.getMessage()).append(NEW_LINE));
            throw new MetaException(validationErrorMessage.toString());
        }
    }
}
