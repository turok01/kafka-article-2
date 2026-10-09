package com.igor.inventory.repository;

import com.igor.inventory.model.ProcessedOrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedOrderIdRepository extends JpaRepository<ProcessedOrderId, String> {

}
