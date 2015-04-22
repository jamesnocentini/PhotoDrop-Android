package android.jamiltz.com.photodrop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Revision;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.listener.LiteListener;
import com.couchbase.lite.listener.LiteServer;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.Manifest;

import nl.changer.polypicker.ImagePickerActivity;

public class MainActivity extends Activity {

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("The internal IP is " +  getLocalIpAddress());

    }

    private void getImages() {
        Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 3);  // allow only upto 3 images to be selected.
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_REQUEST_GET_IMAGES) {
            Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

            if(parcelableUris == null) {
                return;
            }

            // show images using uris returned.

            // open the sender activity, use an extra
            Intent intent = new Intent(MainActivity.this, SenderActivity.class);
            // pass the uris as extra
            intent.putExtra("uris", parcelableUris);
            startActivity(intent);

        }
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public void startReceiver(View v) {
        Intent intent = new Intent(MainActivity.this, ReceiverActivity.class);
        startActivity(intent);
    }

    public void pickPhotos(View v) {
        getImages();
    }

}
