package view;

import java.util.List;
import java.util.Map;

import loader.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.nju.renrenhardest.R;

public class MyStaggeredAdapter extends SimpleAdapter {
	private ImageLoader mLoader;
	private Context context;
	
	public MyStaggeredAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		mLoader = new ImageLoader(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(context);
			convertView = layoutInflator.inflate(R.layout.friend_list_item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView.findViewById(R.id.ItemImage);
			holder.textView = (TextView) convertView.findViewById(R.id.ItemText);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();
		@SuppressWarnings("unchecked")
		Map<String, Object> item = (Map<String, Object>) getItem(position); 
		String url = (String)item.get("image");
		String name = (String)item.get("name");
		mLoader.DisplayImage(url, holder.imageView);
		holder.textView.setText(name);

		return convertView;
	}

	static class ViewHolder {
		ScaleImageView imageView;
		TextView textView;
	}
}
