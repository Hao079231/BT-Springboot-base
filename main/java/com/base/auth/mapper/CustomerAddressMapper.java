package com.base.auth.mapper;

import com.base.auth.dto.customerAddress.CustomerAddressDto;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.form.customerAddress.CreateCustomerAddressForm;
import com.base.auth.form.customerAddress.UpdateCustomerAddressForm;
import com.base.auth.model.Customer;
import com.base.auth.model.CustomerAddress;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CustomerAddressMapper {
  @Mapping(source = "address", target = "address")
  @Mapping(source = "type", target = "type")
  @Mapping(source = "isDefault", target = "isDefault")
  @BeanMapping(ignoreByDefault = true)
  CustomerAddress fromCreateToCustomerAddress(CreateCustomerAddressForm createCustomerAddressForm);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "type", target = "type")
  @Mapping(source = "isDefault", target = "isDefault")
  @Named("mappingForUpdateCustomerAddress")
  @BeanMapping(ignoreByDefault = true)
  void mappingForUpdateCustomerAddress(
      UpdateCustomerAddressForm updateCustomerAddressForm, @MappingTarget CustomerAddress customerAddress);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "type", target = "type")
  @Mapping(source = "isDefault", target = "isDefault")
  @Named("fromCustomerAddressToDto")
  @BeanMapping(ignoreByDefault = true)
  CustomerAddressDto fromCustomerAddressToDto(CustomerAddress customerAddress);

  @IterableMapping(elementTargetType = CustomerAddressDto.class, qualifiedByName = "fromCustomerAddressToDto")
  List<CustomerAddressDto> fromCustomerAddressToListDto(List<CustomerAddress> customerAddresses);
}
