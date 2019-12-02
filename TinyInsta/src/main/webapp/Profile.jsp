<%@ page import="com.google.gae.TinyInsta.*" %>
<%@ page import="com.google.appengine.api.datastore.*" %>
<%@ page import="java.util.*" %>

<%@ include file="Header.jsp" %>

   
<%
   	String heading;
   	Entity user = null;
   	
   	try{
   		user = TinyInstaEndPoint.getUser(userService.getCurrentUser().getEmail());
   		heading = "Edit your profile";
   	}
   	catch (EntityNotFoundException e) {
   		heading = "Add your profile";
   	}
   %>

  <section class="profile container">
  <div class="intro">
    <h2><%=heading%></h2>
  </div>
    
    <!-- Content -->
    
    <section class="content">
      <section class="profile-edit">
        <div class="profile-info">
          <h2><i class="fa fa-edit"></i> My Profile</h2>

          <div class="profile-info-body">
            <form name="edit-user" action="/users/save" method="post">
              <div class="username">
                <label>Username:</label>
                <input type="text" name="username" value="<%=user != null ? user.getProperty("username") : "yourusername"%>"></input>
              </div>

              <div class="email">
                <label>Email:</label>
                <input type="email" name="email" value="<%=userService.getCurrentUser().getEmail()%>"></input>
              </div>

              <div class="following">
                <label>Following:</label>
                <ul>
                <%
                	if (user != null) {
                                	List<Entity> following = TinyInstaEndPoint.getFollowing(user);
                                	if (following != null) {
                	                	for (Entity followee : following) {
                %>
							<li><%=followee.getProperty("username")%></li>
				<%
					} 
						  } 
						}
				%>
                </ul>
              </div>

              <button name="save" type="submit">Update</button>
            </form>
          </div>
        </div>

        <div class="add-new">
          <h2><i class="fa fa-plus"></i> Post a picture</h2>

          <div class="add-new-body">
            <form name="add-new" action="/pictures/save" method="post">
              <div class="name">
                <label>Name:</label>
                <input type="text" name="picture-title" value="New Picture"></input>
              </div>

              
              <div class="pictureFile">
              <label>Upload Picture :</label>
              <input type="text" name="picture-file" value="url"></input>
              </div>
              
              <div class="description">
                <label>Description:</label>
                <textarea name="picture-description"></textarea>
              </div>

              <button name="save" type="submit">Add</button>
            </form>
          </div>
        </div>
      </section>
      </section>
      </section>

      

