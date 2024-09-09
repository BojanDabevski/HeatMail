package diplomska.heatmail.repository;

import diplomska.heatmail.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
    
}
