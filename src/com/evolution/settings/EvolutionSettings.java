/*
 * Copyright (C) 2019-2022 Evolution X
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolution.settings;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.content.Context;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import com.google.android.material.appbar.CollapsingToolbarLayout;

@SearchIndexable
public class EvolutionSettings extends DashboardFragment {

    private static final String TAG = "EvolutionSettings";
    private int mDashBoardStyle;
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        hideToolbar();
        setSigmaDashboardStyle();
    }

    private void hideToolbar() {
        if (mCollapsingToolbarLayout == null) {
            mCollapsingToolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar);
        }
        if (mCollapsingToolbarLayout != null) {
            mCollapsingToolbarLayout.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();
        hideToolbar();
        setSigmaDashboardStyle();
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.evolution_settings;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.EVOLVER;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    private void setSigmaDashboardStyle() {
        int mDashBoardStyle = geSettingstDashboardStyle();
        final PreferenceScreen mScreen = getPreferenceScreen();
        final int mCount = mScreen.getPreferenceCount();
        for (int i = 0; i < mCount; i++) {
            final Preference mPreference = mScreen.getPreference(i);

            String mKey = mPreference.getKey();

            if (mKey == null) continue;

            if (mKey.equals("sigma_header")) {
                mPreference.setLayoutResource(R.layout.settings_sigma_toolbox_header);
                continue;
            }

            if (mDashBoardStyle == 2) { // 0 = stock , 1 = Dot, 2 = Nad, 3 = Sigma
                mPreference.setLayoutResource(R.layout.sigma_dashboard_preference_full);
            } else if (mDashBoardStyle == 1 || mDashBoardStyle == 3){
               if (mKey.equals("themes_category")) {
                    mPreference.setLayoutResource(R.layout.dot_dashboard_preference_top);
                } else if (mKey.equals("misc_category")) {
                    mPreference.setLayoutResource(R.layout.dot_dashboard_preference_bottom);
                } else {
                    mPreference.setLayoutResource(R.layout.dot_dashboard_preference_middle); 
                }  
            }
        }
    }

    private int geSettingstDashboardStyle() {
        return Settings.System.getIntForUser(getContext().getContentResolver(),
                Settings.System.SETTINGS_DASHBOARD_STYLE, 2, UserHandle.USER_CURRENT);
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.evolution_settings);
}
