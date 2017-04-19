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

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A fragment for "ToOfficers."
 */
public class ToOfficersFragment extends Fragment {
  public ToOfficersFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment3, container, false);
    WebView webView = (WebView) rootView.findViewById(R.id.section_to_officers);
    webView.setWebViewClient(new WebViewClient() {
      @SuppressWarnings("deprecation")
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.equals("file:///android_asset/to_officers.html?speak=true")) {
          MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.to_officers);
          mediaPlayer.start();
        }
        return false;
      }

      /* TODO(zkim): the following override doesn't work. Find a proper way to override the URL.
      @TargetApi(Build.VERSION_CODES.N)
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return shouldOverrideUrlLoading(view, request.toString());
      }*/
    });
    webView.loadUrl("file:///android_asset/to_officers.html");
    return rootView;
  }
}
