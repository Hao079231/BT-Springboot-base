package com.base.auth.mapper;

import com.base.auth.dto.customer.CustomerDto;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.model.Customer;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
  @Mapping(source = "username", target = "account.username")
  @Mapping(source = "email", target = "account.email")
  @Mapping(source = "phone", target = "account.phone")
  @Mapping(source = "fullName", target = "account.fullName")
  @Mapping(source = "avatarPath", target = "account.avatarPath")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "address", target = "address")
  @BeanMapping(ignoreByDefault = true)
  Customer fromCreateToCustomer(CreateCustomerForm createCustomerForm);

  @Mapping(source = "username", target = "account.username")
  @Mapping(source = "email", target = "account.email")
  @Mapping(source = "phone", target = "account.phone")
  @Mapping(source = "fullName", target = "account.fullName")
  @Mapping(source = "avatarPath", target = "account.avatarPath")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "address", target = "address")
  @Named("mappingForUpdateCustomer")
  @BeanMapping(ignoreByDefault = true)
  void mappingForUpdateCustomer(UpdateCustomerForm updateCustomerForm, @MappingTarget Customer customer);

  @Mapping(source = "account", target = "accountDto", qualifiedByName = "fromAccountToDto")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "gender", target = "gender")
  @Mapping(source = "address", target = "address")
  @Named("fromCustomerToDto")
  CustomerDto fromEntityToCustomerDto(Customer customer);

  @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "fromCustomerToDto")
  List<CustomerDto> fromEntityToListCustomerDto(List<Customer> customers);
}
