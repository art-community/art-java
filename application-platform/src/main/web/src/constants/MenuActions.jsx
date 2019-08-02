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

import {
    ADMINISTRATION_ROUTER,
    BALANCING_MANAGEMENT_ROUTER,
    CLUSTER_MANAGEMENT_ROUTER,
    CONFIGURATION_MANAGEMENT_ROUTER,
    PROJECT_MANAGEMENT_ROUTER,
    USER_CARD_ROUTER
} from "./Routers";
import React from "react";
import ConfigurationManagementComponent from "../configuration/ConfigurationManagementComponent";
import BalancingManagementComponent from "../balancing/BalancingManagementComponent";
import ClusterManagementComponent from "../cluster/ClusterManagementComponent";
import AdministrationComponent from "../administration/component/AdministrationComponent";
import UserCardComponent from "../user/UserCardComponent";
import ProjectManagementComponent from "../release/ReleaseManagementComponent";

export let PROJECT_MANAGEMENT_ACTION = {
    name: 'Управление проектами',
    icon: 'code branch',
    router: PROJECT_MANAGEMENT_ROUTER,
    component: () => <ProjectManagementComponent/>
};

export let CONFIGURATION_MANAGEMENT_ACTION = {
    name: 'Управление конфигурациями',
    icon: 'wrench',
    router: CONFIGURATION_MANAGEMENT_ROUTER,
    component: () => <ConfigurationManagementComponent/>
};

export let BALANCING_MANAGEMENT_ACTION = {
    name: 'Управление балансировкой',
    icon: 'balance scale',
    router: BALANCING_MANAGEMENT_ROUTER,
    component: () => <BalancingManagementComponent/>
};

export let CLUSTER_MANAGEMENT_ACTION = {
    name: 'Управление кластером',
    icon: 'upload',
    router: CLUSTER_MANAGEMENT_ROUTER,
    component: () => <ClusterManagementComponent/>
};

export let ADMINISTRATION_ACTION = {
    name: 'Администрирование',
    icon: 'server',
    router: ADMINISTRATION_ROUTER,
    component: () => <AdministrationComponent/>
};

export let USER_CARD_ACTION = {
    name: 'Пользователь',
    icon: 'address card',
    router: USER_CARD_ROUTER,
    component: () => <UserCardComponent/>
};

export let MENU_ACTIONS =
    [
        PROJECT_MANAGEMENT_ACTION,
        CONFIGURATION_MANAGEMENT_ACTION,
        BALANCING_MANAGEMENT_ACTION,
        CLUSTER_MANAGEMENT_ACTION,
        USER_CARD_ACTION,
        ADMINISTRATION_ACTION
    ];