package com.oms.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oms.model.Customer;

public class CustomerRepository {
	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		String sql = "SELECT * FROM customers";

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				int customerId = resultSet.getInt("CustomerID");
				String name = resultSet.getString("Name");
				String gstNumber = resultSet.getString("GSTNumber");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("city");
				String email = resultSet.getString("Email");
				String phone = resultSet.getString("Phone");
				String pinCode = resultSet.getString("PinCode");

				Customer customer = new Customer(customerId, name, gstNumber, address, city, email, phone, pinCode,
						"****");
				customers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return customers;
	}

	public Customer findByCustomerId(int customerId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = null;

		try {
			connection = DatabaseUtil.getConnection();
			String sql = "SELECT * FROM customers WHERE CustomerName = ?";
			preparedStatement = connection.prepareStatement(sql);
			String cust_name = getCustomerNameByCustomerId(customerId);
			preparedStatement.setString(1, cust_name);
			preparedStatement.setInt(1, customerId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				// Extract customer information from the result set
				int customerIdFromDB = resultSet.getInt("CustomerID");
				String name = resultSet.getString("Name");
				String gstNumber = resultSet.getString("GSTNumber");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("city");
				String email = resultSet.getString("Email");
				String phone = resultSet.getString("Phone");
				String pinCode = resultSet.getString("PinCode");
				String hashedPassword = resultSet.getString("Password");

				// Create a Customer object with the retrieved data
				customer = new Customer();
				customer.setCustomerId(customerIdFromDB);
				customer.setName(name);
				customer.setGstNumber(gstNumber);
				customer.setAddress(address);
				customer.setCity(city);
				customer.setEmail(email);
				customer.setPhone(phone);
				customer.setPinCode(pinCode);
				customer.setHashedpassword(hashedPassword);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle or log the SQL exception
		}

		return customer;
	}

	public String getCustomerNameByCustomerId(int customerId) {
		String customerName = null;
		String sql = "SELECT CustomerName FROM quotes WHERE CustomerID = ?";

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, customerId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					customerName = resultSet.getString("CustomerName");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return customerName;
	}

}
