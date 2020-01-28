package com.ysmstudio.doittomorrow;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ysmstudio.doittomorrow.databinding.RecyclerTodoItemBinding;

import java.util.ArrayList;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TodoData> list;

    private View emptyView;
    private RecyclerView recyclerView;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = View.inflate(parent.getContext(), R.layout.recycler_todo_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolderNormal(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderNormal) holder).binding.setTodo(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (emptyView != null)
            if (list.size() <= 0) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            } else {
                emptyView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        return list.size();
    }

    public static class ViewHolderNormal extends RecyclerView.ViewHolder {
        RecyclerTodoItemBinding binding;

        public ViewHolderNormal(@NonNull View itemView) {
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

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
