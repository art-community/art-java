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

import {ComponentStore} from "../../../../framework/store/ComponentStore";
import isEmpty from "licia/isEmpty";

export class ApplicationsTabComponentStore extends ComponentStore {
    applications = new Set();
    projectGroups = new Set();
    projects = new Set();
    newApplication;
    newProjectGroup;
    newProject;
    selectedApplication;
    selectedProjectGroup;
    selectedProject;

    addApplication = (application) => {
        if (isEmpty(application) || this.applications.has(application)) return false;
        this.applications.add(application);
        return true
    };

    deleteApplication = (application) => {
        if (isEmpty(application) || !this.applications.has(application)) return false;
        this.applications.delete(application);
        return true
    };

    addProjectGroup = (group) => {
        if (isEmpty(group) || this.projectGroups.has(group)) return false;
        this.projectGroups.add(group);
        return true
    };

    deleteProjectGroup = (group) => {
        if (isEmpty(group) || !this.projectGroups.has(group)) return false;
        this.projectGroups.delete(group);
        return true
    };

    addProject = (project) => {
        if (isEmpty(project) || this.projects.has(project)) return false;
        this.projects.add(project);
        return true
    };

    deleteProject = (project) => {
        if (isEmpty(project) || !this.projects.has(project)) return false;
        this.projects.delete(project);
        return true
    };

    toState = () => ({
        applications: Array.from(this.applications),
        projectGroups: Array.from(this.projectGroups),
        projects: Array.from(this.projects),
        selectedApplication: this.selectedApplication,
        selectedProjectGroup: this.selectedProjectGroup,
        selectedProject: this.selectedProject
    })

}
