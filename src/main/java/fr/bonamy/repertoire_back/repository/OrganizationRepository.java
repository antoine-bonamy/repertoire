package fr.bonamy.repertoire_back.repository;

import fr.bonamy.repertoire_back.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query(value = """
            SELECT o.id, o.name, o.comment, o.is_public, o.user_id, u.firstname, u.lastname, u.email, u.password
            FROM ORGANIZATION o INNER JOIN USER u ON o.user_id= u.id
            WHERE u.firstname LIKE CONCAT('%', :name, '%') OR u.lastname LIKE CONCAT('%', :name, '%')
            """, nativeQuery = true)
    Page<Organization> findByUserFirstNameAndUserLastName(@Param("name") String name, Pageable pageable);

    Page<Organization> findByNameContainingIgnoreCaseOrCommentContainingIgnoreCase(String name, String comment,
                                                                                   Pageable pageable);

    Page<Organization> findByIsPublic(Boolean isPublic, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ORGANIZATION SET is_public=:isPublic WHERE id=:id", nativeQuery = true)
    void updateIsPublic(@Param("id") Long id, @Param("isPublic") Boolean isPublic);
}
