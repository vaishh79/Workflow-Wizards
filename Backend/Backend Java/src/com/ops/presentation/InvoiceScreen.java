package com.ops.presentation;

import java.util.List;

import com.oms.model.Invoice;
import com.oms.model.Product;
import com.oms.service.InvoiceService;

public class InvoiceScreen {
	public void displayInvoice(int orderId) {
		Invoice invoice = InvoiceService.generateInvoice(orderId);
		System.out.println("Invoice Details:");
		System.out.println("Invoice ID: " + invoice.getInvoiceId());
		System.out.println("Invoice Date: " + invoice.getInvoiceDate());
		System.out.println("Order ID: " + invoice.getOrder().getOrderId());
		System.out.println("Customer Name: " + invoice.getCustomer().getName());

		// Print other invoice details in a similar manner
		System.out.println("GST Type: " + invoice.getGstType());
		System.out.println("Total GST Amount: " + invoice.getTotalGSTAmount());
		System.out.println("Total Invoice Value: " + invoice.getTotalInvoiceValue());
		System.out.println("Invoice Status: " + invoice.getStatus());
		System.out.println("Total Order Value: " + invoice.calculateTotalOrderValue());

		// If you have a productList, you can iterate over it and print each product's
		// details
		List<Product> productList = invoice.getProductList();
		if (productList != null) {
			System.out.println("Product List:");
			for (Product product : productList) {
				System.out.println("Product ID: " + product.getProductId());
				System.out.println("Product Name: " + product.getName());
			}
		} else {
			System.out.println("Invoice not found with OrderId: " + orderId);
		}
	}
}
