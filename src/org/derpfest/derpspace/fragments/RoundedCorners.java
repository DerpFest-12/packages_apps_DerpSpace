/*
 * Copyright (C) 2020-21 The Project-Xtended
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.derpfest.derpspace.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.*;
import androidx.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import com.android.settings.R;

import java.util.Arrays;
import java.util.HashSet;

import com.android.settings.SettingsPreferenceFragment;
import com.derp.support.preferences.CustomSeekBarPreference;
import com.derp.support.preferences.SecureSettingSwitchPreference;
import com.derp.support.preferences.SystemSettingSeekBarPreference;

public class RoundedCorners extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "RoundedCorners";

    private static final String STATUSBAR_LEFT_PADDING = "statusbar_left_padding";
    private static final String STATUSBAR_RIGHT_PADDING = "statusbar_right_padding";

    private SystemSettingSeekBarPreference mSbLeftPadding;
    private SystemSettingSeekBarPreference mSbRightPadding;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.rounded_corners);

        final PreferenceScreen prefScreen = getPreferenceScreen();

        final ContentResolver resolver = getActivity().getContentResolver();

        Resources res = null;
        Context ctx = getContext();
        float density = Resources.getSystem().getDisplayMetrics().density;

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        mSbLeftPadding = (SystemSettingSeekBarPreference) findPreference(STATUSBAR_LEFT_PADDING);
        int sbLeftPadding = Settings.System.getIntForUser(ctx.getContentResolver(),
                Settings.System.LEFT_PADDING, ((int) (res.getIdentifier("com.android.systemui:dimen/status_bar_padding_start", null, null) / density)), UserHandle.USER_CURRENT);
        mSbLeftPadding.setValue(sbLeftPadding);
        mSbLeftPadding.setOnPreferenceChangeListener(this);

        mSbRightPadding = (SystemSettingSeekBarPreference) findPreference(STATUSBAR_RIGHT_PADDING);
        int sbRightPadding = Settings.System.getIntForUser(ctx.getContentResolver(),
                Settings.System.RIGHT_PADDING, ((int) (res.getIdentifier("com.android.systemui:dimen/status_bar_padding_end", null, null) / density)), UserHandle.USER_CURRENT);
        mSbRightPadding.setValue(sbRightPadding);
        mSbRightPadding.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        Resources res = null;
        Context ctx = getContext();
        float density = Resources.getSystem().getDisplayMetrics().density;

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (preference == mSbLeftPadding) {
            int leftValue = (Integer) newValue;
            int sbLeft = ((int) (leftValue / density));
            Settings.System.putIntForUser(getContext().getContentResolver(),
                    Settings.System.LEFT_PADDING, sbLeft, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mSbRightPadding) {
            int rightValue = (Integer) newValue;
            int sbRight = ((int) (rightValue / density));
            Settings.System.putIntForUser(getContext().getContentResolver(),
                    Settings.System.RIGHT_PADDING, sbRight, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DERP;
    }
}
