package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.nation.NationDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.nation.CreateNationForm;
import com.base.auth.form.nation.UpdateNationForm;
import com.base.auth.mapper.NationMapper;
import com.base.auth.model.Nation;
import com.base.auth.model.criteria.NationCriteria;
import com.base.auth.repository.CustomerAddressRepository;
import com.base.auth.repository.NationRepository;
import java.util.List;
import javax.transaction.Transactional;
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
@RequestMapping("/v1/nation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NationController extends ABasicController{
  @Autowired
  private NationRepository nationRepository;

  @Autowired
  private CustomerAddressRepository customerAddressRepository;

  @Autowired
  NationMapper nationMapper;

  @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('NA_C')")
  public ApiMessageDto<String> create(@Valid @RequestBody CreateNationForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Nation nation = nationRepository.findFirstByName(request.getName());
    if (nation != null){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Nation name already exists!");
      return apiMessageDto;
    }

    if (!isValidParent(request.getType(), request.getParentId())) {
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Invalid parent ID for the given nation type!");
      return apiMessageDto;
    }

    nation = nationMapper.fromCreateNation(request);
    if (request.getParentId() != null) {
      Nation parentNation = nationRepository.findById(request.getParentId()).orElse(null);
      nation.setParent(parentNation);
    } else {
      nation.setParent(null);
    }
    nationRepository.save(nation);
    apiMessageDto.setMessage("Create nation success");
    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('NA_L')")
  public ApiMessageDto<ResponseListDto<List<NationDto>>> getList(NationCriteria request, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<NationDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<Nation> nations = nationRepository.findAll(request.getSpecification(), pageable);
    List<NationDto> list = nationMapper.fromNationToDtoList(nations.getContent());
    ResponseListDto<List<NationDto>> pageResult = new ResponseListDto<>(list, nations.getTotalElements(), nations.getTotalPages());
    apiMessageDto.setMessage("List nation");
    apiMessageDto.setData(pageResult);
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('NA_V')")
  public ApiMessageDto<NationDto> getNation(@PathVariable Long id){
    Nation nation = nationRepository.findById(id).orElseThrow(()
        -> new NotFoundException("Nation id not found"));
    ApiMessageDto<NationDto> apiMessageDto = new ApiMessageDto<>();
    apiMessageDto.setMessage("Nation information");
    apiMessageDto.setData(nationMapper.fromNationToCompleteDto(nation));
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('NA_U')")
  public ApiMessageDto<String> update(@Valid @RequestBody UpdateNationForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Nation nation = nationRepository.findById(request.getId()).orElseThrow(()
        -> new NotFoundException("Nation id not found"));
    if (!nation.getName().equals(request.getName())){
      Nation existingNation = nationRepository.findFirstByName(request.getName());
      if (existingNation != null){
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage("Nation name already exists with a different ID!");
        return apiMessageDto;
      }
    }

    if (!isValidParent(request.getType(), request.getParentId())) {
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Invalid parent ID for the given nation type!");
      return apiMessageDto;
    }

    nationMapper.mappingForUpdateNation(request, nation);
    if (request.getParentId() != null) {
      Nation parentNation = nationRepository.findById(request.getParentId()).orElse(null);
      nation.setParent(parentNation);
    } else {
      nation.setParent(null);
    }
    nationRepository.save(nation);
    apiMessageDto.setMessage("Update nation success");
    return apiMessageDto;
  }

  @Transactional
  @DeleteMapping(value = "/delete/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('NA_D')")
  public ApiMessageDto<String> delete(@PathVariable Long id){
    Nation nation = nationRepository.findById(id).orElseThrow(()
        -> new NotFoundException("Nation id not found"));
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    if (customerAddressRepository.existsByProvinceOrDistrictOrCommune(nation.getId())){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Cannot delete nation because there are still customer living");
      return apiMessageDto;
    }
    if (nationRepository.existsByParentId(id)) {
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Cannot delete nation because it has child nations!");
      return apiMessageDto;
    }
    nationRepository.deleteById(id);
    apiMessageDto.setMessage("Delete nation success");
    return apiMessageDto;
  }

  private boolean isValidParent(Integer type, Long parentId) {
    if (type.equals(UserBaseConstant.NATION_TYPE_PROVINCE)) {
      return parentId == null;
    }
    if (parentId == null) {
      return false;
    }
    Nation parentNation = nationRepository.findById(parentId).orElse(null);
    if (parentNation == null) {
      return false;
    }
    return (type.equals(UserBaseConstant.NATION_TYPE_DISTRICT) && parentNation.getType().equals(UserBaseConstant.NATION_TYPE_PROVINCE)) ||
        (type.equals(UserBaseConstant.NATION_TYPE_COMMUNE) && parentNation.getType().equals(UserBaseConstant.NATION_TYPE_DISTRICT));
  }
}
