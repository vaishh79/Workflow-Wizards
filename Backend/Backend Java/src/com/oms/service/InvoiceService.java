package com.oms.service;

import java.util.Date;
import java.util.List;

import com.oms.model.Invoice;
import com.oms.model.Order;
import com.oms.model.Product;
import com.oms.model.Invoice.GSTType;
import com.oms.model.Invoice.InvoiceStatus;
import com.oms.persistence.InvoiceRepository;
import com.oms.persistence.OrderRepository;

public class InvoiceService {
	private static InvoiceRepository invoiceRepository;
	private static OrderRepository orderRepository;

	public InvoiceService() {
		this.invoiceRepository = new InvoiceRepository();
	}

	public static Invoice generateInvoice(int orderId) {
		// Fetch the Order from the database using the provided orderId
		Order order = orderRepository.findOrderById(orderId);

		if (order != null) {
			// Create a new Invoice instance
			Invoice invoice = new Invoice();

			// Set the attributes of the invoice based on the Order and other parameters
			invoice.setInvoiceDate(new Date()); // Set the invoice date to the current date
			invoice.setOrder(order); // Set the associated order
			invoice.setCustomer(order.getCustomer()); // Set the customer from the order
			invoice.setProductList(order.getProductList()); // Set the products from the order
			invoice.setGstType(GSTType.SAME_STATE); // Set the GST type (you can customize this)
			invoice.setTotalGSTAmount(calculateTotalGST(order)); // Calculate and set the total GST amount
			invoice.setTotalInvoiceValue(calculateTotalInvoiceValue(order)); // Calculate and set the total invoice
																				// value
			invoice.setStatus(InvoiceStatus.UNPAID); // Set the initial status to UNPAID (you can customize this)
			invoice.setTotalInvoiceValue(order.getTotalOrderValue()); // Set the total order value from the order

			// Save the generated invoice in your database or perform any other necessary
			// operations

			invoiceRepository.generateInvoice(invoice);
		}

		return null; // Return null if the order with the provided orderId is not found
	}

	private static double calculateTotalGST(Order order) {
		List<Product> productList = order.getProductList();
		double totalGST = 0.0;

		// Check if the product list is not null
		if (productList != null) {
			for (Product product : productList) {
				// Determine the GST rate based on the GSTType of the invoice
				GSTType gstType = order.getInvoice().getGstType();
				double gstRate = (gstType == GSTType.INTER_STATE) ? 0.1 : 0.05;

				// Calculate the GST for each product and add it to the total
				double productValue = product.getPrice();
				double gstAmount = productValue * gstRate;
				totalGST += gstAmount;
			}
		}

		return totalGST;
	}

	private static double calculateTotalInvoiceValue(Order order) {
		double totalOrderValue = order.getTotalOrderValue();
		double totalGST = calculateTotalGST(order);

		// Calculate the total invoice value as the sum of total order value and total
		// GST
		double totalInvoiceValue = totalOrderValue + totalGST;

		return totalInvoiceValue;
	}

}
