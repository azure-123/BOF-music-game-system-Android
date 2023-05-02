package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bof_project.R;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import Bean.MusicItem;
import activities.PlayBofActivity;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MusicListAdapter extends ArrayAdapter<MusicItem> {

    private final HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private Context context;
    public MusicListAdapter(Context context, List<MusicItem> objects) {
        super(context, 0, objects);
        this.context=context;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        MusicItem item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell_music, parent, false);
            // binding view parts to view holder
            viewHolder.titlePosition=cell.findViewById(R.id.title_pos);
            viewHolder.titleMusicName=cell.findViewById(R.id.title_music_name);
            viewHolder.contentPosition=cell.findViewById(R.id.content_pos);
            viewHolder.contentMusicName=cell.findViewById(R.id.content_music_name);
            viewHolder.buttonBof=cell.findViewById(R.id.btn_bga);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.titlePosition.setText(position+1+"");
        viewHolder.titleMusicName.setText(item.getMusicName());
        viewHolder.contentPosition.setText(position+1+"");
        viewHolder.contentMusicName.setText(item.getMusicName());
        viewHolder.buttonBof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PlayBofActivity.class);
                intent.putExtra("bofName",item.getBofName());
                intent.putExtra("bofPosition",position);
                context.startActivity(intent);
            }
        });

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }


    // View lookup cache
    private static class ViewHolder {
        TextView titlePosition;
        TextView titleMusicName;
        TextView contentPosition;
        TextView contentMusicName;
        FancyButton buttonMusic;
        FancyButton buttonBof;
    }
}
