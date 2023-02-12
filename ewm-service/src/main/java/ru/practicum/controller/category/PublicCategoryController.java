package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService service;

    @GetMapping
    public Collection<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("public get all categories with param: from: {}, size: {}", from, size);
        return service.getCategories(PageRequest.of(from, size));
    }

    @GetMapping("/{id}")
    private CategoryDto getById(@PathVariable Long id) {
        log.info("get category by id: {}", id);
        return service.getCategoryById(id);
    }
}
