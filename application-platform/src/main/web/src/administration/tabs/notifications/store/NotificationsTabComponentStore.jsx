import {ComponentStore} from "../../../../framework/store/ComponentStore";
import isEmpty from "licia/isEmpty";
import {safeSet} from "licia";

export class NotificationsTabComponentStore extends ComponentStore {
    applications = new Set();
    projectGroups = new Set();
    projects = new Set();
    selectedApplication;
    selectedProjectGroup;
    selectedProject;
    selectedApplicationNotifications = [];
    selectedProjectGroupNotifications = [];
    selectedProjectNotifications = [];
    events = [];

    addApplicationNotification = (id) => {
        if (isEmpty(id)) return false;
        this.selectedApplicationNotifications.push({id: id});
        return true;
    };

    deleteApplicationNotification = (index) => {
        if (index >= this.selectedApplicationNotifications.length || !this.selectedApplicationNotifications[index]) return;
        this.selectedApplicationNotifications = this.selectedApplicationNotifications.filter((value, index) => index !== index);
        return true
    };

    setApplicationNotificationProperty = (index, name, value) => {
        if (index >= this.selectedApplicationNotifications.length || !this.selectedApplicationNotifications[index]) return;
        let notification = this.selectedApplicationNotifications[index];
        safeSet(notification, name, value);
        this.selectedApplicationNotifications[index] = notification;
        return true
    };

    applicationNotificationsCount = () => this.selectedApplicationNotifications.length;


    addProjectGroupNotification= (id) => {
        if (isEmpty(id)) return false;
        this.selectedProjectGroupNotifications.push({id: id});
        return true;
    };

    deleteProjectGroupNotification = (index) => {
        if (index >= this.selectedProjectGroupNotifications.length || !this.selectedProjectGroupNotifications[index]) return;
        this.selectedProjectGroupNotifications = this.selectedProjectGroupNotifications.filter((value, index) => index !== index);
        return true
    };

    setProjectGroupNotificationProperty = (index, name, value) => {
        if (index >= this.selectedProjectGroupNotifications.length || !this.selectedProjectGroupNotifications[index]) return;
        let notification = this.selectedProjectGroupNotifications[index];
        safeSet(notification, name, value);
        this.selectedProjectGroupNotifications[index] = notification;
        return true
    };

    projectGroupNotificationsCount = () => this.selectedProjectGroupNotifications.length;


    addProjectNotification = (id) => {
        if (isEmpty(id)) return false;
        this.selectedProjectNotifications.push({id: id});
        return true;
    };

    deleteProjectNotification= (index) => {
        if (index >= this.selectedProjectNotifications.length || !this.selectedProjectNotifications[index]) return;
        this.selectedProjectNotifications = this.selectedProjectNotifications.filter((value, index) => index !== index);
        return true
    };

    setProjectNotificationProperty = (index, name, value) => {
        if (index >= this.selectedProjectNotifications.length || !this.selectedProjectNotifications[index]) return;
        let notification = this.selectedProjectNotifications[index];
        safeSet(notification, name, value);
        this.selectedProjectNotifications[index] = notification;
        return true
    };

    projectNotificationsCount = () => this.selectedProjectNotifications.length;

    toState = () => ({
        applications: Array.from(this.applications),
        projectGroups: Array.from(this.projectGroups),
        projects: Array.from(this.projects),
        selectedApplication: this.selectedApplication,
        selectedProjectGroup: this.selectedProjectGroup,
        selectedProject: this.selectedProject,
        selectedApplicationNotifications: this.selectedApplicationNotifications,
        selectedProjectGroupNotifications: this.selectedProjectGroupNotifications,
        selectedProjectNotifications: this.selectedProjectNotifications,
        events: this.events
    })
}
