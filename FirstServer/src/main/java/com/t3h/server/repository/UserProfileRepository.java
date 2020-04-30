package com.t3h.server.repository;

import com.t3h.server.model.database.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM user_profile WHERE " +
                    "username = :usernameTest LIMIT 1" )
    UserProfile findOneByUsernamePassword(
            @Param(value = "usernameTest") String usernameTest

    );
}
