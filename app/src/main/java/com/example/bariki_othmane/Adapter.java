package com.example.bariki_othmane;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bariki_othmane.R;


public class Adapter extends PagerAdapter {

    List<Model> models;
    LayoutInflater inflater;
    Context context;


    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;

    }


    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item,container,false);

        ImageView imageView;
        TextView title,desc;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        imageView.setImageResource(models.get(position).getImage());

        if(position==0) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(view.getContext(),activity2.class);
                    intent.putExtra("caller","Main");
                    view.getContext().startActivity(intent);
                }
            });
        }

        else {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(view.getContext(),CapitainLogin.class);
                    intent.putExtra("caller","Main");
                    view.getContext().startActivity(intent);
                }
            });
        }
        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());

        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
