package com.example.crm.service;

import com.example.crm.model.Role;
import com.example.crm.model.User;
import com.example.crm.model.Product;
import com.example.crm.repository.RoleRepository;
import com.example.crm.repository.UserRepository;
import com.example.crm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;


    public void save(User user){
        user.setPassword(encoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("CUSTOMER");
        user.setRole(userRole);

        userRepository.save(user);
    }


    public void update(Long id,User user){

        User updated = userRepository.getOne(id);

        updated.setName(user.getName());
        updated.setSurname(user.getSurname());
        updated.setEmail(user.getEmail());
        updated.setPhone(user.getPhone());

        userRepository.save(updated);
    }


    public User getById(Long id){
        return userRepository.getOne(id);
    }

    public List<User> listAllCustomer(){
        Role role =  roleRepository.findByName("CUSTOMER");
        return userRepository.findAll().stream().filter(user -> user.getRole().equals(role) ).collect(Collectors.toList());
    }

    public  User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void deleteById(Long id){

        User user = userRepository.getOne(id);

        List<Product> products = user.getProducts();

        for (Product product : products) {
            if(product.getIsCompleted() != 1){
                product.setIsCompleted(0);
                product.setUser(null);
                productRepository.save(product);
            }
        }

        userRepository.deleteById(id);
    }

}
