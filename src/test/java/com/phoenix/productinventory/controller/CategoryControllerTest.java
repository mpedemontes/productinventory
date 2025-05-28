package com.phoenix.productinventory.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.productinventory.dto.CategoryRequestDto;
import com.phoenix.productinventory.dto.CategoryResponseDto;
import com.phoenix.productinventory.service.CategoryService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

  private final CategoryResponseDto responseDto = new CategoryResponseDto(1L, "Test", "Desc");
  @Autowired private MockMvc mockMvc;
  @MockitoBean private CategoryService categoryService;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("When createCategory then returns created category with 201 status")
  void whenCreateCategory_thenReturnsCreatedCategory() throws Exception {
    CategoryRequestDto requestDto = new CategoryRequestDto("Test", "Desc");
    when(categoryService.createCategory(any())).thenReturn(responseDto);

    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(responseDto.getName()));
  }

  @Test
  @DisplayName("When getCategoryById then returns category with 200 status")
  void whenGetCategoryById_thenReturnsCategory() throws Exception {
    when(categoryService.getCategoryById(1L)).thenReturn(responseDto);

    mockMvc
        .perform(get("/categories/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(responseDto.getName()));
  }

  @Test
  @DisplayName("When getAllCategories then returns list of categories with 200 status")
  void whenGetAllCategories_thenReturnsListOfCategories() throws Exception {
    when(categoryService.getAllCategories(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(responseDto)));

    mockMvc
        .perform(get("/categories").param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("When updateCategory then returns updated category with 200 status")
  void whenUpdateCategory_thenReturnsUpdatedCategory() throws Exception {
    CategoryRequestDto requestDto = new CategoryRequestDto("Updated", "Desc");
    when(categoryService.updateCategory(eq(1L), any())).thenReturn(responseDto);

    mockMvc
        .perform(
            put("/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(responseDto.getName()));
  }

  @Test
  @DisplayName("When deleteCategory then returns 204 status")
  void whenDeleteCategory_thenReturnsNoContent() throws Exception {
    mockMvc.perform(delete("/categories/1")).andExpect(status().isNoContent());
  }
}
