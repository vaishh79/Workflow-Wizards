package com.ops.presentation;

import java.util.List;

import com.oms.model.Product;
import com.oms.persistence.ProductRepository;
import com.oms.service.ProductService;

public class ImportProductsScreen {
	private ProductRepository productRepository;

	public ImportProductsScreen(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public void importProducts(String filePath) {
		try {
			ProductService productService = new ProductService();
			List<Product> importedProducts = productService.importProductsFromJsonFile(filePath);

			if (importedProducts != null && !importedProducts.isEmpty()) {
				// Save the imported products to the database
				int[] result = productRepository.saveProducts(importedProducts);

				if (result != null && result.length == importedProducts.size()) {
					System.out.println("Import status: Completed");
					System.out.println("Number of products imported: " + importedProducts.size());
					System.out.println("Number of products not imported: 0");
				} else {
					System.out.println("Import status: Partial (Some products failed to import)");
					System.out.println("Number of products imported: " + (result != null ? result.length : 0));
					System.out.println("Number of products not imported: "
							+ (importedProducts.size() - (result != null ? result.length : 0)));
				}
			} else {
				System.out.println("Import status: Failed (No valid products found in the file)");
				System.out.println("Number of products imported: 0");
				System.out.println("Number of products not imported: 0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Import status: Failed (An error occurred)");
			System.out.println("Number of products imported: 0");
			System.out.println("Number of products not imported: 0");
		}
	}

}
