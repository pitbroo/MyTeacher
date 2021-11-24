package pl.pbrodziak.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import pl.pbrodziak.education.entity.user.User;

import java.util.List;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByEmail(String email);

    @RestResource(path = "/name", rel = "name")
    List<User> findByName(@Param("name")String name);

    @RestResource(path ="/role", rel="role")
    List<User> findByUserRoleOrderByEmail(@Param("role")String role);

}
