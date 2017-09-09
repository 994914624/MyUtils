package com.example.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AudienL(591928179@qq.com) on 2016/4/22
 */
public abstract class MySuperAdapter<DATA, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected Context context;
    protected List<DATA> list = new ArrayList<>();
    protected int layoutResID;

    public MySuperAdapter(Context context, int layoutResID) {

        this.context = context;
        this.layoutResID=layoutResID;
    }

    /**
     * 设置数据源,把原来的替换掉
     */
    public void setListToReplace(List<DATA> list) {
        if (list != null) {
            this.list = list;
            notifyItemRangeChanged(0,this.list.size());
        }
    }


    /**
     * 添加一个数据集，在开头追加
     */
    public void addListToFirst(List<DATA> list) {
        if (list != null) {
            list.addAll(this.list);
            this.list = list;
            notifyItemRangeChanged(0,this.list.size());
        }
    }
    /**
     * 添加一个数据集，在末尾追加
     */
    public void addListToLast(List<DATA> list) {
        if (list != null) {
            this.list.addAll(list);
            notifyItemRangeChanged(this.list.size(),list.size());
        }
    }

    /**
     * 添加一条数据
     */
    public void addItem(DATA item) {
        if (item != null) {
            this.list.add(item);
        }
    }

    /**
     * 获取一条数据
     */
    public DATA getItem(int position) {
        return list.get(position);
    }

    /**
     * 清空数据
     */
    public void clear() {
        list.clear();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutResID, parent, false);
        return (VH) new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, holder.getAdapterPosition());
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(v, holder.getAdapterPosition());
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClick(v, holder.getAdapterPosition());
                }
                return true;
            }
        });
        onBindDatas(holder, list.get(position), position);
    }


    /**
     * @param item 为当前 item 对应的数据
     */
    public abstract void onBindDatas(VH holder, DATA item, int position);


    /**
     * 会先调用此方法，再调用 OnItemClickListener 中的 onItemClick。
     * 会先调用此方法，再调用 setOnItemLongClickListener 中的 onItemLongClick。
     */

    protected void onItemClick(View view, int position) {
    }
    protected void onItemLongClick(View view, int position) {
    }

    /**
     *
     * 监听
     *
     * */

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    /**
     *
     * ViewHolder
     *
     * */

    public  class MyViewHolder extends RecyclerView.ViewHolder
    {
        //用这个数组来保存view
        private SparseArray<View> arrViews;

        private View view=null;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            arrViews=new SparseArray<>();


        }

        //提供一个暴露View的方法
        public View getView(int id)
        {
            return findView(id);
        }
        //通过id来实例化view
        private <T extends View>T findView(int id)
        {

            if(view==null)
            {
                view=itemView.findViewById(id);
                //实例化之后放入数组中,用于减少findViewById次数
                arrViews.put(id,view);
            }else {
                view = arrViews.get(id);
            }
            return (T) view;
        }

        //提得到的控件的方法
        public TextView getTextView(int id)
        {
            return findView(id);
        }
        public ImageView getImageView(int id)
        {
            return findView(id);
        }
        public Button getButton(int id)
        {
            return findView(id);
        }


    }


}
