package fr.bonamy.repertoire_back.repository;

import fr.bonamy.repertoire_back.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = """
            SELECT g.id, g.name, g.comment, g.user_id, u.firstname, u.lastname, u.email, u.password
            FROM `GROUP` g INNER JOIN USER u ON g.user_id= u.id
            WHERE (g.name LIKE CONCAT('%', :keyword, '%') OR g.comment LIKE CONCAT('%', :keyword, '%'))
            AND g.user_id = :userId
            """, nativeQuery = true)
    Page<Group> search(
            @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO LINK_CONTACT_GROUP (group_id, contact_id) VALUES (:groupId, :contactId);",
            nativeQuery = true)
    void addContact(@Param("groupId") Long groupId, @Param("contactId") Long contactId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM LINK_CONTACT_GROUP WHERE group_id=:groupId AND contact_id=:contactId",
            nativeQuery = true)
    void removeContact(@Param("groupId") Long groupId, @Param("contactId") Long contactId);

    @Query(value = """
            SELECT IF(EXISTS(
                SELECT * FROM LINK_CONTACT_GROUP lcg 
                WHERE lcg.group_id = :groupId AND lcg.contact_id = :contactId), TRUE, FALSE)""",
            nativeQuery = true)
    Long isContactInGroup(@Param("groupId") Long groupId, @Param("contactId") Long contactId);

}
