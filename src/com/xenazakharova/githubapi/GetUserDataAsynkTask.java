package com.xenazakharova.githubapi;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.xenazakharova.githubapi.model.RepositoryDataModel;
import com.xenazakharova.githubapi.model.UserDataModel;

public class GetUserDataAsynkTask extends AsyncTask<Object,Void,UserDataModel> {
		String LOG_TAG="GetUserDataAsynkTaskLOG";
		String urlGithubUserApi;
			
		Context mContext;
		String userNickname;
		
		String urlRepos;
		
	  	@Override
		protected UserDataModel doInBackground(Object... params) {
	  		UserDataModel userData = null;
	  		JSONObject jsonObject = null;
	  		
			this.mContext = (Context) params[0];
	  		this.userNickname = (String) params[1];
	  		
	  		urlGithubUserApi = mContext.getString(R.string.url_user_api)+userNickname;	
	  		HttpClient client = new DefaultHttpClient();

	  		HttpGet request = new HttpGet(urlGithubUserApi);
	  		
	  		HttpResponse response;
	  		
	  	    try {
	  	    	response = client.execute(request);
	        	HttpEntity httpEntity = response.getEntity();	  	       
		  	    String jsonResponse = EntityUtils.toString(httpEntity);
		  	    
		  	    jsonObject = new JSONObject(jsonResponse);
	          
		          userData = new UserDataModel();
		          if (jsonObject!=null ) {
		        	  
	        		  userData.setId(jsonObject.getInt("id"));
	        		  userData.setLogin(jsonObject.getString("login"));
	        		  userData.setName(jsonObject.getString("name"));
	        		  userData.setCompany(jsonObject.getString("company"));
	        		  userData.setAvatar(jsonObject.getString("avatar_url"));
	        		  userData.setFollowers(jsonObject.getInt("followers"));
	        		  userData.setFollowing(jsonObject.getInt("following"));
	        		  
	        		  urlRepos = jsonObject.getString("repos_url");
	        		  
	        		  ArrayList<RepositoryDataModel> dataRepositories = new ArrayList<RepositoryDataModel>();
	        		  if (urlRepos.length()>0) {
	        	  		  
	        	  		  HttpGet reposRequest = new HttpGet(urlRepos);
	        	  		  HttpResponse reposResponse = client.execute(reposRequest);
	        	  		  HttpEntity reposHttpEntity = reposResponse.getEntity();
	    	   	  		 
	    	   	  		  String reposJson = EntityUtils.toString(reposHttpEntity);
	    	   	  		
	    	   	  		  JSONArray reposArray = new JSONArray(reposJson);
	    	   	  		
	    	   	  		  
	    	   	  		  for (int i=0;i<reposArray.length();i++) {
			       				JSONObject reposItem = reposArray.getJSONObject(i);
			       				RepositoryDataModel reposModel = new RepositoryDataModel();
			       				reposModel.setId(Integer.parseInt(reposItem.getString("id")));
			       				reposModel.setTitle(reposItem.getString("full_name"));
			       				reposModel.setLanguage(reposItem.getString("language"));
			       				reposModel.setCountStars(reposItem.getString("stargazers_count"));
			       				reposModel.setCountForks(reposItem.getString("forks_count"));
			       				
			       				dataRepositories.add(reposModel); 
	    	   	  		  }
		       			
	    	   	  	  	  userData.setRepositoryList(dataRepositories);
	    	   	  	}
	        	  
	          }
	          			
	      	  } catch (ClientProtocolException e) {
	              // TODO Auto-generated catch block
	      		Log.v("LOG_TAG","KKKK ClientProtocolException");
	          } catch (IOException e) {
	              // TODO Auto-generated catch block
	        	  Log.v("LOG_TAG","KKKK IOException");
	          } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("LOG_TAG","KKKK JSONException");
			 }	  	    
	  	  
			return userData;
	  	}
		
		protected void onPostExecute(Boolean result) {	
		}
	 }