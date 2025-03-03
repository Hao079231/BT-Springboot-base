package com.base.auth.repository;

import com.base.auth.model.CustomerAddress;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>,
    JpaSpecificationExecutor<CustomerAddress> {

  @Modifying
  @Query("UPDATE CustomerAddress c SET c.isDefault = false WHERE c.customer.id = :customerId")
  @Transactional
  void updateIsDefaultFalseByCustomerId(@Param("customerId") Long id);

  @Modifying
  @Query("DELETE FROM CustomerAddress c WHERE c.id = :id AND c.customer.id = :customerId")
  @Transactional
  void deleteByIdAndCustomerId(@Param("id") Long id, @Param("customerId") Long customerId);

  @Query("SELECT COUNT(ca) > 0 "
      + "FROM CustomerAddress ca "
      + "WHERE ca.province.id = :id OR ca.district.id = :id OR ca.commune.id = :id")
  boolean existsByProvinceOrDistrictOrCommune(@Param("id") Long id);

  Optional<CustomerAddress> findByIdAndCustomerId(Long id, long currentUser);
}
