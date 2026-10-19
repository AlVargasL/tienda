package com.example.tienda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tienda.exception.InsufficientStockException;
import com.example.tienda.exception.ResourceNotFoundException;
import com.example.tienda.model.Category;
import com.example.tienda.model.Product;
import com.example.tienda.repository.CategoryRepository;
import com.example.tienda.repository.ProductRepository;

@Service 
public class ProductService {

    private ProductRepository repo;
    private CategoryRepository repoCategory;    

    public ProductService(ProductRepository repo, CategoryRepository repoCategory){
        this.repoCategory = repoCategory;
        this.repo = repo;
    }

    public List<Product> getAll(){
        return repo.findAll();
    }

    public Optional<Product> findById(Long id){
        return repo.findById(id);
    }

    public Product save(Product product, Long categoryId){
        Category category = repoCategory
            .findById(categoryId)
            .orElseThrow(
            () -> // Expresion anonima(landa), solo se lanza cuando no encuentre una categoria, devuelve esta excepción
                new ResourceNotFoundException("Category not found whit id: " + categoryId)
        );//Brujeria para encontrar un error, cuando no encuentra el id de la categoria, lanza una excepcion
        product.setCategory(category);
        return repo.save(product);
    }

    public Product update(long id, Product product, Long categoryId) {

        Product productDb = repo.findById(id).orElseThrow(
            () ->
                new ResourceNotFoundException("Product not found with id: " + id)
        );
        
        Category category = repoCategory.findById(categoryId).orElseThrow(
            () ->
                new ResourceNotFoundException("Category not found with id: " + categoryId)
        );
        // Product productDb = optional.get();
        productDb.setName(product.getName());
        productDb.setPrice(product.getPrice());
        productDb.setStock(product.getStock());
        productDb.setCategory(category);
        return repo.save(productDb);
    }

    public Boolean delete(long id){
        if(!repo.existsById(id)){
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    public Product addOrRemove(long id, Product product) {

        Product productDb = repo.findById(id).orElseThrow(
            () ->
                new ResourceNotFoundException("Product not found with id: " + id)
        );

        productDb.setStock(product.getStock());
        return repo.save(productDb);

    }

    public Product updateStock(long id, String action, Integer quantity) {
    // Buscar el producto en la base de datos
        Product productDb = repo.findById(id).orElseThrow(
            () -> 
                new ResourceNotFoundException("Product not found with id: " + id)
        );

        // Ejecutar la acción
        if ("add".equalsIgnoreCase(action)) {
            productDb.setStock(productDb.getStock() + quantity);

        } else if ("remove".equalsIgnoreCase(action)) {
            // Validar si el stock disponible alcanza, si es mayor manda mensaje con 409
            if (quantity > productDb.getStock()) {
                throw new InsufficientStockException(
                    "Stock insuficiente. Disponible: " + productDb.getStock() + ", intentas remover: " + quantity
                );
            }
            productDb.setStock(productDb.getStock() - quantity);

        } else {
            throw new ResourceNotFoundException("Acción no válida. Usa 'add' o 'remove'");
        }

        // Guardar cambios en BD
        return repo.save(productDb);
    }

}