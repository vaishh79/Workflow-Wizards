package com.oms.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.oms.model.Product;
import com.oms.model.Product.ProductCategory;

public class ProductService {

	public List<Product> importProductsFromJsonFile(String filePath) {
		List<Product> importedProducts = new ArrayList<>();

		try {
			// Read the JSON file using a FileReader
			FileReader fileReader = new FileReader(filePath);

			// Parse the JSON data
			JSONArray productsArray = new JSONArray(new JSONTokener(fileReader));

			for (Object productObj : productsArray) {
				if (productObj instanceof JSONObject) {
					JSONObject productJson = (JSONObject) productObj;
					int productId = productJson.optInt("productId");
					String name = productJson.optString("name");
					Double price = productJson.optDouble("price");
					ProductCategory category = Product.ProductCategory.valueOf(productJson.optString("category"));

					// Validate product data

					if (productId != 0 && name != null && category != null) {
						// Create a Product object and add it to the list
						Product product = new Product(productId, name, price, category);
						importedProducts.add(product);
					}
				}
			}

			// Close the FileReader
			fileReader.close();

		} catch (IOException | JSONException e) {
			e.printStackTrace();
			// Handle exceptions (e.g., file not found, JSON parsing errors)
		}

		return importedProducts;
	}
}
