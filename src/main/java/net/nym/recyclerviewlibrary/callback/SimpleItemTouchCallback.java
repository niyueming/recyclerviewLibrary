/*
 * Copyright (c) 2017  Ni YueMing<niyueming@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */

package net.nym.recyclerviewlibrary.callback;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author niyueming
 * @date 2017-03-14
 * @time 17:35
 */

public class SimpleItemTouchCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener mListener;

    public SimpleItemTouchCallback(ItemTouchHelperListener listener){
        mListener = listener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;   //false：不需要长按拖拽功能，手动控制；true：长按触发拖拽功能
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;   //false：不能滑动；true：滑动触发事件
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags;      //拖动
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        // 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END；swipeFlags = 0不支持滑动 //左右滑动
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //不同type之间不可移动
        if (viewHolder.getItemViewType() != target.getItemViewType()){
            return false;
        }

        if (mListener != null){
            mListener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        }

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mListener != null){
            mListener.onItemSwiped(viewHolder.getAdapterPosition());
        }
    }

    /**
     * 在每次View Holder的状态变成拖拽 (ACTION_STATE_DRAG) 或者 滑动 (ACTION_STATE_SWIPE)的时候被调用。
     * 这是把你的item view变成激活状态的最佳地点。
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setSelected(true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 在一个view被拖拽然后被放开的时候被调用，同时也会在滑动被取消或者完成ACTION_STATE_IDLE)的时候被调用。
     * 这里是恢复item view idle状态的典型地方。
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setSelected(false);
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * 可以在这自定义动画
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {//重写默认的滑动动画
//            float width = (float) viewHolder.itemView.getWidth();
//            float alpha = 1.0f - Math.abs(dX) / width;
//            viewHolder.itemView.setAlpha(alpha);
//            viewHolder.itemView.setTranslationX(dX);
//        } else {
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
//                    actionState, isCurrentlyActive);
//        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                actionState, isCurrentlyActive);
    }
}
