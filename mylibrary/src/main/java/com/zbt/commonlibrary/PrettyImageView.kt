package com.zbt.commonlibrary

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Author       :zbt
 * Date         :2020/10/22 下午6:22
 * Version      :1.0.0
 * Description  :
 */

class PrettyImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
) : AppCompatImageView(context, attributeSet) {

    enum class ShapeType {
        SHAPE_CIRCLE,
        SHAPE_ROUND
    }

    // defAttr var
    private var shapeType = ShapeType.SHAPE_CIRCLE
        set(value) {
            field = value
            invalidate()
        }

    private var borderWidth = 20f
        set(value) {
            field = value
            invalidate()
        }

    private var borderColor = Color.parseColor("#FF9900")
        set(value) {
            field = value
            invalidate()
        }

    private var leftTopRadiusX = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var leftTopRadiusY = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var rightTopRadiusX = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var rightTopRadiusY = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var leftBottomRadiusX = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var leftBottomRadiusY = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var rightBottomRadiusX = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var rightBottomRadiusY = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var showBorder = true
        set(value) {
            field = value
            invalidate()
        }

    private var showCircleDot = false
        set(value) {
            field = value
            invalidate()
        }

    private var circleDotColor = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    private var circleDotRadius = 20f
        set(value) {
            field = value
            invalidate()
        }

    private lateinit var shapePath: Path
    private lateinit var borderPath: Path
    private lateinit var bitmapPaint: Paint
    private lateinit var borderPaint: Paint
    private lateinit var circleDotPaint: Paint
    private lateinit var shapeMatrix: Matrix

    // temp var
    private var viewWidth = 200// View宽度
    private var viewHeight = 200// View高度
    private var radius = 100f// 圆半径

    init {
        LogUtil.d("attributeSet: $attributeSet")
        initAttr(context, attributeSet, 0)
        initDrawTools()
    }

    private fun initAttr(context: Context, attributeSet: AttributeSet?, defAttrStyle: Int) {
        LogUtil.d("initAttr attributeSet: $attributeSet")
        val array =
            context.obtainStyledAttributes(
                attributeSet,
                R.styleable.PrettyImageView,
                defAttrStyle,
                0
            )
        (0..array.indexCount)
            .asSequence()
            .map { array.getIndex(it) }
            .forEach {
                LogUtil.d("array it: $it")
                when (it) {
                    R.styleable.PrettyImageView_shape_type -> shapeType = when {
                        array.getInt(it, 0) == 0 -> ShapeType.SHAPE_CIRCLE
                        array.getInt(it, 0) == 1 -> ShapeType.SHAPE_ROUND
                        else -> ShapeType.SHAPE_CIRCLE
                    }
                    R.styleable.PrettyImageView_border_width -> borderWidth = array.getDimension(
                        it,
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            4f,
                            resources.displayMetrics
                        )
                    )
                    R.styleable.PrettyImageView_border_color -> {
                        borderColor = array.getColor(it, Color.parseColor("#FF0000"))
                        LogUtil.d("it:$it")
                    }
                    R.styleable.PrettyImageView_left_top_radiusX -> leftTopRadiusX =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.PrettyImageView_left_top_radiusY -> leftTopRadiusY =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.PrettyImageView_left_bottom_radiusX -> leftBottomRadiusX =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.PrettyImageView_left_bottom_radiusY -> leftBottomRadiusY =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.PrettyImageView_right_top_radiusX -> rightTopRadiusX =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )
                    R.styleable.PrettyImageView_right_top_radiusY -> rightTopRadiusY =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )

                    R.styleable.PrettyImageView_right_bottom_radiusX -> rightBottomRadiusX =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )

                    R.styleable.PrettyImageView_right_bottom_radiusY -> rightBottomRadiusY =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                0f,
                                resources.displayMetrics
                            )
                        )

                    R.styleable.PrettyImageView_show_border -> showBorder =
                        array.getBoolean(it, false)
                    R.styleable.PrettyImageView_show_circle_dot -> showCircleDot =
                        array.getBoolean(it, false)
                    R.styleable.PrettyImageView_circle_dot_color -> circleDotColor =
                        array.getColor(it, Color.parseColor("#FF0000"))
                    R.styleable.PrettyImageView_circle_dot_radius -> circleDotRadius =
                        array.getDimension(
                            it,
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                5f,
                                resources.displayMetrics
                            )
                        )
                }
            }
        array.recycle()
    }

    private fun initDrawTools() {
        bitmapPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {//最终绘制图片的画笔,需要设置BitmapShader着色器，从而实现把图片绘制在不同形状图形上
                style = Paint.Style.FILL
            }
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = borderColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = borderWidth
        }

        circleDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = circleDotColor
        }

        shapePath = Path() // 描述轮廓的path路径
        borderPath = Path() // 描述图片边框轮廓的path路径
        shapeMatrix = Matrix() // 缩放矩阵
        scaleType = ScaleType.CENTER_CROP
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (shapeType == ShapeType.SHAPE_CIRCLE) {
            viewWidth = measuredWidth.coerceAtMost(measuredHeight)
            radius = viewWidth / 2.0f
            setMeasuredDimension(viewWidth, viewHeight)
        } else {
            viewHeight = measuredHeight
            viewWidth = measuredWidth
            setMeasuredDimension(viewWidth, viewHeight)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        borderPath.reset()
        shapePath.reset()

        when (shapeType) {
            ShapeType.SHAPE_CIRCLE -> {
                buildCirclePath()
            }
            ShapeType.SHAPE_ROUND -> {
                viewWidth = w
                viewHeight = h
                buildRoundPath()
            }
        }
    }

    private fun buildCirclePath() {// 构建圆角类型
        if (!showBorder) {// 绘制不带边框的圆形实际上只需要一个圆形扔进path即可
            shapePath.addCircle(radius, radius, radius, Path.Direction.CW)
        } else {// 绘制带边框的圆形需要把内部圆形和外部圆形边框都要扔进path
            shapePath.addCircle(radius, radius, radius - borderWidth, Path.Direction.CW)
            borderPath.addCircle(radius, radius, radius - borderWidth / 2.0f, Path.Direction.CW)
        }
    }

    private fun buildRoundPath() {
        if (!showBorder) {//绘制不带边框的圆角实际上只需要把一个圆角矩形扔进path即可
            floatArrayOf(
                leftTopRadiusX, leftTopRadiusY, rightTopRadiusX, rightTopRadiusY,
                rightBottomRadiusX, rightBottomRadiusY, leftBottomRadiusX, leftBottomRadiusY
            ).run {
                shapePath.addRoundRect(
                    RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat()),
                    this, Path.Direction.CW
                )
            }
        } else {// 绘制带边框的圆角时间上只需要把一个圆角矩形和一个圆角矩形变量都扔进Path即可
            floatArrayOf(
                leftTopRadiusX - borderWidth / 2.0f, leftTopRadiusY - borderWidth / 2.0f,
                rightTopRadiusX - borderWidth / 2.0f, rightTopRadiusY - borderWidth / 2.0f,
                rightBottomRadiusX - borderWidth / 2.0f, rightBottomRadiusY - borderWidth / 2.0f,
                leftBottomRadiusX - borderWidth / 2.0f, leftBottomRadiusY - borderWidth / 2.0f
            ).run {
                borderPath.addRoundRect(
                    RectF(
                        borderWidth / 2.0f,
                        borderWidth / 2.0f,
                        viewWidth.toFloat() - borderWidth / 2.0f,
                        viewHeight.toFloat() - borderWidth / 2.0f,
                    ), this, Path.Direction.CW
                )
            }

            floatArrayOf(
                leftTopRadiusX - borderWidth, leftTopRadiusY - borderWidth,
                rightTopRadiusX - borderWidth, rightTopRadiusY - borderWidth,
                rightBottomRadiusX - borderWidth, rightBottomRadiusY - borderWidth,
                leftBottomRadiusX - borderWidth, leftBottomRadiusY - borderWidth
            ).run {
                shapePath.addRoundRect(
                    RectF(
                        borderWidth,
                        borderWidth,
                        viewWidth.toFloat() - borderWidth,
                        viewHeight.toFloat() - borderWidth,
                    ), this, Path.Direction.CW
                )
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        drawable ?: return
        bitmapPaint.shader = getBitmapShader()
        when (shapeType) {
            ShapeType.SHAPE_CIRCLE -> {
                if (showBorder) {
                    canvas?.drawPath(borderPath, borderPaint)
                }
                canvas?.drawPath(shapePath, bitmapPaint)
                if (showCircleDot) {
                    drawCircleDot(canvas)
                }
            }
            ShapeType.SHAPE_ROUND -> {
                if (showBorder) {
                    canvas?.drawPath(borderPath, borderPaint)
                }
                canvas?.drawPath(shapePath, bitmapPaint)
            }
        }
    }

    private fun drawCircleDot(canvas: Canvas?) {
        canvas?.run {
            drawCircle(
                (radius + radius * (sqrt(2.0) / 2.0f)).toFloat(),
                (radius - radius * (sqrt(2.0) / 2.0f)).toFloat(),
                circleDotRadius,
                circleDotPaint
            )
        }
    }

    private fun getBitmapShader(): BitmapShader {
        val bitmap = drawableToBitmap(drawable)
        return BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
            var scale = 1.0f
            if (shapeType == ShapeType.SHAPE_CIRCLE) {
                scale = (viewWidth * 1.0f / min(bitmap.width, bitmap.height))
            } else if (shapeType == ShapeType.SHAPE_ROUND) {
                if (!(width == bitmap.width && width == bitmap.height)) {
                    scale = max(width * 1.0f / bitmap.width, height * 1.0f / bitmap.height)
                }
            }
            shapeMatrix.setScale(scale, scale)
            setLocalMatrix(shapeMatrix)
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        return Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ).apply {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(Canvas(this@apply))
        }
    }

    companion object {
        private const val STATE_INSTANCE = "state_instance"
        private const val STATE_INSTANCE_SHAPE_TYPE = "state_shape_type"
        private const val STATE_INSTANCE_BORDER_WIDTH = "state_border_width"
        private const val STATE_INSTANCE_BORDER_COLOR = "state_border_color"
        private const val STATE_INSTANCE_RADIUS_LEFT_TOP_X = "state_radius_left_top_x"
        private const val STATE_INSTANCE_RADIUS_LEFT_TOP_Y = "state_radius_left_top_y"
        private const val STATE_INSTANCE_RADIUS_RIGHT_TOP_X = "state_radius_right_top_x"
        private const val STATE_INSTANCE_RADIUS_RIGHT_TOP_Y = "state_radius_right_top_y"
        private const val STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X = "state_radius_left_bottom_x"
        private const val STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y = "state_radius_left_bottom_y"
        private const val STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X = "state_radius_right_bottom_x"
        private const val STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y = "state_radius_right_bottom_y"
        private const val STATE_INSTANCE_RADIUS = "state_radius"
        private const val STATE_INSTANCE_SHOW_BORDER = "state_radius_show_border"
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable(STATE_INSTANCE, super.onSaveInstanceState())
        putInt(
            STATE_INSTANCE_SHAPE_TYPE, when (shapeType) {
                ShapeType.SHAPE_ROUND -> 1
                ShapeType.SHAPE_CIRCLE -> 0
            }
        )
        putFloat(STATE_INSTANCE_BORDER_WIDTH, borderWidth)
        putInt(STATE_INSTANCE_BORDER_COLOR, borderColor)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_X, leftTopRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_Y, leftTopRadiusY)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X, leftBottomRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y, leftBottomRadiusY)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_X, rightTopRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_Y, rightTopRadiusY)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X, rightBottomRadiusX)
        putFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y, rightBottomRadiusY)
        putFloat(STATE_INSTANCE_RADIUS, radius)
        putBoolean(STATE_INSTANCE_SHOW_BORDER, showBorder)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is Bundle) {
            super.onRestoreInstanceState(state)
            return
        }
        with(state) {
            super.onRestoreInstanceState(getParcelable(STATE_INSTANCE))
            shapeType = when {
                getInt(STATE_INSTANCE_SHAPE_TYPE) == 0 -> ShapeType.SHAPE_CIRCLE
                getInt(STATE_INSTANCE_SHAPE_TYPE) == 1 -> ShapeType.SHAPE_ROUND
                else -> ShapeType.SHAPE_CIRCLE
            }

            borderWidth = getFloat(STATE_INSTANCE_BORDER_WIDTH)
            borderColor = getInt(STATE_INSTANCE_BORDER_COLOR)
            leftTopRadiusX = getFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_X)
            leftTopRadiusY = getFloat(STATE_INSTANCE_RADIUS_LEFT_TOP_Y)
            leftBottomRadiusX = getFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_X)
            leftBottomRadiusY = getFloat(STATE_INSTANCE_RADIUS_LEFT_BOTTOM_Y)
            rightTopRadiusX = getFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_X)
            rightTopRadiusY = getFloat(STATE_INSTANCE_RADIUS_RIGHT_TOP_Y)
            rightBottomRadiusX = getFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_X)
            rightBottomRadiusY = getFloat(STATE_INSTANCE_RADIUS_RIGHT_BOTTOM_Y)
            radius = getFloat(STATE_INSTANCE_RADIUS)
            showBorder = getBoolean(STATE_INSTANCE_SHOW_BORDER)
        }
    }
}