import {registry} from "../../../../framework/registry/Registry";
import {ComponentController} from "../../../../framework/controller/ComponentController";
import {loadProjectGroups, loadProjects} from "../../../../service/AdministrationService";
import isEmpty from "licia/isEmpty";

export class ApplicationsTabComponentController extends ComponentController {
    selectApplication = (application) => {
        if (application === registry.applicationsTab.store.selectedApplication) return;
        registry.applicationsTab.store.selectedApplication = application;
        registry.applicationsTab.store.selectedProjectGroup = null;
        registry.applicationsTab.store.selectedProject = null;
        loadProjectGroups(application, groups => {
            if (isEmpty(groups)) {
                registry.applicationsTab.store.projectGroups = new Set();
                registry.applicationsTab.store.projects = new Set();
                registry.applicationsTab.store.apply();
                return;
            }
            registry.applicationsTab.store.projectGroups = new Set(groups);
            registry.applicationsTab.store.selectedProjectGroup = groups[0];
            loadProjects(application, groups[0], projects => {
                if (isEmpty(projects)) {
                    registry.applicationsTab.store.projects = new Set();
                    registry.applicationsTab.store.apply();
                    return;
                }
                registry.applicationsTab.store.projects = new Set(projects);
                registry.applicationsTab.store.selectedProject = projects[0];
                if (registry.projectConfiguration) {
                    registry.projectConfiguration.controller.loadProjectConfiguration(application, groups[0], projects[0]);
                }
                registry.applicationsTab.store.apply();
            });
        });
    };

    selectProjectGroup = (group) => {
        if (group === registry.applicationsTab.store.selectedProjectGroup) return;
        registry.applicationsTab.store.selectedProjectGroup = group;
        registry.applicationsTab.store.selectedProject = null;
        loadProjects(registry.applicationsTab.store.selectedApplication, group, projects => {
            if (isEmpty(projects)) {
                registry.applicationsTab.store.projects = new Set();
                registry.applicationsTab.store.apply();
                return;
            }
            registry.applicationsTab.store.projects = new Set(projects);
            registry.applicationsTab.store.selectedProject = projects[0];
            if (registry.projectConfiguration) {
                registry.projectConfiguration.controller.loadProjectConfiguration(registry.applicationsTab.store.selectedApplication, group, projects[0]);
            }
            registry.applicationsTab.store.apply();
        });
    };

    selectProject = (project) => {
        if (project === registry.applicationsTab.store.selectedProject) return;
        registry.applicationsTab.store.selectedProject = project;
        if (registry.projectConfiguration) {
            registry.projectConfiguration.controller.loadProjectConfiguration(registry.applicationsTab.store.selectedApplication, registry.applicationsTab.store.selectedProjectGroup, project);
        }
        registry.applicationsTab.store.apply();
    };

    getSelectedApplication = () => registry.applicationsTab.store.selectedApplication;
    getSelectedProjectGroup = () => registry.applicationsTab.store.selectedProjectGroup;
    getSelectedProject = () => registry.applicationsTab.store.selectedProject;
}