package com.sxjs.common.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class NXHooldeView extends ImageView implements ValueAnimator.AnimatorUpdateListener {

    public static int VIEW_SIZE = 20;
    private boolean isAddAnimtor = false;

    protected Context mContext;
    protected Paint mPaint4Circle;
    protected int radius;

    protected Point startPosition;
    protected Point endPosition;
    private int width = 0;
    private int height = 0;

    public NXHooldeView(Context context) {
        this(context,null, null);
    }

    public NXHooldeView(Context context, Drawable drawable) {
        this(context,drawable, null);
    }

    public NXHooldeView(Context context, Drawable drawable,int width, int height) {
        this(context,drawable, null);
        this.height = height;
        this.width = width;
    }

    public NXHooldeView(Context context,Drawable drawable, AttributeSet attrs) {
        this(context,drawable, attrs, 0);
    }

    public NXHooldeView(Context context, Drawable drawable,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mPaint4Circle = new Paint();
        mPaint4Circle.setColor(Color.TRANSPARENT);
        mPaint4Circle.setAntiAlias(true);

        setImageDrawable(drawable);
    }


    public void setStartPosition(Point startPosition) {
        startPosition.y -= 10;
        this.startPosition = startPosition;
    }

    public void setEndPosition(Point endPosition) {
        this.endPosition = endPosition;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

       if(width>height){
           VIEW_SIZE = height;
        } else{
           VIEW_SIZE = width;
       }
        int PX4SIZE = (int) convertDpToPixel(VIEW_SIZE, mContext);
        setMeasuredDimension(PX4SIZE, PX4SIZE);
        radius = PX4SIZE / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, mPaint4Circle);
        super.onDraw(canvas);
    }


    public void startBeizerAnimation() {
        if (startPosition == null || endPosition == null) return;
        AnimatorSet animatorSet = new AnimatorSet();
        int pointX = (startPosition.x + endPosition.x) / 2;
        int pointY = (int) (startPosition.y - convertDpToPixel(100, mContext));
        Point controllPoint = new Point(pointX, pointY);
        BezierEvaluator bezierEvaluator = new BezierEvaluator(controllPoint);
        final ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator, startPosition, endPosition);
        anim.addUpdateListener(this);
        anim.setDuration(400);
        ViewGroup viewGroup = (ViewGroup) getParent();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) getParent();
                viewGroup.removeView(NXHooldeView.this);
                isAddAnimtor = false;
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(NXHooldeView.this, "scaleX", 1.0f, 0.4f);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(NXHooldeView.this, "scaleY", 1.0f, 0.4f);

        animatorSet.playTogether(anim,objectAnimator2,objectAnimator3);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        Point point = (Point) animation.getAnimatedValue();
        setX(point.x);
        setY(point.y);
        invalidate();
    }


    public class BezierEvaluator implements TypeEvaluator<Point> {

        private Point controllPoint;

        public BezierEvaluator(Point controllPoint) {
            this.controllPoint = controllPoint;
        }

        @Override
        public Point evaluate(float t, Point startValue, Point endValue) {
            int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
            int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
            return new Point(x, y);
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();

        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


    /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    // 2)Drawable → Bitmap
    public static Bitmap convertDrawable2BitmapSimple(Drawable drawable){
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }




    // Bitmap → Drawable
    public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        return bd;
    }

}
