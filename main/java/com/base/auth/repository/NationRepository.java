package com.base.auth.repository;

import com.base.auth.model.Nation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {

  Nation findFirstByName(String name);
  Optional<Nation> findByIdAndType(Long id, int type);
}
