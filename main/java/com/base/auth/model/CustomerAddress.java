package com.base.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "db_user_base_customer_address")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CustomerAddress extends Auditable<String>{
  @GenericGenerator(name = "idGenerator", strategy = "com.base.auth.service.id.IdGenerator")
  @GeneratedValue(generator = "idGenerator")
  @Id
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "province_id")
  @JsonIgnore
  private Nation province;

  @ManyToOne
  @JoinColumn(name = "district_id")
  @JsonIgnore
  private Nation district;

  @ManyToOne
  @JoinColumn(name = "commune_id")
  @JsonIgnore
  private Nation commune;

  private String address;
  private Integer type;
  private Boolean isDefault;
}
