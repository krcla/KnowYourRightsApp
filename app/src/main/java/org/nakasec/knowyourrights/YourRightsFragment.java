// Know Your Rights App
// Copyright (C) 2017 Zu Kim
// 
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
package org.nakasec.knowyourrights;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.Locale;

/**
 * A fragment for "Your Rights."
 */
public class YourRightsFragment extends Fragment implements NumberPicker.OnValueChangeListener{
  private static final String LOCALE_KEY = "Locale";
  private static final String USER_SETTINGS_KEY = "UserSettings";

  private static final String ALL_LOCALES[] = { "en", "es", "ko", "pt", "zh", "ht" };
  private static final String LOCALE_NAMES[] =
      { "English", "Español", "한국어", "Português", "中文", "Kreyòl Ayisyen" };

  private String locale;

  public YourRightsFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment1, container, false);
    final NumberPicker.OnValueChangeListener listener = this;
    setLocale();
    Button languagePickerButton = (Button) rootView.findViewById(R.id.language_picker_button);
    languagePickerButton.setText(getLocaleName(locale));
    languagePickerButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        LanguagePickerDialogFragment newFragment = new LanguagePickerDialogFragment();
        newFragment.setValueChangeListener(listener);
        newFragment.show(getFragmentManager(), "language picker");
      }
    });
    LoadYourRights(rootView);
    return rootView;
  }

  @Override
  public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
    changeLocale(ALL_LOCALES[newVal]);
    Button languagePickerButton = (Button) getView().findViewById(R.id.language_picker_button);
    languagePickerButton.setText(LOCALE_NAMES[newVal]);
    LoadYourRights(getView());
  }

  // Reads the user default or use the system locale.
  private void setLocale() {
    String systemLocale = Locale.getDefault().toString();
    SharedPreferences settings = getContext().getSharedPreferences(USER_SETTINGS_KEY, 0);
    locale = settings.getString(LOCALE_KEY, systemLocale);
  }

  // Change the locale value and store it to user default.
  private void changeLocale(String toLocale) {
    locale = toLocale;
    SharedPreferences setting = getContext().getSharedPreferences(USER_SETTINGS_KEY, 0);
    SharedPreferences.Editor editor = setting.edit();
    editor.putString(LOCALE_KEY, locale);
    editor.apply();
  }

  // Returns the local name (LOCALE_NAMES) given a locale string.
  private String getLocaleName(String locale) {
    for (int i = 0; i < ALL_LOCALES.length; i++) {
      if (locale.startsWith(ALL_LOCALES[i])) {
        return LOCALE_NAMES[i];
      }
    }
    return LOCALE_NAMES[0];
  }

  private void LoadYourRights(View rootView) {
    String yourRightsUrl = "file:///android_asset/your_rights_en.html";
    switch (locale.substring(0, 2)) {
      case "es":
        yourRightsUrl = "file:///android_asset/your_rights_es.html";
        break;
      case "ko":
        yourRightsUrl = "file:///android_asset/your_rights_ko.html";
        break;
      case "pt":
        yourRightsUrl = "file:///android_asset/your_rights_pt.html";
        break;
      case "zh":
        yourRightsUrl = "file:///android_asset/your_rights_zh.html";
        break;
      case "ht":
        yourRightsUrl = "file:///android_asset/your_rights_ht.html";
        break;
      default:
        // Show the default (English).
        break;
    }
    WebView webView = (WebView) rootView.findViewById(R.id.section_yourrights);
    webView.loadUrl(yourRightsUrl);

    // Enable pinch zooming.
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setDisplayZoomControls(false);
  }

  public static class LanguagePickerDialogFragment extends DialogFragment {
    private NumberPicker.OnValueChangeListener valueChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
      final NumberPicker languagePicker = new NumberPicker(getActivity());
      languagePicker.setMinValue(0);
      languagePicker.setMaxValue(5);
      languagePicker.setDisplayedValues(LOCALE_NAMES);

      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle("Languages");
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          valueChangeListener.onValueChange(languagePicker,
              languagePicker.getValue(), languagePicker.getValue());
        }
      });
      builder.setView(languagePicker);
      return builder.create();
    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
      this.valueChangeListener = valueChangeListener;
    }
  }
}
