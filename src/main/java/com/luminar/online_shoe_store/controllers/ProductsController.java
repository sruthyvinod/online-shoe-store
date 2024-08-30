package com.luminar.online_shoe_store.controllers;

import java.io.InputStream;
import java.nio.file.*;

import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.luminar.online_shoe_store.models.Product;
import com.luminar.online_shoe_store.models.ProductDto;
import com.luminar.online_shoe_store.services.ProductsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {
	@Autowired
	private ProductsRepository repo;

	@GetMapping({ "", "/" })
	public String showProductList(Model model) {
		List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("products", products);
		return "products/index";
	}

	@GetMapping("/create")
	public String showCreatePage(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto", productDto);
		return "products/CreateProduct";
	}

	@PostMapping("/create")
	public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {

		if (productDto.getImageFile().isEmpty()) {
			result.addError(new FieldError("productDto", "imageFile", "The image file is required"));
		}
		if (result.hasErrors()) {
			return "products/CreateProduct";
		}

		// save image file
		MultipartFile image = productDto.getImageFile();

		String storageFileName = image.getOriginalFilename();

		try {
			String uploadDir = "public/images/";
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = image.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());

		}

		Product product = new Product();
		product.setBrand(productDto.getBrand());
		product.setCategory(productDto.getCategory());
		product.setDescription(productDto.getDescription());
		product.setImageFileName(storageFileName);
		product.setPrice(productDto.getPrice());
		product.setName(productDto.getName());

		repo.save(product);

		return "redirect:/products";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam int id) {

		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);

			ProductDto productDto = new ProductDto();
			productDto.setBrand(product.getBrand());
			productDto.setCategory(product.getCategory());
			productDto.setDescription(product.getDescription());
			productDto.setPrice(product.getPrice());
			productDto.setName(product.getName());

			model.addAttribute("productDto", productDto);
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());
			return "redirect:/products";
		}

		return "products/EditProduct";
	}

	@PostMapping("/edit")
	public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto,
			BindingResult result) {

		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);

			if (result.hasErrors()) {
				return "products/EditProduct";
			}

			if (!productDto.getImageFile().isEmpty()) {
				// delete old image
				String uploadDir = "public/images/";
				Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
				try {
					Files.delete(oldImagePath);
				} catch (Exception ex) {
					System.out.println("Exception:" + ex.getMessage());
				}

				// save new image file
				MultipartFile image = productDto.getImageFile();
				String storageFileName = image.getOriginalFilename();
				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
							StandardCopyOption.REPLACE_EXISTING);

				}
				product.setImageFileName(storageFileName);
			}

			product.setBrand(productDto.getBrand());
			product.setCategory(productDto.getCategory());
			product.setDescription(productDto.getDescription());
			product.setPrice(productDto.getPrice());
			product.setName(productDto.getName());

			repo.save(product);
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());
		}
		return "redirect:/products";
	}

	@GetMapping("/delete")
	public String DeleteProduct(@RequestParam int id) {

		try {
			Product product = repo.findById(id).get();
			// delete product image
			Path imagePath = Paths.get("public/images/" + product.getImageFileName());
			try {
				Files.delete(imagePath);
			} catch (Exception ex) {
				System.out.println("Exception:" + ex.getMessage());
			}
			// delete the product
			repo.delete(product);
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getMessage());
		}
		return "redirect:/products";
	}

}
