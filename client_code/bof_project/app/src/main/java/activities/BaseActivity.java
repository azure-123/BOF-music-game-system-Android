package activities;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;


/**
 * Created by hanks on 2017/3/14.
 */

public class BaseActivity extends AppCompatActivity {
    String[] sentences = {"BOF manager",
            "Login",""};
    int index;

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof HTextView) {
                if (index + 1 >= sentences.length) {
                    index = 0;
                }
                ((HTextView) v).animateText(sentences[index++]);
            }
        }
    }

//    class SimpleAnimationListener implements AnimationListener {
//
//        private Context context;
//
//        public SimpleAnimationListener(Context context) {
//            this.context = context;
//        }
//        @Override
//        public void onAnimationEnd(HTextView hTextView) {
//            Toast.makeText(context, "Animation finished", Toast.LENGTH_SHORT).show();
//        }
//    }

}
