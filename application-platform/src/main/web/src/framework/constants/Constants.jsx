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

export const DEFAULT_URL = "http://1.1.1.2:5678";
export const DEFAULT_IMAGE_URL = (defaultUrl = DEFAULT_URL) => `${DEFAULT_BASE_URL(defaultUrl)}/image`;
export const DEFAULT_IMAGE_PATH = (defaultUrl = DEFAULT_URL) => name => module.hot ? name : `${DEFAULT_IMAGE_URL(defaultUrl)}/${name.substring(name.lastIndexOf("/"))}`;
export const EMPTY_STRING = "";
export const EMPTY_TAG = <></>;
export const DEFAULT_BASE_URL = (defaultUrl = DEFAULT_URL) => module.hot ? defaultUrl : window.location.href;
export const MAIN_COMPONENT = 'mainComponent';