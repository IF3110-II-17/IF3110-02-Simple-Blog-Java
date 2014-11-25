package entities;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;

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

	public String login() {
		DatabaseUtility dbUtil = DatabaseUtility.getInstance();

		details = dbUtil.findUser(username, password);
		if (details != null) {
			loggedIn = true;
			return ("index?faces-redirect=true");
		}

		return null;
	}

	public void check() {
		if (isLoggedIn()) {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("index.jsf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getLoginLink(){
		if(!loggedIn){
			return "<a href=\"login.jsf\"><button type=\"button\" class=\"btn btn-warning\">Login</button> </a>";
		}else{
			return "<a href=\"logout.jsf\"><button type=\"button\" class=\"btn btn-warning\">Logout</button> </a>";
		}
	}
	
	public String logout(){

		loggedIn = false;
		return "index?faces-redirect=true";
	}

}
