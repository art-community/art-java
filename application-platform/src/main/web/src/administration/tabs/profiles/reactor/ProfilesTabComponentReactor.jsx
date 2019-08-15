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

import {loadApplications, loadProfiles, saveProfiles} from "../../../../service/AdministrationService";
import {NEW_PROFILE} from "../constants/ProfilesTabComponentConstants";
import {registry} from "../../../../framework/registry/Registry";

export const reactors = {
    onMount: () => loadProfiles(profiles => {
        registry.profilesTab.store.profiles = profiles;
        loadApplications(applications => {
            registry.profilesTab.store.applications = applications;
            registry.profilesTab.store.apply();
        })
    }),

    onSelectApplication: (application) => {
        registry.profilesTab.store.selectedApplication = application;
        registry.profilesTab.store.apply()
    },

    onAddProfile: () => {
        registry.profilesTab.store.addProfile(`${NEW_PROFILE} ${registry.profilesTab.store.count() + 1}`);
        registry.profilesTab.store.apply()
    },

    onDeleteProfile: (index) => {
        registry.profilesTab.store.deleteProfile(index);
        registry.profilesTab.store.profiles = registry.profilesTab.store.profiles.map((profile, index) => profile.includes(`${NEW_PROFILE}`) ? `${NEW_PROFILE} ${index + 1}` : profile);
        registry.profilesTab.store.apply()
    },

    onProfileInput: (index, newProfile) => {
        registry.profilesTab.store.updateProfile(index, newProfile);
    },

    onSave: () => {
        saveProfiles(registry.profilesTab.store.selectedApplication, registry.profilesTab.store.profiles);
        registry.profilesTab.store.apply()
    }
};