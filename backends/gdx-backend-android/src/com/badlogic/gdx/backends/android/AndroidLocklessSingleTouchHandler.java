/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.badlogic.gdx.backends.android;

import android.view.MotionEvent;

import com.badlogic.gdx.backends.android.AndroidLocklessInput.TouchEvent;

/**
 * Single touch handler for devices running <= 1.6
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public class AndroidLocklessSingleTouchHandler implements AndroidLocklessTouchHandler {
	public void onTouch (MotionEvent event, AndroidLocklessInput input) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		input.touchX[0] = x;
		input.touchY[0] = y;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			postTouchEvent(input, TouchEvent.TOUCH_DOWN, x, y, 0);
			input.touched[0] = true;
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			postTouchEvent(input, TouchEvent.TOUCH_DRAGGED, x, y, 0);
			input.touched[0] = true;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			postTouchEvent(input, TouchEvent.TOUCH_UP, x, y, 0);
			input.touched[0] = false;
		}

		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			postTouchEvent(input, TouchEvent.TOUCH_UP, x, y, 0);
			input.touched[0] = false;
		}
	}

	private void postTouchEvent (AndroidLocklessInput input, int type, int x, int y, int pointer) {
		TouchEvent event = input.freeTouchEvents.poll();
		if (event == null) event = new TouchEvent();
		event.timeStamp = System.nanoTime();
		event.pointer = 0;
		event.x = x;
		event.y = y;
		event.type = type;
		input.touchEvents.put(event);
	}
}
