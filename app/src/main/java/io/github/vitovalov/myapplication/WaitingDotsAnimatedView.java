package io.github.vitovalov.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class WaitingDotsAnimatedView extends View {

  Paint[] paint;
  private int circleDiameterDp = 0;
  private float density;
  private int[] alphas;

  public WaitingDotsAnimatedView(Context context) {
    super(context);
    init();
  }

  public WaitingDotsAnimatedView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public WaitingDotsAnimatedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    density = getResources().getDisplayMetrics().density;
    paint = new Paint[] { new Paint(), new Paint(), new Paint() };
    alphas = new int[] { 0, 0, 0 };
    for (int i = 0; i < paint.length; i++) {
      Paint p = paint[i];
      p.setColor(Color.parseColor("#FFFFFF"));
      p.setAlpha(alphas[i]);
      p.setAntiAlias(true);
      p.setStyle(Paint.Style.FILL);
    }

    final int animationDuration = 1000;

    final Handler handler = new Handler();

    final ValueAnimator animator2 = ValueAnimator.ofInt(0, 255);
    animator2.setDuration(animationDuration / 2);
    animator2.setStartDelay((animationDuration / 2));
    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        alphas[1] = (int) valueAnimator.getAnimatedValue();
        paint[1].setAlpha(alphas[1]);
      }
    });
    final ValueAnimator animator3 = ValueAnimator.ofInt(0, 255);
    animator3.setDuration(animationDuration / 3);
    animator3.setStartDelay((animationDuration / 3 * 2));
    animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        alphas[2] = (int) valueAnimator.getAnimatedValue();
        paint[2].setAlpha(alphas[2]);
      }
    });

    final ValueAnimator animator1 = ValueAnimator.ofInt(0, 255);
    animator1.setDuration(animationDuration);
    animator1.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationCancel(Animator animation) {
        super.onAnimationCancel(animation);
      }

      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);

        handler.postDelayed(new Runnable() {
          @Override public void run() {
            repeat(animator2, animator3);
            animator1.start();
          }
        }, 1000);
      }

      @Override public void onAnimationRepeat(Animator animation) {
        super.onAnimationRepeat(animation);
        System.out.println("WaitingDotsAnimatedView.onAnimationRepeat " + System.identityHashCode(this));
      }

      @Override public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
      }

      @Override public void onAnimationPause(Animator animation) {
        super.onAnimationPause(animation);
      }

      @Override public void onAnimationResume(Animator animation) {
        super.onAnimationResume(animation);
      }
    });
    animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        alphas[0] = (int) valueAnimator.getAnimatedValue();
        paint[0].setAlpha(alphas[0]);
        invalidate();

      }
    });

    animator1.start();
    animator2.start();
    animator3.start();
  }

  private void repeat(ValueAnimator animator2, ValueAnimator animator3) {
    animator2.end();
    animator3.end();
    paint[1].setAlpha(0);
    paint[2].setAlpha(0);
    invalidate();

    animator2.start();
    animator3.start();
  }

  private int dpToPx(int dp) {
    return (int) (density * dp);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int part = circleDiameterDp;
    for (int i = 0; i < paint.length; i++) {
      canvas.drawCircle(part, getHeight() / 2, circleDiameterDp / 2, paint[i]);
      if (part < getWidth()) {
        part += ((getWidth() - circleDiameterDp) / paint.length) + circleDiameterDp / 3;
      } else {
        part = circleDiameterDp;
      }
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int desiredWidth = dpToPx(30);
    int desiredHeight = dpToPx(10);

    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int width;
    int height;

    if (widthMode == MeasureSpec.EXACTLY) {
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      width = Math.min(desiredWidth, widthSize);
    } else {
      width = desiredWidth;
    }

    if (heightMode == MeasureSpec.EXACTLY) {
      height = heightSize;
    } else if (heightMode == MeasureSpec.AT_MOST) {
      height = Math.min(desiredHeight, heightSize);
    } else {
      height = desiredHeight;
    }
    circleDiameterDp = width / 6;

    setMeasuredDimension(width, height);
  }
}
