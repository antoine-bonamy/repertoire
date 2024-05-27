package fr.bonamy.repertoire_back.repository;

import fr.bonamy.repertoire_back.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query(value = """
            SELECT c.id, c.firstname, c.lastname, c.email, c.phone, c.address, c.comment, c.is_public,
            c.user_id, u.firstname AS user_firstname, u.lastname AS user_lastname, u.email AS user_email,
            c.organization_id, o.name, o.comment AS organization_comment, o.is_public AS organization_comment
            FROM CONTACT c INNER JOIN  USER u ON c.user_id = u.id INNER JOIN ORGANIZATION o on c.organization_id = o.id
            WHERE c.firstname LIKE CONCAT('%', :keyword, '%') OR c.lastname LIKE CONCAT('%', :keyword, '%')
            OR c.email LIKE CONCAT('%', :keyword, '%') OR c.phone LIKE CONCAT('%', :keyword, '%')
            OR c.address LIKE CONCAT('%', :keyword, '%')
            """, nativeQuery = true)
    Page<Contact> search(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
            SELECT c.id, c.firstname, c.lastname, c.email, c.phone, c.address, c.comment, c.is_public,
            c.user_id, u.firstname AS user_firstname, u.lastname AS user_lastname, u.email AS user_email,
            c.organization_id, o.name, o.comment AS organization_comment, o.is_public AS organization_comment
            FROM CONTACT c INNER JOIN  USER u ON c.user_id = u.id INNER JOIN ORGANIZATION o on c.organization_id = o.id
            WHERE c.user_id = :id
            """, nativeQuery = true)
    Page<Contact> findByUserId(@Param("id") Long id, Pageable pageable);

    @Query(value = """
            SELECT c.id, c.firstname, c.lastname, c.email, c.phone, c.address, c.comment, c.is_public,
            c.user_id, u.firstname AS user_firstname, u.lastname AS user_lastname, u.email AS user_email,
            c.organization_id, o.name, o.comment AS organization_comment, o.is_public AS organization_comment
            FROM CONTACT c INNER JOIN  USER u ON c.user_id = u.id INNER JOIN ORGANIZATION o on c.organization_id = o.id
            WHERE c.organization_id = :id
            """, nativeQuery = true)
    Page<Contact> findByOrganizationId(@Param("id") Long id, Pageable pageable);

    Page<Contact> findByIsPublic(Boolean isPublic, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CONTACT SET is_public=:isPublic WHERE id=:id", nativeQuery = true)
    void updateIsPublic(@Param("id") Long id, @Param("isPublic") Boolean isPublic);

}
