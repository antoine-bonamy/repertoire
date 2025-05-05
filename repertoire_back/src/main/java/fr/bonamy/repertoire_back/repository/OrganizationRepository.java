package fr.bonamy.repertoire_back.repository;

import fr.bonamy.repertoire_back.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query(value = """
            SELECT o.id, o.name, o.comment, o.user_id, u.firstname, u.lastname, u.email, u.password
            FROM ORGANIZATION o INNER JOIN USER u ON o.user_id= u.id
            WHERE (o.name LIKE CONCAT('%', :keyword, '%') OR o.comment LIKE CONCAT('%', :keyword, '%'))
            AND o.user_id = :userId
            """, nativeQuery = true)
    Page<Organization> search(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

}
