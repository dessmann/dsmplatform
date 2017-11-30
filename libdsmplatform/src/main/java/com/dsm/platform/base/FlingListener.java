package com.dsm.platform.base;

import android.view.MotionEvent;
import android.view.View;

public class FlingListener implements android.view.GestureDetector.OnGestureListener{

	private View view;
	private ItemClickListener itemClickListener;
    public void setView(View view) {
		this.view = view;
	}

	public void setItemClickListener(ItemClickListener itemClickListener){
    	this.itemClickListener = itemClickListener;
    }
	
	@Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    	if(e2.getX()-e1.getX()>20){
//        	view.setTranslationX(0);
            view.setVisibility(View.GONE);
        }else if(e1.getX()-e2.getX()>20){
//            view.setTranslationX((-1)*44);
            view.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {//点击列表项
		if (itemClickListener != null) {
			itemClickListener.onItemClick();
		}
		return true;
    }
    
    public interface ItemClickListener{
    	void onItemClick();
    }
}
