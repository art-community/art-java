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

import * as ReactDOM from "react-dom";
import * as React from "react";
import {AUTHORIZED_STORE, MAIN_COMPONENT} from "./constants/Constants";
import {RoutingComponent} from "./component/routing/RoutingComponent";
import {putStore} from "./store/Store";

const main = () => {
    putStore(AUTHORIZED_STORE, false);
    ReactDOM.render(<RoutingComponent/>, document.getElementById(MAIN_COMPONENT));
};

declare var module: any;
if (module.hot) {
    module.hot.accept(main);
}
main();

Set.prototype.mapToArray = function <T, R>(this: Set<T>, functor: (value: T) => R): R[] {
    return Array.from(this).map(functor);
};

Set.prototype.addValue = function <T>(this: Set<T>, value: T): Set<T> {
    this.add(value);
    return new Set(this);
};

Set.prototype.deleteValue = function <T>(this: Set<T>, value: T): Set<T> {
    this.delete(value);
    return new Set(this);
};

Map.prototype.mapValuesToArray = function <K, V, R>(this: Map<K, V>, functor: (value: V) => R): R[] {
    return Array.from(this.values()).map(functor);
};

Map.prototype.deleteKey = function <K, V, R>(this: Map<K, V>, key: K): Map<K, V> {
    this.delete(key);
    return new Map(this);
};

Map.prototype.addValue = function <K, V, R>(this: Map<K, V>, key: K, value: V): Map<K, V> {
    this.set(key, value);
    return new Map(this);
};