package com.xenazakharova.githubapi;

import java.util.ArrayList;

import parallax.scroll.swipe.effect.ObservableScrollView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKRequest.VKRequestListener;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiLink;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.xenazakharova.githubapi.database.DataBaseAdapter;
import com.xenazakharova.githubapi.model.RepositoryDataModel;
import com.xenazakharova.githubapi.model.UserDataModel;
import com.xenazakharova.githubapi.ui.CircularImageView;

public class DetailUserActivity extends Activity {
	
	Typeface typeface;
	
	private DataBaseAdapter mDbHelper;
	UserDataModel userDM;
	
	private static final String[] sMyScope = new String[] {
	        VKScope.FRIENDS,
	        VKScope.WALL,
	        VKScope.PHOTOS,
	        VKScope.NOHTTPS
	    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_user);
		
		typeface = Typeface.createFromAsset(getAssets(), "fonts/BARBH.TTF");
		
		showActionBar();
		
		userDM = (UserDataModel) getIntent().getSerializableExtra("USER_DATA");
		
		Log.v("LOG_TAG", "Github userDM="+userDM.getName());
		
		TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
		nameTextView.setTypeface(typeface);
		
		TextView companyTextView = (TextView) findViewById(R.id.companyTextView);
		companyTextView.setTypeface(typeface);
		
		TextView followersTextView = (TextView) findViewById(R.id.followersTextView);
		followersTextView.setTypeface(typeface);
		
		TextView followersLabelTextView = (TextView) findViewById(R.id.followersLabelTextView);
		followersLabelTextView.setTypeface(typeface);
		
		TextView followingTextView = (TextView) findViewById(R.id.followingTextView);
		followingTextView.setTypeface(typeface);
		
		TextView followingLabelTextView = (TextView) findViewById(R.id.followingLabelTextView);
		followingLabelTextView.setTypeface(typeface);
		
		CircularImageView avatarImageView = (CircularImageView) findViewById(R.id.avatarImageView);
		
		
		final View mainLinerLayout = (View) findViewById(R.id.mainLinerLayout);
		
		ListView reposListView = (ListView) findViewById(R.id.reposListView);
		
		final ImageButton globeImageButton = (ImageButton) findViewById(R.id.globeImageButton);
		final ImageButton saveImageButton = (ImageButton) findViewById(R.id.saveImageButton);
		final ImageButton shareVKImageButton = (ImageButton) findViewById(R.id.shareImageButton);
		
