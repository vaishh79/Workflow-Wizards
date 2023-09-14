package com.oms.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.oms.model.Invoice;
import com.oms.model.Order.OrderStatus;

public class InvoiceRepository {
	private static InvoiceRepository invoiceRepository = new InvoiceRepository();

	public void generateInvoice(Invoice invoice) {
		// Update the order's status to reflect that an invoice has been generated
		try (Connection connection = DatabaseUtil.getConnection()) {

			// Update the order status
			String updateOrderStatusSql = "UPDATE orders SET status = ? WHERE order_id = ?";
			try (PreparedStatement updateOrderStatusStatement = connection.prepareStatement(updateOrderStatusSql)) {
				updateOrderStatusStatement.setString(1, OrderStatus.INVOICED.toString());
				updateOrderStatusStatement.setInt(2, invoice.getOrder().getOrderId());
				updateOrderStatusStatement.executeUpdate();
			}

			String insertInvoiceSql = "INSERT INTO invoices (invoice_id, invoice_date, order_id, customer_id, total_invoice_value, total_gst_amount, status, total_order_value) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement insertInvoiceStatement = connection.prepareStatement(insertInvoiceSql)) {
				// Set the parameters for the prepared statement
				insertInvoiceStatement.setInt(1, invoice.getInvoiceId()); // Set the invoiceId explicitly
				insertInvoiceStatement.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
				insertInvoiceStatement.setInt(3, invoice.getOrder().getOrderId());
				insertInvoiceStatement.setInt(4, invoice.getCustomer().getCustomerId());
				insertInvoiceStatement.setDouble(5, invoice.getTotalInvoiceValue());
				insertInvoiceStatement.setDouble(6, invoice.getTotalGSTAmount());
				insertInvoiceStatement.setString(7, invoice.getStatus().toString());
				insertInvoiceStatement.setDouble(8, invoice.calculateTotalOrderValue());

				// Execute the INSERT statement
				insertInvoiceStatement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * //Method for generating invoice of all orders at 12 pm everyday public void
	 * generateInvoicesForApprovedOrders() { // Retrieve all approved orders from
	 * the previous day List<Order> approvedOrders =
	 * OrderRepository.getApprovedOrdersFromPreviousDay();
	 * 
	 * for (Order order : approvedOrders) {
	 * 
	 * // Create an invoice and save it to the database
	 * invoiceRepository.generateInvoice(); } }
	 */
}
