package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.customerDAO;
import dao.customerDAOImpl;
import dao.itemDAO;
import dao.itemDAOImpl;
import dao.addressDAO;
import dao.addressDAOImpl;
import model.Address;
import model.Customer;
import model.Item;
import dao.poDAO;
import dao.poDAOImpl;
import model.PurchaseOrder;


/**
 * Servlet implementation class FilmSiteController
 */
@WebServlet("/landing_page")
public class FilmSiteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String dbPath;
	itemDAO itemDao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilmSiteController() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		ServletContext context = config.getServletContext();
		dbPath = context.getRealPath("/WEB-INF/sqlitedb/Product.db");
		itemDao = new itemDAOImpl(dbPath);
		List<Item> filmList = itemDao.findAllItems();
		context.setAttribute("filmList", filmList);
		context.setAttribute("brandList", itemDao.findAllBrands());
		context.setAttribute("categoryList", itemDao.findAllCategories());
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		String view = "/jsp/home.jsp";		// default view
		
		// CLIENT INPUT
		if (action != null) {
			
			// GO TO REGISTER
			if (action.equalsIgnoreCase("Register an account")) {
				view = "/jsp/register.jsp";
			}
			// REGISTER ACCOUNT
			else if (action.equalsIgnoreCase("Register")) {
				
				addCustomer(request, response);
				
				view = "/jsp/response.jsp";
			}
			
			// GO TO SIGN-IN
			else if (action.equalsIgnoreCase("Sign-in to your account")) {
				view = "/jsp/sign-in.jsp";
			}
			// SIGN IN
			else if (action.equalsIgnoreCase("Sign-in")) {
				signIn(request, response);
								
				view = "/jsp/response.jsp";
				
			}
			
			// SIGN OUT
			else if (action.equalsIgnoreCase("Sign-out of your account")) {
				signOut(request, response);
				view = "/jsp/response.jsp";
			}
			
			// Sort items by prices or names
			else if (action.startsWith("Sort items") || action.startsWith("Filter")) {
				HttpSession session = request.getSession();
				String key = "priceSort";
				String by = "price";
				if(action.contains("by names")) {
					key = "nameSort";
					by = "name";
				}
				if (action.startsWith("Sort items")) {
					if (session.getAttribute(key) == null) {
						session.setAttribute(key, "asc");
					} else if (session.getAttribute(key).equals("asc")) {
						session.setAttribute(key, "desc");
					} else if (session.getAttribute(key).equals("desc")) {
						session.setAttribute(key, "asc");
					}
				}
				String brand = (String) request.getParameter("brand");
				String category = (String) request.getParameter("category");
				request.setAttribute("selectedBrand", brand);
				request.setAttribute("selectedCategory", category);
				if(action.startsWith("Filter")) {
					by = null;
				}
				List<Item> filmList = itemDao.findAllItemsSort(brand, category, by, (String)session.getAttribute(key));
				request.setAttribute("filmList", filmList);
				request.setAttribute("title", action);
				view = "/jsp/home.jsp";
			}
			
			// FILM SELECTION
			else if (action.split("-")[0].equalsIgnoreCase("film")) {
				// iterate through all films
				String film = action.split("-")[1];
				
				ServletContext context = request.getServletContext();
				List<Item> filmList = (List<Item>) context.getAttribute("filmList");
				
				for (Item f : filmList) {
					if (film.equalsIgnoreCase(f.getName())) {
						view = "/jsp/item_focus.jsp";
						request.setAttribute("item_focus", f);
					}
				}
			}
			
			// CART SELECTION
			else if (action.equalsIgnoreCase("view cart")) {
				HttpSession session = request.getSession();
				
				if (session.getAttribute("account") == null) {
					view = "/jsp/sign-in.jsp";
				}
				else if (session.getAttribute("account") != null){
					
					view = "/jsp/cart.jsp";
				}
			}
			else if (action.split(" ")[0].equalsIgnoreCase("add")) {
				
				HttpSession session = request.getSession();
				
				Customer account = (Customer) session.getAttribute("account");
				
				// REDIRECT TO SIGN IN
				if (account == null) {
					view = "/jsp/sign-in.jsp";
				}
				else if (account != null){
					
					// ADD FILM TO CART AND PO (db)
					
					// find film
					itemDAO fdao = new itemDAOImpl(dbPath);
					
					String [] cartRequest = action.split(" ");
					StringBuilder parsedFilm = new StringBuilder();
					
					for (int i = 1; i < cartRequest.length - 2; i++) {
						parsedFilm.append(cartRequest[i]).append(" ");
					}
					
					String film = parsedFilm.toString().trim();
					
					Item f = fdao.findFilmByName(film);
					
					// create new purchase order (incomplete / pending)
					poDAO poDao = new poDAOImpl();
					PurchaseOrder po = new PurchaseOrder();
					
					po.setId(poDao.generatePurchaseOrderID());
					po.setCustomerID(account.getId());
					po.setItemID(f.getItemID());
					po.setDateOfPurchase("2023-08-14");
					po.setPurchased(0);
					po.setPrice(f.getPrice());
					po.setQuantity(1);
					po.setFilmName(f.getName());
					
					// add purchase order to database
					poDao.addPurchaseOrder(po);
					
					// add purchase order to cart
					List<PurchaseOrder> cart = (List<PurchaseOrder>) session.getAttribute("cart");
					cart.add(po);
					session.setAttribute("cart", cart);
					
					// cart view
					view="/jsp/cart.jsp";
				}
				
			}
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher(view);
		dispatch.forward(request, response);
	}
	
	// 
	private List<Item> sortFilmsByPriceAsc() {
		
		return null;
	}
	
	private void signOut(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		if (session.getAttribute("account") == null) {
			request.setAttribute("message", "Sign-out Failed. You are not currently signed into an account.");
		}
		else {
			session.setAttribute("account", null);
			session.setAttribute("cart", null);
			request.setAttribute("message", "Sign-out Successful. You are no longer signed-into your account.");
		}
		request.setAttribute("redirect", "Home");
		
	}
	
	
	private void signIn(HttpServletRequest request, HttpServletResponse response) {
		
		// if login_status true redirect to sign out
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		customerDAO cdao = new customerDAOImpl();
		Customer c = cdao.findCustomerBySignIn(email, password);
		
		HttpSession session = request.getSession();

		if (c != null) {
			session.setAttribute("account", c);
			request.setAttribute("message", "Sign-in Successful.");
			request.setAttribute("redirect", "Home");
			
			// retrieve customer cart
			poDAO pdao = new poDAOImpl();
			List<PurchaseOrder> cart = pdao.findPurchaseOrdersByCustomerId(c.getId());
			session.setAttribute("cart", cart);
		}
		else {
			request.setAttribute("message", "Sign-in Failed. Incorrect E-mail or Password.");
			request.setAttribute("redirect", "Sign-in to your account");
		}
		
		
	}
	
	
	private boolean regexTest(String regex, String input) {
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		return matcher.matches();
	}
	
	
	private void addCustomer(HttpServletRequest request, HttpServletResponse response) {
		
		
		try {
			request.setAttribute("redirect", "Register an account");
			
			// FIRST NAME: non-digit
			String firstName = request.getParameter("firstName");
			if (!regexTest("^\\D+$", firstName)) {
				request.setAttribute("message", "Invalid First Name Input");
				return;
			}
			
			// LAST NAME: non-digit
			String lastName = request.getParameter("lastName");
			if (!regexTest("^\\D+$", lastName)) {
				request.setAttribute("message", "Invalid Last Name Input");
				return;
			}
			
			// STREET: ## street_name lane/ave/drive
			String street = request.getParameter("street");
			if (!regexTest("^\\d+\\s{1}\\w+\\s{1}\\w+$", street)) {
				request.setAttribute("message", "Invalid Street Input");
				return;
			}
			
			// PROVINCE: non-digit
			String province = request.getParameter("province");
			if (!regexTest("^\\D+$", province)) {
				request.setAttribute("message", "Invalid Province Input");
				return;
			}
			
			// COUNTRY: non-digit
			String country = request.getParameter("country");
			if (!regexTest("^\\D+$", country)) {
				request.setAttribute("message", "Invalid Country Input");
				return;
			}
			
			// PHONE: ###-###-#####
			String phone = request.getParameter("phone");
			if (!regexTest("^\\d{3}-\\d{3}-\\d{4}", phone)) {
				request.setAttribute("message", "Invalid Phone Input");
				return;
			}
			
			// ZIP: 6-digit alphanumeric
			String zip = request.getParameter("zip").replace(" ", "");
			if (!regexTest("^[a-zA-Z0-9]{6}$", zip)) {
				request.setAttribute("message", "Invalid ZIP Input");
				return;
			}
			
			// EMAIL: name @ domain . ca/com/eu/...
			String email = request.getParameter("email");
			if (!regexTest("^[A-Za-z0-9._]+@[A-Za-z0-9._]+\\.\\w{2,}$", email)) {
				request.setAttribute("message", "Invalid E-mail Input");
				return;
			}
			
			// PASSWORD: any alphanumeric
			String password = request.getParameter("password");
			if (!regexTest("^\\S{4,}$", password)) {
				return;
			}
			
			Address a = new Address();
			addressDAO adao = new addressDAOImpl();
			a.setStreet(street);
			a.setProvince(province);
			a.setCountry(country);
			a.setPhone(phone);
			a.setZip(zip);
			a.setId(adao.generateAddressID());
			
			Customer c = new Customer();
			customerDAO cdao = new customerDAOImpl();
			c.setFirstName(firstName);
			c.setLastName(lastName);
			c.setAddressID(a.getId());
			c.setId(cdao.generateCustomerID());
			c.setAddressID(a.getId());
			c.setEmail(email);
			c.setPassword(password);
			
			adao.addAddress(a);
			cdao.addCustomer(c);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("message", "Account Registered.");
		request.setAttribute("redirect", "Sign-in to your account");
	}
	
	
	private void findAllCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			// calling DAO method to retrieve a list of all books 
			customerDAO customerDao = new customerDAOImpl();
			List<Customer> customerList = customerDao.findAllCustomers();
			request.setAttribute("customerList", customerList);
		} 
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	/*private void addCustomer(HttpServletRequest request, HttpServletResponse response, Customer c) {
		
	}*/

}