		Button.OnClickListener menuBtnListener = new Button.OnClickListener(){
			@Override
	        public void onClick(View v) {
	        	
	        	if (v == globeImageButton){
	        		if(userDM.getName()!=null){
		        		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_user_profile)+userDM.getLogin()));
		        		startActivity(browserIntent);
	        		}else{
	        			//display a message
	        		}
	            } 
	            if (v == saveImageButton){
	            	mDbHelper = new DataBaseAdapter(DetailUserActivity.this);
	            	mDbHelper.open(); 
	            	
	            	Cursor dataUserCursor = mDbHelper.fetchUser(userDM.getId());
	            	
	            	if(dataUserCursor.moveToFirst()){
	            		boolean updateResult  = mDbHelper.updateUser(
	            				Long.parseLong(dataUserCursor.getString(0)),//dataUserCursor.getColumnIndex(DataBaseAdapter.KEY_ROWID))),
	            				userDM.getId(),
	            	    		userDM.getLogin(),
		            			userDM.getName(),	            			
		            			userDM.getCompany(),
		            			userDM.getAvatar(),
		            			userDM.getFollowers(),
		            			userDM.getFollowing());
	            		
	            		if (updateResult){
				        	AlertDialog ad = new AlertDialog.Builder(DetailUserActivity.this).create();  
				        	ad.setCancelable(false); 
				        	ad.setTitle(getString(R.string.warning));
				        	ad.setMessage(getString(R.string.update_user_DB).replace("@ID@", dataUserCursor.getString(0)));
				        	
				        	ad.setButton("OK", new DialogInterface.OnClickListener() {  
				        	    @Override  
				        	    public void onClick(DialogInterface dialog, int which) {  
				        	    	dialog.dismiss();
				        	    }  
				        	});  
				        	ad.show();
		            	} 
	                   		
	            	}else{
		            	Long _id = mDbHelper.insertUser(userDM.getId(),
		            			userDM.getLogin(),
		            			userDM.getName(),	            			
		            			userDM.getCompany(),
		            			userDM.getAvatar(),
		            			userDM.getFollowers(),
		            			userDM.getFollowing());
	

		            	if (_id>0){
				        	AlertDialog ad = new AlertDialog.Builder(DetailUserActivity.this).create();  
				        	ad.setCancelable(false); 
				        	ad.setTitle(getString(R.string.warning));
				        	ad.setMessage(getString(R.string.add_user_DB).replace("@ID@", _id.toString()));
				        	
				        	ad.setButton("OK", new DialogInterface.OnClickListener() {  
				        	    @Override  
				        	    public void onClick(DialogInterface dialog, int which) {  
				        	    	dialog.dismiss();
				        	    }  
				        	});  
				        	ad.show();
		            	} 
	            	}
	              mDbHelper.close();
	            } 
	            if (v == shareVKImageButton){		            	
	            	Call_VK();    	        	 
	            }	            
	        }
	    };
	    
	    globeImageButton.setOnClickListener(menuBtnListener);
	    saveImageButton.setOnClickListener(menuBtnListener);
	    shareVKImageButton.setOnClickListener(menuBtnListener);
	    
	    ObservableScrollView parallaxScrollView = (ObservableScrollView) findViewById(R.id.parallaxScrollView);
	    
		parallaxScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
	       @Override
	       public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
	           
	
	           if(mainLinerLayout!=null){
	        	   mainLinerLayout.setTranslationY(scrollView.getScrollY() /2);
	           }
	       }
	     });
		
		nameTextView.setTypeface(typeface);
		companyTextView.setTypeface(typeface);
		followersTextView.setTypeface(typeface);
		followingTextView.setTypeface(typeface);
		
		nameTextView.setText((userDM.getName()!=null && userDM.getName().length()>0) ? userDM.getName():userDM.getLogin());
		if(userDM.getCompany()!=null)
			companyTextView.setText(", "+userDM.getCompany());
		followersTextView.setText(Integer.toString(userDM.getFollowers()));
		followingTextView.setText(Integer.toString(userDM.getFollowing()));
		
		Picasso.with(this).load(userDM.getAvatar()).error(R.drawable.github).into(avatarImageView);
		
		Display mDisplay = getWindowManager().getDefaultDisplay();
		int widthDevice  = mDisplay.getWidth();	

	    LayoutParams paramsNewsImage = (LayoutParams) avatarImageView.getLayoutParams();
	    paramsNewsImage.width = (int) (widthDevice*0.4);
	    paramsNewsImage.height = (int) (widthDevice*0.4);//LayoutParams.WRAP_CONTENT;
	    avatarImageView.setLayoutParams(paramsNewsImage);
		
		ArrayList<RepositoryDataModel> reposArray = userDM.getRepositoryList();	
		
		if(reposArray!=null && reposArray.size()>0){
			TextView labelReposTextView = new TextView(this);
			labelReposTextView.setTextSize(30);
			labelReposTextView.setTextColor(Color.parseColor("#515151"));
			labelReposTextView.setText(getString(R.string.repositories));
			labelReposTextView.setBackgroundColor(Color.parseColor("#ffffff"));
			labelReposTextView.setTypeface(typeface);
			reposListView.addHeaderView(labelReposTextView);
		}
		
		RepositoryListAdapter repositoryListAdapter = new RepositoryListAdapter(reposArray);
		reposListView.setAdapter(repositoryListAdapter);	
		repositoryListAdapter.notifyDataSetChanged();
		
		Utils.setListViewHeightBasedOnChildren(reposListView);
		
	}
	
	@Override 
	protected void onResume() { 
		super.onResume(); 
		VKUIHelper.onResume(this); 
	} 
	
	@Override 
	protected void onDestroy() { 
		super.onDestroy(); 
		VKUIHelper.onDestroy(this); 
	} 
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		super.onActivityResult(requestCode, resultCode, data); 
		VKUIHelper.onActivityResult(requestCode, resultCode, data); 
	}
	
	private final VKSdkListener sdkListener = new VKSdkListener() {
	        @Override
	        public void onCaptchaError(VKError captchaError) {
	            new VKCaptchaDialog(captchaError).show();
	        }

	        @Override
	        public void onTokenExpired(VKAccessToken expiredToken) {
	            VKSdk.authorize(sMyScope);
	        }

	        @Override
	        public void onAccessDenied(VKError authorizationError) {
	            new AlertDialog.Builder(DetailUserActivity.this)
	                    .setMessage(authorizationError.errorMessage)
	                    .show();
	        }

	        @Override
	        public void onReceiveNewToken(VKAccessToken newToken) {
	            startVKActivity();
	        }

	        @Override
	        public void onAcceptUserToken(VKAccessToken token) {
	        	startVKActivity();
	        }
	};
	
	void Call_VK(){
		VKSdk.initialize(sdkListener, getString(R.string.vk_appID)); 
		VKSdk.authorize(sMyScope, true, true);
	};
		
	private void startVKActivity() {
	 		
    	VKAttachments attachments = new VKAttachments();	
        VKApiLink link = new VKApiLink();
        link.url =  this.getString(R.string.url_user_profile)+userDM.getLogin();
        Log.v("LOG_TAG", "SocialShare startTestActivity link.url = "+link.url);
        attachments.add(link);

        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, getString(R.string.app_name)));

        post.executeWithListener(new VKRequestListener() { 
        	@Override 
        	public void onComplete(VKResponse response) {
        	} 
        	@Override 
        	public void onError(VKError error) { 
        	} 
        	
        	@Override 
        	public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) { 
        	} 
        	
    	}); 
    }
	
	public class RepositoryListAdapter extends BaseAdapter {
		LayoutInflater inflator;
		ArrayList<RepositoryDataModel> dataRepos;
		
		public RepositoryListAdapter(ArrayList<RepositoryDataModel> data){
			 this.dataRepos = data;
		}			 
		 
		public class ViewHolder{
			 public TextView reposTitle;
			 public TextView reposLang;
			 public TextView reposCountStars;
			 public TextView reposCountForks;
		}
		 
		 public Object getItem(int position){
			 return position;
		 }
		 
		 public View getView(final int position, View convertView, ViewGroup parent){
			 View view = convertView;
			 ViewHolder holder;
			 
			  if (convertView == null) {
				  holder = new ViewHolder();
				  
				  LayoutInflater inflater = LayoutInflater.from(parent.getContext());

				   view = inflater.inflate(R.layout.repos_cell, parent, false);
					  
				  holder.reposTitle = (TextView) view.findViewById(R.id.reposTitle);
				  holder.reposLang = (TextView) view.findViewById(R.id.reposLang);
				  holder.reposCountStars = (TextView) view.findViewById(R.id.reposCountStars);
				  holder.reposCountForks = (TextView) view.findViewById(R.id.reposCountForks);
		  
				  
				  holder.reposTitle.setTypeface(typeface);
				  holder.reposLang.setTypeface(typeface);
				  holder.reposCountStars.setTypeface(typeface);
				  holder.reposCountForks.setTypeface(typeface);
					 
				   view.setTag(holder);
				  
			  } else{
				  holder = (ViewHolder) view.getTag();
			  }

			  holder.reposTitle.setText(dataRepos.get(position).getTitle());
			  if(!dataRepos.get(position).getLanguage().equals("null"))
				  holder.reposLang.setText(getString(R.string.language)+": "+dataRepos.get(position).getLanguage());
			  holder.reposCountStars.setText(dataRepos.get(position).getCountStars());
			  holder.reposCountForks.setText(dataRepos.get(position).getCountForks());
			  
			  return view;
			}
		 
		 
			@Override
			public int getCount() {	
				if(dataRepos!=null && dataRepos.size()>0)
					return dataRepos.size();
				else return 0;
			}
	
			@Override
			public long getItemId(int arg0) {
				return arg0;
			}	 
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
}
