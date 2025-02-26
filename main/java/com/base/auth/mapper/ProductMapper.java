package com.base.auth.mapper;

import com.base.auth.dto.product.ProductDto;
import com.base.auth.form.product.CreateProductForm;
import com.base.auth.form.product.UpdateProductForm;
import com.base.auth.model.Product;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "shortDescription", target = "shortDescription")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "saleOff", target = "saleOff")
  @Mapping(source = "image", target = "image")
  @BeanMapping(ignoreByDefault = true)
  Product fromCreateToProduct(CreateProductForm createProductForm);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "shortDescription", target = "shortDescription")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "saleOff", target = "saleOff")
  @Mapping(source = "image", target = "image")
  @BeanMapping(ignoreByDefault = true)
  void mappingForUpdateProduct(UpdateProductForm updateProductForm, @MappingTarget Product product);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "shortDescription", target = "shortDescription")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "saleOff", target = "saleOff")
  @Mapping(source = "image", target = "image")
  @BeanMapping(ignoreByDefault = true)
  @Named("fromProductToProductDto")
  ProductDto fromProductToProductDto(Product product);

  @IterableMapping(elementTargetType = ProductDto.class, qualifiedByName = "fromProductToProductDto")
  List<ProductDto> fromProductToDtoList(List<Product> products);
}
