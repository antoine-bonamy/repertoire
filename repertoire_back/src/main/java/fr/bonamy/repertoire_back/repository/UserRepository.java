package fr.bonamy.repertoire_back.repository;

import fr.bonamy.repertoire_back.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstname, String lastname, String email, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE USER SET password=:password WHERE id=:id", nativeQuery = true)
    void updatePassword(@Param("id") Long id, @Param("password") String password);

}
