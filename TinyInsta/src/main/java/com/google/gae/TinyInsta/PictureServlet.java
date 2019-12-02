package com.google.gae.TinyInsta;


import javax.servlet.http.*;


import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import com.google.gae.TinyInsta.TinyInstaEndPoint;


import java.io.IOException;

import java.util.logging.Logger;


@SuppressWarnings("serial")
public class PictureServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(PictureServlet.class.getName()); 
	private UserService userService = UserServiceFactory.getUserService(); 
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// POST from profile add form
		if (req.getParameter("save") != null) {
			log.info("saving Picture");
			
			TinyInstaEndPoint.savePost(userService.getCurrentUser().getEmail(), 
					req.getParameter("picture-title"),
					req.getParameter("picture-description"),
					req.getParameter("picture-file"));
			
			resp.sendRedirect("/Profile.jsp");
		}
		
	}
	
public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	
		if(req.getParameter("action").equals("toto"))
		{
			log.info("starting");
			TinyInstaEndPoint.TestContention();
			log.info("testes ");
			resp.sendRedirect("/");
		}
		// GET from tag liked
		if (req.getParameter("action").equals("liked")) {
			
			TinyInstaEndPoint.likePost(req.getParameter("id"), 
					userService.getCurrentUser().getEmail());
			
			log.info("liked picture ");
			resp.sendRedirect("/");
		}

		
		
		
	}

	
	
}
