package com.example.tabbing.ui.main.listFragment;

import androidx.annotation.NonNull;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private static final List<RecyclerViewItem> mValues = new ArrayList<>();
  private final OnListFragmentInteractionListener mListener;

  RecyclerViewAdapter(Set<InetAddress> items, OnListFragmentInteractionListener listener) {
    addItems(items);
    mListener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
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
    final View mView;
    final TextView mIdView;
    final TextView mContentView;
    RecyclerViewItem mItem;

    ViewHolder(View view) {
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
    private final String id;
    private final String content;
    private final String details;
    private final InetAddress inetAddress;
    private static int id_counter = 0;

    RecyclerViewItem(InetAddress givenInetAddress) {
      this.id = String.format(Locale.GERMAN,"%d", id_counter++);
      this.content = givenInetAddress.getHostName();
      this.details = "";
      this.inetAddress = givenInetAddress;
    }

    @Override
    public String toString() {
      return content;
    }

    public String getId() {
      return id;
    }

    public String getContent() {
      return content;
    }

    public String getDetails() {
      return details;
    }

    public InetAddress getInetAddress() {
      return inetAddress;
    }


  }
}
