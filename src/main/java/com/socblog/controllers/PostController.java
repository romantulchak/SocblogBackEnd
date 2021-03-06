package com.socblog.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.socblog.dto.PostByDateDTO;
import com.socblog.dto.PostDTO;
import com.socblog.dto.PostPageableDTO;
import com.socblog.models.Post;
import com.socblog.models.User;
import com.socblog.models.Views;
import com.socblog.services.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600)
@RequestMapping("/api/posts")
public class PostController {

    private PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService){
        this.postService = postService;
    }

    @PostMapping(value = "/createPost")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart("postDTO") String postString) throws IOException {
        return postService.createPost(postString, image);
    }


    @GetMapping("/myPosts/{userId}/{currentUserId}")
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.PostFull.class)
    public PostPageableDTO myPosts(@PathVariable("userId")User user, @RequestParam(value = "page", defaultValue = "0") int page, @PathVariable("currentUserId") User currentUser){
        return postService.getAllForUser(user, page, currentUser);
    }

    @DeleteMapping("/deletePost/{postId}/{userId}")
    @PreAuthorize("hasRole('USER') and @userSecurity.hasUserId(authentication, #post.user.id)")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Post post, @PathVariable("userId") User user){
        return postService.deletePost(post, user);

    }

    @GetMapping("/news/{userId}")
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.PostFull.class)
    public PostPageableDTO news (@PathVariable("userId") User user, @RequestParam(value = "page",defaultValue = "0") int page){
        return postService.getAllPost(user, page);
    }

    @GetMapping("/postsByTag/{tagName}/{userId}")
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.PostFull.class)
    public PostPageableDTO postsByTag(@PathVariable("tagName") String tagName, @RequestParam(value = "page", defaultValue = "0") int page, @PathVariable("userId") User currentUser){
        return postService.getPostsByTag(tagName, page, currentUser);

    }

    @GetMapping("/postsForChart/{userId}")
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.PostFull.class)
    public List<PostByDateDTO> postsForChart(@PathVariable("userId")User user){
        return postService.getPostsForChart(user);
    }
    @GetMapping("/getPostById/{postId}/{userId}")
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.PostFull.class)
    public PostDTO getPostById(@PathVariable("postId") Post post, @PathVariable("userId") User user){
        return postService.getPostsBy(post, user);
    }

    @MessageMapping("/setLike/{currentUserId}/{postId}")
    @SendTo("/topic/like/")
    @JsonView(Views.PostFull.class)
    public PostDTO setLike(@DestinationVariable("currentUserId")Long currentUser, @DestinationVariable("postId") Long post){
        return postService.setLike(post, currentUser);
    }

    @GetMapping("/explorePosts/{currentUserId}")
    @PreAuthorize("hasRole('USER')")
    @JsonView(Views.PostFull.class)
    public PostPageableDTO explorePosts(@PathVariable("currentUserId") User user, @RequestParam(value = "page", defaultValue = "0") int page){
        return postService.explorePosts(user, page);
    }

    @PutMapping("/editPost")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> editPost(@RequestPart(value = "image", required = false) MultipartFile image, @RequestPart("postDTO") String postString) throws IOException {
        return postService.editPost(image, postString);
    }
}
