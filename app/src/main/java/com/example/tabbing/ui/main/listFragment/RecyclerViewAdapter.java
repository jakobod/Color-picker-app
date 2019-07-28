package com.example.tabbing.ui.main.listFragment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tabbing.R;
import com.example.tabbing.ui.main.listFragment.ListFragment.OnListFragmentInteractionListener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecyclerViewAdapter.RecyclerViewItem} and
 * makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private static final List<RecyclerViewItem> mValues = new ArrayList<>();
  private final OnListFragmentInteractionListener mListener;

  public RecyclerViewAdapter(Set<InetAddress> items, OnListFragmentInteractionListener listener) {
    addItems(items);
    mListener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.mItem = mValues.get(position);
    holder.mIdView.setText(mValues.get(position).id);
    holder.mContentView.setText(mValues.get(position).content);

    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != mListener) {
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          mListener.onListFragmentInteraction(holder.mItem);
        }
      }
    });
  }

  public static void addItems(Set<InetAddress> items) {
    mValues.clear();
    for (InetAddress i : items) {
      RecyclerViewItem rvi = new RecyclerViewItem(i);
      mValues.add(rvi);
      System.out.println("new RVI: " + rvi);
    }
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;
    public RecyclerViewItem mItem;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      mIdView = view.findViewById(R.id.item_number);
      mContentView = view.findViewById(R.id.content);
    }

    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }

  public static class RecyclerViewItem {
    public final String id;
    public final String content;
    public final String details;
    public final InetAddress inetAddress;
    private static int id_counter = 0;

    public RecyclerViewItem(InetAddress givenInetAddress) {
      this.id = String.format(Locale.GERMAN,"%d", id_counter++);
      this.content = givenInetAddress.getHostAddress();;
      this.details = "";
      this.inetAddress = givenInetAddress;
    }

    @Override
    public String toString() {
      return content;
    }
  }
}
