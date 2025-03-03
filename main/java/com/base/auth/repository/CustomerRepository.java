package com.base.auth.repository;

import com.base.auth.model.Customer;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long>,
    JpaSpecificationExecutor<Customer> {

  @Modifying
  @Query("DELETE FROM Customer c WHERE c.id = :customerId")
  void deleteCustomerById(@Param("customerId") Long customerId);

  Optional<Customer> findByAccountUsername(String username);
}
