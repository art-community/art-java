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

import {registry} from "../../../../framework/registry/Registry";
import {ComponentController} from "../../../../framework/controller/ComponentController";
import {loadProjectGroups, loadProjects} from "../../../../service/AdministrationService";
import isEmpty from "licia/isEmpty";

export class ApplicationsTabComponentController extends ComponentController {
    selectApplication = (application) => {
        if (application === registry.applicationsTab.store.selectedApplication) return;
        registry.applicationsTab.store.selectedApplication = application;
        registry.applicationsTab.store.selectedProjectGroup = null;
        registry.applicationsTab.store.selectedProject = null;
        loadProjectGroups(application, groups => {
            if (isEmpty(groups)) {
                registry.applicationsTab.store.projectGroups = new Set();
                registry.applicationsTab.store.projects = new Set();
                registry.applicationsTab.store.apply();
                return;
            }
            registry.applicationsTab.store.projectGroups = new Set(groups);
            registry.applicationsTab.store.selectedProjectGroup = groups[0];
            loadProjects(application, groups[0], projects => {
                if (isEmpty(projects)) {
                    registry.applicationsTab.store.projects = new Set();
                    registry.applicationsTab.store.apply();
                    return;
                }
                registry.applicationsTab.store.projects = new Set(projects);
                registry.applicationsTab.store.selectedProject = projects[0];
                if (registry.projectConfiguration) {
                    registry.projectConfiguration.controller.loadProjectConfiguration(application, groups[0], projects[0]);
                }
                registry.applicationsTab.store.apply();
            });
        });
    };

    selectProjectGroup = (group) => {
        if (group === registry.applicationsTab.store.selectedProjectGroup) return;
        registry.applicationsTab.store.selectedProjectGroup = group;
        registry.applicationsTab.store.selectedProject = null;
        loadProjects(registry.applicationsTab.store.selectedApplication, group, projects => {
            if (isEmpty(projects)) {
                registry.applicationsTab.store.projects = new Set();
                registry.applicationsTab.store.apply();
                return;
            }
            registry.applicationsTab.store.projects = new Set(projects);
            registry.applicationsTab.store.selectedProject = projects[0];
            if (registry.projectConfiguration) {
                registry.projectConfiguration.controller.loadProjectConfiguration(registry.applicationsTab.store.selectedApplication, group, projects[0]);
            }
            registry.applicationsTab.store.apply();
        });
    };

    selectProject = (project) => {
        if (project === registry.applicationsTab.store.selectedProject) return;
        registry.applicationsTab.store.selectedProject = project;
        if (registry.projectConfiguration) {
            registry.projectConfiguration.controller.loadProjectConfiguration(registry.applicationsTab.store.selectedApplication, registry.applicationsTab.store.selectedProjectGroup, project);
        }
        registry.applicationsTab.store.apply();
    };

    getSelectedApplication = () => registry.applicationsTab.store.selectedApplication;
    getSelectedProjectGroup = () => registry.applicationsTab.store.selectedProjectGroup;
    getSelectedProject = () => registry.applicationsTab.store.selectedProject;
}