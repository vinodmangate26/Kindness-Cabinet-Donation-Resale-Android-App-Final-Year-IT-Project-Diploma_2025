package com.gauravpatil.kindnesscabinet;

import static android.app.Activity.RESULT_OK;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gauravpatil.kindnesscabinet.Comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView civProfilePhoto;
    EditText etName, etMobileNo, etEmailid, etAddress,
            etGender, etAge, etUsername;
    Button btnSaveChanges;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String strusername;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        civProfilePhoto = view.findViewById(R.id.civProfilePhoto);
        // Make the profile image clickable for selecting a new image.
        civProfilePhoto.setOnClickListener(v -> showFileChooser());

        etName = view.findViewById(R.id.etProfileName);
        etMobileNo = view.findViewById(R.id.etProfileMobileNo);
        etEmailid = view.findViewById(R.id.etProfileEmailId);
        etGender = view.findViewById(R.id.etProfileGender);
        etAge = view.findViewById(R.id.etProfileAge);
        etAddress = view.findViewById(R.id.etProfileAddress);
        etUsername = view.findViewById(R.id.etProfileUsername);
        btnSaveChanges = view.findViewById(R.id.btnProfileSaveChanges);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        // Retrieve the username (set during login)
        strusername = preferences.getString("username", "");

        btnSaveChanges.setOnClickListener(v -> saveProfileData());

        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                // Get the selected image as a Bitmap.
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                civProfilePhoto.setImageBitmap(bitmap);
                // Convert bitmap to Base64 string and store it permanently with a unique key.
                String encodedImage = encodeImage(bitmap);
                editor.putString("profile_image_" + strusername, encodedImage);
                editor.apply();
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Encode a Bitmap into a Base64 string.
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 80, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    // Decode a Base64 string into a Bitmap.
    private Bitmap decodeImage(String encodedImage) {
        byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    // Save text profile data into SharedPreferences.
    private void saveProfileData() {
        editor.putString("name", etName.getText().toString());
        editor.putString("mobile_no", etMobileNo.getText().toString());
        editor.putString("email_id", etEmailid.getText().toString());
        editor.putString("gender", etGender.getText().toString());
        editor.putString("age", etAge.getText().toString());
        editor.putString("address", etAddress.getText().toString());
        editor.putString("username", etUsername.getText().toString());
        editor.apply();
        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Profile");
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getMydetails();
    }

    // Retrieve text profile details from the database (using your API).
    // If the database returns an image for this username, that image is displayed.
    // Otherwise, the image stored in SharedPreferences for that username is used.
    // If no image exists at all, the default drawable (R.drawable.image_myprofile) is shown.
    private void getMydetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", strusername);

        client.post(Urls.myDetails, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (getActivity() == null) return;
                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        etName.setText(jsonObject.getString("name"));
                        etMobileNo.setText(jsonObject.getString("mobile_no"));
                        etEmailid.setText(jsonObject.getString("email_id"));
                        etGender.setText(jsonObject.getString("gender"));
                        etAge.setText(jsonObject.getString("age"));
                        etAddress.setText(jsonObject.getString("address"));
                        etUsername.setText(jsonObject.getString("username"));

                        // Check if the database provides an image manually set for the user.
                        String dbImage = jsonObject.getString("image");
                        if (dbImage != null && !dbImage.trim().isEmpty()) {
                            // Load image from the database using Glide.
                            Glide.with(getActivity())
                                    .load(Urls.image + dbImage)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.image_myprofile)
                                    .into(civProfilePhoto);
                        } else {
                            // Otherwise, load image from SharedPreferences unique to the username.
                            String encodedImage = preferences.getString("profile_image_" + strusername, "");
                            if (!encodedImage.isEmpty()) {
                                Bitmap storedBitmap = decodeImage(encodedImage);
                                civProfilePhoto.setImageBitmap(storedBitmap);
                            } else {
                                // If no image exists, set the default drawable.
                                civProfilePhoto.setImageResource(R.drawable.image_myprofile);
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Data Parsing Error", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
}
