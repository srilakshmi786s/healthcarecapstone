package com.edutech.progressive.controller;

import com.edutech.progressive.entity.Notification;
import com.edutech.progressive.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(@RequestParam("userId") Integer userId) {
        return new ResponseEntity<>(notificationService.getUserNotifications(userId), HttpStatus.OK);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Integer id) {
        notificationService.markAsRead(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(@RequestParam("userId") Integer userId) {
        notificationService.markAllAsRead(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
