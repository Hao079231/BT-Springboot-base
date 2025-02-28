package com.base.auth.model.criteria;

import com.base.auth.model.Account;
import com.base.auth.model.Customer;
import com.base.auth.model.Order;
import com.base.auth.model.OrderItem;
import com.base.auth.model.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class OrderItemCriteria {
  private Long id;
  private String productName;
  private Integer sortById;

  public Specification<OrderItem> getSpecification() {
    return (Root<OrderItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      Join<OrderItem, Product> productJoin = root.join("product");

      if (id != null) {
        predicates.add(cb.equal(productJoin.get("id"), id));
      }

      if (getProductName() != null && !getProductName().isEmpty()) {
        predicates.add(cb.like(cb.lower(productJoin.get("name")), "%" + getProductName().toLowerCase() + "%"));
      }

      if (sortById != null) {
        if (sortById == 1) {
          query.orderBy(cb.asc(productJoin.get("id")));
        } else {
          query.orderBy(cb.desc(productJoin.get("id")));
        }
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
