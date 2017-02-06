package com.mynewmain4444;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message3333;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import org.videolan.adasvlv.R;
import org.videolan.adasvlv.gui.video.VideoPlayerActivity;

import android.net.*;


/*
import com.mediedictionary.playerlibrary.protocol;
import com.mediedictionary.playerlibrary.packet;

import com.mediedictionary.playerlibrary.MySettingMenuActivity;
import com.mediedictionary.playerlibrary.MyPlaybackActivity;
import com.mediedictionary.playerlibrary.PlayerActivity;;
*/

public class MyNewMainActivity extends Activity {
	public static test_client client = new test_client();
	public static int sdcard_flag = 0;

	public static packet.tag_JAVA_SettingMenuParm main_settingmenu_parm = protocol.pk.new tag_JAVA_SettingMenuParm();
	public static packet.tag_JAVA_CarModelParm main_carmodel_parm = protocol.pk.new tag_JAVA_CarModelParm();
	
	private static int client_connect_flag = -1;

	private Button btnLive=null;
	private Button btnSetting=null;
	private Button btnPlayback=null;
	private Button btnDatabase=null;
	private Switch btnConnect=null;
	private int connectBtn_flag = 0;
	
	private void setConnectBtnChecked(boolean b)
	{
		connectBtn_flag = 1;
	    btnConnect.setChecked(b);
	    connectBtn_flag = 0;    
	}
	
    public static Handler handler = null;
	
	public static boolean isClientConnect()
	{
		return client_connect_flag >= 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_new_main_activity);

		btnLive=(Button)super.findViewById(R.id.live);
		btnLive.setOnClickListener(new LiveOnClickListener());
		btnSetting=(Button)super.findViewById(R.id.setting);
		btnSetting.setOnClickListener(new SettingOnClickListener());
		btnPlayback=(Button)super.findViewById(R.id.playback);
		btnPlayback.setOnClickListener(new PlaybackOnClickListener());
		btnDatabase=(Button)super.findViewById(R.id.database);
		btnDatabase.setOnClickListener(new DatabaseOnClickListener());
		btnConnect=(Switch)super.findViewById(R.id.connectBtn);
		btnConnect.setOnCheckedChangeListener(new OnCheckedChangeListener() {  		  
				@Override  
	            public void onCheckedChanged(CompoundButton buttonView,  
	                    boolean isChecked) {  
	                // TODO Auto-generated method stub  
					if(connectBtn_flag == 1)
						return;
	                if (isChecked) {  
			        	Message msg = new Message(); 
			        	msg.what = 10;
			        	MyNewMainActivity.handler.sendMessage(msg);
	                } else {  
	                	client.disconnect();
	                	/*
	                	for(int i = 0; i < 20; i++)
	            		{
	            			if(my_socket.isReady())
	            			{
	            				rec = 0; 
	            				break;
	            			}
	                		try{
	                			Thread.currentThread().sleep(50); 		
	                		} catch (InterruptedException e) {
	            				// TODO Auto-generated catch block
	            				e.printStackTrace();
	            				return -1;
	            			}
	            		}
	            		*/
	                }  
	            }  
	      });
		
		// 定义Handler对象
		handler = new Handler() {  
		      @Override  
		      // 当有消息发送出来的时候就执行Handler的这个方法  
		      public void handleMessage(Message msg) {  
		          super.handleMessage(msg);  
		          if(msg.what == 11)
		          {
		        	  setConnectBtnChecked(false);
		        	  client_connect_flag = -1;
		        	  showToast("Connection to server was broke");
		          }
		          else if(msg.what == 10)
		          {

		        	  client_connect_flag = client.try_to_connect();
		        	  if(client_connect_flag >= 0)
		        	  {
		        		  setConnectBtnChecked(true);
		        		  if(client.tryToSyncMenuParam() >= 0)
		      			  {
		        			  main_settingmenu_parm.GetDataFromByteBuf(test_client.Gprocess.GetSettingMenuParmBuf()); 
		        			  main_carmodel_parm.GetDataFromByteBuf(test_client.Gprocess.GetCarModelParmBuf());  
		        			  Log.e("PDA", "----->" + main_settingmenu_parm.vehicle_parm.camera_height+""+main_carmodel_parm.camera_height_default[0]);
		      			  }
		        	  }
		        	  else
		        		  setConnectBtnChecked(false);

		          }
		          else if(msg.what == 12)
		          {
		        	  showToast("SDcard error!");
		          }
		            /*
		            int recv_code = msg.getData().getInt("recv_code");
		            
		            */
		      }  
		}; 
		
