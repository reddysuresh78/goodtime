package goodtime.com.goodtime;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import core.HoraRange;
import core.Utils;

public class HoraAdapter extends ArrayAdapter<HoraRange> {
  private final Context context;
  private final List<HoraRange> values;
  private String[] starsArray;
  private String[] planetsArray;


  public HoraAdapter(Context context, List<HoraRange> values) {
    super(context, -1, values);
    this.context = context;
    this.values =  values ;

    starsArray = context.getResources().getStringArray(R.array.stars_array);
    planetsArray = context.getResources().getStringArray(R.array.planets_array);

  }

  public void setValues(List<HoraRange> newValues) {
    values.clear();
    values.addAll( newValues );
    this.notifyDataSetChanged();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.list_item_hora, parent, false);
    TextView planetTextView = (TextView) rowView.findViewById(R.id.list_item_hora_planet_textview);
    TextView timeTextView = (TextView) rowView.findViewById(R.id.list_item_hora_startTime_textview);

    planetTextView.setText(planetsArray[  values.get(position).getPlanet()]);
    timeTextView.setText(values.get(position).getDisplayString());

    if(Utils.isGoodPlanet(values.get(position).getPlanet()) ){
        planetTextView.setTextColor(ContextCompat.getColor(context, R.color.wallet_holo_blue_light));
    }else {
        planetTextView.setTextColor(Color.RED);

    }
//    timeTextView.setText(values.get(position).getStartTime().getTime().toString());


//    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
//    textView.setText(values[position]);
    // change the icon for Windows and iPhone
//    String s = values[position];
//    if (s.startsWith("iPhone")) {
//      imageView.setImageResource(R.drawable.no);
//    } else {
//      imageView.setImageResource(R.drawable.ok);
//    }

    return rowView;
  }
} 
