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

import {ComponentStore} from "../../../../framework/store/ComponentStore";
import isEmpty from 'licia/isEmpty'
import contain from 'licia/contain'

export class ProfilesTabComponentStore extends ComponentStore {
    selectedApplication;
    applications = [];
    profiles = [];

    addProfile = (profile) => {
        if (isEmpty(profile) || contain(this.profiles, profile)) return false;
        this.profiles.push(profile);
        return true;
    };

    deleteProfile = (deletingIndex) => {
        if (deletingIndex >= this.profiles.length || !this.profiles[deletingIndex]) return false;
        this.profiles = this.profiles.filter((profile, index) => deletingIndex !== index);
        return true;
    };

    updateProfile = (index, newProfile) => {
        this.profiles[index] = newProfile;
    };

    count = () => this.profiles.length;

    toState = () => ({
        profiles: this.profiles,
        applications: this.applications,
        selectedApplication: this.selectedApplication
    })
}