package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bof_project.R;

import java.util.ArrayList;
import java.util.List;

import Bean.MusicItem;

public class SearchAdapter extends BaseAdapter {

    private List<MusicItem> list=new ArrayList<>();
    private LayoutInflater inflater;

    public SearchAdapter(Context context,List<MusicItem> list)
    {
        super();
         this.list=list;
         inflater=LayoutInflater.from(context);
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
        View view=inflater.inflate(R.layout.item_search,null);
        //控件
        TextView musicName=view.findViewById(R.id.music_name);
        TextView musicCreator=view.findViewById(R.id.music_creator);
        musicName.setText(list.get(position).getMusicName());
        musicCreator.setText(list.get(position).getMusicCreator());

        return view;
    }
}