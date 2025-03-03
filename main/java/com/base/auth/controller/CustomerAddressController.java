package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.customerAddress.CustomerAddressDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.customerAddress.CreateCustomerAddressForm;
import com.base.auth.form.customerAddress.UpdateCustomerAddressForm;
import com.base.auth.mapper.CustomerAddressMapper;
import com.base.auth.model.Customer;
import com.base.auth.model.CustomerAddress;
import com.base.auth.model.Nation;
import com.base.auth.model.criteria.CustomerAddressCriteria;
import com.base.auth.repository.CustomerAddressRepository;
import com.base.auth.repository.CustomerRepository;
import com.base.auth.repository.NationRepository;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customer-address")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CustomerAddressController extends ABasicController{
  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  private CustomerAddressMapper customerAddressMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private NationRepository nationRepository;

  @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ADR_C')")
  public ApiMessageDto<String> createAddress(@Valid @RequestBody CreateCustomerAddressForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(getCurrentUser()).orElseThrow(()
    -> new NotFoundException("User not found"));

    Nation provinceNation = nationRepository.findByIdAndType(request.getProvinceId(),
        UserBaseConstant.NATION_TYPE_PROVINCE).orElseThrow(()
    -> new NotFoundException("Province id not found"));

    Nation districtNation = nationRepository.findByIdAndType(request.getDistrictId(),
        UserBaseConstant.NATION_TYPE_DISTRICT).orElseThrow(()
        -> new NotFoundException("District id not found"));

    Nation communeNation = nationRepository.findByIdAndType(request.getCommuneId(),
        UserBaseConstant.NATION_TYPE_COMMUNE).orElseThrow(()
        -> new NotFoundException("Commune id not found"));

    if (districtNation.getParent() == null || !districtNation.getParent().getId().equals(provinceNation.getId())){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("The district must belong to the province");
      return apiMessageDto;
    }

    if (communeNation.getParent() == null || !communeNation.getParent().getId().equals(districtNation.getId())){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("The commune must belong to the district");
      return apiMessageDto;
    }

    if (request.getIsDefault()) {
      customerAddressRepository.updateIsDefaultFalseByCustomerId(customer.getId());
    }

    CustomerAddress customerAddress = customerAddressMapper.fromCreateToCustomerAddress(request);
    customerAddress.setCustomer(customer);
    customerAddress.setProvince(provinceNation);
    customerAddress.setDistrict(districtNation);
    customerAddress.setCommune(communeNation);
    customerAddressRepository.save(customerAddress);
    apiMessageDto.setMessage("Create customer address success");

    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ADR_L')")
  public ApiMessageDto<ResponseListDto<List<CustomerAddressDto>>> getListCustomerAddress(CustomerAddressCriteria request, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<CustomerAddressDto>>> apiMessageDto = new ApiMessageDto<>();
    request.setCustomerId(getCurrentUser());
    Page<CustomerAddress> customerAddresses = customerAddressRepository.findAll(request.getSpecification(), pageable);
    List<CustomerAddressDto> list = customerAddressMapper.fromCustomerAddressToListDto(customerAddresses.getContent());
    ResponseListDto<List<CustomerAddressDto>> pageResult = new ResponseListDto<>(list, customerAddresses.getTotalElements(), customerAddresses.getTotalPages());
    apiMessageDto.setMessage("List address");
    apiMessageDto.setData(pageResult);
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ADR_V')")
  public ApiMessageDto<CustomerAddressDto> getCustomerAddress(@PathVariable Long id){
    CustomerAddress customerAddress = customerAddressRepository.findByIdAndCustomerId(id, getCurrentUser()).orElseThrow(()
        -> new NotFoundException("Address not found for this user"));
    ApiMessageDto<CustomerAddressDto> apiMessageDto = new ApiMessageDto<>();
    apiMessageDto.setMessage("Address information");
    apiMessageDto.setData(customerAddressMapper.fromCustomerAddressToDto(customerAddress));
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ADR_U')")
  public ApiMessageDto<String> updateAddress(@Valid @RequestBody UpdateCustomerAddressForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(getCurrentUser()).orElseThrow(()
        -> new NotFoundException("User not found"));

    CustomerAddress customerAddress =  customerAddressRepository.findById(request.getId()).orElseThrow(()
    -> new NotFoundException("Address id not found"));

    Nation provinceNation = nationRepository.findByIdAndType(request.getProvinceId(),
        UserBaseConstant.NATION_TYPE_PROVINCE).orElseThrow(()
        -> new NotFoundException("Province id not found"));

    Nation districtNation = nationRepository.findByIdAndType(request.getDistrictId(),
        UserBaseConstant.NATION_TYPE_DISTRICT).orElseThrow(()
        -> new NotFoundException("District id not found"));

    Nation communeNation = nationRepository.findByIdAndType(request.getCommuneId(),
        UserBaseConstant.NATION_TYPE_COMMUNE).orElseThrow(()
        -> new NotFoundException("Commune id not found"));

    if (districtNation.getParent() == null || !districtNation.getParent().getId().equals(provinceNation.getId())){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("The district must belong to the province");
      return apiMessageDto;
    }

    if (communeNation.getParent() == null || !communeNation.getParent().getId().equals(districtNation.getId())){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("The commune must belong to the district");
      return apiMessageDto;
    }

    if (request.getIsDefault()) {
      customerAddressRepository.updateIsDefaultFalseByCustomerId(customer.getId());
    }

    customerAddressMapper.mappingForUpdateCustomerAddress(request, customerAddress);
    customerAddress.setCustomer(customer);
    customerAddress.setProvince(provinceNation);
    customerAddress.setDistrict(districtNation);
    customerAddress.setCommune(communeNation);
    customerAddressRepository.save(customerAddress);
    apiMessageDto.setMessage("Update customer address success");

    return apiMessageDto;
  }

  @DeleteMapping(value = "/delete/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ADR_D')")
  public ApiMessageDto<String> deleteCustomerAddress(@PathVariable Long id){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    CustomerAddress customerAddress = customerAddressRepository.findById(id).orElseThrow(()
    -> new NotFoundException("Customer address id not found"));
    Customer customer = customerRepository.findById(getCurrentUser()).orElseThrow(()
    -> new NotFoundException("Customer id not found"));
    customerAddressRepository.deleteByIdAndCustomerId(id, customer.getId());
    apiMessageDto.setMessage("Delete customer address success");
    return apiMessageDto;
  }
}
