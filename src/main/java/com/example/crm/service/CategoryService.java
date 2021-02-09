package com.example.crm.service;

import com.example.crm.model.Category;
import com.example.crm.model.Product;
import com.example.crm.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public void save(Category category){

        categoryRepository.save(category);
    }

    public void deleteById(Long id){

        Category category = categoryRepository.getOne(id);

        for (Product product : category.getProducts()) {
            product.setCategory(null);
        }

        categoryRepository.deleteById(id);
    }

}
