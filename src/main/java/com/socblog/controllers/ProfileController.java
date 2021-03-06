package com.socblog.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.socblog.dto.NotificationBoxDTO;
import com.socblog.dto.UserDTO;
import com.socblog.models.*;
import com.socblog.payload.request.ChangePasswordRequest;
import com.socblog.services.impl.ProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(value = "*", maxAge = 3600)
public class ProfileController {

    private ProfileServiceImpl profileService;

    @Autowired
    public ProfileController(ProfileServiceImpl profileService){
        this.profileService = profileService;
    }

    @GetMapping("/getUserData/{userId}")
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.UserFull.class)
    public UserDTO getUserData(@PathVariable("userId") Long userId){
        return profileService.getDataForUser(userId);
    }


    @PutMapping(value = "/setAvatar/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> setAvatar(@RequestPart("file") MultipartFile file, @RequestPart("avatar") String image, @PathVariable("userId") User user) throws IOException {
        return profileService.setUserAvatar(image, user, file);
    }

    @PutMapping("/updateUserData")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserData(@RequestBody User user, @RequestParam(value = "username") String username){
        return profileService.updateUserData(user, username);
    }



    @GetMapping("/userById/{userId}/{currentUserById}")
    @JsonView(Views.UserFull.class)
    public UserDTO userById(@PathVariable("userId") User user, @PathVariable("currentUserById") User userInSystem){
        return profileService.userById(user,userInSystem);
    }

    @PutMapping("/startFollowing/{userId}/{currentUserById}")
    public ResponseEntity<?> startFollowing(@PathVariable("userId") User user, @PathVariable("currentUserById") User currentUser){
        return profileService.startFollowing(user, currentUser);
    }

    @MessageMapping("/startFollow/")
    @SendTo("/topic/notification")
    public Long notification(String id){
        return Long.parseLong(id);
    }


    @GetMapping("/explore/{userId}")
    @JsonView(Views.UserFull.class)
    public Set<UserDTO> explorePeople(@PathVariable("userId") User user){
      return  profileService.explorePeople(user);

    }
    @PutMapping("/stopFollowing/{userId}/{currentUserById}")
    public ResponseEntity<?> stopFollowing(@PathVariable("userId") User user, @PathVariable("currentUserById") User currentUser){
        return profileService.stopFollowing(user, currentUser);
    }

    @GetMapping("/subscriptions/{userId}/{currentUser}")
    @JsonView(Views.UserSubscribeFull.class)
    public List<UserDTO> getSubscriptions(@PathVariable("userId") User user, @PathVariable("currentUser") User currentUser){
        return profileService.getSubscriptions(user, currentUser);
    }
    @GetMapping("/subscribers/{userId}/{currentUser}")
    @JsonView(Views.UserSubscribeFull.class)
    public List<UserDTO> getSubscribers(@PathVariable("userId") User user, @PathVariable("currentUser") User currentUser){
        return profileService.getSubscribers(user, currentUser);
    }
    @GetMapping("/getNotificationsForUser/{userId}")
    @JsonView(Views.NotificationFull.class)
    public NotificationBoxDTO getNotificationsForUser(@PathVariable("userId") User user){
        return profileService.getNotificationsForUser(user);
    }

    @PutMapping("/readNotification/{notificationBoxId}/{notificationId}")
    @JsonView(Views.UserFull.class)
    public NotificationBoxDTO readNotification(@PathVariable("notificationBoxId") NotificationBox notificationBox, @PathVariable("notificationId")Notification notification){
        return profileService.readNotification(notificationBox, notification);
    }

    @PutMapping("/addInterests")
    public ResponseEntity<?> addInterests(@RequestBody Tag tag, Principal principal){
        return profileService.addInterests(tag, principal.getName());
    }
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal){
        return profileService.changePassword(principal.getName(), changePasswordRequest.getOldPassword(),changePasswordRequest.getNewPassword());
    }
    @DeleteMapping("/deleteImage")
    @PreAuthorize("hasRole('USER') and @userSecurity.hasUserId(authentication, #image.user.id)")
    public ResponseEntity<?> deleteUserImage(@RequestParam(value = "userId") User user, @RequestParam(value = "imageId") Image image){
        return profileService.deleteUserImage(user, image);
    }

    @DeleteMapping("/removeAccount/{userId}")
    @PreAuthorize("hasRole('USER')")
    public void removeAccount(@PathVariable("userId") User user){
        profileService.removeAccount(user);
    }

}
