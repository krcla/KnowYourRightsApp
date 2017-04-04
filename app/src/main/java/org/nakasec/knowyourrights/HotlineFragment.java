// Copyright 2017 Google Inc.
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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A fragment for "Hotline."
 */
public class HotlineFragment extends Fragment {
  public static final String HAS_PHONE_KEY = "hasPhone";
  public static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;

  private boolean hasPhone;

  // Do not use this constructor but call getInstance() instead.
  public HotlineFragment() {}

  public static HotlineFragment getInstance(boolean hasPhone) {
    HotlineFragment fragment = new HotlineFragment();
    Bundle args = new Bundle();
    args.putBoolean(HAS_PHONE_KEY, hasPhone);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void setArguments(Bundle bundle) {
    this.hasPhone = bundle.getBoolean(HAS_PHONE_KEY);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment2, container, false);
    if (hasPhone) {
      if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
          == PackageManager.PERMISSION_GRANTED) {
        // Already have the permission.
        DisplayHotline(rootView);
        return rootView;
      }
      // Asynchronously request the permission.
      requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
          PERMISSIONS_REQUEST_CALL_PHONE);
    }
    // By default, phone is not available.
    Button button = (Button) rootView.findViewById(R.id.button);
    button.setText(getString(R.string.phone_not_available));
    return rootView;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String permissions[],
                                         @NonNull int[] grantResults) {
    switch (requestCode) {
      case PERMISSIONS_REQUEST_CALL_PHONE:
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          DisplayHotline(getView());
        }
        return;
      default:
        throw new IllegalStateException("Unknown request code: " + requestCode);
    }
  }

  private void DisplayHotline(View rootView) {
    Button button = (Button) rootView.findViewById(R.id.button);
    final String phoneNumber = getString(R.string.hotline_phone_number);
    button.setText(getString(R.string.call_emergency_hotline) + "\n" + phoneNumber);
    button.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String uri = "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
      }
    });
  }
}