		Message msg = new Message(); 
    	msg.what = 10;
    	MyNewMainActivity.handler.sendMessage(msg);
	}
	  

   
    
	 
	private class LiveOnClickListener implements OnClickListener{
	    	
		@Override
		public void onClick(View v){
			if(!isClientConnect())
			{
				//String url = "rtsp://192.168.10.253/live";
				//Uri u = Uri.parse("rtsp://192.168.10.253/live");
				//Uri u = Uri.parse("file:///storage/sdcard1/20160817161947.avi");
				//Uri u = Uri.parse("file:///storage/sdcard1/20150910192516.avi");
				//Uri u = Uri.parse("file:///storage/sdcard1/20150902144055.avi");
				//Uri u = Uri.parse("file:///storage/sdcard1/20161121144413.avi");
				Uri u = Uri.parse("file:///storage/sdcard1/20161130151259.avi");


				//Uri u = Uri.parse("file:///storage/sdcard1/test.mp4");
				VideoPlayerActivity.start(MyNewMainActivity.this, u, "aaa");
				//startActivity(new Intent(MyNewMainActivity.this, PlayerActivity.class).putExtra("url",url ));
			}
			else
			{
				showToast("Not connected server");
				return;
			}
		}	
	}
	
	private class SettingOnClickListener implements OnClickListener{
    	
		@Override
		public void onClick(View v){
			if(isClientConnect())
			{
				startActivity(new Intent(MyNewMainActivity.this, MySettingMenuActivity.class));
			}
			else
			{
				//startActivity(new Intent(MyNewMainActivity.this, MapActivity.class));
				showToast("Not connected server");
				return;
			}
		}	
	}

	int SDcard_files_flag = 0;
	private String[] str_base_path = null;
	private String[] str_path = null;

	private class PlaybackOnClickListener implements OnClickListener{
    	
		@Override
		public void onClick(View v){
			SDcard_files_flag = 0;
			str_base_path = null;
			str_path = null;
			if(!SDCardUtils.CheckFilePathExists(MyNewMainActivity.this))
			{

				if(SDCardUtils.isSDMounted(MyNewMainActivity.this))
				{
					int sum = SDCardUtils.internal_path.size() + SDCardUtils.external_path.size();
					if(sum > 0) {
						str_base_path = new String[sum];
						str_path = new String[sum];
					}
					else
						SDcard_files_flag = -1;


					for(int i = 0;i < SDCardUtils.internal_path.size();i++) {
						str_base_path[i] = "Internal" + (i + 1);
						str_path[i] = SDCardUtils.internal_path.get(i);
					}
					for(int i = 0;i < SDCardUtils.external_path.size();i++) {
						str_base_path[i + SDCardUtils.external_path.size()] = "SDcard" + (i + 1);
						str_path[i + SDCardUtils.external_path.size()] = SDCardUtils.external_path.get(i);
					}
				}
				else
				{
					int sum = SDCardUtils.internal_path.size();
					if(sum > 0)
						str_base_path = new String[sum];
					else
						SDcard_files_flag = -1;

					for(int i = 0;i < SDCardUtils.internal_path.size();i++){
						str_base_path[i] = "Internal"+(i+1);
						str_path[i] = SDCardUtils.internal_path.get(i);
					}
				}

				if(SDcard_files_flag >= 0) {
					new AlertDialog.Builder(MyNewMainActivity.this).setTitle("Select Save Location").setIcon(
							android.R.drawable.ic_dialog_info).setSingleChoiceItems(
							str_base_path, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									SDCardUtils.SaveDefaultPath(MyNewMainActivity.this, str_path[which]);
									openPlaybackActivity();
									dialog.dismiss();
								}
							}).setNegativeButton("Cancel", null).show();
				}
				return;
			}
			openPlaybackActivity();



			//startActivity(new Intent(MyNewMainActivity.this, MyPlaybackActivity.class));




		}	
	}

	private void openPlaybackActivity()
	{
		Intent tmp = new Intent(MyNewMainActivity.this, MyPlaybackActivity.class);
		if(isClientConnect())
		{
			if(client.tryToGetPlaybackViewFileList() >= 0) {
				int list_length = test_client.Gprocess.GetSimpleResult();
				byte[] playbackviewlist = test_client.Gprocess.GetPlaybackViewFilelistBuf();
				if (playbackviewlist != null && list_length > 0) {
					tmp.putExtra("playbackviewlist", playbackviewlist);
					tmp.putExtra("viewlist_length", list_length);
				}
			}
			if(client.tryToGetPlaybackPictureFileList() >= 0) {
				int list_length = test_client.Gprocess.GetSimpleResult();
				byte[] playbackpicturelist = test_client.Gprocess.GetPlaybackPictureFilelistBuf();
				if (playbackpicturelist != null && list_length > 0) {
					tmp.putExtra("playbackpicturelist", playbackpicturelist);
					tmp.putExtra("picturelist_length", list_length);
				}
			}
		}
		else
		{
			showToast("Not connected server");
		}


		if (SDcard_files_flag >= 0)
			tmp.putExtra("SDcard_files_flag", SDcard_files_flag);
		sdcard_flag = 0;
		startActivity(tmp);
		return;
/*
		if(SDcard_files_flag >= 0)
		{
			//SDCardUtils.ReadFileNameList(type);
			if(SDCardUtils.files_in_SDcard.size() > 0) {
				Intent tmp = new Intent(MyNewMainActivity.this, MyPlaybackActivity.class);
				startActivity(tmp);
				return;
			}
		}
		showToast("Have no file!");*/
	}

	private class DatabaseOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			SDcard_files_flag = 0;
			str_base_path = null;
			str_path = null;
			if(!SDCardUtils.CheckFilePathExists(MyNewMainActivity.this))
			{

				if(SDCardUtils.isSDMounted(MyNewMainActivity.this))
				{
					int sum = SDCardUtils.internal_path.size() + SDCardUtils.external_path.size();
					if(sum > 0) {
						str_base_path = new String[sum];
						str_path = new String[sum];
					}
					else
						SDcard_files_flag = -1;


					for(int i = 0;i < SDCardUtils.internal_path.size();i++) {
						str_base_path[i] = "Internal" + (i + 1);
						str_path[i] = SDCardUtils.internal_path.get(i);
					}
					for(int i = 0;i < SDCardUtils.external_path.size();i++) {
						str_base_path[i + SDCardUtils.external_path.size()] = "SDcard" + (i + 1);
						str_path[i + SDCardUtils.external_path.size()] = SDCardUtils.external_path.get(i);
					}
				}
				else
				{
					int sum = SDCardUtils.internal_path.size();
					if(sum > 0)
						str_base_path = new String[sum];
					else
						SDcard_files_flag = -1;

					for(int i = 0;i < SDCardUtils.internal_path.size();i++){
						str_base_path[i] = "Internal"+(i+1);
						str_path[i] = SDCardUtils.internal_path.get(i);
					}
				}

				if(SDcard_files_flag >= 0) {
					new AlertDialog.Builder(MyNewMainActivity.this).setTitle("Select Save Location").setIcon(
							android.R.drawable.ic_dialog_info).setSingleChoiceItems(
							str_base_path, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									SDCardUtils.SaveDefaultPath(MyNewMainActivity.this, str_path[which]);
									openDatabaseActivity();
									dialog.dismiss();
								}
							}).setNegativeButton("Cancel", null).show();
				}
				return;
			}
			openDatabaseActivity();
		}
	}

	private void openDatabaseActivity()
	{
		Intent tmp = new Intent(MyNewMainActivity.this, MyDatabaseActivity.class);
		if(isClientConnect())
		{
			if(client.tryToGetDatabaseFileList() >= 0) {
				int list_length = test_client.Gprocess.GetSimpleResult();
				byte[] dateabaselist = test_client.Gprocess.GetDatabaseFilelistBuf();
				if (dateabaselist != null && list_length > 0) {
					tmp.putExtra("databaselist", dateabaselist);
					tmp.putExtra("databaselist_length", list_length);
				}
			}
		}
		else
		{
			showToast("Not connected server");
		}


		if (SDcard_files_flag >= 0)
			tmp.putExtra("SDcard_files_flag", SDcard_files_flag);
		sdcard_flag = 0;
		startActivity(tmp);
		return;
	}

	private Toast mToast;  
	
    public void showToast(String text) {    
        if(mToast == null) {    
            mToast = Toast.makeText(MyNewMainActivity.this, text, Toast.LENGTH_SHORT);    
        } else {    
            mToast.setText(text);      
            mToast.setDuration(Toast.LENGTH_SHORT);    
        }    
        mToast.show();    
    }    
        
    public void cancelToast() {    
            if (mToast != null) {    
                mToast.cancel();    
            }    
        }    
    
    @Override
    protected void onStop() {
        super.onStop();
        cancelToast();
    }


    @Override
    protected void onPause() {
        super.onPause();
        cancelToast();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelToast();
    }
        
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	        	showToast("Press the return key again to exit");                             
	            exitTime = System.currentTimeMillis();   
	        } else {
	        	client.stop_client();
	        	cancelToast(); 
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	

}
