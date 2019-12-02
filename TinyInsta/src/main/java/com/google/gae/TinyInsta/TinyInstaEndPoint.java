package com.google.gae.TinyInsta;


import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;





import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.Map;

import java.util.logging.*;




@SuppressWarnings("unchecked")
public class TinyInstaEndPoint {
	

	private static final Logger log = Logger.getLogger(TinyInstaEndPoint.class.getName()); 
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	
	public static List<Entity> getLastPosts() throws EntityNotFoundException {
		
		// create a query targeting Posts entities
		Query q = new Query("Posts");	
		
		// sort by created descending
		q.addSort("created", SortDirection.DESCENDING);
		
		// execute and return the first 5 results
		return datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));
	}	

	
	
	
	public static void savePost(String userEmail, String title, String description, String imagePath)
	{
		
		// retrieve User entity
		Entity user = null;
		try {
			user = datastore.get(KeyFactory.createKey("User", userEmail));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		// create a new Posts entity
		Entity post = new Entity("Posts");
			
		// set properties
		post.setProperty("title", title);
		post.setProperty("description", description);
		post.setProperty("contributor", (String) user.getProperty("username"));
		post.setProperty("contributorID", userEmail);
		post.setProperty("numLiked", 0);
		post.setProperty("created", new Date());
		post.setProperty("imageFile", imagePath);
		
		// save to datastore
		datastore.put(post);

	}
	
	public static void likePost(String postKey, String userEmail)
	{

		Entity post = null, user = null;
				
		// get User entity from datastore, construct key from email
		try{
			user = datastore.get(KeyFactory.createKey("User", userEmail));
		}
		catch (EntityNotFoundException e){
			e.printStackTrace();
		}

		// get Posts entity from datastore
		try{
			post = datastore.get(KeyFactory.stringToKey(postKey));
		}
		catch (EntityNotFoundException e){
			e.printStackTrace();
		}
				
		// get liked key property
		List<Key> liked = (List<Key>) user.getProperty("liked");
					
		if (liked == null)
			// user has no likes yet, initialize key property
			liked = new ArrayList<Key>();
		else {
			// check if user already liked achievement and ignore if they have
			for (Iterator<Key> iter = liked.listIterator(); iter.hasNext(); ) {
				Key likedKey = iter.next();
				if (likedKey.equals(post.getKey())) {
					log.info("already liked!");
					return;						
			      }
			    }
		}
					
		// add achievement key to liked key property
		liked.add(post.getKey());
					
		// set liked property of keys to User entity
		user.setProperty("liked", liked);
					
		// increment numLiked property on Posts entity
		post.setProperty("numLiked", ((long) post.getProperty("numLiked")) + 1);
					
		// save to datastore
		datastore.put(user);
		datastore.put(post);	
	}	

	public static void TestContention()
	{
		Entity user1 = new Entity("User", "user1@example.com");
		user1.setProperty("username", "user1");
		user1.setProperty("liked", new ArrayList<Key>());
		user1.setProperty("followers", new ArrayList<Key>());
		user1.setProperty("following", new ArrayList<Key>());
		user1.setProperty("numFollowers", 0);
		user1.setProperty("numFollowing", 0);
		datastore.put(user1);
		
		for(int i = 0;i<100;i++)
		{
			String name = "user" + (i+2);
			String mail = name + "@example.com";
			Entity us = new Entity("User", mail);
			us.setProperty("username", name);
			us.setProperty("liked", new ArrayList<Key>());
			us.setProperty("followers", new ArrayList<Key>());
			us.setProperty("following", new ArrayList<Key>());
			us.setProperty("numFollowers", 0);
			us.setProperty("numFollowing", 0);
			
			datastore.put(us);
		}
		
		Entity post = new Entity("Posts");
		
		// set properties
		post.setProperty("title", "Shit");
		post.setProperty("description", "this is a test");
		post.setProperty("contributor", "user1");
		post.setProperty("contributorID", "user1@example.com");
		post.setProperty("numLiked", 0);
		post.setProperty("created", new Date());
		post.setProperty("imageFile", "https://timesofindia.indiatimes.com/thumb/msid-67586673,width-800,height-600,resizemode-4/67586673.jpg");
		
		datastore.put(post);
		
		String postKey = KeyFactory.keyToString(post.getKey());
		
		
		/*for(int i = 0;i<100;i++)
		{
			System.out.println("abc");
			String name = "user" + (i+2);
			String mail = name + "@example.com";
			likePost(postKey, mail);
		}*/
		
		Thread[] th = new Thread[2];
		for(int i = 0;i<th.length;i++)
		{
			final int deb = i*50;
			final int fin = (i+1) * 50;
			th[i] = ThreadManager.createThreadForCurrentRequest(new Runnable() {
				public void run() {
					for(int j=deb; j<fin;j++)
					{
						String name = "user" + (j+2);
						String mail = name + "@example.com";
						likePost(postKey,mail);
					}
				}
			});
			th[i].start();
		}
		
	}
	
	public static void saveUser(String userEmail, String username)
	{
		// get User, if they don't exist, create entity with default values
		Entity user;
		try{
			user = datastore.get(KeyFactory.createKey("User", userEmail));
			user.setProperty("username", username);
		}
		catch (EntityNotFoundException enf){
			user = new Entity("User", userEmail);
			user.setProperty("username", username);
			user.setProperty("liked", new ArrayList<Key>());
			user.setProperty("followers", new ArrayList<Key>());
			user.setProperty("following", new ArrayList<Key>());
			user.setProperty("numFollowers", 0);
			user.setProperty("numFollowing", 0);					
		}
		

		// save to datastore
		datastore.put(user);	
	}	
	
	public static void followUser(String followedKey, String userEmail)
	{
		Entity user = null, followed = null;
		
		// get User entity (user) from datastore, construct key from email
		try{
			//user = datastore.get(KeyFactory.createKey("User", userEmail));
			user = getUser(userEmail);
		}
		catch (EntityNotFoundException enf){
			enf.printStackTrace();
		}
		
		// get User entity (user following) from datastore
		try{
			//followed = datastore.get(KeyFactory.stringToKey(followedKey));
			followed = getUser(followedKey);
		}
		catch (EntityNotFoundException enf){
			enf.printStackTrace();
		}		

		// get following key list property
		List<Key> following = (List<Key>) user.getProperty("following");
		List<Key> followers = (List<Key>) followed.getProperty("followers");		
		
		// user is not following anyone yet, initialize key list
		if (following == null)
			following = new ArrayList<Key>();

		// user is not followed by anyone yet, initialize key list
		if (followers == null)
			followers = new ArrayList<Key>();			
			
		// add User key to followers key property
		following.add(followed.getKey());
		followers.add(user.getKey());
			
		// set following property on User entity
		user.setProperty("following", following);
		followed.setProperty("followers", followers);
			
		//updates datastore
		datastore.put(user);
		datastore.put(followed);				
	}

	
	public static boolean isFollowing(Entity user, Entity followedUser) {
		
		// get following property of User keys
		List<Key> following = (List<Key>) user.getProperty("following");		
		
		// check if user already follows this User
		if (following != null)
		{
		    for (Iterator<Key> iter = following.listIterator(); iter.hasNext();) {
			  Key curKey = iter.next();
			  if (curKey.equals(followedUser.getKey())) {
				  return true;						
		      }
		    }
		}
		
		return false;
	}
	
	
	
	public static List<Entity> getFollowers(Entity user){
		
		// get followers property of User keys
		List<Key> followerKeys = (List<Key>) user.getProperty("followers");
		if (followerKeys == null)
			return new ArrayList<Entity>();

		// get all users from keys
		Map<Key, Entity> followersMap = datastore.get(followerKeys);
		List<Entity> followers = new ArrayList<Entity>(followersMap.values());
		
		return followers;	
	}
	
	public static List<Entity> getFollowing(Entity user)
	{
		List<Key> followingKeys = (List<Key>) user.getProperty("following");
		if (followingKeys == null)
			return new ArrayList<Entity>();

		// get all users from keys
		Map<Key, Entity> followingMap = datastore.get(followingKeys);
		List<Entity> following = new ArrayList<Entity>(followingMap.values());
		
		return following;
	}
	
	public static Entity getUser(String userEmail) throws EntityNotFoundException {
		// return user via get operation from email
		try {
			return datastore.get(KeyFactory.createKey("User", userEmail));	
		}
		catch(EntityNotFoundException enf)
		{
			return null;
		}
	    	
	}

	
	
	
	
	
	
}
