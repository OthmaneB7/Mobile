package com.example.bariki_othmane;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bariki_othmane.R;

public class SliderAdapterCapitain extends PagerAdapter {


        Context context;
        LayoutInflater layoutInflater;

        public SliderAdapterCapitain(Context context) {
            this.context = context;
        }
        //1
        int champs1 [] =  {
                R.string.nom,
                R.string.email,
                R.string.CIN
        };
        //2
        int champs2 [] = {
                R.string.prenom,
                R.string.username,
                R.string.blaka
        }; //3
        int champs3[] = {
                R.string.date,R.string.mdp,R.string.tonnage
        };

        //4
        int champs5 [] = {
                R.string.tel,
                R.string.mdp2,
                R.string.CIN
        };
        //
        int champs4 [] = {
                R.string.slogan1,
                R.string.slogan2,
                R.string.slogan3,
                R.string.CIN
        };

        @Override
        public int getCount() {
            return champs1.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (ConstraintLayout)object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.slides_layout,container,false);

            //hooks
            TextView champ1 = view.findViewById(R.id.champ1);
            TextView champ2 = view.findViewById(R.id.champ2);
            TextView champ3 = view.findViewById(R.id.champ3);
            TextView champ4 = view.findViewById(R.id.champ4);
            TextView champ5 = view.findViewById(R.id.champ5);

            //edit the hooks
            champ1.setHint(champs1[position]);
            champ2.setHint(champs2[position]);
            champ3.setHint(champs3[position]);
            champ4.setText(champs4[position]);
            if(position != 2) {
                champ5.setHint(champs5[position]);
            }
            else{
                champ5.setVisibility(View.INVISIBLE);
            }
            //
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container,int position,@NonNull Object object){
            container.removeView((ConstraintLayout)object);
        }



}
