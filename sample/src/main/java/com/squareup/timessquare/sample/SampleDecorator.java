package com.squareup.timessquare.sample;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import java.util.Calendar;
import java.util.Date;

public class SampleDecorator implements CalendarCellDecorator {
  private Context context;

  public SampleDecorator(Context context) {
    this.context = context;
  }

  @Override
  public void decorate(CalendarCellView cellView, Date date) {
    String dateString = Integer.toString(date.getDate());
//    SpannableString string = new SpannableString(dateString + "\ntitle");
//    string.setSpan(new RelativeSizeSpan(0.5f), 0, dateString.length(),
//        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//    cellView.getDayOfMonthTextView().setText(string);
    cellView.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_bg_selector));
    cellView.getDayOfMonthTextView().setTextColor(ContextCompat.getColorStateList(context, R.color.calendar_text_selector));
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    Date today = new Date(calendar.getTimeInMillis());
    if(today.getTime() == date.getTime()){
      cellView.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_bg_selector_holiday));
      cellView.getDayOfMonthTextView().setTextColor(ContextCompat.getColorStateList(context, R.color.calendar_text_selector_white));
    }
  }
}
