package com.igor.inventory.repository;

import com.igor.inventory.model.OutboxEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findBySentFalseOrderByCreatedAtAsc(Pageable pageable);

}
