package com.base.auth.model.criteria;

import com.base.auth.model.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class CustomerCriteria {
  private Long id;
  private String username;
  private Integer sortById;
  public Specification<Customer> getSpecification(){
    return new Specification<Customer>() {
      @Override
      public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Join<Object, Object> accountJoin = root.join("account");
        if (getId() != null){
          predicates.add(cb.equal(root.get("id"), getId()));
        }
        if (getUsername() != null || !getUsername().isEmpty()){
          predicates.add(cb.like(cb.lower(accountJoin.get("username")), "%"+getUsername().toLowerCase()+"%"));
        }
        if (sortById != null) {
          if (sortById == 1) {
            criteriaQuery.orderBy(cb.asc(root.get("id")));
          } else {
            criteriaQuery.orderBy(cb.desc(root.get("id")));
          }
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    };
  }
}
