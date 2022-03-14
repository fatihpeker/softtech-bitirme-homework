package tr.softtech.patika.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.softtech.patika.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    User getUserByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findUserByUsername(String username);

}
