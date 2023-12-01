package com.biswanath.promhighschoolhs.ClassFive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.biswanath.promhighschoolhs.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ClassFivesFragment extends Fragment {
    private ViewPagerAdapter adapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_fives, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        setupViewPager(viewPager);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position)-> {tab.setText(adapter.mFragmentTitleList.get(position));
//                tab.setCustomView(R.layout.custom_tab);
                }).attach();

        for (int i = 0; i < tabLayout.getTabCount(); i++){

            TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);

            tabLayout.getTabAt(i).setCustomView(tv);
        }
        return view;
    }

    private void setupViewPager(ViewPager2 viewPager) {

        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity().getLifecycle()     );
        adapter.addFragment(new ClassFiveClassesFragment(), "Classes");
        adapter.addFragment(new ClassFiveMockTestFragment(), "Mock Test");



        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }

    class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String>mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }


}