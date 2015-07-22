package com.xenazakharova.githubapi;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xenazakharova.githubapi.model.UserDataModel;

public class StartActivity extends Activity {
	
	Typeface typeface;
	
	private GetUserDataAsynkTask getUserData_at;
	private UserDataModel userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		String deviceLocale=Locale.getDefault().getLanguage();
		if(deviceLocale.equals("ru")){
			Utils.setLocal(this,"ru");
		}else{
			Utils.setLocal(this,""); 
		}  
		
		typeface = Typeface.createFromAsset(getAssets(), "fonts/BARBH.TTF");
		
		showActionBar();
		
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setTypeface(typeface);
		
		final EditText userNameEditText = (EditText) findViewById(R.id.userNameEditText);
		userNameEditText.setTypeface(typeface);
		
		Button findButton = (Button) findViewById(R.id.findButton);
		findButton.setTypeface(typeface);
		
		findButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				
				if (!Utils.isOnline(StartActivity.this)){
		        	AlertDialog ad = new AlertDialog.Builder(StartActivity.this).create();  
		        	ad.setCancelable(false); 
		        	ad.setTitle(getString(R.string.warning));
		        	ad.setMessage(getString(R.string.no_internet_connection));
		        	
		        	ad.setButton("OK", new DialogInterface.OnClickListener() {  
		        	    @Override  
		        	    public void onClick(DialogInterface dialog, int which) {  
		        	    	dialog.dismiss();
		        	    }  
		        	});  
		        	ad.show();
			    }else{
			    	getUserData_at = new GetUserDataAsynkTask();
					Object[] paramsAssyncTask = new Object[2];
					paramsAssyncTask[0] = StartActivity.this;
					paramsAssyncTask[1] = userNameEditText.getText().toString();
					
					getUserData_at.execute(paramsAssyncTask);	
					
					try {
						userInfo = getUserData_at.get();
						if(userInfo != null && userInfo.getId()>0){
							Intent intent = new Intent(StartActivity.this, DetailUserActivity.class);
							intent.putExtra("USER_DATA", userInfo);
							startActivity(intent);
						}else{
							AlertDialog ad = new AlertDialog.Builder(StartActivity.this).create();  
				        	ad.setCancelable(false); 
				        	ad.setTitle(getString(R.string.oops));
				        	ad.setMessage(getString(R.string.user_not_found).replace("@LOGIN@", userNameEditText.getText().toString()));
				        	
				        	ad.setButton("OK", new DialogInterface.OnClickListener() {  
				        	    @Override  
				        	    public void onClick(DialogInterface dialog, int which) {  
				        	    	dialog.dismiss();
				        	    }  
				        	});  
				        	ad.show();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		    	
			    }					
				
			}
		});
		
	}
	
	@SuppressLint("NewApi")
	void showActionBar(){
		ActionBar actionBar = getActionBar();
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
			
  	    View customActionBarView = getLayoutInflater().inflate(R.layout.ab_main, null, true);
		
 	    final TextView actionBarTitle = (TextView) customActionBarView.findViewById(R.id.actionBarTitle);
 	    actionBarTitle.setTypeface(typeface);
 	   
   		actionBarTitle.setText(R.string.app_name);
   		actionBar.setCustomView(customActionBarView);
   		actionBar.setDisplayShowCustomEnabled(true);   		
	}
	
	@Override
    public void onRestart() {
      super.onRestart();
      
      	String deviceLocale=Locale.getDefault().getLanguage();
		if(deviceLocale.equals("ru")){
			Utils.setLocal(this,"ru");
		}else{
			Utils.setLocal(this,""); 
		} 
	 }

}
