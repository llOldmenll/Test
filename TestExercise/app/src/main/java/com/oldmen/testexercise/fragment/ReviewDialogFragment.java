package com.oldmen.testexercise.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.oldmen.testexercise.R;
import com.oldmen.testexercise.activity.MainActivity;

public class ReviewDialogFragment extends DialogFragment {

    private CoordinatorLayout coord;
    private int productId;
    onDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.dialog_fragment, null);
        final EditText reviewText = (EditText) view.findViewById(R.id.user_review);
        final RatingBar reviewRating = (RatingBar) view.findViewById(R.id.user_review_rating);

        Bundle data = getArguments();
        if (data != null){
            productId = data.getInt(MainActivity.PRODUCT_ID_KEY);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String review = reviewText.getText().toString();
                        int rating = (int) reviewRating.getRating();

                        Log.i("info ReviewDialog", review);
                        Log.i("info ReviewDialog", String.valueOf(rating));

                        if (review == null || review.equals("")) {
                            Toast.makeText(getActivity(), "Message wasn't published. \nPlease write something first.", Toast.LENGTH_LONG).show();
                            dismiss();
                        } else
                            listener.onSubmitReviewClicked(review, rating, productId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return builder.create();
    }



    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        listener = (onDialogListener) context;
    }

    public interface onDialogListener {

        void onSubmitReviewClicked(String review, int rating, int productId);
    }
}