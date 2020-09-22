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
class MouseLoadingView : View {

    //嘴的半径
    private var mouseRadius = 0f
    //小圆的半径
    private var ballRadius = 0f
    //嘴和小球的间距
    private var space = 0f

    //嘴张开的角度 - >张嘴的动画因子
     private var mouseAngle = 0f

    //小球移动的动画因子
    private var ballTranslateX = 0f

    //保存所有的动画对象
    private var animators = mutableListOf<ValueAnimator>()

    //嘴的圆心
    private var cx = 0f
    private var cy = 0f

    //画笔
    private val mPaint = Paint().apply {
        style = Paint.Style.FILL
        color = context.resources.getColor(R.color.colorAccent,null)
    }

    constructor(context: Context):super(context){}
    constructor(context: Context,attrs:AttributeSet?):super(context,attrs){}
    constructor(context: Context,attrs:AttributeSet?,style:Int):super(context,attrs,style){}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //计算尺寸
        //小球的半径
        ( Math.min(measuredHeight,measuredWidth)/8.5f ).also {r->
            ballRadius = r
            //嘴的半径
            mouseRadius = 3*r
            //嘴和小球的间距
            space = r/2

            //嘴的圆心
            cx = ((measuredWidth- 8.5*r)/2 + 3*r).toFloat()
            cy = measuredHeight/2f

        }

    }

    override fun onDraw(canvas: Canvas?) {
        //绘制圆弧
       canvas?.drawArc(
           cx-mouseRadius,
           cy-mouseRadius,
           cx+mouseRadius,
           cy+mouseRadius,
           mouseAngle,360-2*mouseAngle,true,mPaint
       )
        //绘制小球
        canvas?.drawCircle(cx+ballTranslateX,cy,ballRadius,mPaint)
    }

    //创建动画
    private fun createAnimator() {
        //mouseAngle  0 - 45 - 0
        ValueAnimator.ofFloat(0f, 45f, 0f).apply {
            duration = 650
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                mouseAngle = it.animatedValue as Float
                //刷新界面
                invalidate()
            }
            animators.add(this)
        }

        //小球 4.5R - > 0
        ValueAnimator.ofFloat(4.5f*ballRadius, 0f).apply {
            duration = 650
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                ballTranslateX = it.animatedValue as Float
                //刷新界面
                invalidate()
            }
            animators.add(this)
        }
    }
//启动动画
    private fun start(){
        for(anim in animators){
            anim.start()
        }
    }
//暂停动画
    private fun stop(){
    for(anim in animators){
        anim.end()
    }
    }
    //提供给外部使用
    //显示动画
    fun show(){
     createAnimator()
        start()
    }

    //隐藏动画
    fun hide(){
      stop()
    }
}