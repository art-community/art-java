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