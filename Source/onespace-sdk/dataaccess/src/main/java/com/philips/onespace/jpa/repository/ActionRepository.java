package com.philips.onespace.jpa.repository;

import com.philips.onespace.jpa.entity.ActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ActionRepository extends JpaRepository<ActionEntity, UUID> {

    @Query("SELECT a FROM ActionEntity a JOIN a.actionOwnersEntities p WHERE p.potentialOwner = :actionOwnerId")
    List<ActionEntity> findByPotentialOwner(@Param("actionOwnerId") UUID actionOwnerId);
}
