package org.guvi.repo;

import org.apache.catalina.User;
import org.guvi.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
//    Users findByUsername(String username);
    Optional<Users> findByEmailIgnoreCase(String email);
    boolean existsByIdAndActive(String Id, boolean active);
    boolean existsByEmailIgnoreCase(String email);
}
