package com.oms.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oms.model.Product;
import com.oms.model.Product.ProductCategory;

public class ProductRepository {
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String sql = "SELECT * FROM products";

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				int productId = resultSet.getInt("product_id");
				String name = resultSet.getString("name");
				double price = resultSet.getDouble("price");
				String category = resultSet.getString("category");
				ProductCategory product_category = ProductCategory.valueOf(category);

				Product product = new Product(productId, name, price, product_category);
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return products;
	}

	public int[] saveProducts(List<Product> products) {
		try (Connection connection = DatabaseUtil.getConnection()) {
			connection.setAutoCommit(false); // Disable auto-commit for batch insert
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO products (product_id, name, category) VALUES (?, ?, ?)")) {

				for (Product product : products) {
					preparedStatement.setLong(1, product.getProductId());
					preparedStatement.setString(2, product.getName());
					ProductCategory prod_cat = product.getCategory();
					String category = prod_cat.toString();
					preparedStatement.setString(3, category);
					preparedStatement.addBatch();
				}

				int[] result = preparedStatement.executeBatch();
				connection.commit();
				return result;
			} catch (SQLException e) {
				connection.rollback();
				throw e; // Re-throw the exception after rolling back the transaction
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
