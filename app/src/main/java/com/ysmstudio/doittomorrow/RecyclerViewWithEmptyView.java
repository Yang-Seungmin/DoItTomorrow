package com.ysmstudio.doittomorrow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewWithEmptyView extends RecyclerView {

    private View emptyView;

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Adapter<?> adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() > 0) {
                    RecyclerViewWithEmptyView.this.setVisibility(VISIBLE);
                    emptyView.setVisibility(GONE);
                } else {
                    RecyclerViewWithEmptyView.this.setVisibility(GONE);
                    emptyView.setVisibility(VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            onChanged();
        }
    };

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) adapter.registerAdapterDataObserver(observer);
        observer.onChanged();
    }

    public RecyclerViewWithEmptyView(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewWithEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWithEmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
