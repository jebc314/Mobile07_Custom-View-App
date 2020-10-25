package com.cuijeb.customviewapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {
    float dy = 1f;
    float[] leafy;
    float[] leafx;
    int[] fallingcolors;
    Paint paint = new Paint();
    // Leaves color
    // red, mandarin, orange, yellow
    int[] leafColors = {
            Color.parseColor("#C91E0A"),
            Color.parseColor("#DF3908"),
            Color.parseColor("#E98604"),
            Color.parseColor("#EDA421")
    };
    // leaves size
    float leafSize = getWidth() / 40f;

    // ground leaves
    float[] groundx = new float[900];
    float[] groundy = new float[900];
    int[] groundcolors = new int[900];

    //ranges for leaves on trees
    float xrange;
    float yrange;

    // tree leaves
    float[][] treex = new float[4][];
    float[][] treey = new float[4][];
    int[][] treecolors = new int[4][];

    // path object
    Path pathShape = new Path();

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // tree 1
        treex[1] = new float[50];
        treey[1] = new float[50];
        treecolors[1] = new int[50];
        // tree 2
        treex[2] = new float[400];
        treey[2] = new float[400];
        treecolors[2] = new int[400];
        // tree 3
        treex[3] = new float[60];
        treey[3] = new float[60];
        treecolors[3] = new int[60];
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        generate();

    }

    public void generate() {
        // leaves position ranges
        double xrange = getWidth();
        double yrange = getHeight() / 2d;
        leafSize = getWidth() / 40f;
        double yshift = getHeight() / 2d + leafSize;
        // ground leaf position
        for (int i = 0; i < groundx.length; i++) {
            groundx[i] = (float) (Math.random() * xrange);
            groundy[i] = (float) (Math.random() * yrange + yshift);
            groundcolors[i] = (int) (Math.random() * leafColors.length);
        }

        // tree leaves
        for (int i = 1; i < treex.length; i++) {
            for (int j = 0; j < treex[i].length; j++) {
                double angle = Math.random() * Math.PI * 2;
                int colorPos = (int) (Math.random() * leafColors.length);
                switch (i) {
                    case 1:
                        xrange = (float) (getWidth() / 7f * (Math.random() * 2 - 1));
                        yrange = (float) (getWidth() / 9f * (Math.random() * 2 - 1));
                        treex[i][j] = (float) (Math.sin(angle) * xrange) + 3 * getWidth() / 8f;
                        treey[i][j] = (float) (Math.cos(angle) * yrange) + getHeight() / 4f * 1.1f;
                        treecolors[i][j] = leafColors[colorPos];
                        break;
                    case 2:
                        xrange = (float) (4 * getWidth() / 9f * (Math.random() * 2 - 1));
                        yrange = (float) (4 * getWidth() / 9f * (Math.random() * 2 - 1));
                        treex[i][j] = (float) (Math.sin(angle) * xrange) + getWidth() / 18f;
                        treey[i][j] = (float) (Math.cos(angle) * yrange);
                        treecolors[i][j] = leafColors[colorPos];
                        break;
                    case 3:
                        xrange = (float) (getWidth() / 3f * (Math.random() * 2 - 1));
                        yrange = (float) (getWidth() / 3f * (Math.random() * 2 - 1));
                        treex[i][j] = (float) (Math.sin(angle) * xrange) + getWidth();
                        treey[i][j] = (float) (Math.cos(angle) * yrange);
                        treecolors[i][j] = leafColors[colorPos];
                        break;
                }
            }
        }



        // Generate moving leaves positions
        leafy = new float[25];
        leafx = new float[25];
        fallingcolors = new int[25];
        for(int i = 0; i < leafx.length; i++) {
            leafy[i] = (float)(Math.random() * getHeight());
            leafx[i] = (float)(Math.random() * getWidth());
            int colorPos = (int) (Math.random() * leafColors.length);
            fallingcolors[i] = leafColors[colorPos];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the background
        // Pastel yellow
        paint.setColor(Color.rgb(253, 253, 150));
        // Draws the rectangle
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // Draw the ground / dirt
        paint.reset();
        // pastel brown
        paint.setColor(Color.rgb(131, 105, 83));
        // Draw the rectangle
        canvas.drawRect(0, getHeight() / 2f, getWidth(), getHeight(), paint);

        // Draw the leaves on the ground (all below half the screen)
        paint.reset();
        // Drawing the leaves
        for (int i = 0; i < groundx.length; i++) {
            float x = groundx[i];
            float y = groundy[i];
            int colorPos = groundcolors[i];
            paint.setColor(leafColors[colorPos]);
            canvas.drawCircle(x, y, leafSize, paint);
        }

        // Draw the road
        paint.reset();
        // Pastel brown
        //paint.setColor(Color.rgb(131, 105, 83));
        // dark tan
        paint.setColor(Color.rgb(210, 180, 140));
        paint.setStyle(Paint.Style.FILL);
        // Path points
        float[] pathx = {
                0, getWidth() / 3f, 2 * getWidth() / 3f,
                2 * getWidth() / 3f + getWidth() / 7f,
                2 * getWidth() / 3f, getWidth()
        };
        float[] pathy = {
                getHeight(), getHeight() / 2f + getHeight() / 8f,
                getHeight() / 2f, getHeight() / 2f,
                getHeight() / 2f + getHeight() / 8f, getHeight()
        };
        // Path to make road
        pathShape.moveTo(pathx[0], pathy[0]);
        for (int i = 1; i < pathx.length + 1; i++) {
            int pos = i % pathx.length;
            pathShape.lineTo(pathx[pos], pathy[pos]);
        }
        // polygon for road
        canvas.drawPath(pathShape, paint);

        // Draw tree1
        paint.reset();
        // Pastel brown
        paint.setColor(Color.rgb(131, 105, 83));
        paint.setStyle(Paint.Style.FILL);
        pathShape.reset();
        // Path points
        int w = getWidth();
        float[] tree3x = {
                w / 6f, w / 4f,
                w / 3f, w / 2f,
                2 * w / 3f, w,
                w, 5 * w / 6f,
                7 * w / 12f, 7 * w / 12f,
                5 * w / 8f, 3 * w / 10f,
                w / 3f, w / 3f,
                0, 0,
                w / 3f, w / 3f
        };
        int h = getHeight();
        float[] tree3y = {
                0, 0,
                2 * h / 25f, 5 * h / 16f,
                2 * h / 25f, 0,
                2 * h / 25f, h / 8f,
                3 * h / 8f, 5 * h / 6f,
                h, h,
                13 * h / 16f, 3 * h / 8f,
                3 * h / 16f, 2 * h / 25f,
                5 * h / 16f, 3 * h / 16f
        };
        // Path to make tree
        pathShape.reset();
        pathShape.moveTo(tree3x[0] + w / 4f, tree3y[0] + h / 4f);
        for (int i = 1; i < tree3x.length + 1; i++) {
            int pos = i % tree3x.length;
            pathShape.lineTo(tree3x[pos] / 4 + w / 4f, tree3y[pos] / 4 + h / 4f);
        }
        // polygon for tree
        canvas.drawPath(pathShape, paint);

        // Draw tree1 leaves
        paint.reset();
        // leaves size
        leafSize = getWidth() / 60f;
        // leaf position
        for (int i = 0; i < treex[1].length; i++) {
            float x = treex[1][i];
            float y = treey[1][i];
            paint.setColor(treecolors[1][i]);
            canvas.drawCircle(x, y, leafSize, paint);
        }


        // Draw tree2
        paint.reset();
        // Pastel brown
        paint.setColor(Color.rgb(131, 105, 83));
        paint.setStyle(Paint.Style.FILL);
        pathShape.reset();
        // Path points
        float[] tree2x = {
                0, getWidth() / 9f,
                getWidth() / 6f, 2 * getWidth() / 9f,
                getWidth() / 3f + getWidth() / 18f, getWidth() / 3f + getWidth() / 9f,
                2 * getWidth() / 9f, 2 * getWidth() / 9f,
                getWidth() / 9f - 20, getWidth() / 9f,
                getWidth() / 9f
        };
        float[] tree2y = {
                0, 0,
                getHeight() / 4f, getHeight() / 8f,
                0, 0,
                getHeight() / 4f + getHeight() / 16f, getHeight() / 2f + getHeight() / 8f + getHeight() / 16f,
                getHeight() / 2f + getHeight() / 8f + getHeight() / 16f, getHeight() / 2f + getHeight() / 16f,
                getHeight() / 4f + getHeight() / 16f
        };
        // Path to make tree
        pathShape.reset();
        pathShape.moveTo(tree2x[0], tree2y[0]);
        for (int i = 1; i < tree2x.length + 1; i++) {
            int pos = i % tree2x.length;
            pathShape.lineTo(tree2x[pos], tree2y[pos]);
        }
        // polygon for tree
        canvas.drawPath(pathShape, paint);

        // Draw tree2 leaves
        paint.reset();
        // leaves size
        leafSize = getWidth() / 30f;
        // leaf position
        for (int i = 0; i < treex[2].length; i++) {
            float x = treex[2][i];
            float y = treey[2][i];
            paint.setColor(treecolors[2][i]);
            canvas.drawCircle(x, y, leafSize, paint);
        }

        // Draw tree3
        paint.reset();
        // Pastel brown
        paint.setColor(Color.rgb(131, 105, 83));
        paint.setStyle(Paint.Style.FILL);
        // Path points
        float[] tree1x = {
                2 * getWidth() / 3f, 5 * getWidth() / 6f,
                getWidth(), getWidth(),
                5 * getWidth() / 6f, 5 * getWidth() / 6f + 20,
                5 * getWidth() / 6f + 20
        };
        float[] tree1y = {
                0, 0,
                getHeight() / 8f, 3 * getHeight() / 4f,
                3 * getHeight() / 4f, getHeight() / 2f + getHeight() / 8f,
                getHeight() / 5f
        };
        // Path to make tree
        pathShape.reset();
        pathShape.moveTo(tree1x[0], tree1y[0]);
        for (int i = 1; i < tree1x.length + 1; i++) {
            int pos = i % tree1x.length;
            pathShape.lineTo(tree1x[pos], tree1y[pos]);
        }
        // polygon for road
        canvas.drawPath(pathShape, paint);

        // Draw tree3 leaves
        paint.reset();
        // leaves size
        leafSize = getWidth() / 20f;
        // leaf position
        for (int i = 0; i < treex[3].length; i++) {
            float x = treex[3][i];
            float y = treey[3][i];
            paint.setColor(treecolors[3][i]);
            canvas.drawCircle(x, y, leafSize, paint);
        }

        // Reset leaf size
        leafSize = getWidth() / 30f;


        paint.reset();
        for (int i = 0; i < leafx.length; i++) {
            paint.setColor(fallingcolors[i]);
            canvas.drawCircle(leafx[i], leafy[i], leafSize, paint);
            leafx[i] += (float)(Math.random()*2-1)/0.5;
            leafx[i] %= getWidth();
            leafy[i] += dy/0.5;
            leafy[i] %= getHeight();
        }
        /*new CountDownTimer(500, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

            }

        }.start();*/
        invalidate();
    }
}
