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

export const EMPTY_STRING = "";
export const SLASH = "/";
export const CONFIGURATOR_PATH = "/configurator";
export const WEB_UI_PATH = '/ui';
export const BASE_URL = window.location.href.split(WEB_UI_PATH)[0];
export const API_URL = `${BASE_URL}/api`;
export const MAIN_COMPONENT = 'mainComponent';
export const TOKEN_COOKIE = 'TOKEN';
export const COOKIE_MAX_AGE = '86400';

export const PRIMARY_COLOR = 'violet';
export const SECONDARY_COLOR = 'orange';