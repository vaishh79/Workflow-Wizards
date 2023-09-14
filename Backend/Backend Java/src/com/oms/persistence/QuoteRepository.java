package com.oms.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.oms.model.Quote;

public class QuoteRepository {
	public List<Quote> getAllQuotes() {
		List<Quote> quotes = new ArrayList<>();
		String sql = "SELECT * FROM quotes";

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				int quoteId = resultSet.getInt("QuoteID");
				Date orderDate = resultSet.getDate("OrderDate");
				String customerName = resultSet.getString("CustomerName");
				String customerGSTNo = resultSet.getString("CustomerGSTNo");
				String shippingAddress = resultSet.getString("ShippingAddress");
				String customerCity = resultSet.getString("CustomerCy");
				String phoneNumber = resultSet.getString("PhoneNumber");
				String email = resultSet.getString("Email");
				String pinCode = resultSet.getString("PinCode");
				double shippingCost = resultSet.getDouble("ShippingCost");
				double totalOrderValue = resultSet.getDouble("TotalOrderValue");
				String status = resultSet.getString("Status");

				Quote quote = new Quote(quoteId, orderDate, customerName, customerGSTNo, shippingAddress, customerCity,
						phoneNumber, email, pinCode, shippingCost, totalOrderValue, status);
				quotes.add(quote);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return quotes;
	}

	public List<Quote> getAllQuotesByCustomerId(int customerId) {
		List<Quote> customerQuotes = new ArrayList<>();
		String sql = "SELECT * FROM quotes WHERE customerName = ?";

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			String cust_name = getCustomerNameByCustomerId(customerId);
			preparedStatement.setString(1, cust_name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					int quoteId = resultSet.getInt("QuoteID");
					Date orderDate = resultSet.getDate("OrderDate");
					String customerName = resultSet.getString("CustomerName");
					String customerGSTNo = resultSet.getString("CustomerGSTNo");
					String shippingAddress = resultSet.getString("ShippingAddress");
					String customerCity = resultSet.getString("CustomerCy");
					String phoneNumber = resultSet.getString("PhoneNumber");
					String email = resultSet.getString("Email");
					String pinCode = resultSet.getString("PinCode");
					double shippingCost = resultSet.getDouble("ShippingCost");
					double totalOrderValue = resultSet.getDouble("TotalOrderValue");
					String status = resultSet.getString("Status");

					// Construct a Quote object from retrieved data
					Quote quote = new Quote(quoteId, orderDate, customerName, customerGSTNo, shippingAddress,
							customerCity, phoneNumber, email, pinCode, shippingCost, totalOrderValue, status);

					customerQuotes.add(quote);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return customerQuotes;
	}

	public boolean insertQuote(Quote quote) {
		String sql = "INSERT INTO quotes (order_id,order_date, customer_name, customer_gst_no, "
				+ "shipping_address, customer_city, phone_number, email, pin_code, "
				+ "shipping_cost, total_order_value, status) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, quote.getQuoteId());
			preparedStatement.setDate(2, new java.sql.Date(quote.getOrderDate().getTime()));
			preparedStatement.setString(3, quote.getCustomerName());
			preparedStatement.setString(4, quote.getCustomerGSTNo());
			preparedStatement.setString(5, quote.getShippingAddress());
			preparedStatement.setString(6, quote.getCustomerCity());
			preparedStatement.setString(7, quote.getPhoneNumber());
			preparedStatement.setString(8, quote.getEmail());
			preparedStatement.setString(9, quote.getPinCode());
			preparedStatement.setDouble(10, quote.getShippingCost());
			preparedStatement.setDouble(11, quote.getTotalOrderValue());
			preparedStatement.setString(12, quote.getStatus());

			int rowsInserted = preparedStatement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateQuote(Quote quote) {
		String sql = "UPDATE quotes SET Status = ? WHERE QuoteID = ?";

		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, quote.getStatus());
			preparedStatement.setInt(2, quote.getQuoteId());

			int rowsUpdated = preparedStatement.executeUpdate();

			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Quote findQuoteById(int quoteId) {
		String sql = "SELECT * FROM quotes WHERE QuoteID = ?";
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, quoteId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return createQuoteFromResultSet(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // Quote not found
	}

	private Quote createQuoteFromResultSet(ResultSet resultSet) throws SQLException {
		int quoteId = resultSet.getInt("quoteId");
		Date orderDate = resultSet.getDate("orderDate");
		String customerName = resultSet.getString("customerName");
		String gstNumber = resultSet.getString("gstNumber");
		String address = resultSet.getString("address");
		String city = resultSet.getString("city");
		String email = resultSet.getString("email");
		String phone = resultSet.getString("phone");
		String pinCode = resultSet.getString("pinCode");
		double shippingCost = resultSet.getDouble("shippingCost");
		double totalOrderValue = resultSet.getDouble("totalOrderValue");
		String orderStatus = resultSet.getString("status");

		return new Quote(quoteId, orderDate, customerName, gstNumber, address, city, phone, email, pinCode,
				shippingCost, totalOrderValue, orderStatus);
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
