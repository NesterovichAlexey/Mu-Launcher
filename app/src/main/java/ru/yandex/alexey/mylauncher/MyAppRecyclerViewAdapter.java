package ru.yandex.alexey.mylauncher;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MyAppRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public final static int APP = 1;
    public final static int HEADER = 2;
    private final String nameHeaders[];
    private final List<Bitmap> icons;
    private final List<App> appList;
    private Toast activToast;

    public MyAppRecyclerViewAdapter(String nameHeaders[], List<Bitmap> icons, List<App> appList) {
        this.nameHeaders = nameHeaders;
        this.icons = icons;
        this.appList = appList;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return appList.get(position).hashCode();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case APP:
                return new ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_app, parent, false));
            case HEADER:
                return new HeaderHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_header, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_app, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case APP:
                ((ViewHolder)holder).iconView.setImageBitmap(icons.get(appList.get(position).iconNumber));
                ((ViewHolder)holder).nameView.setText(Integer.toHexString(appList.get(position).name));
                break;
            case HEADER:
                ((HeaderHolder)holder).nameView.setText(nameHeaders[appList.get(position).name]);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return appList.get(position).iconNumber == -1 ? HEADER : APP;
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView nameView;

        public HeaderHolder(View view) {
            super(view);
            this.view = view;
            nameView = (TextView) view.findViewById(R.id.header);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener{
        public final View view;
        public final ImageView iconView;
        public final TextView nameView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            iconView = (ImageView) view.findViewById(R.id.icon);
            nameView = (TextView) view.findViewById(R.id.name);
            view.setOnCreateContextMenuListener(this);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (activToast != null)
                activToast.cancel();
            activToast = Toast.makeText(v.getContext(), Integer.toHexString(appList.get(position).name), Toast.LENGTH_SHORT);
            activToast.show();
            ++appList.get(getAdapterPosition()).clicks;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int position = getAdapterPosition();
            menu.add("Info").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (activToast != null)
                        activToast.cancel();
                    activToast = Toast.makeText(v.getContext(), Integer.toHexString(appList.get(position).name), Toast.LENGTH_SHORT);
                    activToast.show();
                    return true;
                }
            });
            menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MyAppRecyclerViewAdapter.this.notifyItemRemoved(position);
                    appList.remove(position);
                    return true;
                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            v.showContextMenu();
            return true;
        }

        @Override
        public String toString() {
            return (String) nameView.getText();
        }
    }
}
