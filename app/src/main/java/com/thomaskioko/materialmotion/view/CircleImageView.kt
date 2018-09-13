package com.thomaskioko.materialmotion.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import com.thomaskioko.materialmotion.R


/**
 * This class is borrowed from {@link https://github.com/hdodenhof/CircleImageView} and re-written in kotlin
 */
open class CircleImageView : ImageView {

    private val drawableRect = RectF()
    private val borderRect = RectF()

    private val shaderMatrix = Matrix()
    private val bitmapPaint = Paint()
    private val borderPaint = Paint()
    private val circleBackgroundPaint = Paint()

    private var defaultBorderColor = DEFAULT_BORDER_COLOR
    private var defaultBorderWidth = DEFAULT_BORDER_WIDTH
    private var defaultCircleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR

    private var mBitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0

    private var drawableRadius: Float = 0.toFloat()
    private var borderRadius: Float = 0.toFloat()

    private var colorFilter: ColorFilter? = null

    private var ready: Boolean = false
    private var setupPending: Boolean = false
    private var overlay: Boolean = false
    private var isDisableCircularTransformation: Boolean = false
        set(disableCircularTransformation) {
            if (isDisableCircularTransformation == disableCircularTransformation) {
                return
            }

            field = disableCircularTransformation
            initializeBitmap()
        }

    private var borderColor: Int
        get() = defaultBorderColor
        set(@ColorInt borderColor) {
            if (borderColor == defaultBorderColor) {
                return
            }

            defaultBorderColor = borderColor
            borderPaint.color = defaultBorderColor
            invalidate()
        }

    private var circleBackgroundColor: Int
        get() = defaultCircleBackgroundColor
        set(@ColorInt circleBackgroundColor) {
            if (circleBackgroundColor == defaultCircleBackgroundColor) {
                return
            }

            defaultCircleBackgroundColor = circleBackgroundColor
            circleBackgroundPaint.color = circleBackgroundColor
            invalidate()
        }

    var fillColor: Int
        @Deprecated("Use {@link #getCircleBackgroundColor()} instead.")
        get() = circleBackgroundColor
        @Deprecated("Use {@link #setCircleBackgroundColor(int)} instead.")
        set(@ColorInt fillColor) {
            circleBackgroundColor = fillColor
        }

    var borderWidth: Int
        get() = defaultBorderWidth
        set(borderWidth) {
            if (borderWidth == defaultBorderWidth) {
                return
            }

            defaultBorderWidth = borderWidth
            setup()
        }

    var isBorderOverlay: Boolean
        get() = overlay
        set(borderOverlay) {
            if (borderOverlay == overlay) {
                return
            }

            overlay = borderOverlay
            setup()
        }


    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int = 0) {

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0)
            defaultBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, DEFAULT_BORDER_WIDTH)
            defaultBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color, DEFAULT_BORDER_COLOR)
            overlay = a.getBoolean(R.styleable.CircleImageView_civ_border_overlay, DEFAULT_BORDER_OVERLAY)

            // Look for deprecated civ_fill_color if civ_circle_background_color is not set
            if (a.hasValue(R.styleable.CircleImageView_civ_circle_background_color)) {
                defaultCircleBackgroundColor = a.getColor(R.styleable.CircleImageView_civ_circle_background_color,
                        DEFAULT_CIRCLE_BACKGROUND_COLOR)
            } else if (a.hasValue(R.styleable.CircleImageView_civ_fill_color)) {
                defaultCircleBackgroundColor = a.getColor(R.styleable.CircleImageView_civ_fill_color,
                        DEFAULT_CIRCLE_BACKGROUND_COLOR)
            }

            a.recycle()

            super.setScaleType(SCALE_TYPE)
            ready = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                outlineProvider = OutlineProvider()
            }

            if (setupPending) {
                setup()
                setupPending = false
            }
        }

    }

    override fun getScaleType(): ImageView.ScaleType {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ImageView.ScaleType) {
        if (scaleType != SCALE_TYPE) {
            throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType))
        }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (adjustViewBounds) {
            throw IllegalArgumentException("adjustViewBounds not supported.")
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isDisableCircularTransformation) {
            super.onDraw(canvas)
            return
        }

        if (mBitmap == null) {
            return
        }

        if (defaultCircleBackgroundColor != Color.TRANSPARENT) {
            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, circleBackgroundPaint)
        }
        canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, bitmapPaint)
        if (defaultBorderWidth > 0) {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }


    @Deprecated("Use {@link #setBorderColor(int)} instead")
    fun setBorderColorResource(@ColorRes borderColorRes: Int) {
        borderColor = context.resources.getColor(borderColorRes)
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === colorFilter) {
            return
        }

        colorFilter = cf
        applyColorFilter()
        invalidate()
    }

    override fun getColorFilter(): ColorFilter? {
        return colorFilter
    }

    private fun applyColorFilter() {
        bitmapPaint.colorFilter = colorFilter
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {
            val bitmap: Bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun initializeBitmap() {
        mBitmap = if (isDisableCircularTransformation) {
            null
        } else {
            getBitmapFromDrawable(drawable)
        }
        setup()
    }

    private fun setup() {
        if (!ready) {
            setupPending = true
            return
        }

        if (width == 0 && height == 0) {
            return
        }

        if (mBitmap == null) {
            invalidate()
            return
        }

        bitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = defaultBorderColor
        borderPaint.strokeWidth = defaultBorderWidth.toFloat()

        circleBackgroundPaint.style = Paint.Style.FILL
        circleBackgroundPaint.isAntiAlias = true
        circleBackgroundPaint.color = defaultCircleBackgroundColor

        bitmapHeight = mBitmap!!.height
        bitmapWidth = mBitmap!!.width

        borderRect.set(calculateBounds())
        borderRadius = Math.min((borderRect.height() - defaultBorderWidth) / 2.0f, (borderRect.width() - defaultBorderWidth) / 2.0f)

        drawableRect.set(borderRect)
        if (!overlay && defaultBorderWidth > 0) {
            drawableRect.inset(defaultBorderWidth - 1.0f, defaultBorderWidth - 1.0f)
        }
        drawableRadius = Math.min(drawableRect.height() / 2.0f, drawableRect.width() / 2.0f)

        applyColorFilter()
        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        shaderMatrix.set(null)

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight.toFloat()
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth.toFloat()
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate((dx + 0.5f).toInt() + drawableRect.left, (dy + 0.5f).toInt() + drawableRect.top)

        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
    }

    private fun inTouchableArea(x: Float, y: Float): Boolean {
        return Math.pow((x - borderRect.centerX()).toDouble(), 2.0) + Math.pow((y - borderRect.centerY()).toDouble(), 2.0) <= Math.pow(borderRadius.toDouble(), 2.0)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private inner class OutlineProvider : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            val bounds = Rect()
            borderRect.roundOut(bounds)
            outline.setRoundRect(bounds, bounds.width() / 2.0f)
        }

    }

    companion object {

        private val SCALE_TYPE = ImageView.ScaleType.CENTER_CROP

        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 2

        private const val DEFAULT_BORDER_WIDTH = 0
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT
        private const val DEFAULT_BORDER_OVERLAY = false
    }

}
