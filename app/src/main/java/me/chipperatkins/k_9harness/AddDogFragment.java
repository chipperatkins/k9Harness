package me.chipperatkins.k_9harness;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Iterator;
import java.util.Map;


/**
 *
 */
public class AddDogFragment extends DialogFragment {

    EditText mEdit;

    private static final String TAG = "dog fragment";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater linf = LayoutInflater.from(getContext());
        final View inflator = linf.inflate(R.layout.fragment_add_dog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        mEdit = inflator.findViewById(R.id.dogname);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If text is valid, add new dog to db
                        if(mEdit.getText() != null) {

                            Dog dog = new Dog(mEdit.getText().toString());
                            StorageHandler mHandler = new StorageHandler();
                            dog.heartRateThreshold = 100.0;
                            mHandler.storeDog(dog);

                            ((LoginActivity) getActivity()).updateList();
                        } else {
                            Log.d(TAG, "EditText null");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddDogFragment.this.getDialog().cancel();
                    }
                })
                .setTitle("Add a dog")
                .setView(inflator);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_dog, container, false);
    }
}
