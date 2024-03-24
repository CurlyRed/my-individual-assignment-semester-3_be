package Marketplace.controller;

import Marketplace.business.CategoryService;
import Marketplace.domain.Category.Category;
import Marketplace.domain.Category.CreateCategoryRequest;
import Marketplace.domain.Category.CreateCategoryResponse;
import Marketplace.domain.Category.UpdateCategoryRequest;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CreateCategoryResponse> createCategory(@RequestBody @Valid CreateCategoryRequest request){
        CreateCategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<Category> getCategory(@PathVariable(value = "id") final long categoryId){
        final Optional<Category> categoryOptional = categoryService.getCategory(categoryId);
        return categoryOptional.map(category -> ResponseEntity.ok().body(category)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable("id") long id,
                                               @RequestBody @Valid UpdateCategoryRequest request){
        request.setId(id);
        categoryService.updateCategory(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok().body(categories);
    }
}
