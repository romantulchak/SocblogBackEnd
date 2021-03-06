package com.socblog.repo;

import com.socblog.dto.UserDTO;
import com.socblog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllBySubscriptions(User user);

    List<User> findAllBySubscribers(User user);

    @Query(value = "SELECT u FROM User  u where u.id <> :id and (u.country = :country or u.city = :city) and (:user not member of u.subscribers) ")
    List<User> explorePeople(@Param("id") Long user, @Param("country") String country, @Param("city") String city, @Param("user") User sub);

    @Query(value = "SELECT u FROM User u where u.id <> :id and (:user not member of u.subscribers)")
    Page<User> findAllForUser(@Param("id") Long userId, @Param("user") User user, Pageable pageable);

    List<User> findAllByUsernameContaining(@NotBlank @Size(max = 25, min = 3) String username);
}
