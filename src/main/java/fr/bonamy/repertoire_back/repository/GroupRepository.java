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

    Page<Group> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Group> findByNameContainingIgnoreCaseOrCommentContainingIgnoreCase(
            String name, String comment, Pageable pageable);

    Page<Group> findByIsPublic(Boolean isPublic, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE `GROUP` SET is_public=:isPublic WHERE id=:id", nativeQuery = true)
    void updateIsPublic(@Param("id") Long id, @Param("isPublic") Boolean isPublic);

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

}
