package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.entity.Category;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.storage.CategoryRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Category with name: " + categoryDto.getName() + " already exist");
        }
        return CategoryMapper.fromCategory(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    public CategoryDto getCategoryById(Long id) {
        return CategoryMapper.fromCategory(categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Category with id: " + id + " not found")));
    }

    public void deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("category with id: " + id + " not found");
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Category not empty");
        }
    }

    public CategoryDto updateCategory(Long id, NewCategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Category with id: " + id + " not found"));
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Category with name: " + categoryDto.getName() + " already exist");
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.fromCategory(categoryRepository.save(category));
    }

    public Collection<CategoryDto> getCategories(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest)
                .stream().map(CategoryMapper::fromCategory).collect(Collectors.toList());
    }
}
