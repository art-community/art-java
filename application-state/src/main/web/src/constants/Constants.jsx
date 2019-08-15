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

export const DEFAULT_URL = "http://1.1.1.2:5678";
export const EMPTY_STRING = "";
export const BASE_URL = module.hot ?  DEFAULT_URL : window.location.href;
export const STATE_PATH = module.hot ? '/' : '/state';
export const IMAGE_URL = `${BASE_URL}/image`;
export const IMAGE_PATH = name => module.hot ? name : `${IMAGE_URL}/${name.substring(name.lastIndexOf("/"))}`;
export const MAIN_COMPONENT = 'mainComponent';
export const TOKEN_COOKIE = 'TOKEN';
export const COOKIE_MAX_AGE = '86400';
export const PRIMARY_COLOR = 'violet';
export const SECONDARY_COLOR = 'orange';
export const REFRESH_RATE = 1000;
