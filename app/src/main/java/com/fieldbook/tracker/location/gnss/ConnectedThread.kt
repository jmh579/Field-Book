package com.fieldbook.tracker.location.gnss

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream

/**
 * Thread implementation that reads strings with \r\n ending.
 * Takes a bluetooth socket, created in ConnectThread and a handler used to broadcast messages.
 */
class ConnectedThread constructor(private val socket: BluetoothSocket, val handler: Handler, private val outputCode: Int): Thread() {

    private lateinit var mmInStream: InputStream
    private lateinit var mmBuffer: ByteArray

    private companion object {
        const val TAG = "ConnectedThread"
    }

    //initialize input stream to read bytes from
    init {
        try {
            mmInStream = socket.inputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun run() {

        mmBuffer = ByteArray(512)
        var bytes = 0

        while (socket.isConnected) try {

            if (bytes == 512) bytes = 0

            mmBuffer[bytes] = mmInStream.read().toByte()

            //check if the buffer has a line end, send the message
            if (bytes > 1 && String(mmBuffer, 0, bytes-1).endsWith("\r\n")) {

                val msg = String(mmBuffer, 0, bytes-1)

//                val readMsg = handler.obtainMessage(
//                        GNSSResponseReceiver.MESSAGE_OUTPUT_CODE, bytes, -1,
//                        msg)
                val readMsg = handler.obtainMessage(
                        outputCode, bytes, -1,
                        msg)

                readMsg.sendToTarget()

                bytes = 0

            } else bytes++

        } catch (e: Exception) {

            e.printStackTrace()

            //if the thread is a daemon and still reading messages, cancel the thread
            if ("socket closed" in (e.message ?: String())) {
                cancel()
            }
        }
    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() = try {

        mmInStream.close()

        socket.close()

    } catch (e: IOException) {

        Log.e(TAG, "Could not close the client socket", e)

    }
}