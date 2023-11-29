package com.fieldbook.tracker.dialogs;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fieldbook.tracker.R;
import com.fieldbook.tracker.activities.CollectActivity;
import com.fieldbook.tracker.adapters.SearchAdapter;
import com.fieldbook.tracker.objects.SearchData;
import com.fieldbook.tracker.preferences.GeneralKeys;
import com.fieldbook.tracker.utilities.Utils;
import com.fieldbook.tracker.views.RangeBoxView;

import java.util.Arrays;

public class SearchDialog extends DialogFragment {

    public static String TICK = "\"";
    private static String TAG = "Field Book";
    private SharedPreferences ep;
    private int rangeUntil;
    private CollectActivity originActivity;
    public SearchDialog(CollectActivity activity) {
        originActivity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ep = requireContext().getSharedPreferences(GeneralKeys.SHARED_PREF_FILE_NAME, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppAlertDialog);

        View customView = getLayoutInflater().inflate(R.layout.activity_search, null);
        builder.setTitle("Search");
        builder.setView(customView);

        LinearLayout parent = customView.findViewById(R.id.rowParent);

        Button start = customView.findViewById(R.id.startBtn);
        Button close = customView.findViewById(R.id.closeBtn);
        Button clear = customView.findViewById(R.id.clearBtn);
        Button add = customView.findViewById(R.id.addBtn);

        start.setTransformationMethod(null);
        close.setTransformationMethod(null);
        clear.setTransformationMethod(null);
        add.setTransformationMethod(null);

        addRow("", parent);

        start.setOnClickListener(arg0 -> {
            Spinner c = parent.getChildAt(0).findViewById(R.id.columns);
            Spinner s = parent.getChildAt(0).findViewById(R.id.like);
            SharedPreferences.Editor ed = ep.edit();
            ed.putInt(GeneralKeys.SEARCH_COLUMN_DEFAULT, c.getSelectedItemPosition());
            ed.putInt(GeneralKeys.SEARCH_LIKE_DEFAULT, s.getSelectedItemPosition());
            ed.apply();

            try {
                // Create the sql query based on user selection
                String sql1 = "select ObservationUnitProperty.id, ObservationUnitProperty." + TICK + ep.getString(GeneralKeys.UNIQUE_NAME, "") + TICK + ", " + " ObservationUnitProperty." + TICK + ep.getString(GeneralKeys.PRIMARY_NAME, "") + TICK + "," + " ObservationUnitProperty." + TICK + ep.getString(GeneralKeys.SECONDARY_NAME, "") + TICK + " from ObservationUnitProperty where ObservationUnitProperty.id is not null ";
                String sql2 = "select ObservationUnitProperty.id, ObservationUnitProperty." + TICK + ep.getString(GeneralKeys.UNIQUE_NAME, "") + TICK + ", " + " ObservationUnitProperty." + TICK + ep.getString(GeneralKeys.PRIMARY_NAME, "") + TICK + "," + " ObservationUnitProperty." + TICK + ep.getString(GeneralKeys.SECONDARY_NAME, "") + TICK + " from observation_variables, ObservationUnitProperty, observations where observations.observation_unit_id = ObservationUnitProperty." + TICK + ep.getString(GeneralKeys.UNIQUE_NAME, "") + TICK + " and observations.observation_variable_name = observation_variables.observation_variable_name and observations.observation_variable_field_book_format = observation_variables.observation_variable_field_book_format ";

                String sql = "";

                boolean threeTables = false;

                for (int i = 0; i < parent.getChildCount(); i++) {
                    LinearLayout child = (LinearLayout) parent.getChildAt(i);

                    EditText t = child.findViewById(R.id.searchText);

                    c = child.findViewById(R.id.columns);
                    s = child.findViewById(R.id.like);

                    String value = "";
                    String prefix;

                    boolean before;

                    if (c.getSelectedItemPosition() < rangeUntil) {
                        before = true;
                        prefix = "ObservationUnitProperty.";
                    } else {
                        before = false;
                        threeTables = true;
                        prefix = "observation_variables.observation_variable_name=";
                    }

                    // This is to prevent crashes when the user uses special characters
                    String trunc = DatabaseUtils.sqlEscapeString(t.getText().toString());

                    // We only want to escape the string, but not the encapsulating "'"
                    // For example 'plot\'s', we only want plot\'s
                    if (trunc.length() > 3)
                        trunc = trunc.substring(1, trunc.length() - 2);

                    switch (s.getSelectedItemPosition()) {

                        // 0: Equals to
                        case 0:
                            if (before)
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " = " + DatabaseUtils.sqlEscapeString(t.getText().toString()) + "";
                            else
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " and value = " + DatabaseUtils.sqlEscapeString(t.getText().toString()) + "";
                            break;

                        // 1: Not equals to
                        case 1:
                            if (before)
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " != " + DatabaseUtils.sqlEscapeString(t.getText().toString()) + "";
                            else
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " and value != " + DatabaseUtils.sqlEscapeString(t.getText().toString()) + "";
                            break;

                        // 2: Is Like
                        case 2:
                            if (before)
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " like " + DatabaseUtils.sqlEscapeString("%" + t.getText().toString() + "%") + "";
                            else
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " and observations.value like " + DatabaseUtils.sqlEscapeString("%" + t.getText().toString() + "%") + "";
                            break;

                        // 3: Not is like
                        case 3:
                            if (before)
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " not like " + DatabaseUtils.sqlEscapeString("%" + t.getText().toString() + "%") + "";
                            else
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " and observations.value not like " + DatabaseUtils.sqlEscapeString("%" + t.getText().toString() + "%") + "";
                            break;

                        // 4: More than
                        case 4:
                            if (before)
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " > " + trunc;
                            else
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " and value > " + trunc;
                            break;

                        // 5: less than
                        case 5:
                            if (before)
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " < " + trunc;
                            else
                                value = prefix + TICK + c.getSelectedItem().toString() + TICK + " and value < " + trunc;
                            break;

                    }

                    if (i == parent.getChildCount() - 1)
                        sql += "and " + value + " ";
                    else
                        sql += "and " + value + " ";

                }

                if (threeTables)
                    sql = sql2 + sql;
                else
                    sql = sql1 + sql;

                final SearchData[] data = originActivity.getDatabase().getRangeBySql(sql);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(), R.style.AppAlertDialog);

                View layout = getLayoutInflater().inflate(R.layout.dialog_search_results, null);
                builder1.setTitle(R.string.search_results_dialog_title)
                        .setCancelable(true)
                        .setView(layout);

                final AlertDialog dialog = builder1.create();

                WindowManager.LayoutParams params2 = dialog.getWindow().getAttributes();
                params2.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params2.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(params2);

                TextView primaryTitle = layout.findViewById(R.id.range);
                TextView secondaryTitle = layout.findViewById(R.id.plot);

                primaryTitle.setText(ep.getString(GeneralKeys.PRIMARY_NAME, getString(R.string.search_results_dialog_range)));
                secondaryTitle.setText(ep.getString(GeneralKeys.SECONDARY_NAME, getString(R.string.search_results_dialog_plot)));

                Button closeBtn = layout.findViewById(R.id.closeBtn);
                ListView myList = layout.findViewById(R.id.myList);

                myList.setDivider(new ColorDrawable(Color.BLACK));
                myList.setDividerHeight(5);

                closeBtn.setTransformationMethod(null);

                myList.setOnItemClickListener((arg012, arg1, position, arg3) -> {
                    // When you click on an item, send the data back to the main screen
                    CollectActivity.searchUnique = data[position].unique;
                    CollectActivity.searchRange = data[position].range;
                    CollectActivity.searchPlot = data[position].plot;
                    CollectActivity.searchReload = true;
                    dialog.dismiss();

                    //Reloading collect activity screen to move to the selected plot
                    RangeBoxView rangeBox = originActivity.findViewById(R.id.act_collect_range_box);
                    int[] rangeID = rangeBox.getRangeID();
                    originActivity.moveToSearch("search", rangeID, CollectActivity.searchRange, CollectActivity.searchPlot, null, -1);
                });

                closeBtn.setOnClickListener(arg01 -> dialog.dismiss());

                // If search has results, show them, otherwise display error message
                if (data != null) {
                    myList.setAdapter(new SearchAdapter(getActivity(), data));
                    //Dismiss the search dialog
                    dismiss();
                    //Show the results dialog
                    dialog.show();
                } else {
                    Utils.makeToast(getActivity(),getString(R.string.search_results_missing));
                }
            } catch (Exception z) {
                Log.e(TAG, "" + z.getMessage());
            }
        });
        add.setOnClickListener(arg0 -> addRow("", parent));

