<%@ page import="com.google.gae.TinyInsta.*" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.File" %>

<%@ include file="Header.jsp" %>



  
  

  <!-- Home Container -->

  <section class="home container">

    <div class="intro">
      <h2>Partagez vos images avec le reste du monde !</h2>
    </div>

    <!-- Content -->

    <section class="content">

      <div class="pictures">
        <h3><i class="fa fa-fire"></i> Last Pictures </h3>
        <ul>
        
        <%
                	List<Entity> lastPictures = TinyInstaEndPoint.getLastPosts();
                        	
                        	if (lastPictures != null){
                        		for (Entity picture : lastPictures) {
									String auteur = (String) picture.getProperty("contributorID");
									Entity user = TinyInstaEndPoint.getUser(auteur);
                %>            
        
          <li>
            <div class="heading">
              <h4><%= picture.getProperty("title") %></h4>
              
              <img src=<%= picture.getProperty("imageFile") %> style="width:300px;height:300px;"/>
              
              <p><%= picture.getProperty("description") %> <br><br></p>
             
              <div class="stats">
               <% if(userFlag){ %>
                <div>
                  <i class="fa fa-thumbs-up"></i>
                  <a href="/picture?action=liked&id=<%= KeyFactory.keyToString(picture.getKey()) %>"><span>Likes (<%= picture.getProperty("numLiked") %>)</span></a>
                </div>
                
                <div>
				  <%
						if(!userService.getCurrentUser().getEmail().equals(auteur)) 
						{
					%>
                  <i class="fa fa-heart"></i>
                  <%
						if(!TinyInstaEndPoint.isFollowing(TinyInstaEndPoint.getUser(userService.getCurrentUser().getEmail()),user))
						{
				  %>
                  <a href="/user?action=follow&id=<%= auteur %>"><span>Follow</span></a>
                  <% } else 
						{ %>
						<span>Following</span> <% }}%> </div> <% }   else { %>
						<div>
                  <i class="fa fa-thumbs-up"></i>
                  <a><span>Likes (<%= picture.getProperty("numLiked") %>)</span></a>
                </div> <% } %>
                 
                
                
                
                
              </div>

            </div>

            <div class="info">
              
              <ul>
                <li>
                  <span>Auteur:</span>
                  <span><%= picture.getProperty("contributor") %></span>
                </li>
                <li>
                  <span>Date:</span>
                  <span><%= df.format(picture.getProperty("created")) %></span>
                </li>
              </ul>
              </div>
          </li>

		<%
            }}
		%>

        </ul>
       
      </div>
    </section>
    </section>