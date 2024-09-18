package dao;

import java.util.List;

import model.Item;

public interface itemDAO {

	public List<Item> findAllItems();
	
	public List<Item> findAllItemsSort(String brand, String category, String by, String ascending);
	
	public List<Item> findItemsByGenre(String genre);
	
	public List<Item> findFilmsByStudio(String studio);
	
	public List<Item> findFilmsByDateRange(String date);	// YYYY-MM-DD
	
	public Item findFilmByName(String name);
	
	public List<String> findAllBrands();
	
	public List<String> findAllCategories();
}
