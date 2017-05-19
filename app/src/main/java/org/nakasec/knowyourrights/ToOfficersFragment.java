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

  MediaPlayer mediaPlayer;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    if (mediaPlayer == null) {
      mediaPlayer = MediaPlayer.create(getContext(), R.raw.to_officers);
    }
    View rootView = inflater.inflate(R.layout.fragment3, container, false);
    WebView webView = (WebView) rootView.findViewById(R.id.section_to_officers);
    webView.setWebViewClient(new WebViewClient() {
      @SuppressWarnings("deprecation")
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.equals("file:///android_asset/to_officers.html?speak=true")) {
          if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
          } else {
            mediaPlayer.start();
          }
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

    // Enable pinch zooming.
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setDisplayZoomControls(false);

    return rootView;
  }
}
