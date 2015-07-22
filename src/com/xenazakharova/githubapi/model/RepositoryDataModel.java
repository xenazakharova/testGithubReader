package com.xenazakharova.githubapi.model;

import java.io.Serializable;

public class RepositoryDataModel implements Serializable{
		 private int id;
	     private String title;
	     private String language;
	     private String countStars;
	     private String countForks;
	     
	     public RepositoryDataModel(){}
	     
		 public RepositoryDataModel(String title, String language, String countStars, String countForks) {
	        this.title = title;
	        this.language = language;
	        this.countStars = countStars;
	        this.countForks = countForks;
		 }
	
		public int getId() {
			return id;
	    }
		public void setId(int id) {
	        this.id = id;
	    }

		public String getTitle() {
	    	return title;
	    }
	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public String getLanguage() {
	    	return language;
	    }
		public void setLanguage(String language) {
	        this.language = language;
	    }

		public String getCountStars() {
	    	return countStars;
	    }
		public void setCountStars(String countStars) {
	        this.countStars = countStars;
	    }

		public String getCountForks() {
	    	return countForks;
	    }
		public void setCountForks(String countForks) {
	        this.countForks = countForks;
	    }

	}