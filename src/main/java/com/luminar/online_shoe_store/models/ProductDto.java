package com.luminar.online_shoe_store.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class ProductDto {
	@NotEmpty(message = "The brand is required")
	private String brand;
	
	@NotEmpty(message = "The name is required")
	private String category;
	
	@Size(min = 10, message = "The description should be atleast 10 characters")
	@Size(max = 2000, message = "The description cannot exceed 2000 characters")
	private String description;
	
	private MultipartFile imageFile;
	
	@Min(0)
	private double price;
	
	@NotEmpty(message = "The name is required")
	private String name;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
