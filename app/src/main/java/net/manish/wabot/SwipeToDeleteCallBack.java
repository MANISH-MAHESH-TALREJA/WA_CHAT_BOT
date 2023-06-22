package net.manish.wabot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class SwipeToDeleteCallBack extends ItemTouchHelper.Callback
{
    private final int backgroundColor = Color.parseColor("#b80f0a");
    private final Drawable deleteDrawable;
    private final int intrinsicHeight;
    private final int intrinsicWidth;
    private final ColorDrawable mBackground = new ColorDrawable();
    private final Paint mClearPaint;

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder)
    {
        return 0.7f;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder2)
    {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
    {
    }

    public SwipeToDeleteCallBack(Context context)
    {
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        intrinsicWidth = Objects.requireNonNull(deleteDrawable).getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder)
    {
        return makeMovementFlags(0, 4);
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z)
    {
        super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        View view = viewHolder.itemView;
        int height = view.getHeight();
        if (f == 0.0f && !z)
        {
            clearCanvas(canvas, ((float) view.getRight()) + f, (float) view.getTop(), (float) view.getRight(), (float) view.getBottom());
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, false);
            return;
        }
        mBackground.setColor(backgroundColor);
        mBackground.setBounds(view.getRight() + ((int) f), view.getTop(), view.getRight(), view.getBottom());
        mBackground.draw(canvas);
        int top = view.getTop();
        int i3 = top + ((height - intrinsicHeight) / 2);
        int i4 = (height - intrinsicHeight) / 2;
        deleteDrawable.setBounds((view.getRight() - i4) - intrinsicWidth, i3, view.getRight() - i4, intrinsicHeight + i3);
        deleteDrawable.draw(canvas);
    }


    private void clearCanvas(Canvas canvas, Float f, Float f2, Float f3, Float f4)
    {
        canvas.drawRect(f, f2, f3, f4, mClearPaint);
    }
}
