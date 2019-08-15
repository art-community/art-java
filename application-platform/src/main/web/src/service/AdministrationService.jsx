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

export function loadApplications(onSuccess) {
    onSuccess(['CRM', 'OMS'])
}

export function loadProjectGroups(application, onSuccess) {
    onSuccess([`${application}-group`])
}

export function loadProjects(application, group, onSuccess) {
    onSuccess([`${application}-${group}-project`])
}

export function loadProfiles(onSuccess) {
    onSuccess(['crm-blue', 'crm-green'])
}

export function loadProject(application, group, project, onSuccess) {
    onSuccess(
        {
            id: project,
            name: `Проект ${project}`,
            gitLabUrl: `http://10.35.215.200/gitlab/${application}_${group}_${project}`,
            gitUrl: 'git@10.35.215.200',
            artifactoryUrl: `http://10.35.215.200/artifactory/${project}`,
            group: `Группа ${group}`,
            type: 'module',
            module: {
                id: 'CAMPAIGN_MODULE',
                role: 'state',
                availableEnvironments: ['crm-dev', 'crm-test'],
                availableServers: ['crm-prod-node-1', 'crm  -dev-node-2']
            }
        }
    )
}

export function loadEnvironments(onSuccess) {
    onSuccess(['crm-dev', 'crm-prod'])
}

export function loadClusters(environment, onSuccess) {
    onSuccess([`${environment}-blue`, `${environment}-green`])
}

export function loadServers(environment, cluster, onSuccess) {
    onSuccess([`${environment}-${cluster}-node-0`, `${environment}-${cluster}-node-1`])
}

export function loadServersByEnvironments(environments, onSuccess) {
    onSuccess(['crm-prod-node-1', 'crm  -dev-node-2'])
}


export function loadServer(environment, cluster, server, onSuccess) {
    onSuccess(
        {
            id: server,
            ipAddress: '127.0.0.1',
            availablePorts: '80,8000,10000-2000',
            cpuCount: 4,
            ramSize: '4gb',
            romSize: '500gb'
        })
}


export function loadApplicationNotifications(application, onSuccess) {
    onSuccess([
        {
            id: 'releaseNotification',
            name: 'Релизы',
            chatName: '[РТИ] Релизы',
            chatUrl: 'https://t.me/joinchat/AAAAAFJm7k15NMUTnN2Rww',
            chatHookUrl: 'https://integram.org/webhook/c4TSsqTq9we',
            events: ['RELEASE_EVENT', 'GITLAB_EVENT']
        }
    ])
}

export function loadProjectsGroupNotifications(application, group, onSuccess) {
    onSuccess([
        {
            id: 'adkNotifications',
            name: 'АДК Нотификации',
            chatName: '[РТИ] АДК Нотификации',
            chatUrl: 'https://t.me/joinchat/AAAAAE7oSfnS2x6nYhNxNA',
            chatHookUrl: 'https://integram.org/gitlab/cHbLqO9v8W7',
            events: ['CLUSTER_MANAGEMENT_EVENT', 'GITLAB_EVENT']
        }
    ])

}

export function loadProjectNotifications(application, group, project, onSuccess) {
    onSuccess([
        {
            id: 'adkNotifications',
            name: 'АДК Нотификации',
            chatName: '[РТИ] АДК Нотификации',
            chatUrl: 'https://t.me/joinchat/AAAAAE7oSfnS2x6nYhNxNA',
            chatHookUrl: 'https://integram.org/gitlab/cHbLqO9v8W7',
            events: ['CLUSTER_MANAGEMENT_EVENT', 'GITLAB_EVENT']
        }
    ])
}

export function loadEvents(onSuccess) {
    onSuccess([
        'GITLAB_EVENT',
        'RELEASE_EVENT',
        'CLUSTER_MANAGEMENT_EVENT'
    ])
}

export function saveProfiles(application, profiles) {

}

export function saveProject(application, group, project) {

}

export function saveEnvironments(environments) {

}

export function saveServer(environment, server) {

}