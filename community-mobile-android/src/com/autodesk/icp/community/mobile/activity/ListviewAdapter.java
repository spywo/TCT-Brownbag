package com.autodesk.icp.community.mobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autodesk.icp.community.mobile.util.DataDefine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListviewAdapter  extends BaseAdapter {
	public List<Map<String, Object>> list;
    Activity activity;
    //private DBlite dBlite1 = null;
 
    public ListviewAdapter(Activity activity, List<Map<String, Object>> list) {
        super();
        this.activity = activity;
        this.list = list;
        //dBlite1 = ((MaterialList)activity).getDB();
    }
 

    public int getCount() {
        // TODO Auto-generated method stub
    	int temp = list.size();
        return temp;
    }
 

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }
 

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
 
    private class ViewHolder {
           TextView txtFirst;
           TextView txtSecond;
           TextView txtThird;
           //TextView txtFourth;
      }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
 
        // TODO Auto-generated method stub
                ViewHolder holder;
                LayoutInflater inflater =  activity.getLayoutInflater();
 
                if (convertView == null)
                {
                    convertView = inflater.inflate(R.layout.listview_row, null);
                    holder = new ViewHolder();
                    holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
                    holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
                    holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);
                    //holder.txtFourth = (TextView) convertView.findViewById(R.id.FourthText);
                    convertView.setTag(holder);
                }
                else
                {
                    holder = (ViewHolder) convertView.getTag();
                }
 
                Map<String, Object> map = list.get(position);
                holder.txtFirst.setText(map.get(DataDefine.NUMBER).toString());
                //holder.txtFirst.setWidth(20);
                //holder.txtFirst.setTextSize(16);
                holder.txtSecond.setText(map.get(DataDefine.MATERIALNAME).toString());
                //holder.txtSecond.setWidth(500);
                //holder.txtSecond.setTextSize(16);
                holder.txtThird.setText(map.get(DataDefine.MATERIALQUANTITY).toString());
                //holder.txtThird.setWidth(30);
                //holder.txtThird.setTextSize(16);
                //holder.txtFourth.setText(map.get(DataDefine.EMAIL).toString());
                
              //对ListView中的每一行信息配置OnClick事件  
                convertView.setOnClickListener(new OnClickListener(){  

                    public void onClick(View v) {
                    	String strNum = ((TextView)v.findViewById(R.id.FirstText)).getText().toString();
                    	String strName = ((TextView)v.findViewById(R.id.SecondText)).getText().toString();
                    	String strQuan = ((TextView)v.findViewById(R.id.ThirdText)).getText().toString();
                    	
                    	Intent intent =new Intent(activity, null);
                    	
                    	Bundle bundle =new Bundle();
                        bundle.putString(DataDefine.NUMBER, strNum);
                        bundle.putString(DataDefine.MATERIALNAME, strName);
                        bundle.putString(DataDefine.MATERIALQUANTITY, strQuan); 
                        bundle.putString("ACTION", "check");
                        intent.putExtras(bundle);
                        activity.startActivity(intent);                		
                		
                		try{
                		
                			/*Cursor c = dBlite1.getReadableDatabase().query(true, DataDefine.TABLE_CUSTOMER, columns, selection, selectionArgs, groupBy, having, orderBy, null);
                			if(c != null && c.getCount() == 1){
                				Intent signOn = new Intent();
                				c.moveToFirst();
                        		signOn.putExtra(DataDefine.ID, c.getString(c.getColumnIndex(DataDefine.ID)));
                        		signOn.putExtra(DataDefine.ACTION, DataDefine.DISPLAY);
                        		//signOn.setClass(activity, SignOn.class);
                        		activity.startActivity(signOn);
                        		
        	        			c.close();
        	        			dBlite1.getReadableDatabase().close();
        	        			return;
                			}*/
                			return;
                			
                		}catch(Exception e){
                			e.printStackTrace();
                		}
                		//dBlite1.getReadableDatabase().close();
                    }  
                      
                });  
                  
                //对ListView中的每一行信息配置OnLongClick事件  
                convertView.setOnLongClickListener(new OnLongClickListener(){  

                    public boolean onLongClick(View v) {  
                    	String strTelephone = ((TextView)v.findViewById(R.id.ThirdText)).getText().toString();
                    	
                    	//get the information for current customer.
                		String[] columns = new String[] { DataDefine.ID };
                		String selection = DataDefine.TELEPHONE +  " =?";
                		String[] selectionArgs = new String[] { strTelephone };
                		String groupBy = null;
                		String having = null;
                		String orderBy = "id desc";
                		
                		
                		try{
                			/*Cursor c = dBlite1.getReadableDatabase().query(true, DataDefine.TABLE_CUSTOMER, columns, selection, selectionArgs, groupBy, having, orderBy, null);
                			if(c != null && c.getCount() == 1){
                				c.moveToFirst();
                				final String strID = c.getString(c.getColumnIndex(DataDefine.ID));
                        		signOn.putExtra(DataDefine.ID, c.getString(c.getColumnIndex(DataDefine.ID)));
                        		signOn.putExtra(DataDefine.ACTION, DataDefine.DISPLAY);
                        		signOn.setClass(activity, SignOn.class);
                        		activity.startActivity(signOn);
                				new AlertDialog.Builder(activity)
                		        .setTitle(activity.getString(R.string.title_activity_sign_on))
                		        .setMessage(activity.getString(R.string.cn_tipsfordeleting))
                		        .setPositiveButton(activity.getString(R.string.cn_ok), new DialogInterface.OnClickListener() {
                		            public void onClick(DialogInterface dialog, int which) {
                		            	dBlite1.getWritableDatabase().delete(DataDefine.TABLE_CUSTOMER, DataDefine.ID + "=?", new String[]{strID});
                	        			dBlite1.getWritableDatabase().close();
                		            	
                		            }
                		        })
                		        .setNegativeButton(activity.getString(R.string.cn_cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //cancel event

                                    }
                                })
                		        .show();
                        		
        	        			c.close();
        	        			dBlite1.getReadableDatabase().close();
        	        			return true;
                			}*/
                			return true;
                			
                		}catch(Exception e){
                			e.printStackTrace();
                		}
                		//dBlite1.getReadableDatabase().close();
                		return true;
                    }  
                }); 
                
 
            return convertView;
    }
}
