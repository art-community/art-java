import {ComponentStore} from "../../../../framework/store/ComponentStore";
import isEmpty from "licia/isEmpty";

export class ApplicationsTabComponentStore extends ComponentStore {
    applications = new Set();
    projectGroups = new Set();
    projects = new Set();
    newApplication;
    newProjectGroup;
    newProject;
    selectedApplication;
    selectedProjectGroup;
    selectedProject;

    addApplication = (application) => {
        if (isEmpty(application) || this.applications.has(application)) return false;
        this.applications.add(application);
        return true
    };

    deleteApplication = (application) => {
        if (isEmpty(application) || !this.applications.has(application)) return false;
        this.applications.delete(application);
        return true
    };

    addProjectGroup = (group) => {
        if (isEmpty(group) || this.projectGroups.has(group)) return false;
        this.projectGroups.add(group);
        return true
    };

    deleteProjectGroup = (group) => {
        if (isEmpty(group) || !this.projectGroups.has(group)) return false;
        this.projectGroups.delete(group);
        return true
    };

    addProject = (project) => {
        if (isEmpty(project) || this.projects.has(project)) return false;
        this.projects.add(project);
        return true
    };

    deleteProject = (project) => {
        if (isEmpty(project) || !this.projects.has(project)) return false;
        this.projects.delete(project);
        return true
    };

    toState = () => ({
        applications: Array.from(this.applications),
        projectGroups: Array.from(this.projectGroups),
        projects: Array.from(this.projects),
        selectedApplication: this.selectedApplication,
        selectedProjectGroup: this.selectedProjectGroup,
        selectedProject: this.selectedProject
    })

}
