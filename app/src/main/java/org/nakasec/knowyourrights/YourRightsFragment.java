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
  private static final Locale ENGLISH = new Locale("en-US");
  private static final Locale SPANISH = new Locale("es-US");
  private static final Locale KOREAN = new Locale("ko-KR");
  private static final Locale PORTUGUESE = new Locale("pt-PT");
  private static final Locale CHINESE = new Locale("zh-CN");
  private static final Locale HAITIAN = new Locale("ht-HT");
  private static final Locale ALL_LOCALES[] =
      { ENGLISH, SPANISH, KOREAN, PORTUGUESE, CHINESE, HAITIAN };
  private static final String LOCALE_NAMES[] =
      { "English", "Español", "한국어", "Português", "中文", "Kreyòl Ayisyen" };

  private Locale locale;

  public YourRightsFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment1, container, false);
    final NumberPicker.OnValueChangeListener listener = this;
    Button languagePickerButton = (Button) rootView.findViewById(R.id.language_picker_button);
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
    locale = ALL_LOCALES[newVal];
    Button languagePickerButton = (Button) getView().findViewById(R.id.language_picker_button);
    languagePickerButton.setText(LOCALE_NAMES[newVal]);
    LoadYourRights(getView());
  }

  private void LoadYourRights(View rootView) {
    String yourRightsUrl = getString(R.string.your_rights_url);
    if (locale != null) {
      // TODO(zkim): do this properly by loading a proper resource for the locale.
      String localeString = locale.toString();
      switch (localeString) {
        case "en-us":
          yourRightsUrl = "file:///android_asset/your_rights_en.html";
          break;
        case "es-us":
          yourRightsUrl = "file:///android_asset/your_rights_es.html";
          break;
        case "ko-kr":
          yourRightsUrl = "file:///android_asset/your_rights_ko.html";
          break;
        case "pt-pt":
          yourRightsUrl = "file:///android_asset/your_rights_pt.html";
          break;
        case "zh-cn":
          yourRightsUrl = "file:///android_asset/your_rights_zh.html";
          break;
        case "ht-ht":
          yourRightsUrl = "file:///android_asset/your_rights_ht.html";
          break;
        default:
          throw new UnsupportedOperationException("Unknown locale: " + localeString);
      }
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
