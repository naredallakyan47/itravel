package com.example.itravel;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;


public class ImageUtils {

    // Метод для обрезки изображения в форме круга
    public static Bitmap getCircularBitmap(Bitmap srcBitmap) {
        // Получаем минимальный размер изображения (высоту или ширину) для обрезки в круг
        int size = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());

        // Создаем квадратное изображение с минимальным размером
        Bitmap squareBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        // Создаем холст для рисования
        Canvas canvas = new Canvas(squareBitmap);

        // Создаем кисть для рисования
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Создаем прямоугольник, описывающий область квадрата
        Rect rect = new Rect(0, 0, size, size);

        // Создаем объект Shader с использованием рисунка квадрата
        BitmapShader shader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // Устанавливаем Shader для кисти
        paint.setShader(shader);

        // Создаем объект RectF, описывающий границы круга
        RectF rectF = new RectF(rect);

        // Рисуем круг на холсте
        canvas.drawRoundRect(rectF, size / 2, size / 2, paint);

        return squareBitmap;
    }
}

