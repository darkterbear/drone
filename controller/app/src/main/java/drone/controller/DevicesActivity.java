package drone.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DevicesActivity extends AppCompatActivity {

    ListView deviceList;

    private BluetoothAdapter bluetoothAdapter = null;
    private Set<BluetoothDevice> pairedDevices;

    public static String EXTRA_DEVICE_NAME = "device_name";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        deviceList = (ListView) findViewById(R.id.deviceListView);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not available", Toast.LENGTH_LONG).show();

            this.finish();
            System.exit(0);
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent turnOnBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnBTIntent, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        listPairedDevices();
    }

    private void listPairedDevices() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> deviceLabels = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceLabels.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No paired devices found", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceLabels);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(deviceListClickListener);
    }

    private AdapterView.OnItemClickListener deviceListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String deviceLabel = ((TextView) view).getText().toString();
            String deviceName = deviceLabel.split("\n")[0];
            String deviceAddress = deviceLabel.split("\n")[1];

            Intent controllerIntent = new Intent(DevicesActivity.this, ControllerActivity.class);
            controllerIntent.putExtra(EXTRA_DEVICE_NAME, deviceName);
            controllerIntent.putExtra(EXTRA_DEVICE_ADDRESS, deviceAddress);
            startActivity(controllerIntent);
        }
    };
}
