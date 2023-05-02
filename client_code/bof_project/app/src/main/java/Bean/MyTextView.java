package Bean;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写这个方法，强制返回true
    @Override
    public boolean isFocused() {
        return true;
    }
}