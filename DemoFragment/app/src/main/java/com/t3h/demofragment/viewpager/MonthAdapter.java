package com.t3h.demofragment.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MonthAdapter extends FragmentPagerAdapter {

    public MonthAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        PareHtmlFragment fr = new PareHtmlFragment();
        Bundle b = new Bundle();
        switch (position) {
            case 0:
                b.putString("PATH",
                        "https://www.mevabe.vn/the-gioi-cua-be/be-khoe--dep/page-");
                b.putString("KEY",
                        "over-box");
                break;
            case 1:
                b.putString("PATH",
                        "https://www.mevabe.vn/the-gioi-cua-me/kinh-nghiem-hay/page-");
                b.putString("KEY",
                        "content-box");
                break;
            default:
                b.putString("PATH",
                        "https://www.mevabe.vn/the-gioi-cua-be/be-chao-doi/page-");
                b.putString("KEY",
                        "over-box");
                break;
        }
        fr.setArguments(b);
//        fr.setPath()
        return fr;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Bé Khỏe";
            case 1:
                return "Kinh Nghiệm";
            default:
                return "Chào đời";
        }
    }
}
