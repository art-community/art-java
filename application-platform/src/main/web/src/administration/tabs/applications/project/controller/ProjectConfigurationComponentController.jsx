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

import {ComponentController} from "../../../../../framework/controller/ComponentController";
import {loadEnvironments, loadProject, loadServersByEnvironments} from "../../../../../service/AdministrationService";
import {registry} from "../../../../../framework/registry/Registry";
import isEmpty from 'licia/isEmpty'
import contain from "licia/contain";

export class ProjectConfigurationComponentController extends ComponentController {
    loadProjectConfiguration = (application, group, project) => {
        loadProject(application, group, project, project => {
            if (isEmpty(project)) {
                registry.projectConfiguration.store.project = {};
                registry.projectConfiguration.store.apply();
                return
            }
            registry.projectConfiguration.store.project = project;
            loadEnvironments(environments => {
                if (isEmpty(environments)) {
                    registry.projectConfiguration.store.environments = new Set();
                    registry.projectConfiguration.store.servers = new Set();
                    registry.projectConfiguration.store.project.module.availableEnvironments = [];
                    registry.projectConfiguration.store.project.module.availableServers = [];
                    registry.projectConfiguration.store.apply();
                    return
                }
                registry.projectConfiguration.store.environments = new Set(environments);
                const newAvailableEnvironments = [];
                registry.projectConfiguration.store.project.module.availableEnvironments.forEach(environment => {
                    if (contain(environments, environment)) {
                        newAvailableEnvironments.push(environment);
                    }
                });
                registry.projectConfiguration.store.project.module.availableEnvironments = newAvailableEnvironments;
                loadServersByEnvironments(environments, servers => {
                    if (isEmpty(servers)) {
                        registry.projectConfiguration.store.servers = new Set();
                        registry.projectConfiguration.store.project.module.availableServers = [];
                        registry.projectConfiguration.store.apply();
                        return
                    }
                    const newAvailableServers = [];
                    registry.projectConfiguration.store.project.module.availableServers.forEach(server => {
                        if (contain(servers, server)) {
                            newAvailableServers.push(server);
                        }
                    });
                    registry.projectConfiguration.store.project.module.availableServers = newAvailableServers;
                    registry.projectConfiguration.store.servers = new Set(servers);
                    registry.projectConfiguration.store.apply();
                })
            });
        });
    }
}