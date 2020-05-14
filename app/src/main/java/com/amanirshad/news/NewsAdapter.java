package com.amanirshad.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> events){
        super(context,0,events);
    }

    public static class ViewHolder{
        TextView tvTitle;
        TextView tvCategory;
        TextView tvAuthor;
        TextView tvDate;

        ViewHolder(View v){
            tvTitle = v.findViewById(R.id.template_title_textview);
            tvCategory = v.findViewById(R.id.template_category_textview);
            tvAuthor = v.findViewById(R.id.template_author_textview);
            tvDate = v.findViewById(R.id.template_date_textview);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        News news = getItem(position);
        if(news!=null){
            viewHolder.tvTitle.setText(news.getTitle());
            viewHolder.tvCategory.setText(news.getCategory());
            viewHolder.tvAuthor.setText(news.getAuthor());
            viewHolder.tvDate.setText(news.getDate());
        }

        return convertView;
    }
}