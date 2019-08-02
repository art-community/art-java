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

import React from "react";
import {DEFAULT_BASE_URL, DEFAULT_IMAGE_PATH, DEFAULT_IMAGE_URL} from "../framework/constants/Constants";

export const DEFAULT_URL = "http://1.1.1.2:5678";
export const BASE_URL = DEFAULT_BASE_URL(DEFAULT_URL);
export const IMAGE_URL = DEFAULT_IMAGE_URL(DEFAULT_URL);
export const IMAGE_PATH = DEFAULT_IMAGE_PATH(DEFAULT_URL);
export const PRIMARY_COLOR = 'violet';
export const SECONDARY_COLOR = 'orange';
export const TOKEN_COOKIE = 'TOKEN';
export const COOKIE_MAX_AGE = '86400';
export const PLATFORM_PATH = '/platform/web';