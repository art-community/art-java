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

export const PROJECT_TYPES = {
    MODULE: {
        name: 'Модуль',
        type: 'module'
    },
    LIBRARY: {
        name: 'Библиотека',
        type: 'library'
    }
};

export const MODULE_ROLES = {
    STATE: {
        name: 'Состояние',
        role: 'state'
    },
    CONFIGURATOR: {
        name: 'Конфигуратор',
        role: 'configurator'
    },
    SCHEDULER: {
        name: 'Шедулер',
        role: 'scheduler'
    },
    DB: {
        name: 'База данных',
        role: 'db'
    },
    SERVICE: {
        name: 'Сервис',
        role: 'service'
    }
};


export const PROJECT_TYPE_OPTIONS = [
    {value: PROJECT_TYPES.LIBRARY.type, text: PROJECT_TYPES.LIBRARY.name},
    {value: PROJECT_TYPES.MODULE.type, text: PROJECT_TYPES.MODULE.name},
];

export const MODULE_ROLE_OPTIONS = [
    {value: MODULE_ROLES.STATE.role, text: MODULE_ROLES.STATE.name},
    {value: MODULE_ROLES.CONFIGURATOR.role, text: MODULE_ROLES.CONFIGURATOR.name},
    {value: MODULE_ROLES.SCHEDULER.role, text: MODULE_ROLES.SCHEDULER.name},
    {value: MODULE_ROLES.DB.role, text: MODULE_ROLES.DB.name},
    {value: MODULE_ROLES.SERVICE.role, text: MODULE_ROLES.SERVICE.name},
];
