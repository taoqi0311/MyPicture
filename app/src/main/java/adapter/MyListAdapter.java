package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.synway.mypicture.MyBean;
import com.synway.mypicture.R;

import java.util.ArrayList;


public class MyListAdapter extends BaseAdapter {
    private ArrayList<MyBean> list;
    private LayoutInflater inflater;
    private Context context;

    public MyListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setList(ArrayList<MyBean> list) {
        if (list != null&&list.size()>0) {
            this.list.clear();
            this.list .addAll(list);
        }else {
            this.list.clear();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size() == 0 ? 0 : list.size();
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
        MyHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.picker_item_directory, parent, false);
            holder = new MyHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }

        holder.bindData(list.get(position));
        return convertView;
    }

    private class MyHolder {
        private TextView textView;
        private ImageView imageView;

        private MyHolder(View rootview) {
            textView = (TextView) rootview.findViewById(R.id.file_text);
            imageView = (ImageView) rootview.findViewById(R.id.item_file_icon);
        }

        private void bindData(MyBean s) {
            textView.setText(s.name);
            if (s.type == 0) {
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_folder));
            } else {
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_unknown));
            }
        }
    }
}
