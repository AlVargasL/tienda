package com.example.tienda.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.dto.ProductRequest;
import com.example.tienda.dto.ProductResponse;
import com.example.tienda.dto.StockRequest;
import com.example.tienda.exception.ResourceNotFoundException;
import com.example.tienda.model.Product;
import com.example.tienda.service.ProductService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService service;

    public ProductController(ProductService service) {
        this.service=service;
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> listResponse = new ArrayList<>();
        for (Product p : service.getAll()) {
            listResponse.add(toResponse(p));
        }
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getMethodName(@PathVariable Long id) {
        Product product = service.findById(id).orElseThrow(
            () ->
                new ResourceNotFoundException("Product not found with id" + id)
        );
        
        return ResponseEntity.ok(toResponse(product));
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest product) {

        Product save = service.save(toEntity(product),product.categoryId()); //save recibe el producto y atraves del de service.save con toEntity de producto lo guardamos empatando los tipos de datos
        return  ResponseEntity.status(HttpStatus.CREATED).body(toResponse(save));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted = service.delete(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,@Valid @RequestBody ProductRequest product) {
        Product optional = service.update(
            id, 
            toEntity(product),
            product.categoryId()
        );
        
        return ResponseEntity.notFound().build();
    }

    private ProductResponse toResponse(Product product){
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getStock(), 
            product.getCategory().getId(),
            product.getCategory().getName());
    }

    private Product toEntity(ProductRequest request){
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        return product;
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id, 
            @Valid @RequestBody StockRequest stockRequest) {

        Product updatedProduct = service.updateStock(
            id, 
            stockRequest.action(), 
            stockRequest.cantidad()
        );

        return ResponseEntity.ok(toResponse(updatedProduct));
    }
}