package adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bof_project.R;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends BaseAdapter {
    private List<String> list=new ArrayList<>();
    private LayoutInflater inflater;//布局装载器对象

    public GameAdapter(Context context, List<String> list)
    {
        super();
        this.list=list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_game_index,null);
        //item_game_index中的控件
        TextView gameName=view.findViewById(R.id.game_name);
        gameName.setText(list.get(position));
        return view;
    }
}