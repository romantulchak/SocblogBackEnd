package com.socblog.repo;

import com.socblog.dto.PostByDateDTO;
import com.socblog.models.Post;
import com.socblog.models.Tag;
import com.socblog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {



    @Query(value = "SELECT p FROM Post p where p.user = ?1 order by p.id DESC ")
    Page<Post> findAllForUser(User user, Pageable pageable);



   @Query(value = "SELECT p FROM Post p where p.user in (SELECT u FROM User u where u in :subscriptions) order by p.createdDate DESC ")
   Page<Post> findAllBySubscriptions(@Param("subscriptions") Set<User> subscriptions, Pageable pageable);

   @Query(value = "SELECT p FROM Post p join p.tags t where t.name = ?1 order by p.createdDate")
   Page<Post> findPostsByTagName(String tagName, Pageable pageable);


   @Query(value = "SELECT new com.socblog.dto.PostByDateDTO(p.createdDate, count (p)) FROM Post p where p.user = :user group by p.createdDate")
   List<PostByDateDTO> postsByDate(@Param("user")User user);


    @Query(value = "SELECT p FROM Post p left join p.tags t where p.user <> :user and t in :userInterests")
    Page<Post> findPostsDto(@Param("userInterests")Set<Tag> tags, @Param("user") User user, Pageable pageable);

    List<Post> findAllByNameContaining(String name);
}
