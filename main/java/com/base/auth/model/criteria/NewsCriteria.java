package com.base.auth.model.criteria;

import com.base.auth.model.News;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class NewsCriteria{
  private Long id;
  private String title;
  private Integer sortById;

  public Specification<News> getSpecification(){
    return new Specification<News>() {
      @Override
      public Predicate toPredicate(Root<News> root, CriteriaQuery<?> criteriaQuery,
          CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (getId() != null){
          predicates.add(cb.equal(root.get("id"), getId()));
        }
        if (getTitle() != null || !getTitle().isEmpty()){
          predicates.add(cb.like(cb.lower(root.get("title")), "%"+getTitle().toLowerCase()+"%"));
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
