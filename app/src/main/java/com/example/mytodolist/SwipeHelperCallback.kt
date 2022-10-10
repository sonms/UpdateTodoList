package com.example.mytodolist

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.Adapter.TodoAdapter

class SwipeHelperCallback(private val mAdapter : TodoAdapter) : ItemTouchHelper.Callback() {

    // swipe_view 를 swipe 했을 때 <삭제> 화면이 보이도록 고정하기 위한 변수들
    private var currentPosition: Int? = null    // 현재 선택된 recycler view의 position
    private var previousPosition: Int? = null   // 이전에 선택했던 recycler view의 position
    private var currentDx = 0f                  // 현재 x 값
    private var clamp = 0f                      // 고정시킬 크기

    //이동 방향 결정정
   override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(UP or DOWN, LEFT or RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }

}