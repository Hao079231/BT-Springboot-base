package com.base.auth.repository;

import com.base.auth.model.Customer;
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

  @Query("SELECT COUNT(c) > 0 "
      + "FROM Customer c "
      + "WHERE c.province.id = :id OR c.district.id = :id OR c.commune.id = :id")
  boolean existsByProvinceOrDistrictOrCommune(@Param("id") Long id);
}
