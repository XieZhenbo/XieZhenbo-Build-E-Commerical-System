package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Item;

public class itemDAOImpl implements itemDAO {
	public static String dbPath;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
		}
	}
	
	public itemDAOImpl(String dbPath) {
        this.dbPath = dbPath;
    }
	
	private Connection getConnection() throws SQLException {
		 return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
	}

	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		} catch (SQLException ex) {
		}
	}
	
	private List<Item> getList(ResultSet resultSet) throws Exception {
		List<Item> result = new ArrayList<Item>();
		while (resultSet.next()) {
			Item f = new Item();
			f.setItemID(resultSet.getString("itemID"));
			f.setName(resultSet.getString("name"));
			f.setBrand(resultSet.getString("brand"));
			f.setCategory(resultSet.getString("category"));
			f.setDescription(resultSet.getString("description"));
			f.setQuantity(Integer.parseInt(resultSet.getString("quantity")));
			f.setPrice(Double.parseDouble(resultSet.getString("price")));
			result.add(f);
		}
		return result;
	}
	
	@Override
	public List<Item> findAllItems() {
		Connection connection = null;
		String query = "SELECT * FROM Item";
		
		List<Item> result = new ArrayList<Item>();
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			return getList(resultSet);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}

	@Override
	public List<Item> findAllItemsSort(String brand, String category, String by, String ascending){
		Connection connection = null;
		if(by != null && by.contentEquals("name")) {
			by = "lower(name)"; 
		}
		String query = "SELECT * FROM Item WHERE brand LIKE ? and category LIKE ? ";
		if(by != null) {
			query += String.format(" ORDER BY %s %s", by, ascending);
		}
		if(brand == null || brand.trim().equals("")) {
			brand = "%";
		}
		if(category == null || category.trim().equals("")) {
			category = "%";
		}
		
		List<Item> result = new ArrayList<Item>();
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, brand);
			statement.setString(2, category);
			ResultSet resultSet = statement.executeQuery();
			return getList(resultSet);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}
	
	@Override
	public List<Item> findItemsByGenre(String genre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> findFilmsByStudio(String studio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> findFilmsByDateRange(String date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item findFilmByName(String name) {
		Connection connection = null;
		String query = "SELECT * FROM Item WHERE name=?";
		Item f = null;
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			
			statement.setString(1, name);
			
			ResultSet resultSet = statement.executeQuery();
			
			f = new Item();
			f.setItemID(resultSet.getString("itemID"));
			f.setName(resultSet.getString("name"));
			f.setBrand(resultSet.getString("brand"));
			f.setCategory(resultSet.getString("category"));
			f.setDescription(resultSet.getString("description"));
			f.setQuantity(Integer.parseInt(resultSet.getString("quantity")));
			f.setPrice(Double.parseDouble(resultSet.getString("price")));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		return f;
	}

	@Override
	public List<String> findAllBrands(){
		Connection connection = null;
		String query = "SELECT DISTINCT brand FROM item";
		
		List<String> result = new ArrayList<String>();
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				result.add(resultSet.getString("brand"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}
	
	@Override
	public List<String> findAllCategories(){
		Connection connection = null;
		String query = "SELECT DISTINCT category FROM item";
		
		List<String> result = new ArrayList<String>();
		
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				result.add(resultSet.getString("category"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}
}
