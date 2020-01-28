package com.ysmstudio.doittomorrow;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ysmstudio.doittomorrow.databinding.ItemAppInfoBinding;
import com.ysmstudio.doittomorrow.databinding.RecyclerTodoItemBinding;

import java.util.ArrayList;

public class AppInfoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<AppInfoData> list = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = View.inflate(parent.getContext(), R.layout.item_app_info, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolderNormal(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AppInfoData appInfoData = list.get(position);
        ((ViewHolderNormal) holder).binding.textViewTitle.setText(appInfoData.getTitle());
        ((ViewHolderNormal) holder).binding.textViewContent.setText(appInfoData.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderNormal extends RecyclerView.ViewHolder {
        ItemAppInfoBinding binding;

        public ViewHolderNormal(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }

    public ArrayList<AppInfoData> getList() {
        return list;
    }

    public void setList(ArrayList<AppInfoData> list) {
        this.list = list;
    }
}
