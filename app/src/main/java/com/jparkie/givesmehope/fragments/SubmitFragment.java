package com.jparkie.givesmehope.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jparkie.givesmehope.R;
import com.jparkie.givesmehope.network.GivesMeHopeService;
import com.squareup.okhttp.Response;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class SubmitFragment extends DialogFragment {
    public static final String TAG = SubmitFragment.class.getSimpleName();

    private GivesMeHopeService mGivesMeHopeService;
    private Subscription mGivesMeHopeServiceSubscription;

    private EditText mSubmitNameEditText;
    private EditText mSubmitLocationEditText;
    private EditText mSubmitTitleEditText;
    private EditText mSubmitStoryEditText;
    private Spinner mSubmitCategorySpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch an instance of the GivesMeHopeService such that all network requests are executed by one client.
        mGivesMeHopeService = GivesMeHopeService.getInstance(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();
        View submitDialogView = dialogInflater.inflate(R.layout.fragment_submit, null);

        mSubmitNameEditText = (EditText)submitDialogView.findViewById(R.id.submitNameEditText);
        mSubmitLocationEditText = (EditText)submitDialogView.findViewById(R.id.submitLocationEditText);
        mSubmitTitleEditText = (EditText)submitDialogView.findViewById(R.id.submitTitleEditText);
        mSubmitStoryEditText = (EditText)submitDialogView.findViewById(R.id.submitStoryEditText);
        mSubmitCategorySpinner = (Spinner)submitDialogView.findViewById(R.id.submitCategorySpinner);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(submitDialogView)
                .setPositiveButton(R.string.submit_dialog_button_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        // Do Nothing.
                    }
                })
                .setNegativeButton(R.string.submit_dialog_button_reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        // Do Nothing.
                    }
                });

        return dialogBuilder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // This onStart event of the fragment lifecycle is changed to prevent the dialog from closing unnecessarily.
        AlertDialog currentDialog = (AlertDialog)getDialog();
        if (currentDialog != null) {
            Button submitButton = currentDialog.getButton(Dialog.BUTTON_POSITIVE);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionSubmit();
                }
            });
            Button resetButton = currentDialog.getButton(Dialog.BUTTON_NEGATIVE);
            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionClear();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // When the user navigates away from the fragment, free memory and prevent the activity context from leaking from the submission request.
        if (mGivesMeHopeServiceSubscription != null) {
            mGivesMeHopeServiceSubscription.unsubscribe();
            mGivesMeHopeServiceSubscription = null;
        }

        super.onDestroy();
    }

    private void actionClear() {
        mSubmitNameEditText.getEditableText().clear();
        mSubmitLocationEditText.getEditableText().clear();
        mSubmitTitleEditText.getEditableText().clear();
        mSubmitStoryEditText.getEditableText().clear();
        mSubmitCategorySpinner.setSelection(0);
    }

    private void actionSubmit() {
        String submitName = mSubmitNameEditText.getEditableText().toString();
        String submitLocation = mSubmitLocationEditText.getEditableText().toString();
        String submitTitle = mSubmitTitleEditText.getEditableText().toString();
        String submitStory = mSubmitStoryEditText.getEditableText().toString();

        TypedArray submitCategoryValues = getResources().obtainTypedArray(R.array.submit_dialog_category_entry_values);
        String submitCategory = submitCategoryValues.getString(mSubmitCategorySpinner.getSelectedItemPosition());

        if (!(submitTitle.length() > 0)) {
            Toast.makeText(getActivity(), R.string.toast_submit_error_title, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(submitStory.length() > 10)) {
            Toast.makeText(getActivity(), R.string.toast_submit_error_story, Toast.LENGTH_SHORT).show();
            return;
        }

        // This is a Rx call (facilitated by RxJava and RxAndroid) which posts the user-generated story to Gives Me Hope.
        // This call occurs asynchronously and is observed on the main UI thread to prompt the user of the status of the submission.
        // The benefit of the Rx call is the ease in concurrency and the ease in managing the status of a concurrent call through the following functions (i.e. managing the success and all the errors).
        mGivesMeHopeServiceSubscription = mGivesMeHopeService
                .postSubmitStory(submitName, submitLocation, submitTitle, submitStory, submitCategory)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), R.string.toast_submit_success, Toast.LENGTH_SHORT).show();

                        SubmitFragment.this.getDialog().cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), R.string.toast_submit_failure, Toast.LENGTH_SHORT).show();

                        SubmitFragment.this.getDialog().cancel();
                    }

                    @Override
                    public void onNext(Response response) {
                        // Do Nothing.
                    }
                });
    }
}
