package entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constrain.Constant;
import controller.DatabaseUtility;

@ManagedBean
@SessionScoped
public class UserData implements Serializable {

	private static final long serialVersionUID = -8430435915513518517L;
	private String username, password;
	private boolean loggedIn = false;
	private UserDetails details;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserDetails getDetails() {
		return details;
	}

	public String getUserHeader() {
		return ("header.xhtml");
	}

	public String login() {
		DatabaseUtility dbUtil = DatabaseUtility.getInstance();

		details = dbUtil.findUser(username, password);
		if (details != null) {
			System.out.println("Login, Username and Password Found");
			loggedIn = true;
			loginCookie();
			return ("index?faces-redirect=true");
		}

		return null;
	}

	public void check(int p, String page) {
		int now = 1;
		if (details != null)
			now <<= (details.getRole() / 10);

		if ((now & p) == 0) {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(page);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getLoginLink() {
		if (!isLoggedIn()) {
			return "<a href=\"login.jsf\"><button type=\"button\" class=\"btn btn-warning\">Login</button> </a>";
		} else {
			return "<a href=\"logout.jsf\"><button type=\"button\" class=\"btn btn-warning\">Logout</button> </a>";
		}
	}

	public String logout() {
		loggedIn = false;
		setUsername(null);
		setPassword(null);
		logoutCookie();
		System.out.println("Logout Done");
		return "index?faces-redirect=true";
	}

	public List<NavigationMenu> getUserMenu() {
		List<NavigationMenu> result = new ArrayList<NavigationMenu>();
		if (loggedIn == false) {
			return result;
		} else if (details.getRole() == 10) {
			result.add(new NavigationMenu("Home", "index.jsf"));
			result.add(new NavigationMenu("Add Post", "add_post.jsf"));
			result.add(new NavigationMenu("My Post", ""));
			return result;
		} else if (details.getRole() == 20) {
			result.add(new NavigationMenu("Home", "index.jsf"));
			result.add(new NavigationMenu("Editor Menu", ""));
			return result;
		} else if (details.getRole() == 30) {
			result.add(new NavigationMenu("Home", "index.jsf"));
			result.add(new NavigationMenu("Add Post", "add_post.jsf"));
			result.add(new NavigationMenu("Post Manager", ""));
			result.add(new NavigationMenu("User Manager", "crud.jsf"));
			return result;
		} else {
			return result;
		}
	}
	
	private void logoutCookie(){
		FacesContext fc = FacesContext.getCurrentInstance();

		Cookie cookieUser = new Cookie("username", null);
		Cookie cookiePassword = new Cookie("password", null);

		cookieUser.setMaxAge(0);
		cookiePassword.setMaxAge(0);

		HttpServletResponse servletResponse = (HttpServletResponse) (fc
				.getExternalContext().getResponse());
		servletResponse.addCookie(cookieUser);
		servletResponse.addCookie(cookiePassword);
	}
	
	private void loginCookie(){
		FacesContext fc = FacesContext.getCurrentInstance();
		
		Cookie cookieUser = new Cookie("username", username);
		Cookie cookiePassword = new Cookie("password", password);
		
		cookieUser.setMaxAge(Constant.COOKIE_MAX_AGE);
		cookiePassword.setMaxAge(Constant.COOKIE_MAX_AGE);
		
		HttpServletResponse servletResponse = (HttpServletResponse) (fc.getExternalContext().getResponse());
		servletResponse.addCookie(cookieUser);
		servletResponse.addCookie(cookiePassword);
	}
	
	public void checkCookie(){
		FacesContext fc = FacesContext.getCurrentInstance();
		
		HttpServletRequest servletRequest = (HttpServletRequest) (fc.getExternalContext().getRequest());
		Cookie cookies[] = servletRequest.getCookies();
		
		if(cookies != null && !isLoggedIn()){
			for(int i = 0; i< cookies.length; i++){
				String cookieName = cookies[i].getName();
				String cookieValue = cookies[i].getValue();
				
				if(cookieName.equals("username"))
					setUsername(cookieValue);
				
				if(cookieName.equals("password"))
					setPassword(cookieValue);
			}
			
			login();
		}
	}
}
