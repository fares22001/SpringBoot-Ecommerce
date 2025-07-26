package com.project.project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.project.project.model.Cart;
import com.project.project.model.Category;
import com.project.project.model.User;
import com.project.project.model.products;
import com.project.project.repositories.productRepo;
import com.project.project.services.categoryservice;
import com.project.project.services.cartservic;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/products")
public class productcontrollerv1 {
    @Autowired
    private productRepo productRepo;

    @Autowired
    private categoryservice categoryService;

    @Autowired
    private cartservic cartService;

    @GetMapping({"", "/"})
    public ModelAndView getAll() {
        ModelAndView mav = new ModelAndView("products.html");
        List<products> productList = productRepo.findAll();
        mav.addObject("products", productList);
        return mav;
    }

    @GetMapping("/addproduct")
    public ModelAndView addProduct() {
        List<Category> categories = categoryService.findAll();
        ModelAndView mav = new ModelAndView("addproduct.html");
        mav.addObject("product", new products());
        mav.addObject("categories", categories);
        return mav;
    }

    @PostMapping("/addproduct")
    public ModelAndView saveProduct(@Valid @ModelAttribute("product") products product, BindingResult result,
                                    @RequestParam("images") MultipartFile[] multipartFiles) {
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("addproduct.html");
            mav.addObject("bindingResult", result);
            mav.addObject("categories", categoryService.findAll());
            return mav;
        }

        if (multipartFiles.length > 4) {
            result.rejectValue("images", "file.limit", "You can upload a maximum of 4 images");
            ModelAndView mav = new ModelAndView("addproduct.html");
            mav.addObject("bindingResult", result);
            mav.addObject("categories", categoryService.findAll());
            return mav;
        }

        try {
            List<String> fileNames = new ArrayList<>();
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

            for (MultipartFile multipartFile : multipartFiles) {
                String originalFilename = multipartFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

                if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                    result.rejectValue("images", "file.invalid", "File must be a JPG, JPEG, PNG, or GIF");
                    ModelAndView mav = new ModelAndView("addproduct.html");
                    mav.addObject("bindingResult", result);
                    mav.addObject("categories", categoryService.findAll());
                    return mav;
                }

                String fileName = StringUtils.cleanPath(originalFilename);
                fileNames.add(fileName);
            }

            product.setImageFileName(fileNames);
            products savedProduct = productRepo.save(product);

            String uploadDir = "project/src/main/resources/static/uploads/" + savedProduct.getId();

            for (int i = 0; i < multipartFiles.length; i++) {
                FileUploadUtil.saveFile(uploadDir, fileNames.get(i), multipartFiles[i]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new ModelAndView("redirect:/admin/products");
    }

    @GetMapping("/editProduct/{id}")
    public ModelAndView showEditForm(@PathVariable("id") int id) {
        products product = productRepo.findById(id);
        ModelAndView mav = new ModelAndView("editProduct.html");
        mav.addObject("product", product);
        mav.addObject("categories", categoryService.findAll());
        return mav;
    }

    @PostMapping("/editProduct/{id}")
    public ModelAndView editProduct(@PathVariable("id") int id, @Valid @ModelAttribute("product") products product, BindingResult result,
                                    @RequestParam(value = "images", required = false) MultipartFile[] multipartFiles) throws IOException {
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("editProduct.html");
            mav.addObject("bindingResult", result);
            mav.addObject("product", product);
            mav.addObject("categories", categoryService.findAll());
            return mav;
        }

        products existingProduct = productRepo.findById(id);

        if (existingProduct.getImageFileName() != null) {
            String uploadDir = "project/src/main/resources/static/uploads/" + existingProduct.getId();
            for (String fileName : existingProduct.getImageFileName()) {
                FileUploadUtil.deleteFile(uploadDir, fileName);
            }
        }

        existingProduct.setName(product.getName());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setCategoryId(product.getCategoryId());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setRegularDiscount(product.getRegularDiscount());

        if (multipartFiles != null && multipartFiles.length > 0) {
            List<String> fileNames = new ArrayList<>();
            String uploadDir = "project/src/main/resources/static/uploads/" + existingProduct.getId();

            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                fileNames.add(fileName);
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }

            existingProduct.setImageFileName(fileNames);
        }

        productRepo.save(existingProduct);

        return new ModelAndView("redirect:/admin/products");
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public RedirectView deleteProduct(@PathVariable("id") int id) {
        products product = productRepo.findById(id);
        String uploadDir = "project/src/main/resources/static/uploads/" + product.getId();

        if (product.getImageFileName() != null) {
            for (String fileName : product.getImageFileName()) {
                FileUploadUtil.deleteFile(uploadDir, fileName);
            }
        }

        productRepo.deleteById(id);
        return new RedirectView("/admin/products");
    }

    // @GetMapping("/product-details/{id}")
    // public ModelAndView getProduct(@PathVariable("id") int id) {
    //     products product = productRepo.findById(id);
    //     ModelAndView mav = new ModelAndView("product-details.html");
    //     mav.addObject("product", product);
    //     Category category = categoryService.findById(product.getCategoryId());
    //     mav.addObject("category", category);
    //     return mav;
    // }

    @PostMapping("/add-to-cart/{id}")
    public ModelAndView addItem(@Valid @ModelAttribute("Cart") Cart cartItem, BindingResult result,
                                @RequestParam("id") int productId, HttpSession session) {

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("redirect:/admin/products/product-details/" + productId);
            mav.addObject("bindingResult", result);
            return mav;
        }

        if (session == null || session.getAttribute("User_id") == null) {
            return new ModelAndView("redirect:/login");
        }

        int userId = (int) session.getAttribute("User_id");
        products product = productRepo.findById(productId);

        if (product.getQuantity() <= 0) {
            ModelAndView mav = new ModelAndView("redirect:/admin/products/product-details/" + productId);
            mav.addObject("message", "Product is out of stock");
            return mav;
        }

        boolean itemExists = cartService.doesItemExistForUser(userId, productId);
        if (itemExists) {
            ModelAndView mav = new ModelAndView("redirect:/admin/products/product-details/" + productId);
            mav.addObject("message", "Item already exists in the cart");
            return mav;
        }

        User user = new User();
        user.setId(userId);
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartService.addItem(cartItem);

        return new ModelAndView("redirect:/cart");
    }
}
