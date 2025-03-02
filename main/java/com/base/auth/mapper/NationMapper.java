package com.base.auth.mapper;

import com.base.auth.dto.nation.NationAdminDto;
import com.base.auth.dto.nation.NationDto;
import com.base.auth.form.nation.CreateNationForm;
import com.base.auth.form.nation.UpdateNationForm;
import com.base.auth.model.Nation;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface NationMapper {

  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "type", target = "type")
  @BeanMapping(ignoreByDefault = true)
  Nation fromCreateNation(CreateNationForm createNationForm);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "type", target = "type")
  @BeanMapping(ignoreByDefault = true)
  void mappingForUpdateNation(UpdateNationForm updateNationForm, @MappingTarget Nation nation);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "type", target = "type")
  @Mapping(source = "modifiedDate", target = "modifiedDate")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "status", target = "status")
  @Named("fromEntityToNationDto")
  @BeanMapping(ignoreByDefault = true)
  NationAdminDto fromEntityToNationDto(Nation nation);

  @IterableMapping(elementTargetType = NationAdminDto.class, qualifiedByName = "fromEntityToNationDto")
  List<NationAdminDto> fromEntityToDtoList(List<Nation> nations);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "type", target = "type")
  @Named("fromNationToCompleteDto")
  @BeanMapping(ignoreByDefault = true)
  NationDto fromNationToCompleteDto(Nation nation);

  @IterableMapping(elementTargetType = NationDto.class, qualifiedByName = "fromNationToCompleteDto")
  List<NationDto> fromNationToDtoList(List<Nation> nations);

}