        close.setOnClickListener(arg0 -> dismiss());

        clear.setOnClickListener(arg0 -> {
            parent.removeAllViews();
            addRow("", parent);
        });

        return builder.create();
    }

    public void addRow(String text, LinearLayout parent) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.list_item_search_constructor, null);

        Spinner c = v.findViewById(R.id.columns);
        Spinner s = v.findViewById(R.id.like);
        EditText e = v.findViewById(R.id.searchText);

        String[] likes = new String[6];

        likes[0] = getString(R.string.search_dialog_query_is_equal_to);
        likes[1] = getString(R.string.search_dialog_query_is_not_equal_to);
        likes[2] = getString(R.string.search_dialog_query_contains);
        likes[3] = getString(R.string.search_dialog_query_does_not_contain);
        likes[4] = getString(R.string.search_dialog_query_is_more_than);
        likes[5] = getString(R.string.search_dialog_query_is_less_than);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner_layout, likes);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        String[] col = originActivity.getDatabase().getRangeColumns();

        if (col != null) {
            rangeUntil = col.length;

            ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), R.layout.custom_spinner_layout,
                    concat(col, originActivity.getDatabase().getVisibleTrait()));
//            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            c.setAdapter(adapter2);

            if (text.length() > 0)
                e.setText(text);

            parent.addView(v);
        }
        int columnDefault = ep.getInt(GeneralKeys.SEARCH_COLUMN_DEFAULT, 0);
        int likeDefault = ep.getInt(GeneralKeys.SEARCH_LIKE_DEFAULT, 0);
        if (columnDefault < c.getCount()) {
            c.setSelection(columnDefault);
        } else {
            // Set to first column if the default is not present.
            c.setSelection(0);
        }
        s.setSelection(likeDefault);
    }

    public static <T> T[] concat(T[] first, T[] second) {
        //TODO NullPointerException
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}