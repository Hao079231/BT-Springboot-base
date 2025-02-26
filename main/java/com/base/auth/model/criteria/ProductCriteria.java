package com.base.auth.model.criteria;

import com.base.auth.model.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class ProductCriteria {
  private Long id;
  private String name;
  private Integer sortById;

  public Specification<Product> getSpecification(){
    return new Specification<Product>() {
      @Override
      public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (getId() != null){
          predicates.add(cb.equal(root.get("id"), getId()));
        }
        if (getName() != null || !getName().isEmpty()){
          predicates.add(cb.like(cb.lower(root.get("name")), "%"+getName().toLowerCase()+"%"));
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
