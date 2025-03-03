package com.base.auth.service;

import com.base.auth.model.Nation;
import com.base.auth.repository.NationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NationService {
  @Autowired
  private NationRepository nationRepository;

  public boolean isValidParent(Integer type, Long parentId) {
    if (type == 0) {
      return parentId == null;
    }
    if (parentId == null) {
      return false;
    }
    Nation parentNation = nationRepository.findById(parentId).orElse(null);
    if (parentNation == null) {
      return false;
    }
    return (type == 1 && parentNation.getType() == 0) || (type == 2 && parentNation.getType() == 1);
  }
}
