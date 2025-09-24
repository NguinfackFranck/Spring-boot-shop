package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false,name="categoryId") Byte categoryId) {
        List<Product> products ;
        if (categoryId != null){
             products = productRepository.findByCategoryId(categoryId);
            }
        else {
            products = productRepository.findAll();
        }
        return products.stream().map( productMapper::toDto).toList();
    }
    @GetMapping("/id")
    public ResponseEntity<ProductDto> getProductsById(@RequestParam(required = false, name = "id") Long Id) {
    var product = productRepository.findById(Id).orElseThrow(null);
    if (product == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(productMapper.toDto(product));
    }
    @PostMapping ("/add-products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder) {
    var category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(null);
    if (category == null) {
        return ResponseEntity.notFound().build();
    }

    var product = new Product();
    product.setName(productDto.getName());
    product.setCategory(category);
    product.setDescription(productDto.getDescription());
    product.setPrice(productDto.getPrice());
    productRepository.save(product);
    var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
    return  ResponseEntity.created(uri).body(productDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,@RequestBody ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productRepository.findById(id).orElseThrow(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
         productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product=  productRepository.findById(id).orElseThrow(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }

}
