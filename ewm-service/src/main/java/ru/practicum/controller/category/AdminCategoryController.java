package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        log.info("create new category:{}", newCategoryDto);
        return service.createCategory(newCategoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("delete category with id: {}", id);
        service.deleteCategory(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto update(@PathVariable Long id,
                              @RequestBody @Validated NewCategoryDto newCategoryDto) {
        log.info("update category with id: {} to {}", id, newCategoryDto);
        return service.updateCategory(id, newCategoryDto);
    }
}
