package com.base.auth.controller;

import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.product.ProductDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.product.CreateProductForm;
import com.base.auth.form.product.UpdateProductForm;
import com.base.auth.mapper.ProductMapper;
import com.base.auth.model.CartItem;
import com.base.auth.model.Product;
import com.base.auth.model.criteria.ProductCriteria;
import com.base.auth.repository.CartItemRepository;
import com.base.auth.repository.ProductRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private CartItemRepository cartItemRepository;

  @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('P_C')")
  public ApiMessageDto<String> createProduct(@Valid @RequestBody CreateProductForm request){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Product product = productRepository.findFirstByName(request.getName());
    if (product != null){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Product name already exist!");
      return apiMessageDto;
    }
    product = productMapper.fromCreateToProduct(request);
    productRepository.save(product);
    apiMessageDto.setMessage("Create product success");
    return apiMessageDto;
  }

  @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('P_L')")
  public ApiMessageDto<ResponseListDto<List<ProductDto>>> getListProduct(ProductCriteria request, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<ProductDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<Product> products = productRepository.findAll(request.getSpecification(), pageable);
    List<ProductDto> response = productMapper.fromProductToDtoList(products.getContent());
    ResponseListDto<List<ProductDto>> pageResult = new ResponseListDto<>(response, products.getTotalElements(), products.getTotalPages());
    apiMessageDto.setMessage("List product");
    apiMessageDto.setData(pageResult);
    return apiMessageDto;
  }

  @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('P_V')")
  public ApiMessageDto<ProductDto> getProduct(@PathVariable Long id){
    ApiMessageDto<ProductDto> apiMessageDto = new ApiMessageDto<>();
    Product product = productRepository.findById(id).orElseThrow(()
    -> new NotFoundException("Product id not found"));
    apiMessageDto.setData(productMapper.fromProductToProductDto(product));
    apiMessageDto.setMessage("Product information");
    return apiMessageDto;
  }

  @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE )
  @PreAuthorize("hasRole('P_U')")
  public ApiMessageDto<String> updateProduct(@Valid @RequestBody UpdateProductForm request){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Product product = productRepository.findById(request.getId()).orElseThrow(()
    -> new NotFoundException("Product id not found"));
    if (!product.getName().equals(request.getName())){
      Product existProduct = productRepository.findFirstByName(request.getName());
      if (existProduct != null){
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage("Product name already exists exists with a different ID!");
        return apiMessageDto;
      }
    }
    productMapper.mappingForUpdateProduct(request, product);
    productRepository.save(product);
    apiMessageDto.setMessage("Update product success");
    return apiMessageDto;
  }

  @DeleteMapping(value = "/delete/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('P_D')")
  public ApiMessageDto<String> deleteProduct(@PathVariable Long id){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Product product = productRepository.findById(id).orElseThrow(() ->
        new NotFoundException("Product id not found"));
    CartItem cartItem = cartItemRepository.findByProductId(id);
    if (cartItem != null){
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Cannot delete the product because it exists in the cart item!");
      return apiMessageDto;
    }
    productRepository.deleteById(id);
    apiMessageDto.setMessage("Delete success");
    return apiMessageDto;
  }
}
