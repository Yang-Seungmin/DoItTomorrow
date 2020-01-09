package com.ysmstudio.doittomorrow;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ysmstudio.doittomorrow.databinding.RecyclerTodoItemAddBinding;
import com.ysmstudio.doittomorrow.databinding.RecyclerTodoItemBinding;

import java.util.ArrayList;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean addItemVisibility = false;
    private ArrayList<TodoData> list;
    private Editable addItemText;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 1:
                view = View.inflate(parent.getContext(), R.layout.recycler_todo_item_add, null);
                view.setLayoutParams( new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolderAdding(view);

            default:
                view = View.inflate(parent.getContext(), R.layout.recycler_todo_item, null);
                view.setLayoutParams( new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ViewHolderNormal(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderNormal) {
            if (position < list.size())
            ((ViewHolderNormal) holder).binding.setTodo(list.get(position));
        } else if (holder instanceof ViewHolderAdding) {
            addItemText = ((ViewHolderAdding) holder).binding.editTextName.getText();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return addItemVisibility && (position == list.size()) ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return list.size() + (addItemVisibility ? 1 : 0);
    }


    public static class ViewHolderNormal extends RecyclerView.ViewHolder {
        RecyclerTodoItemBinding binding;

        public ViewHolderNormal(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }

    public static class ViewHolderAdding extends RecyclerView.ViewHolder {
        RecyclerTodoItemAddBinding binding;

        public ViewHolderAdding(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }

    public ArrayList<TodoData> getList() {
        return list;
    }

    public void setList(ArrayList<TodoData> list) {
        this.list = list;
    }

    public boolean isAddItemVisibility() {
        return addItemVisibility;
    }

    public void setAddItemVisibility(boolean addItemVisibility) {
        this.addItemVisibility = addItemVisibility;
        if (addItemVisibility) notifyItemInserted(list.size());
        else notifyDataSetChanged();
    }

    public Editable getAddItemText() {
        return addItemText;
    }
}
