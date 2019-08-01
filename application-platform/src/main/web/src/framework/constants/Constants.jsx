import React from "react";

export const DEFAULT_URL = "http://1.1.1.2:5678";
export const DEFAULT_IMAGE_URL = (defaultUrl = DEFAULT_URL) => `${DEFAULT_BASE_URL(defaultUrl)}/image`;
export const DEFAULT_IMAGE_PATH = (defaultUrl = DEFAULT_URL) => name => module.hot ? name : `${DEFAULT_IMAGE_URL(defaultUrl)}/${name.substring(name.lastIndexOf("/"))}`;
export const EMPTY_STRING = "";
export const EMPTY_TAG = <></>;
export const DEFAULT_BASE_URL = (defaultUrl = DEFAULT_URL) => module.hot ? defaultUrl : window.location.href;
export const MAIN_COMPONENT = 'mainComponent';