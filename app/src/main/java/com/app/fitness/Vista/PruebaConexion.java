package com.app.fitness.Vista;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.fitness.ConnectThread;
import com.app.fitness.DataHandler;
import com.app.fitness.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 */
public class PruebaConexion extends Fragment implements AdapterView.OnItemSelectedListener {




    private Spinner spDispositivos;

    private BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> pairedDevices = new ArrayList<>();
    boolean menuBool = false; //display or not the disconnect option


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verificarBluetooth();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_prueba_conexion, container, false);

        spDispositivos = v.findViewById(R.id.spDispositivos);
        return v;
    }

    private void verificarBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter != null){
            if (!mBluetoothAdapter.isEnabled()) {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Bluetooth")
                        .setMessage("¿Desea encender el Bluetooth?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                            public void onClick(DialogInterface dialog, int which) {
                                mBluetoothAdapter.enable();
                                try {
                                    sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                listarDispositivos();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void listarDispositivos() {

        Log.d("Inicio", "Listando dispositivos Bluetooth");

        final List<String> list = new ArrayList<>();
        list.add("Seleccione");

        pairedDevices.clear();
        pairedDevices.addAll(mBluetoothAdapter.getBondedDevices());

        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                list.add(device.getName() + "\n" + device.getAddress());
            }
        }

        Log.d("Inicio", "Listando dispositivos");
        final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                if (!list.contains(device.getName() + "\n" + device.getAddress())) {
                    Log.d("Inicio", "Agregando " + device.getName());
                    list.add(device.getName() + "\n" + device.getAddress());
                    pairedDevices.add(device);
                }
            }
        };

        Thread scannerBTLE = new Thread() {
            public void run() {
                Log.d("Inicio", "Iniciando escaneo");
                mBluetoothAdapter.startLeScan(leScanCallback);
                try {
                    sleep(5000);
                    Log.d("Inicio", "Deteniendo escaneo");
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                } catch (InterruptedException e) {
                    Log.e("Inicio", "Error de escaneo");
                }
            }
        };

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDispositivos.setAdapter(null);
        spDispositivos.setOnItemSelectedListener(this);
        spDispositivos.setAdapter(dataAdapter);

        if (DataHandler.getInstance().getID() != 0 && DataHandler.getInstance().getID() < spDispositivos.getCount())
            spDispositivos.setSelection(DataHandler.getInstance().getID());

        scannerBTLE.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onItemSelected(AdapterView<?> arg0, View arg1, int indice,
                               long arg3) {

        menuBool = false;

        if (indice != 0) {
            //Actual work
            DataHandler.getInstance().setID(indice);

            Log.i("Main Activity", "Conexion normal");
            DataHandler.getInstance().setReader(new ConnectThread((BluetoothDevice) pairedDevices.toArray()[indice - 1], this));
            DataHandler.getInstance().getReader().start();

            menuBool = true;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }



    public void connectionError() {

        Log.w("Main Activity", "Connection error occured");
        if (menuBool) {//did not manually tried to disconnect
            Log.d("Main Activity", "in the app");
            menuBool = false;
            final PruebaConexion pruebaConexion = this;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getContext(), "No se pudo conectar el dispositivo", Toast.LENGTH_SHORT).show();
                    if (DataHandler.getInstance().getID() < spDispositivos.getCount())
                        spDispositivos.setSelection(DataHandler.getInstance().getID());

                    Log.w("Main Activity", "Reanudando conexion normal");
                    DataHandler.getInstance().setReader(new ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], pruebaConexion));
                    DataHandler.getInstance().getReader().start();
                }
            });
        }
    }

}
