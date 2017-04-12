// Copyright 2017 Zu Kim
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// TODO: High-level file comment.
package org.nakasec.knowyourrights;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Locale;

/**
 * A fragment for "Your Rights."
 */
public class YourRightsFragment extends Fragment {
  private static final Locale ENGLISH = new Locale("en-US");
  private static final Locale SPANISH = new Locale("es-US");
  private static final Locale KOREAN = new Locale("ko-KR");
  private Locale locale;

  public YourRightsFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment1, container, false);

    WebView webView = (WebView) rootView.findViewById(R.id.section_languages);
    webView.loadUrl("file:///android_asset/your_rights_languages.html");
    webView.setWebViewClient(new WebViewClient() {
      @SuppressWarnings("deprecation")
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        switch (url) {
          case "file:///android_asset/your_rights_languages.html?locale=en_US":
            locale = ENGLISH;
            break;
          case "file:///android_asset/your_rights_languages.html?locale=es_US":
            locale = SPANISH;
            break;
          case "file:///android_asset/your_rights_languages.html?locale=ko_KR":
            locale = KOREAN;
            break;
          default:
            throw new UnsupportedOperationException("Unknown locale: " + url);
        }
        LoadYourRights(rootView);
        return false;
      }

      /* TODO(zkim): the following override doesn't work. Find a proper way to override the URL.
      @TargetApi(Build.VERSION_CODES.N)
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return shouldOverrideUrlLoading(view, request.toString());
      }*/
    });

    LoadYourRights(rootView);
    return rootView;
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
        default:
          throw new UnsupportedOperationException("Unknown locale: " + localeString);
      }
    }
    WebView webView = (WebView) rootView.findViewById(R.id.section_yourrights);
    webView.loadUrl(yourRightsUrl);
  }
}
