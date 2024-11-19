package com.philips.onespace.jpa.repository;

import com.philips.onespace.jpa.entity.ActionOwnersEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActionOwnersRepository extends JpaRepository<ActionOwnersEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE ActionOwnersEntity a SET a.status =:status, a.completedAtDateTime =:completedAtDateTime WHERE a.actionEntity.id =:actionId AND a.potentialOwner =:potentialOwner")
    int updateByActionIdAndOwner(@Param("status") String status,
                                 @Param("completedAtDateTime") LocalDateTime completedAtDateTime,
                                 @Param("actionId") UUID actionId,
                                 @Param("potentialOwner") UUID potentialOwner);

    @Query("SELECT a FROM ActionOwnersEntity a JOIN a.actionEntity e WHERE e.expiryDateTime < CURRENT_TIMESTAMP AND a.status <> 'expired'")
    List<ActionOwnersEntity> findExpiredActions();

    @Modifying
    @Transactional
    @Query("UPDATE ActionOwnersEntity a SET a.status =:status WHERE a.id = :id")
    int markAsExpired(@Param("id") UUID id,@Param("status") String status);
}
