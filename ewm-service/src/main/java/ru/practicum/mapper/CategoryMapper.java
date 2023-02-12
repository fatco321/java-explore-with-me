package ru.practicum.mapper;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.entity.Category;

public class CategoryMapper {

    public static CategoryDto fromCategory(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(NewCategoryDto categoryDto) {
        return new Category(null, categoryDto.getName());
    }
}
