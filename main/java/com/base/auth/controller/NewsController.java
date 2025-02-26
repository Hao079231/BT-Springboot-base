package com.base.auth.controller;

import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.news.NewsAutoCompleteDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.news.CreateNewsForm;
import com.base.auth.form.news.UpdateNewsForm;
import com.base.auth.mapper.NewsMapper;
import com.base.auth.model.Category;
import com.base.auth.model.News;
import com.base.auth.model.criteria.NewsCriteria;
import com.base.auth.repository.CategoryRepository;
import com.base.auth.repository.NewsRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/news")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {

  @Autowired
  private NewsMapper newsMapper;

  @Autowired
  private NewsRepository newsRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('N_C')")
  public ApiMessageDto<String> create(@Valid @RequestBody CreateNewsForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    News news = newsRepository.findFirstByTitle(request.getTitle());
    if (news != null){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("News title already exist!");
      return apiMessageDto;
    }
    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()->
        new NotFoundException("Category id not found"));

    news = newsMapper.fromCreateNewsFormToEntity(request);
    news.setCategory(category);
    newsRepository.save(news);
    apiMessageDto.setMessage("Create news success");
    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('N_L')")
  public ApiMessageDto<ResponseListDto<List<NewsAutoCompleteDto>>> getList(NewsCriteria request, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<NewsAutoCompleteDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<News> news = newsRepository.findAll(request.getSpecification(), pageable);
    List<NewsAutoCompleteDto> response = newsMapper.fromEntityToNewsAutoCompleteDtoList(news.getContent());
    ResponseListDto<List<NewsAutoCompleteDto>> pageResult = new ResponseListDto<>(response, news.getTotalElements(), news.getTotalPages());
    apiMessageDto.setMessage("List news");
    apiMessageDto.setData(pageResult);
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('N_V')")
  public ApiMessageDto<NewsAutoCompleteDto> getNewsById(@PathVariable("id") Long id){
    ApiMessageDto<NewsAutoCompleteDto> apiMessageDto = new ApiMessageDto<>();
    News news = newsRepository.findById(id).orElseThrow(() ->
        new NotFoundException("News id not found"));
    apiMessageDto.setData(newsMapper.fromEntityToNewsAutoCompleteDto(news));
    apiMessageDto.setMessage("News information");
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE )
  @PreAuthorize("hasRole('N_U')")
  public ApiMessageDto<String> updateNews(@Valid @RequestBody UpdateNewsForm request, BindingResult bindingResult){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    News news = newsRepository.findById(request.getId()).orElseThrow(() ->
        new NotFoundException("News id not found"));
    if (!news.getTitle().equals(request.getTitle())){
      News existingNews = newsRepository.findFirstByTitle(request.getTitle());
      if (existingNews != null){
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage("News title already exists with a different ID!");
        return apiMessageDto;
      }
    }

    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
        new NotFoundException("Category id not found"));

    newsMapper.updateNewsFromUpdateNewsForm(request, news);
    news.setCategory(category);
    newsRepository.save(news);
    apiMessageDto.setMessage("Update news success");
    return apiMessageDto;
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasRole('N_D')")
  public ApiMessageDto<String> deleteNews(@PathVariable("id") Long id){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    News news = newsRepository.findById(id).orElseThrow(() ->
        new NotFoundException("News id not found"));
    newsRepository.deleteById(id);
    apiMessageDto.setMessage("Delete news success");
    return apiMessageDto;
  }
}
