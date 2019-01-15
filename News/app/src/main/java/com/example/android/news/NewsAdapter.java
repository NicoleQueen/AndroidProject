package com.example.android.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News>{

    NewsAdapter(Context context, List<News> news){
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        TextView informationView =  listItemView.findViewById(R.id.information);
        informationView.setText(currentNews.getInformation());

        TextView dateView =  listItemView.findViewById(R.id.date);
        dateView.setText(currentNews.getDate());

        TextView authorView =  listItemView.findViewById(R.id.author);
        authorView.setText(currentNews.getAuthor());

        return listItemView;
    }
}
