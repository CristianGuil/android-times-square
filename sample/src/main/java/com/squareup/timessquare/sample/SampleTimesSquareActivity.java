package com.squareup.timessquare.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import com.squareup.timessquare.CalendarRowView;
import com.squareup.timessquare.DefaultDayViewAdapter;
import com.squareup.timessquare.MonthCellDescriptor;
import com.squareup.timessquare.MonthView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static android.widget.Toast.LENGTH_SHORT;
import static com.squareup.timessquare.CalendarPickerView.cells;
import static com.squareup.timessquare.CalendarPickerView.selectedCals;
import static com.squareup.timessquare.CalendarPickerView.selectedCells;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class SampleTimesSquareActivity extends Activity implements MonthView.Listener{
  private static final String TAG = "SampleTimesSquareActivi";
  private CalendarPickerView calendar;
  private AlertDialog theDialog;
  private CalendarPickerView dialogView;
  private final Set<Button> modeButtons = new LinkedHashSet<Button>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sample_calendar_picker);

    final Calendar nextYear = Calendar.getInstance();
    nextYear.add(Calendar.YEAR, 1);

    final Calendar lastYear = Calendar.getInstance();
    lastYear.add(Calendar.YEAR, -1);

    calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
    calendar.init(lastYear.getTime(), nextYear.getTime(), this) //
        .inMode(SelectionMode.SINGLE) //
        .withSelectedDate(new Date());

    initButtonListeners(nextYear, lastYear);
  }

  private void initButtonListeners(final Calendar nextYear, final Calendar lastYear) {
    final Button single = (Button) findViewById(R.id.button_single);
    final Button multi = (Button) findViewById(R.id.button_multi);
    final Button range = (Button) findViewById(R.id.button_range);
    final Button displayOnly = (Button) findViewById(R.id.button_display_only);
    final Button dialog = (Button) findViewById(R.id.button_dialog);
    final Button customized = (Button) findViewById(R.id.button_customized);
    final Button decorator = (Button) findViewById(R.id.button_decorator);
    final Button hebrew = (Button) findViewById(R.id.button_hebrew);
    final Button arabic = (Button) findViewById(R.id.button_arabic);
    final Button customView = (Button) findViewById(R.id.button_custom_view);

    modeButtons.addAll(Arrays.asList(single, multi, range, displayOnly, decorator, customView));

    single.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        setButtonsEnabled(single);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(lastYear.getTime(), nextYear.getTime()) //
            .inMode(SelectionMode.SINGLE) //
            .withSelectedDate(new Date());

      }
    });

    multi.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        setButtonsEnabled(multi);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        Calendar today = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<Date>();
        for (int i = 0; i < 5; i++) {
          today.add(Calendar.DAY_OF_MONTH, 3);
          dates.add(today.getTime());
        }
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(new Date(), nextYear.getTime()) //
            .inMode(SelectionMode.MULTIPLE) //
            .withSelectedDates(dates);
      }
    });

    range.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        setButtonsEnabled(range);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        Calendar today = Calendar.getInstance();
        ArrayList<Date> dates = new ArrayList<Date>();
        today.add(Calendar.DATE, 3);
        dates.add(today.getTime());
        today.add(Calendar.DATE, 5);
        dates.add(today.getTime());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(new Date(), nextYear.getTime(), SampleTimesSquareActivity.this) //
            .inMode(SelectionMode.RANGE) //
            .withSelectedDates(dates);
      }
    });

    displayOnly.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        setButtonsEnabled(displayOnly);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.init(new Date(), nextYear.getTime()) //
            .inMode(SelectionMode.SINGLE) //
            .withSelectedDate(new Date()) //
            .displayOnly();
      }
    });

    dialog.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        String title = "I'm a dialog!";
        showCalendarInDialog(title, R.layout.dialog);
        dialogView.init(lastYear.getTime(), nextYear.getTime()) //
            .withSelectedDate(new Date());
      }
    });

    customized.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        showCalendarInDialog("Pimp my calendar!", R.layout.dialog_customized);
        dialogView.init(lastYear.getTime(), nextYear.getTime())
            .withSelectedDate(new Date());
      }
    });

    decorator.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        setButtonsEnabled(decorator);

        calendar.setCustomDayView(new DefaultDayViewAdapter());

        calendar.init(lastYear.getTime(), nextYear.getTime()) //
            .inMode(SelectionMode.RANGE);
        calendar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new SampleDecorator(getApplicationContext())));
      }
    });

    hebrew.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        showCalendarInDialog("I'm Hebrew!", R.layout.dialog);
        dialogView.init(lastYear.getTime(), nextYear.getTime(), new Locale("iw", "IL")) //
                .withSelectedDate(new Date());
      }
    });

    arabic.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        showCalendarInDialog("I'm Arabic!", R.layout.dialog);
        dialogView.init(lastYear.getTime(), nextYear.getTime(), new Locale("ar", "EG")) //
            .withSelectedDate(new Date());
      }
    });

    customView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        setButtonsEnabled(customView);

        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
        calendar.setCustomDayView(new SampleDayViewAdapter());
        calendar.init(lastYear.getTime(), nextYear.getTime())
                .inMode(SelectionMode.SINGLE)
                .withSelectedDate(new Date());
      }
    });

    findViewById(R.id.done_button).setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        Log.d(TAG, "Selected time in millis: " + calendar.getSelectedDate().getTime());
        String toast = "Selected: " + calendar.getSelectedDate().getTime();
        Toast.makeText(SampleTimesSquareActivity.this, toast, LENGTH_SHORT).show();
      }
    });
  }

  private void showCalendarInDialog(String title, int layoutResId) {
    dialogView = (CalendarPickerView) getLayoutInflater().inflate(layoutResId, null, false);
    theDialog = new AlertDialog.Builder(this) //
        .setTitle(title)
        .setView(dialogView)
        .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
          }
        })
        .create();
    theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override public void onShow(DialogInterface dialogInterface) {
        Log.d(TAG, "onShow: fix the dimens!");
        dialogView.fixDialogDimens();
      }
    });
    theDialog.show();
  }

  private void setButtonsEnabled(Button currentButton) {
    for (Button modeButton : modeButtons) {
      modeButton.setEnabled(modeButton != currentButton);
    }
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    boolean applyFixes = theDialog != null && theDialog.isShowing();
    if (applyFixes) {
      Log.d(TAG, "Config change: unfix the dimens so I'll get remeasured!");
      dialogView.unfixDialogDimens();
    }
    super.onConfigurationChanged(newConfig);
    if (applyFixes) {
      dialogView.post(new Runnable() {
        @Override public void run() {
          Log.d(TAG, "Config change done: re-fix the dimens!");
          dialogView.fixDialogDimens();
        }
      });
    }
  }

  @Override
  public void handleClick(MonthCellDescriptor cell) {
    Toast.makeText(getApplicationContext(), "oneClick", Toast.LENGTH_SHORT).show();

    Calendar newlySelectedCal = Calendar.getInstance(TimeZone.getTimeZone(cell.getDate().toString()), Locale.getDefault());
    newlySelectedCal.setTime(cell.getDate());
    // Sanitize input: clear out the hours/minutes/seconds/millis.
    newlySelectedCal.set(HOUR_OF_DAY, 0);
    newlySelectedCal.set(MINUTE, 0);
    newlySelectedCal.set(SECOND, 0);
    newlySelectedCal.set(MILLISECOND, 0);

    Date clickedDate = cell.getDate();

    // Clear any remaining range state.
    for (MonthCellDescriptor selectedCell : selectedCells) {
      selectedCell.setRangeState(MonthCellDescriptor.RangeState.NONE);
    }

    if (selectedCals.size() > 1) {
      // We've already got a range selected: clear the old one.
      calendar.clearOldSelections();
    } else if (selectedCals.size() == 1 && newlySelectedCal.before(selectedCals.get(0))) {
      // We're moving the start of the range back in time: clear the old start date.
      calendar.clearOldSelections();
    }

    if (cell.getDate() != null) {
      // Select a new cell.
      if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
        selectedCells.add(cell);
        cell.setSelected(true);
      }
      selectedCals.add(newlySelectedCal);

      if (selectedCells.size() > 1) {
        // Select all days in between start and end.
        Date start = selectedCells.get(0).getDate();
        Date end = selectedCells.get(1).getDate();
        selectedCells.get(0).setRangeState(MonthCellDescriptor.RangeState.FIRST);
        selectedCells.get(1).setRangeState(MonthCellDescriptor.RangeState.LAST);

        for (List<List<MonthCellDescriptor>> month : cells) {
          for (List<MonthCellDescriptor> week : month) {
            for (MonthCellDescriptor singleCell : week) {
              if (singleCell.getDate().after(start)
                      && singleCell.getDate().before(end)
                      && singleCell.isSelectable()) {
                singleCell.setSelected(true);
                singleCell.setRangeState(MonthCellDescriptor.RangeState.MIDDLE);
                selectedCells.add(singleCell);
              }
            }
          }
        }
      }
    }
    calendar.validateAndUpdate();
//    boolean wasSelected = calendar.doSelectDate(clickedDate, cell);
  }
}