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