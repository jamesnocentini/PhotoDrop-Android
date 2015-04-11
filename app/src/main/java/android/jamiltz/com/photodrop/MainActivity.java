package android.jamiltz.com.photodrop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.listener.LiteListener;
import com.couchbase.lite.listener.LiteServer;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.jar.Manifest;

/**
 * Created by jamesnocentini on 11/04/15.
 */
public class MainActivity extends Activity implements ImageChooserListener {

    private ImageChooserManager imageChooserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Manager manager = null;
        try {
            manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LiteListener listener = new LiteListener(manager, 5432, null);
        int port = listener.getListenPort();
        Thread thread = new Thread(listener);
        thread.start();

        System.out.println("The port value is " + port);

        System.out.println("The internal IP is " +  getLocalIpAddress());

        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE);
        imageChooserManager.setImageChooserListener(this);
        try {
            imageChooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onImageChosen(final ChosenImage chosenImage) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (chosenImage != null) {
                    // Use the image
                    // image.getFilePathOriginal();
                    // image.getFileThumbnail();
                    // image.getFileThumbnailSmall();
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // Show error message
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK &&
                (requestCode == ChooserType.REQUEST_PICK_PICTURE ||
                        requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            imageChooserManager.submit(requestCode, data);
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

}
