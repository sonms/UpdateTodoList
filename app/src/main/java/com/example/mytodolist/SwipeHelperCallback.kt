package com.example.mytodolist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mytodolist.Adapter.TodoAdapter
import kotlin.math.min

class SwipeHelperCallback(private val mAdapter : TodoAdapter) : ItemTouchHelper.Callback() {

    // swipetest(linearlayout) 를 swipe 했을 때 <삭제> 화면이 보이도록 고정하기 위한 변수들
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

    //드래그 동작(위 아래 위치 이동)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //위 아래 위치 스왑(현재 선택 데이터와 드래그 위치에있는 데이터교환)
        val beforePos = viewHolder.adapterPosition
        val afterPos = target.adapterPosition
        mAdapter.swapData(beforePos, afterPos)
        return true
    }

    //스와이프 동작(왼 오 이동)
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //mAdapter.removeData(viewHolder.layoutPosition)
    }


    /*여기서 부터 전부 swipe 제어 함수*/

    //여기는 drag와 swipe 전부

    //getDefaultUIUtil : 사용자 상호 작용에 대한 응답으로 view의 시각적 변경을 위해 콜백 클래스에서 ItemTouchUIUtil사용하는 것을 반환합니다.

    //clearView : onSelectedChanged 등 여러 멤버에서 수행된 View의 모든 변경사항을 적용하기의 좋은 위치를 설정하는 함수로
    //요소와 사용자의 상호작용이 끝나고 애니메이션도 종료되면 ItemTouchHelper에 의해 호출됩니다.
    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        currentDx = 0f //초기화
        previousPosition = viewHolder.adapterPosition //드래그 or 스와이프가 끝난 view item의 위치를 가짐(기억)
        //clearview를 적용할 view를 호출
        getDefaultUIUtil().clearView((viewHolder as TodoAdapter.TodoViewHolder).itemView.findViewById(R.id.swipe_basic_layout))
    }


    //여기는 drag와 swipe 전부
    //ItemTouchHelper의 viewholder를 스와이프, 드래그 시 호출
    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition //현재 스와이프, 드래그의 위치를 저장
            getDefaultUIUtil().onSelected((viewHolder as TodoAdapter.TodoViewHolder).itemView.findViewById(R.id.swipe_basic_layout))
        }
    }

    //아이템 터치 또는 스와이프 등 뷰의 변화를 감지 후 호출
   override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int, //행동상태 를 파악하기위함
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder) //고정할지 말지 결정하기 위한 변수 true-고정, false-고정x
            val newX = clampViewPositionHorizontal(dX, isClamped, isCurrentlyActive) //// newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)

            //고정 애니메이션
            if (newX == -clamp) {
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L)
                return
            }

            currentDx = newX
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                newX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }


    //사용자가 스와이프 동작으로 간주할 최소 속도를 정의합니다.
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    //사용자가 스와이프 동작으로 간주하기 위해 이동해야하는 거리반환
    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        //-clamp 이상 스와이프 동작 시 isClamped를 true 아니면 false
        setTag(viewHolder, currentDx <= -clamp)
        return 2f
    }

    //스와이프 시 삭제imageview가 보이도록 고정
    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        //오른쪽으로 swipe 막기 위한 변수
        val max = 0f

        //고정이 가능하면
        val newX = if (isClamped) {
            //현재 스와이프 중이면 영역 제한
            if (isCurrentlyActive) {
                //오른쪽 스와이프일때
                if (dX < 0) {
                    dX/3 - clamp
                } else {
                    //왼쪽
                    dX - clamp
                }

            } else { //스와이프 중이 아니면 그냥 고정
                -clamp
            }
        } else {
            //고정할 수 없으면 newX는 스와이프로 움직인 만큼
            dX/2
        }
        //newX가 0보다 작은지 확인
        return min(newX,max)
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) { viewHolder.itemView.tag = isClamped }
    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean =  viewHolder.itemView.tag as? Boolean ?: false
    private fun getView(viewHolder: RecyclerView.ViewHolder) : View = viewHolder.itemView.findViewById(R.id.swipe_basic_layout)

    //view가 스와이프되었을 때 고정될 크기 설정
    fun setClamp(clamp : Float) {this.clamp = clamp}

    //다른 view를 스와이프할 때 그 전꺼는 닫혀있어야하니 터치나, 스와이프 중이였을 시 고정해제
    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition) return

        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return

            (viewHolder as TodoAdapter.TodoViewHolder).todoItemBinding.swipeBasicLayout.animate().x(0f).setDuration(100L).start()

            setTag(viewHolder,false)

            previousPosition = null
        }
    }
}