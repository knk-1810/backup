package com.websarva.wings.android.backup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Arc extends View{

    private final Paint paint;
    private final Paint paint2;
    private RectF rect;
    private Boolean viewFlag;

    // Animation 開始地点をセット
    private static final int AngleTarget = 270;
    // 初期 Angle
    private float angle = 0;

    public Arc(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Arcの幅
        int strokeWidth = 30;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);

        // Arcの色
        paint.setColor(Color.argb(255, 0, 191, 255));
        // Arcの範囲
        rect = new RectF();

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(strokeWidth);
        paint2.setColor(Color.argb(255, 250, 250, 250));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBaseChart(canvas);
        drawValueChart(canvas);

    }

    private void drawBaseChart(Canvas canvas) {
        // 背景、半透明
        canvas.drawColor(Color.argb(0, 0, 0, 0));
        // Canvas 中心点
        float x = getWidth()/2;
        float y = getHeight()/2;
        // 半径
        float radius = getWidth()/2 - 30;

        // 円弧の領域設定
        rect.set(x - radius, y - radius, x + radius, y + radius);

        // 円弧の描画
        canvas.drawArc(rect, 0, 360, false, paint2);
    }

    private void drawValueChart(Canvas canvas) {
        // 背景、半透明
        canvas.drawColor(Color.argb(0, 0, 0, 0));

        // Canvas 中心点
        float x = getWidth()/2;
        float y = getHeight()/2;
        // 半径
        float radius = getWidth()/2 - 30;

        // 円弧の領域設定
        rect.set(x - radius, y - radius, x + radius, y + radius);

        // 円弧の描画
        canvas.drawArc(rect, AngleTarget, angle, false, paint);
    }


    // AnimationAへ現在のangleを返す
    public float getAngle() {
        return angle;
    }

    // AnimationAから新しいangleが設定される
    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setColor() {
        paint.setColor(Color.argb(255, 255, 0, 0));
    }
}
