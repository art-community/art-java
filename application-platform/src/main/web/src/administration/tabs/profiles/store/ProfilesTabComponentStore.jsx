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