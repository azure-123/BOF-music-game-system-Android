package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.bof_project.R;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;

import java.util.Arrays;

import activities.BofInfoActivity;
import activities.LoginActivity;
import adapter.TestStackAdapter;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContestFragment extends Fragment implements CardStackView.ItemExpendListener{

    String TAG = ContestFragment.class.getCanonicalName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FancyButton buttonPre;
    private FancyButton buttonNext;
    private FancyButton buttonDown;

    public static Integer[] TEST_DATAS = new Integer[]{
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10
    };
    private CardStackView mStackView;
    private LinearLayout mActionButtonContainer;
    private TestStackAdapter mTestStackAdapter;


    public ContestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CentestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContestFragment newInstance(String param1, String param2) {
        ContestFragment fragment = new ContestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        buttonPre=view.findViewById(R.id.btn_pre);
        buttonNext=view.findViewById(R.id.btn_next);
        buttonDown=view.findViewById(R.id.btn_down);
        mStackView = (CardStackView) view.findViewById(R.id.stackview_main);
        mActionButtonContainer = (LinearLayout) view.findViewById(R.id.button_container);
        mStackView.setItemExpendListener(this);
        mTestStackAdapter = new TestStackAdapter(getContext());
        mStackView.setAdapter(mTestStackAdapter);
        buttonDown.setOnClickListener(v->mStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(mStackView)));
        buttonPre.setOnClickListener(v -> mStackView.pre());
        buttonNext.setOnClickListener(v -> mStackView.next());

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mTestStackAdapter.updateData(Arrays.asList(TEST_DATAS));
                    }
                }
                , 200
        );

//        int [] imageButtonIdArray={R.id.btn_bof_2021,R.id.btn_bof_2020,R.id.btn_bof_2019,
//                R.id.btn_bof_2018,R.id.btn_bof_2017,R.id.btn_bof_2016,R.id.btn_bof_2015,
//                R.id.btn_bof_2014,R.id.btn_bof_2013,R.id.btn_bof_2012};
//        String [] imageButtonNameArray={
//                "BOFXVII","BOFXVI","BOFXV","G2R2018","BOFU2017","BOFU2016","BOFU2015",
//                "G2R2014","BOF2013","BOF2012"};
//        for(int i=0;i<imageButtonIdArray.length;i++) {
//            ImageButton ButtonBof2021 = getView().findViewById(imageButtonIdArray[i]);
//            int finalI = i;
//            ButtonBof2021.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //ButtonBof2021.getResources().getString();
//                    Intent intent = new Intent(getActivity(), BofInfoActivity.class);
//                    intent.putExtra("bof_name",imageButtonNameArray[finalI]);
//                    Log.e(TAG, "onClick: "+imageButtonNameArray[finalI]);
//                    startActivity(intent);
//                }
//            });
//        }
    }
    @Override
    public void onItemExpend(boolean expend) {
        mActionButtonContainer.setVisibility(expend ? View.VISIBLE : View.GONE);
    }
}