package com.example.dhruv.cbrmp3index;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dhruv.cbrmp3index.viewmodel.ItemViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dhruv on 19/3/17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context mContext;
    private FragmentManager fm;
    private ArrayList<ItemViewModel> items = new ArrayList<>(0);

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    public ArrayList<ItemViewModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemViewModel> items) {
        if (items != null) {
            this.items = items;
        }
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.index_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder holder, final int position) {
        final ItemViewModel item = items.get(position);
        final Context context = holder.nameTextView.getContext();

        holder.nameTextView.setText(item.getSongName());
        holder.renamedText.setText(item.getRenamedName());
        if (!item.isDirectory()) {
            holder.enterButton.setVisibility(View.INVISIBLE);
            holder.extraInformationButton.setVisibility(View.VISIBLE);
        } else {
            holder.extraInformationButton.setVisibility(View.INVISIBLE);
            holder.enterButton.setVisibility(View.VISIBLE);
        }
        holder.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getNextScreen() != null) {
                    context.startActivity(item.getNextScreen());
                }
            }
        });

        final String editTextViewString;
        if (item.getRenamedName() == null || item.getRenamedName().equals("") ||
                item.getRenamedName().equals(item.getSongName())) {
            holder.renamedText.setVisibility(View.GONE);
            editTextViewString = item.getSongName();
        } else {
            holder.renamedText.setVisibility(View.VISIBLE);
            editTextViewString = item.getRenamedName();
        }

        holder.renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialogFragment editDialogFragment = new EditDialogFragment();

                Bundle args = new Bundle();
                args.putString(EditDialogFragment.TITLE_KEY, "Rename File");
                args.putString(EditDialogFragment.PREVIOUS_NAME_KEY, editTextViewString);
                editDialogFragment.setArguments(args);

                editDialogFragment.setClickListener(new EditDialogFragment.ClickListener() {
                    @Override
                    public void onClick(String s) {
                        if (!s.equals(editTextViewString)) {
                            item.setRenamedName(s);
                            item.setChanged(item.checkForChange());
                            notifyItemChanged(position);
                        }
                    }
                });
                editDialogFragment.show(fm, "rename window");
            }
        });

        if (item.getExtraInfo() == null || item.getExtraInfo().equals("")) {
            holder.extraInformationText.setVisibility(View.GONE);
        } else {
            holder.extraInformationText.setText(item.getExtraInfo());
            holder.extraInformationText.setVisibility(View.VISIBLE);
        }

        holder.extraInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialogFragment editDialogFragment = new EditDialogFragment();

                Bundle args = new Bundle();
                args.putString(EditDialogFragment.TITLE_KEY, "Extra Information");
                args.putString(EditDialogFragment.PREVIOUS_NAME_KEY, item.getExtraInfo());
                editDialogFragment.setArguments(args);

                editDialogFragment.setClickListener(new EditDialogFragment.ClickListener() {
                    @Override
                    public void onClick(String s) {
                        item.setExtraInfo(s);
                        notifyItemChanged(position);
                    }
                });
                editDialogFragment.show(fm, "edit window");
            }
        });

        holder.relativeLayoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                item.setMarked(!item.isMarked());
                item.setChanged(item.checkForChange());
                notifyItemChanged(position);
                return true;
            }
        });

        if (item.isChanged()) {
            holder.changeNotifier.setVisibility(View.VISIBLE);
        } else {
            holder.changeNotifier.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_text_view)
        TextView nameTextView;
        @BindView(R.id.rename_button)
        Button renameButton;
        @BindView(R.id.renamed_text)
        TextView renamedText;
        @BindView(R.id.enter_button)
        Button enterButton;
        @BindView(R.id.extra_information_button)
        Button extraInformationButton;
        @BindView(R.id.extra_information_text)
        TextView extraInformationText;
        @BindView(R.id.change_notifier)
        ImageView changeNotifier;
        @BindView(R.id.relative_layout_item)
        RelativeLayout relativeLayoutItem;

        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }
}
