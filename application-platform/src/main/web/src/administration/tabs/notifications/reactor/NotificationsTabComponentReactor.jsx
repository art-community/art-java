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
    loadApplicationNotifications,
    loadApplications,
    loadEvents,
    loadProjectGroups,
    loadProjectNotifications,
    loadProjects,
    loadProjectsGroupNotifications
} from "../../../../service/AdministrationService";
import {registry} from "../../../../framework/registry/Registry";
import isEmpty from 'licia/isEmpty'
import {NEW_NOTIFICATION} from "../constants/NotificationsTabComponentConstants";

export const reactors = {
    onMount: () => {
        loadApplications(applications => {
            registry.notificationsTab.store.applications = new Set(applications);
            loadEvents(events => {
                registry.notificationsTab.store.events = events;
                registry.notificationsTab.store.apply()
            });
        })
    },

    onSelectApplication: (application) => {
        registry.notificationsTab.store.selectedApplication = application;
        loadApplicationNotifications(application, notifications => {
            registry.notificationsTab.store.selectedApplicationNotifications = notifications;
            loadProjectGroups(application, groups => {
                if (isEmpty(groups)) {
                    registry.notificationsTab.store.projectGroups = [];
                    registry.notificationsTab.store.projects = [];
                    registry.notificationsTab.store.selectedProject = null;
                    registry.notificationsTab.store.selectedProjectGroup = null;
                    registry.notificationsTab.store.apply();
                    return
                }
                registry.notificationsTab.store.projectGroups = new Set(groups);
                registry.notificationsTab.store.apply()
            })
        });
    },

    onSelectProjectGroup: (group) => {
        registry.notificationsTab.store.selectedProjectGroup = group;
        loadProjectsGroupNotifications(registry.notificationsTab.store.selectedApplication, group, notifications => {
            registry.notificationsTab.store.selectedProjectGroupNotifications = notifications;
            loadProjects(registry.notificationsTab.store.selectedApplication, group, projects => {
                if (isEmpty(projects)) {
                    registry.notificationsTab.store.projects = [];
                    registry.notificationsTab.store.selectedProject = null;
                    registry.notificationsTab.store.apply();
                    return
                }
                registry.notificationsTab.store.projects = new Set(projects);
                registry.notificationsTab.store.apply()
            })
        });
    },

    onSelectProject: (project) => {
        registry.notificationsTab.store.selectedProject = project;
        loadProjectNotifications(registry.notificationsTab.store.selectedApplication, registry.notificationsTab.store.selectedProjectGroupNotifications, project, notifications => {
            registry.notificationsTab.store.selectedProjectNotifications = notifications;
            registry.notificationsTab.store.apply();
        })
    },


    onAddApplicationNotification: () => {
        registry.notificationsTab.store.addApplicationNotification(`${NEW_NOTIFICATION} ${registry.notificationsTab.store.applicationNotificationsCount()}`);
        registry.notificationsTab.store.apply()
    },

    onDeleteApplicationNotification: (index) => {
        registry.notificationsTab.store.deleteApplicationNotification(index);
        registry.notificationsTab.store.selectedApplicationNotifications = registry.notificationsTab.store.selectedApplicationNotifications.map((notification, index) => profile.includes(`${NEW_NOTIFICATION}`) ? `${NEW_NOTIFICATION} ${index + 1}` : notification);
        registry.notificationsTab.store.apply()
    },


    onAddProjectGroupNotification: () => {
        registry.notificationsTab.store.addProjectGroupNotification(`${NEW_NOTIFICATION} ${registry.notificationsTab.store.projectGroupNotificationsCount()}`);
        registry.notificationsTab.store.apply()
    },

    onDeleteProjectGroupNotification: (index) => {
        registry.notificationsTab.store.deleteProjectGroupNotification(index);
        registry.notificationsTab.store.selectedProjectGroupNotifications = registry.notificationsTab.store.selectedProjectGroupNotifications.map((notification, index) => profile.includes(`${NEW_NOTIFICATION}`) ? `${NEW_NOTIFICATION} ${index + 1}` : notification);
        registry.notificationsTab.store.apply()
    },


    onAddProjectNotification: () => {
        registry.notificationsTab.store.addProjectNotification(`${NEW_NOTIFICATION} ${registry.notificationsTab.store.projectNotificationsCount()}`);
        registry.notificationsTab.store.apply()
    },

    onDeleteProjectNotification: (index) => {
        registry.notificationsTab.store.deleteProjectNotification(index);
        registry.notificationsTab.store.selectedProjectNotifications = registry.notificationsTab.store.selectedProjectNotifications.map((notification, index) => profile.includes(`${NEW_NOTIFICATION}`) ? `${NEW_NOTIFICATION} ${index + 1}` : notification);
        registry.notificationsTab.store.apply()
    },


    onApplicationNotificationPropertyInput: (index, name, value) => registry.notificationsTab.store.setApplicationNotificationProperty(index, name, value),

    onProjectGroupNotificationPropertyInput: (index, name, value) => registry.notificationsTab.store.setProjectGroupNotificationProperty(index, name, value),

    onProjectNotificationPropertyInput: (index, name, value) => registry.notificationsTab.store.setProjectNotificationProperty(index, name, value),

    onSave: () => {
    }
};