package Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.prm392_team333_courtbooking.fragements.player.player_search.BookingFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class CalendarPagerAdapter extends FragmentStateAdapter {

    private CalendarDay selectedDate;

    public CalendarPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setSelectedDate(CalendarDay date) {
        this.selectedDate = date;
    }

    @Override
    public int getItemCount() {
        return 100; // You can set this to a large number for many dates
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create a new instance of your fragment and pass the selected date as an argument
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putLong("selectedDate", selectedDate.getDate().getTime()); // Pass date as long
        fragment.setArguments(args);
        return fragment;
    }
}
