package com.base.auth.model;

import javax.persistence.Column;
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
@Table(name = "db_user_base_nation")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Nation extends Auditable<String>{
  @Id
  @GenericGenerator(name = "idGenerator", strategy = "com.base.auth.service.id.IdGenerator")
  @GeneratedValue(generator = "idGenerator")
  private Long id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "type")
  private Integer type;
  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Nation parent;
}
