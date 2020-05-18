package bupt.FirstGroup.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import bupt.FirstGroup.framework.Pool;
import bupt.FirstGroup.framework.Input.TouchEvent;
import bupt.FirstGroup.framework.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler {
    private static final int MAX_TOUCHPOINTS = 10;

    private boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
    private int[] touchX = new int[MAX_TOUCHPOINTS];
    private int[] touchY = new int[MAX_TOUCHPOINTS];
    private int[] id = new int[MAX_TOUCHPOINTS];
    private Pool<TouchEvent> touchEventPool;
    private List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    private float scaleX;
    private float scaleY;

    //一个触屏事件对象池
    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        //制造触摸事件
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        //触摸事件持
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //安卓中使用32位（int）来存储触控事件的动作信息和触控索引。高16位暂时不用，后16位中高8位存储触控信息，低8位存储动作信息。
        //ACTION_MASK = 8(0x00ff)  :动作信息掩码,用于截取目标动作信息；
        //ACTION_MASK_SHIFT = 8(0x00ff) : 截取动作信息时所需移位个数
        //ACTION_POINTER_INDEX_MASK =65280(0xff00) :高8位的位置信息掩码，用于截取索引信息
        //ACTION_POINTER_INDEX_SHIFT= 8(0x00ff) :截取触控索引时所需的移位个数
        synchronized (this) {
            //and之后，无论你多少根手指加进来，都是会ACTION_POINTER_DOWN或者ACTION_POINTER_UP
            //保证多指触摸的正确性，获取触摸的动作信息
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            //ACTION_POINTER_INDEX_SHIFT = 8 触摸点索引掩码，获取触摸点索引需要移动的位数
            //获取触摸点索引， 取mAction(触控动作)高8位信息，然后再右移8位得到索引。
            //从而得知是哪一个点离开了或者哪一个点按下了。
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            //getPointerCount(); 获取触控点的数量，比如2则可能是两个手指同时按压屏幕
            int pointerCount = event.getPointerCount();
            TouchEvent touchEvent;
            for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
                //初始化isTouched数组和id数组
                if (i >= pointerCount) {
                    isTouched[i] = false;
                    id[i] = -1;
                    continue;
                }
                //每根手指从按下、移动到离开屏幕，每个手指都会拥有一个固定PointerId.PointerId的值可以是任意的值。
                //每根手指从按下、移动到离开屏幕，每根手指在每一个事件的Index可能是不固定的,因为受到其它手指的影响。
                // 比如，A跟B两根手指同时按在屏幕上，此时A的PointerIndex为0，B的则为1.当A先离开屏幕时，B的PointerIndex则变为了0.
                //但是，PointerIndex的值的不是任意的，它必须在[0，PointerCount-1]的范围内。其中PointerCount为参与触控的手指数量。
                //因此，我们追踪手指的动作事件不可依赖PointerIndex，只能靠PointerId.
                int pointerId = event.getPointerId(i);
                if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                    // if it's an up/down/cancel/out event, mask the id to see if we should process it for this touch
                    // point
                    continue;
                }
                // 动作为ACTION_MOVE || i == pointerIndex
                switch (action) {
                    //ACTION_DOWN:表示用户开始触摸(在第一个点被按下时触发)
                    case MotionEvent.ACTION_DOWN:
                    //ACTION_POINTER_DOWN:当屏幕上已经有一个点被按住，此时再按下其他点时触发
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = TouchEvent.TOUCH_DOWN;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                        touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                        Log.i("touch","ACTION_POINTER_DOWN: X-"+touchX[i]+" Y-"+touchY[i]);
                        isTouched[i] = true;
                        id[i] = pointerId;
                        touchEventsBuffer.add(touchEvent);
                        break;
                    //ACTION_UP:表示用户抬起了手指(当屏幕上唯一的点被放开时触发)
                    case MotionEvent.ACTION_UP:
                    //ACTION_POINTER_UP:当屏幕上有多个点被按住，松开其中一个点时触发(非最后一个点)
                    case MotionEvent.ACTION_POINTER_UP:
                    //ACTION_CANCEL:表示手势被取消了
                    case MotionEvent.ACTION_CANCEL:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = TouchEvent.TOUCH_UP;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                        touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                        isTouched[i] = false;
                        id[i] = -1;
                        touchEventsBuffer.add(touchEvent);
                        break;
                    //ACTION_MOVE: 当有点在屏幕上移动时触发，注意的是，由于灵敏度很高，所以基本上只要有点在屏幕上，此事件就会不停地被触发
                    case MotionEvent.ACTION_MOVE:
                        touchEvent = touchEventPool.newObject();
                        //dragged ??
                        touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                        touchEvent.pointer = pointerId;
                        //获取相对于手机屏幕的触摸点位置
                        touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                        touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                        isTouched[i] = true;
                        id[i] = pointerId;
                        touchEventsBuffer.add(touchEvent);
                        break;
                }
            }
            return true;
        }
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return false;
            else
                return isTouched[index];
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchX[index];
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchY[index];
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    // returns the index for a given pointerId or -1 if no index.
    private int getIndex(int pointerId) {
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            if (id[i] == pointerId) {
                return i;
            }
        }
        return -1;
    }
}
