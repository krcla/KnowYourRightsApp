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
    Button nakasecButton = (Button) rootView.findViewById(R.id.nakasec_button);
    nakasecButton.setText(getString(R.string.phone_not_available));
    Button uwdButton = (Button) rootView.findViewById(R.id.uwd_button);
    uwdButton.setText(getString(R.string.phone_not_available));
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
    Button nakasecButton = (Button) rootView.findViewById(R.id.nakasec_button);
    final String nakasecPhoneNumber = getString(R.string.nakasec_phone_number);
    nakasecButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String uri = "tel:" + nakasecPhoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
      }
    });
    Button uwdButton = (Button) rootView.findViewById(R.id.uwd_button);
    final String uwdPhoneNumber = getString(R.string.uwd_phone_number);
    uwdButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String uri = "tel:" + uwdPhoneNumber;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
      }
    });
  }
}
