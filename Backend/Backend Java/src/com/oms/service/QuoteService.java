package com.oms.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.oms.model.Quote;
import com.oms.persistence.DatabaseUtil;
import com.oms.persistence.QuoteRepository;

public class QuoteService {
	private QuoteRepository quoteRepository;

	public QuoteService() {
		this.quoteRepository = new QuoteRepository();
	}

	public void createNewQuote(Quote quote) {
		Quote new_quote = quote;
		new_quote.setQuoteId(quote.getQuoteId());
		new_quote.setOrderDate(quote.getOrderDate());
		new_quote.setCustomerName(quote.getCustomerName());
		new_quote.setCustomerGSTNo(quote.getCustomerGSTNo());
		new_quote.setShippingAddress(quote.getShippingAddress());
		new_quote.setCustomerCity(quote.getCustomerCity());
		new_quote.setPhoneNumber(quote.getPhoneNumber());
		new_quote.setEmail(quote.getEmail());
		new_quote.setPinCode(quote.getPinCode());
		new_quote.setShippingCost(quote.getShippingCost());
		new_quote.setTotalOrderValue(quote.getTotalOrderValue());
		new_quote.setStatus(quote.getStatus());

		quoteRepository.insertQuote(new_quote);
	}

	public static boolean checkCustomerExists(int customerId) {
		String sql = "SELECT COUNT(*) FROM customers WHERE CustomerID = ?";
		try (Connection connection = DatabaseUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, customerId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt(1);
					return count > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean approveQuote(int quoteId) {
		Quote quote = quoteRepository.findQuoteById(quoteId);

		if (quote != null && isWithin30Days(quote.getOrderDate())) {
			quote.setStatus("approved");
			quoteRepository.updateQuote(quote);
			return true;
		} else {
			quote.setStatus("expired");
			quoteRepository.updateQuote(quote);
			return false;
		}
	}

	public boolean isWithin30Days(Date orderDate) {
		// Convert the Date object to LocalDate
		LocalDate localOrderDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		// Get the current date
		LocalDate currentDate = LocalDate.now();

		// Calculate the difference in days
		long daysDifference = ChronoUnit.DAYS.between(localOrderDate, currentDate);

		return daysDifference <= 30;
	}
}
