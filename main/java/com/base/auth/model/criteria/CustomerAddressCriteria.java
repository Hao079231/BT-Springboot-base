package com.base.auth.model.criteria;

import com.base.auth.model.Customer;
import com.base.auth.model.CustomerAddress;
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
public class CustomerAddressCriteria {
  private Long id;
  private Long customerId;
  private String address;
  private Integer type;
  private Integer sortById;

  public Specification<CustomerAddress> getSpecification(){
    return new Specification<CustomerAddress>() {
      @Override
      public Predicate toPredicate(Root<CustomerAddress> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        Join<CustomerAddress, Customer> customerJoin = root.join("customer");
        if (getId() != null){
          predicates.add(cb.equal(root.get("id"), getId()));
        }
        if (getCustomerId() != null){
          predicates.add(cb.equal(customerJoin.get("id"), getCustomerId()));
        }
        if (getAddress() != null || !getAddress().isEmpty()){
          predicates.add(cb.like(cb.lower(root.get("address")), "%"+getAddress().toLowerCase()+"%"));
        }
        if (getType() != null){
          predicates.add(cb.equal(root.get("type"), getType()));
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
