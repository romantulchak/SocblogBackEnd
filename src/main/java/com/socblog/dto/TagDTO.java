package com.socblog.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.socblog.models.Post;
import com.socblog.models.Tag;
import com.socblog.models.User;
import com.socblog.models.Views;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

public class TagDTO {
    @JsonView({Views.UserFull.class, Views.TagFull.class, Views.PostFull.class})
    private Long id;

    @JsonView({Views.UserFull.class, Views.TagFull.class, Views.PostFull.class})
    private String name;


    @JsonView({Views.PostFull.class, Views.TagFull.class})
    private User user;

    private List<Post> posts;

    @JsonView({Views.PostFull.class, Views.TagFull.class})
    private Integer postsCounter;

    public TagDTO() {

    }

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.user = tag.getUser();
        this.posts = tag.getPosts();
        this.postsCounter = tag.getPosts().size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Integer getPostsCounter() {
        return postsCounter;
    }

    public void setPostsCounter(Integer postsCounter) {
        this.postsCounter = postsCounter;
    }
}
