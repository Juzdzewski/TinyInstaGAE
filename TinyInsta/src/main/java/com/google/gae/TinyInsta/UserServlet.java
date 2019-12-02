package com.google.gae.TinyInsta;


import javax.servlet.http.*;


import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gae.TinyInsta.TinyInstaEndPoint;

import java.io.IOException;

import java.util.logging.Logger;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet{
	private static final Logger log = Logger.getLogger(UserServlet.class.getName()); 
	private UserService userService = UserServiceFactory.getUserService();
	
	
public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// POST from profile save
		if (req.getParameter("save") != null) {
			log.info("saving user profile");
			
			TinyInstaEndPoint.saveUser(req.getParameter("email"), 
					req.getParameter("username"));
			
			resp.sendRedirect("/Profile.jsp");
		}
		
	}

public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	
	
	if(req.getParameter("action").equals("follow"))
	{
		TinyInstaEndPoint.followUser(req.getParameter("id"), userService.getCurrentUser().getEmail());
	}
	resp.sendRedirect("/");
	
}

}
