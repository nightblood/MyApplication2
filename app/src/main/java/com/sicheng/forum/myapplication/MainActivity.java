package com.sicheng.forum.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private View mLlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLlView = findViewById(R.id.ll_view);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLlView.isShown()) {
                    mLlView.setVisibility(View.GONE);
                } else {
                    mLlView.setVisibility(View.VISIBLE);
                }
            }
        });

        Bitmap bitmap = addLogo(BitmapFactory.decodeResource(getResources(), R.drawable.test), "四城ID");
        saveBitmap(bitmap);
    }


    private Bitmap addLogo(Bitmap src, String id) {
        int qrSize = dip2px(150); // 二维码大小
        int width = src.getWidth();
        int height = src.getHeight();
        int padding = dip2px(8); // 左右边距

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(sp2px(30));
        textPaint.setColor(Color.GRAY);
        float textLength = getFontlength(textPaint, id);
        float fontHeight = getFontHeight(textPaint);

        int viewHeight = (int)fontHeight + qrSize; //底部的高度

        Bitmap bitmap = Bitmap.createBitmap(width, height + viewHeight , Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(src, 0, 0, null);

        Bitmap  logo = BitmapFactory.decodeResource(getResources(), R.drawable.theme_title_logo);
        int logoHeight = logo.getHeight();
        canvas.drawBitmap(logo, padding, height + (viewHeight - logoHeight) / 2, null);


        Bitmap qr = QRCodeUtil.createQRBitmap("http://tech.e0575.com/attachment/editor/image/20170809/1502247135353605.png", qrSize, qrSize);
        canvas.drawBitmap(qr, width - qrSize, height + fontHeight , null);

        canvas.drawText(id, width - textLength - padding, height + fontHeight, textPaint);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }
    /**
     * @return 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }
    public int dip2px(float dipValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public void saveBitmap(Bitmap bitmap) {
        File f = new File("/sdcard/", "test.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
