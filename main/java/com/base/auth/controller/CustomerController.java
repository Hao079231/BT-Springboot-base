package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.customer.CustomerDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.mapper.CustomerMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Customer;
import com.base.auth.model.Group;
import com.base.auth.model.Nation;
import com.base.auth.model.criteria.CustomerCriteria;
import com.base.auth.repository.AccountRepository;
import com.base.auth.repository.CustomerRepository;
import com.base.auth.repository.GroupRepository;
import com.base.auth.repository.NationRepository;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CustomerController {
  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private NationRepository nationRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private CustomerMapper customerMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('C_C')")
  public ApiMessageDto<String> create(@Valid @RequestBody CreateCustomerForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Account account = accountRepository.findAccountByUsername(request.getUsername());
    if (account != null){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Username already exists!");
      return apiMessageDto;
    }

    Group group = groupRepository.findFirstByKind(UserBaseConstant.GROUP_KIND_CUSTOMER);

    Nation provinceNation = nationRepository.findByIdAndType(request.getProvinceId(),
        UserBaseConstant.NATION_TYPE_PROVINCE).orElseThrow(()
        -> new NotFoundException("Province id not found"));

    Nation districtNation = nationRepository.findByIdAndType(request.getDistrictId(), UserBaseConstant.NATION_TYPE_DISTRICT).orElseThrow(()
        -> new NotFoundException("District id not found"));

    Nation communeNation = nationRepository.findByIdAndType(request.getCommuneId(), UserBaseConstant.NATION_TYPE_COMMUNE).orElseThrow(()
        -> new NotFoundException("Commune id not found"));
    Customer customer = customerMapper.fromCreateToCustomer(request);
    customer.getAccount().setPassword(passwordEncoder.encode(request.getPassword()));
    customer.getAccount().setLastLogin(new Date());
    customer.getAccount().setGroup(group);
    customer.getAccount().setKind(UserBaseConstant.USER_KIND_USER);
    customer.setProvince(provinceNation);
    customer.setDistrict(districtNation);
    customer.setCommune(communeNation);
    customerRepository.save(customer);
    apiMessageDto.setMessage("Create customer success");
    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('C_L')")
  public ApiMessageDto<ResponseListDto<List<CustomerDto>>> getList(CustomerCriteria request, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<CustomerDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<Customer> customers = customerRepository.findAll(request.getSpecification(), pageable);
    List<CustomerDto> list = customerMapper.fromEntityToListCustomerDto(customers.getContent());
    ResponseListDto<List<CustomerDto>> pageResult = new ResponseListDto<>(list, customers.getTotalElements(), customers.getTotalPages());
    apiMessageDto.setMessage("List customer");
    apiMessageDto.setData(pageResult);
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('C_V')")
  public ApiMessageDto<CustomerDto> getCustomer(@PathVariable Long id){
    Customer customer = customerRepository.findById(id).orElseThrow(()
        -> new NotFoundException("Customer id not found"));
    ApiMessageDto<CustomerDto> apiMessageDto = new ApiMessageDto<>();
    apiMessageDto.setMessage("Customer information");
    apiMessageDto.setData(customerMapper.fromEntityToCustomerDto(customer));
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('C_U')")
  public ApiMessageDto<String> update(@Valid @RequestBody UpdateCustomerForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(()
        -> new NotFoundException("Customer id not found"));
    if (!customer.getAccount().getUsername().equals(request.getUsername())){
      Account account = accountRepository.findAccountByUsername(request.getUsername());
      if (account != null){
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage("Username already exists with a different ID!");
        return apiMessageDto;
      }
    }
    Group group = groupRepository.findFirstByKind(UserBaseConstant.GROUP_KIND_CUSTOMER);

    Nation provinceNation = nationRepository.findByIdAndType(request.getProvinceId(), UserBaseConstant.NATION_TYPE_PROVINCE).orElseThrow(()
        -> new NotFoundException("Province id not found"));

    Nation districtNation = nationRepository.findByIdAndType(request.getDistrictId(), UserBaseConstant.NATION_TYPE_DISTRICT).orElseThrow(()
        -> new NotFoundException("District id not found"));

    Nation communeNation = nationRepository.findByIdAndType(request.getCommuneId(), UserBaseConstant.NATION_TYPE_COMMUNE).orElseThrow(()
        -> new NotFoundException("Commune id not found"));
    customerMapper.mappingForUpdateCustomer(request, customer);
    customer.getAccount().setPassword(passwordEncoder.encode(request.getPassword()));
    customer.getAccount().setLastLogin(new Date());
    customer.getAccount().setGroup(group);
    customer.getAccount().setKind(UserBaseConstant.USER_KIND_USER);
    customer.setProvince(provinceNation);
    customer.setDistrict(districtNation);
    customer.setCommune(communeNation);
    customerRepository.save(customer);
    apiMessageDto.setMessage("Update customer success");
    return apiMessageDto;
  }

  @Transactional
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('C_D')")
  public ApiMessageDto<String> deleteCustomer(@PathVariable Long id) {
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Customer id not found"));

    customerRepository.deleteCustomerById(id);
    accountRepository.deleteAccountById(id);

    apiMessageDto.setMessage("Delete customer success");
    return apiMessageDto;
  }
}
