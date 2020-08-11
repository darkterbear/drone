package drone.controller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import drone.controller.logger.Log;

public class ControllerActivity extends AppCompatActivity {

    private String mConnectedDeviceName = null;
    private StringBuffer mOutStringBuffer;
    private StringBuffer mInStringBuffer;

    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothChatService bluetoothChatServiceLeft = null;
    private BluetoothChatService bluetoothChatServiceRight = null;

    String deviceAddress = null;
    String deviceName = null;

    private ProgressDialog progress;
    private double lastLeftX = 0;
    private double lastLeftY = 0;

    private double lastRightX = 0;
    private double lastRightY = 0;

    private DynamicMatrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        Intent intent = getIntent();
        deviceName = intent.getStringExtra(DevicesActivity.EXTRA_DEVICE_NAME);
        deviceAddress = intent.getStringExtra(DevicesActivity.EXTRA_DEVICE_ADDRESS);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not available", Toast.LENGTH_LONG).show();

            this.finish();
            System.exit(0);
        }

        bluetoothChatServiceLeft = new BluetoothChatService(this, mHandler);
        bluetoothChatServiceRight = new BluetoothChatService(this, mHandler);

        mOutStringBuffer = new StringBuffer("");
        mInStringBuffer = new StringBuffer("");

        BluetoothDevice drone = bluetoothAdapter.getRemoteDevice(deviceAddress);
        bluetoothChatServiceLeft.connect(drone, 1, true);
        bluetoothChatServiceRight.connect(drone, 2, true);

        matrix = findViewById(R.id.matrix);

        matrix.setOnUseListener(new DynamicMatrix.DynamicMatrixListener() {
            @Override
            public void onPress(DynamicMatrix.MatrixCell cell, int pointerId, float actual_x, float actual_y) {
                double x = calcX(cell, actual_x);
                double y = calcY(cell, actual_y);

                if (cell.getCol() == 0) {
                    send(bluetoothChatServiceLeft, buildMessage("1", x, y));
                    lastLeftX = x;
                    lastLeftY = y;
                } else {
                    send(bluetoothChatServiceRight, buildMessage("1", x, y));
                    lastRightX = x;
                    lastRightY = y;
                }
            }

            @Override
            public void onMove(DynamicMatrix.MatrixCell cell, int pointerId, float actual_x, float actual_y) {
                double x = calcX(cell, actual_x);
                double y = calcY(cell, actual_y);
                if (cell.getCol() == 0) {
                    if ((x != lastLeftX) || (y != lastLeftY)) {
                        send(bluetoothChatServiceLeft, buildMessage("2", x, y));
                        lastLeftX = x;
                        lastLeftY = y;
//                        System.out.println("Left moved");
                    }
                } else {
                    if ((x != lastRightX) || (y != lastRightY)) {
                        send(bluetoothChatServiceRight, buildMessage("2", x, y));
                        lastRightX = x;
                        lastRightY = y;
//                        System.out.println("Right moved");
                    }
                }

            }

            @Override
            public void onRelease(DynamicMatrix.MatrixCell cell, int pointerId, float actual_x, float actual_y) {
                double x = calcX(cell, actual_x);
                double y = calcY(cell, actual_y);

                if (cell.getCol() == 0) {
                    send(bluetoothChatServiceLeft, buildMessage("0", x, y));
                    lastLeftX = x;
                    lastLeftY = y;
                } else {
                    send(bluetoothChatServiceRight, buildMessage("0", x, y));
                    lastRightX = x;
                    lastRightY = y;
                }
            }

        });
    }

    private double calcX(DynamicMatrix.MatrixCell cell, float actual_x) {

        double relative_x = actual_x - cell.getBounds().left;
        relative_x = (relative_x - (cell.getWidth() / 2)) / (cell.getWidth() / 2);
        return (double)Math.round(relative_x * 10000d) / 10000d;
    }

    private double calcY(DynamicMatrix.MatrixCell cell, float actual_y) {

        double relative_y = actual_y - cell.getBounds().top;
        relative_y = (relative_y - (cell.getHeight() / 2)) / (cell.getHeight() / 2) * -1;
        return (double)Math.round(relative_y * 10000d) / 10000d;
    }

    private double calcX(View roundButton, MotionEvent event) {
        double x = (event.getX() - (roundButton.getWidth() / 2)) / (roundButton.getWidth() / 2);
        x = (double)Math.round(x * 10000d) / 10000d;
        return x;
    }

    private double calcY(View roundButton, MotionEvent event) {
        double y = (event.getY() - (roundButton.getHeight() / 2)) / (roundButton.getHeight() /2) * -1;
        y = (double)Math.round(y * 10000d) / 10000d;
        return y;
    }

    private String buildMessage(String operation, double x, double y) {
        return (operation + "," + String.valueOf(x) + "," + String.valueOf(y) + "\n");
    }

    public void send(BluetoothChatService service, String message) {
        // Check that we're actually connected before trying anything
        if (service.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "cant send message - not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            service.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    private void disconnect(BluetoothChatService service) {
        if (service != null) {
            service.stop();
        };

        finish();
    }

    private void parseData(String data) {
        //msg(data);

        // add the message to the buffer
        mInStringBuffer.append(data);

        // debug - log data and buffer
        //Log.d("data", data);
        //Log.d("mInStringBuffer", mInStringBuffer.toString());
        //msg(data.toString());

        // find any complete messages
        String[] messages = mInStringBuffer.toString().split("\\n");
        int noOfMessages = messages.length;
        // does the last message end in a \n, if not its incomplete and should be ignored
        if (!mInStringBuffer.toString().endsWith("\n")) {
            noOfMessages = noOfMessages - 1;
        }

        // clean the data buffer of any processed messages
        if (mInStringBuffer.lastIndexOf("\n") > -1)
            mInStringBuffer.delete(0, mInStringBuffer.lastIndexOf("\n") + 1);

        // process messages
        for (int messageNo = 0; messageNo < noOfMessages; messageNo++) {
            processMessage(messages[messageNo]);
        }

    }

    private void processMessage(String message) {
        // Debug
        // msg(message);
        String parameters[] = message.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        boolean invalidMessage = false;

        // Check the message
        if (parameters.length > 0) {
            // check length
            if (parameters.length == 5) {

                // set matrix
                if (parameters[0].equals("4")) {
                    if (!parameters[1].equals("")) {
                        try {
                            // convert color from #rrggbbaa to #aarrggbb
                            String color =
                                    parameters[1].substring(0,1) +
                                            parameters[1].substring(7,9) +
                                            parameters[1].substring(1,7);

                            matrix.setColor(Color.parseColor(color));
                        }
                        catch(Exception i){
                            invalidMessage = true;
                        }
                    }
                    if (!parameters[2].equals(""))
                        matrix.setSquare(parameters[2].equals("1") ? true : false);
                    if (!parameters[3].equals(""))
                        matrix.setBorder(parameters[3].equals("1") ? true : false);
                    if (!parameters[4].equals(""))
                        matrix.setVisible(parameters[4].equals("1") ? true : false);
                    matrix.update();

                }  else {
                    invalidMessage = true;
                }
            } else {
                invalidMessage = true;
            }
        } else {
            invalidMessage = true;
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.d("status","connected");
                            matrix.setVisibility(View.VISIBLE);
                            // send the protocol version to the server
                            send(bluetoothChatServiceLeft,"3," + Constants.PROTOCOL_VERSION + "," + Constants.CLIENT_NAME + "\n");
                            send(bluetoothChatServiceRight,"3," + Constants.PROTOCOL_VERSION + "," + Constants.CLIENT_NAME + "\n");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Log.d("status","connecting");
                            matrix.setVisibility(View.INVISIBLE);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            Log.d("status","not connected");
                            disconnect(bluetoothChatServiceLeft);
                            disconnect(bluetoothChatServiceRight);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readData = new String(readBuf, 0, msg.arg1);
                    // message received
                    parseData(readData);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != this) {
                        Toast.makeText(getApplicationContext(), "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != this) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

    @Override
    public void onBackPressed() {
        disconnect(bluetoothChatServiceLeft);
        disconnect(bluetoothChatServiceRight);
    }
}
