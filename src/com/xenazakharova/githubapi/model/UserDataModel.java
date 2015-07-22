package com.xenazakharova.githubapi.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDataModel implements Serializable{
		 private int id;
	     private String login;
	     private String name;
	     private String company;
	     private String avatar;
	     private int followers;
	     private int following;
	     
	     private ArrayList<RepositoryDataModel> repositoryList;
	     
	     public UserDataModel(){}
	     
		 public UserDataModel(String login, String name, String company, String avatar, int followers, int following) {
	        this.name = login;
	        this.name = name;
	        this.company = company;
	        this.avatar = avatar;
	        this.followers = followers;
	        this.following = following;
		 }
	
		public int getId() {
			return id;
	    }
		public void setId(int id) {
	        this.id = id;
	    }

		public String getLogin() {
	    	return login;
	    }
	    public void setLogin(String login) {
	        this.login = login;
	    }
	    
	    public String getName() {
	    	return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getCompany() {
	    	return company;
	    }
		public void setCompany(String company) {
	        this.company = company;
	    }

		public String getAvatar() {
	    	return avatar;
	    }
		public void setAvatar(String avatar) {
	        this.avatar = avatar;
	    }

		public int getFollowers() {
	    	return followers;
	    }
		public void setFollowers(int followers) {
	        this.followers = followers;
	    }

		public int getFollowing() {
	    	return following;
	    }
		public void setFollowing(int following) {
	        this.following = following;
	    }
		
		public ArrayList<RepositoryDataModel> getRepositoryList() {
	    	return repositoryList;
	    }
		public void setRepositoryList(ArrayList<RepositoryDataModel> repositoryList) {
	        this.repositoryList = repositoryList;
	    }
}