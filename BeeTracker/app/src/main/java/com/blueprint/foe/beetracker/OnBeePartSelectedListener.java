package com.blueprint.foe.beetracker;

/**
 * This interface lets the BeePickerAdapter tell the IdentifyFragment that an item has been
 * selected. The IdentifyFragment then changes a UI component's colours to indicate the
 * selection has been made.
 */
public interface OnBeePartSelectedListener {
    void onBeePartSelected();
}
