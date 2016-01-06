package addicto.myleader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardArrayAdapter2 extends ArrayAdapter<Card> {
    private static final String TAG = "CardArrayAdapter2";
    private List<Card> cardList = new ArrayList<Card>();
    private Picasso picasso;
    Context thisx;

    static class CardViewHolder {
        ImageView dp;
        TextView text;
        TextView url;
    }

    public CardArrayAdapter2(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        thisx = context;
    }

    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chat, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.dp = (ImageView) row.findViewById(R.id.card_dp);
            viewHolder.text = (TextView) row.findViewById(R.id.card_text);
            viewHolder.url = (TextView) row.findViewById(R.id.card_imgPost);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        Card card = getItem(position);
        viewHolder.text.setText(card.getLine1());
        viewHolder.url.setText(card.getImage());
        if(card.getLine2()!=null)
            Picasso.with(thisx).load(card.getLine2()).into(viewHolder.dp);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
