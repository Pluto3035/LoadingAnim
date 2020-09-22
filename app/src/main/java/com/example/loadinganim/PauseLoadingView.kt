package com.example.loadinganim

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @Description
 * 代码高手
 */
class PauseLoadingView : View {

    //圆球的半径
    private var radius = 0f
    //第一个球的中心点坐标
    private var cx= 0f
    private var cy = 0f

    private val mPaint = Paint().apply {
        color = context.resources.getColor(R.color.colorAccent,null)
        style = Paint.Style.FILL
    }

    //动画因子  缩放的比例
    private var mScale = 1.0f
    //动画对象
    private var mAnimators = mutableListOf<ValueAnimator>()
    //保存延时的时间
    private val delays = arrayOf(120L,240L,360L)
    //保存每个圆缩放的比例
    private val scales = arrayOf(1f,1f,1f)

    constructor(context: Context):super(context){}
    constructor(context: Context, attrs: AttributeSet?):super(context,attrs){}
    constructor(context: Context, attrs: AttributeSet?, style:Int):super(context,attrs,style){}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
       if(measuredWidth>= measuredHeight){
           radius = measuredHeight/2f
           //确定宽度是否能过容纳下
           if(7*radius>measuredWidth){
               //重新计算radius
               radius = measuredWidth/7f
           }
       }else{
          radius = measuredWidth/7f
           //确定高度是否能够容纳下
           if(2*radius>measuredHeight){
           radius = measuredHeight/2f
           }
       }
        cx= (measuredWidth/2f - 2.5*radius).toFloat()
        cy = measuredHeight/2f
    }

    override fun onDraw(canvas: Canvas?) {
        /**
        canvas?.drawCircle(cx,cy,radius,mPaint)
        canvas?.drawCircle(cx+2.5f*radius,cy,radius,mPaint)
        canvas?.drawCircle(cx+5f*radius,cy,radius,mPaint)
        */

        //ps 图层  每个圆就在一个图层上，最后合并成一个图层

        for(i in 0..2){
            canvas?.save()
            canvas?.translate(cx+i*2.5f*radius,cy)
            canvas?.scale(scales[i],scales[i])  //获取每个圆点对应的缩放比例
            canvas?.drawCircle(0f,0f,radius,mPaint)
            canvas?.restore()
        }
    }

    private fun createAnimator(){
        for(i in 0..2){
        ValueAnimator.ofFloat(1f,0.4f).apply {
            duration = 600
            //设置每个圆点对应的延迟时间
            startDelay = delays[i]
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                scales[i] =   it.animatedValue as Float  //设置每个圆点对应的缩放因子的值
                invalidate()
            }
          mAnimators.add(this)
        }
    }
    }

    private fun start(){
        for (item in mAnimators){
            item.start()
        }
    }
    private  fun stop(){
        for (item in mAnimators){
            item.end()
        }
    }
    /**
     三个动画分别作用于每一个圆点
     */
    //启动动画
    fun show(){
        createAnimator()
       start()
    }
    //隐藏动画
    fun hide(){
      stop()
    }
}