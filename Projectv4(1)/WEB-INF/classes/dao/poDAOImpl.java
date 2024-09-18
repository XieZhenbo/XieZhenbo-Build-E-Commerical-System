package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.PurchaseOrder;

public class poDAOImpl implements poDAO {

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}
	
	private Connection getConnection() throws SQLException {
		 return DriverManager.getConnection("jdbc:sqlite:" + itemDAOImpl.dbPath);
	}

	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		} catch (SQLException ex) {
		}
	}
	
	@Override
	public List<PurchaseOrder> findAllPurchaseOrders() {
		
		return null;
	}

	@Override
	public List<PurchaseOrder> findPurchaseOrdersByFilmId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PurchaseOrder> findPurchaseOrdersByDate(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int generatePurchaseOrderID() {
		Connection connection = null;
		String query = "SELECT MAX(id) AS max_id FROM PO";
		
		int id = 0;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			
			id = Integer.parseInt(resultSet.getString("max_id"))+ 1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		finally {
			closeConnection(connection);
		}
		return id;
	}

	@Override
	public List<PurchaseOrder> findPurchaseOrdersByCustomerId(int customerID) {
		Connection connection = null;
		String query = "SELECT * FROM PO WHERE customerID=? AND purchased=?";

		List<PurchaseOrder> poList = new ArrayList<>();
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, customerID);
			statement.setInt(2, 0);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				 PurchaseOrder po = new PurchaseOrder();
				 po.setId(resultSet.getInt("id"));
				 po.setCustomerID(resultSet.getInt("customerID"));
				 po.setItemID(resultSet.getString("filmID"));
				 po.setDateOfPurchase(resultSet.getString("dateOfPurchase"));
				 po.setPurchased(resultSet.getInt("purchased"));
				 po.setPrice(resultSet.getDouble("price"));
				 po.setQuantity(resultSet.getInt("quantity"));
				 po.setFilmName(resultSet.getString("filmName"));
				 poList.add(po);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return poList;
	}

	@Override
	public void addPurchaseOrder(PurchaseOrder p) {
		Connection connection = null;
		String query = "INSERT INTO PO (id, customerID, filmID, filmName, dateOfPurchase, purchased, quantity, price) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setInt(1, p.getId());
			statement.setInt(2, p.getCustomerID());
			statement.setString(3, p.getItemID());
			statement.setString(4, p.getFilmName());
			statement.setString(5, p.getDateOfPurchase());
			statement.setInt(6, p.getPurchased());
			statement.setInt(7, p.getQuantity());
			statement.setDouble(8, p.getPrice());
			
			int r = statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
	}
	
	

}
