package goodtime.com.goodtime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.Date;

public class CustomClock extends View {
    private final float x;
    private final float y;
    private final int r=45;
    private final Date date;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomClock(Context context, float x, float y, Date date) {
        super(context);
        this.x = x;
        this.y = y;
        this.date=date;        
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, r, mPaint);
//        float sec=(float)date.getSeconds();
//        float min=(float)date.getMinutes();
//        float hour=(float)date.getHours()+min/60.0f;
//        mPaint.setColor(0xFFFF0000);
//        canvas.drawLine(x, y, (float)(x+(r-15)*Math.cos(Math.toRadians((hour / 12.0f * 360.0f)-90f))), (float)(y+(r-10)*Math.sin(Math.toRadians((hour / 12.0f * 360.0f)-90f))), mPaint);
//        canvas.save();
//        mPaint.setColor(0xFF0000FF);
//        canvas.drawLine(x, y, (float)(x+r*Math.cos(Math.toRadians((min / 60.0f * 360.0f)-90f))), (float)(y+r*Math.sin(Math.toRadians((min / 60.0f * 360.0f)-90f))), mPaint);
//        canvas.save();
//        mPaint.setColor(0xFFA2BC13);
//        canvas.drawLine(x, y, (float)(x+(r+10)*Math.cos(Math.toRadians((sec / 60.0f * 360.0f)-90f))), (float)(y+(r+15)*Math.sin(Math.toRadians((sec / 60.0f * 360.0f)-90f))), mPaint);
    }
}