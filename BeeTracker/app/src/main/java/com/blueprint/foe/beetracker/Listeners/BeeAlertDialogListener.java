package com.blueprint.foe.beetracker.Listeners;

/**
 * The listener for dialog boxes
 */
public interface BeeAlertDialogListener {
    int ERROR_DIALOG = 1;
    int NORMAL_DIALOG = 2;

    void onDialogFinishClick(int id);
}
