package com.base.auth.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "db_user_base_order")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Order extends Auditable<String>{
  @Id
  @GenericGenerator(name = "idGenerator", strategy = "com.base.auth.service.id.IdGenerator")
  @GeneratedValue(generator = "idGenerator")
  private Long id;
  private String code;
  private Double totalMoney;
  private Integer totalSaleOff;
  @ManyToOne
  private Customer customer;
  private Integer state;
}
