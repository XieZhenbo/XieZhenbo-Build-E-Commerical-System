package dao;

import java.util.List;

import model.PurchaseOrder;

public interface poDAO {
	
	public List<PurchaseOrder> findAllPurchaseOrders ();
	
	public List<PurchaseOrder> findPurchaseOrdersByCustomerId (int id);
	
	public List<PurchaseOrder> findPurchaseOrdersByFilmId (int id);
	
	public List<PurchaseOrder> findPurchaseOrdersByDate (String date);
		
	public void addPurchaseOrder (PurchaseOrder p);
	
	public int generatePurchaseOrderID();
	
}
