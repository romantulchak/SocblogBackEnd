package com.socblog.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.socblog.models.Notification;
import com.socblog.models.NotificationBox;
import com.socblog.models.User;
import com.socblog.models.Views;

import java.util.*;
import java.util.stream.Collectors;

public class NotificationBoxDTO {

    @JsonView(Views.UserFull.class)
    private Long id;

    @JsonView(Views.UserFull.class)
    private List<Notification> notifications;

    @JsonView(Views.UserFull.class)
    private Long notificationCounter;


    public NotificationBoxDTO(NotificationBox notificationBox) {
        this.id = notificationBox.getId();
        this.notifications = notificationBox.getNotifications();
        Collections.reverse(this.notifications);
        this.notificationCounter = notificationBox.getNotifications().stream().filter(x->!x.getRead()).count();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Long getNotificationCounter() {
        return notificationCounter;
    }

    public void setNotificationCounter(Long notificationCounter) {
        this.notificationCounter = notificationCounter;
    }
}