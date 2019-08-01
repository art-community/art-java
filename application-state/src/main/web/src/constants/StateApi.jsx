import {BASE_URL} from "./Constants";

export const GET_CLUSTER_PROFILE_URL = (profile) => `${BASE_URL}/state/getClusterProfile/${profile}`;
export const GET_PROFILES_URL = `${BASE_URL}/state/profiles`;