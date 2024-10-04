package Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prm392_team333_courtbooking.R;

import java.util.List;

import Models.Court;

public class CourtAdapter extends BaseAdapter
{
    private final Context context;

    private final List<Court> courts;

    private final int idLayout;

    public CourtAdapter(Context context, List<Court> courts, int idLayout) {
        this.context = context;
        this.courts = courts;
        this.idLayout = idLayout;
    }


    @Override
    public int getCount() {
        return courts.size();
    }

    @Override
    public Object getItem(int i) {
        return courts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return courts.get(i).getCourtId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        }

        ImageView ivImage = convertView.findViewById(R.id.court_image);
        TextView tvCourtName = convertView.findViewById(R.id.court_title);
        TextView tvDescription = convertView.findViewById(R.id.court_description);
        TextView tvStatus = convertView.findViewById(R.id.tv_status);

        Court court = (Court) getItem(position);

        final RelativeLayout relativeLayout =(RelativeLayout) convertView.findViewById(R.id.relativeLayout);

        tvCourtName.setText(court.getCourtName());

        String a = court.getOpenTime() + " - " + court.getClosedTime();
        tvDescription.setText(a);
        tvStatus.setText(court.getStatus());

        byte[] courtImage =court.getImage();
        if(courtImage != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(courtImage, 0, courtImage.length);
            ivImage.setImageBitmap(bitmap);
        }else {

            ivImage.setImageResource(R.drawable.old_trafford);
        }

        return convertView;
    }
}
