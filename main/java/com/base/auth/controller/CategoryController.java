package com.base.auth.controller;

import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.category.CategoryDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.category.CreateCategoryForm;
import com.base.auth.form.category.UpdateCategoryForm;
import com.base.auth.mapper.CategoryMapper;
import com.base.auth.model.Category;
import com.base.auth.model.criteria.CategoryCriteria;
import com.base.auth.repository.CategoryRepository;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController {
  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private CategoryMapper categoryMapper;

  @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CA_C')")
  public ApiMessageDto<String> create(@Valid @RequestBody CreateCategoryForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Category category = categoryRepository.findFirstByName(request.getName());
    if (category != null){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Category name already exists!");
      return apiMessageDto;
    }

    category = categoryMapper.fromCreateCategory(request);
    categoryRepository.save(category);
    apiMessageDto.setMessage("Create category success");
    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CA_L')")
  public ApiMessageDto<ResponseListDto<List<CategoryDto>>> getList(CategoryCriteria request, Pageable pageable){
    ApiMessageDto< ResponseListDto<List<CategoryDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<Category> categories = categoryRepository.findAll(request.getSpecification(), pageable);
    List<CategoryDto> response = categoryMapper.fromCategoryToComplteDtoList(categories.getContent());

    ResponseListDto<List<CategoryDto>> pageResults = new ResponseListDto<>(response, categories.getTotalElements(), categories.getTotalPages());
    apiMessageDto.setMessage("List category");
    apiMessageDto.setData(pageResults);
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CA_V')")
  public ApiMessageDto<CategoryDto> getCategoryById(@PathVariable("id") Long id){
    Category category = categoryRepository.findById(id).orElseThrow(() ->
        new NotFoundException("Category id not found"));
    ApiMessageDto<CategoryDto> apiMessageDto = new ApiMessageDto<>();
    apiMessageDto.setMessage("Category Information");
    apiMessageDto.setData(categoryMapper.fromCategoryToCompleteDto(category));
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CA_U')")
  public ApiMessageDto<String> update(@Valid @RequestBody UpdateCategoryForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
        new NotFoundException("Category id not found"));
    if (!category.getName().equals(request.getName())){
      Category existingCategory = categoryRepository.findFirstByName(request.getName());
      if (existingCategory != null) {
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage("Category name already exists with a different ID!");
        return apiMessageDto;
      }
    }

    categoryMapper.mappingForUpdateServiceCategory(request, category);
    categoryRepository.save(category);
    apiMessageDto.setMessage("Update category success");
    return apiMessageDto;
  }

  @DeleteMapping(value = "/delete/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CA_D')")
  public ApiMessageDto<String> deleteCategory(@PathVariable("id") Long id){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Category category = categoryRepository.findById(id).orElseThrow(() ->
        new NotFoundException("Category id not found"));
    categoryRepository.deleteById(id);
    apiMessageDto.setMessage("Delete category success");
    return apiMessageDto;
  }
}
