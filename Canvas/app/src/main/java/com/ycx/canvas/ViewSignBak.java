package com.ycx.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李小明 on 17/5/27.
 * 邮箱:287907160@qq.com
 */

public class ViewSignBak extends View {
    /**
     * 笔画X坐标起点
     */
    private float mX;
    /**
     * 笔画Y坐标起点
     */
    private float mY;

    private int[] colors = {Color.BLACK, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

    private List<Path> paths = new ArrayList<>();
    private List<Paint> paints = new ArrayList<>();

    private Bitmap bitmap;
    private Canvas canvasTmp;

    public ViewSignBak(Context context) {
        super(context);
        init(context);
    }

    public ViewSignBak(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewSignBak(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.i("lxm", "onmesure = " + sizeWidth + "...." + sizeHeight);
        bitmap = Bitmap.createBitmap(sizeWidth, sizeHeight, Bitmap.Config.ARGB_8888);
        canvasTmp = new Canvas(bitmap);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void init(Paint paint, int color) {
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置签名笔画样式
        paint.setStyle(Paint.Style.STROKE);
        //设置笔画宽度
        paint.setStrokeWidth(5);
        //设置签名颜色
        paint.setColor(color);
    }

    private int i = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Path path = new Path();
                Paint paint = new Paint();
                if (i >= colors.length) {
                    i = 0;
                }
                init(paint, colors[i]);
                i++;
                mX = x;
                mY = y;
                // mPath绘制的绘制起点
                path.moveTo(x, y);
                paints.add(paint);
                paths.add(path);
                break;
            case MotionEvent.ACTION_MOVE:
                final float previousX = mX;
                final float previousY = mY;
                final float dx = Math.abs(x - previousX);
                final float dy = Math.abs(y - previousY);
                // 两点之间的距离大于等于3时，生成贝塞尔绘制曲线
                if (dx >= 3 || dy >= 3) {
                    // 设置贝塞尔曲线的操作点为起点和终点的一半
                    float cX = (x + previousX) / 2;
                    float cY = (y + previousY) / 2;
                    // 二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
                    paths.get(paths.size() - 1).quadTo(previousX, previousY, cX, cY);
                    // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
                    mX = x;
                    mY = y;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        // 更新绘制
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int j = 0; j < paths.size(); j++) {

            canvasTmp.drawPath(paths.get(j), paints.get(j));

        }
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    public  String saveBitmap(String picName) {

        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lxm/";
        File dirFile = new File(sdCardDir);  //目录转化成文件夹
        if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
            dirFile.mkdirs();
        }

        File f = new File(sdCardDir, picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i("lxm", "保存图片成功");
            return sdCardDir + picName;
        } catch (Exception e) {
            Log.i("lxm", "保存图片异常" + e.toString());
            e.printStackTrace();
        }
        return null;
    }
}
