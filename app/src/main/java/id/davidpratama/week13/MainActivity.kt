package id.davidpratama.week13

import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var magneticReading = FloatArray(3)
    private var accelerometerReading = FloatArray(3)
    private var accelerometerSensor : Sensor?= null
    private var geoMagnectiSensor : Sensor? = null
    private var lightSensor : Sensor?= null
    private var proximitySensor : Sensor?= null
    private var previousV: Float ?= null
    private var tepCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        geoMagnectiSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onResume() {
        super.onResume()
        if(accelerometerSensor != null){
            Toast.makeText(this,"Accelerometer sensor detected",Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_FASTEST)
        }else{
            Toast.makeText(this,"No accelerometer sensor detected",Toast.LENGTH_SHORT).show()
        }

        if(geoMagnectiSensor != null){
            Toast.makeText(this,"Magnetic Field sensor detected",Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this,geoMagnectiSensor,SensorManager.SENSOR_DELAY_FASTEST)
        }else{
            Toast.makeText(this,"No Magnetic Field sensor detected",Toast.LENGTH_SHORT).show()
        }

        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if(lightSensor != null){
            Toast.makeText(this,"Magnetic Field sensor detected",Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_FASTEST)
        }
        else{
            Toast.makeText(this,"No magnecti field sensor detected",Toast.LENGTH_SHORT).show()
        }

        if (proximitySensor != null) {
            Toast.makeText(this, "Proximity Sensor detected",					    Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_FASTEST)
        } else {
            Toast.makeText(this, "No ProximitySensor detected",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)

        if(geoMagnectiSensor != null){
            Toast.makeText(this,"Magnetic Field sensor detected",Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this,geoMagnectiSensor,SensorManager.SENSOR_DELAY_FASTEST)
        }
        else{
            Toast.makeText(this,"No magnecti field sensor detected",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            accelerometerReading = p0.values
            var x = p0.values[0]
            var y = p0.values[1]
            var z = p0.values[2]

            txtMsg.text = "x: $x, : $y, z: $z"

            var v = sqrt(x.pow(2) + y.pow(2) + z.pow(2))

            if(previousV != null){
                var diff = v- previousV!!
                if(diff > 6){
                    tepCount++
                    txtStep.text = "$tepCount steps"
                }
            }
            previousV = v
        }

        if(p0!!.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
            magneticReading = p0.values
        }

        if(accelerometerReading != null && accelerometerSensor!= null) {
            var rotationMatrix = FloatArray(9)
            var orientationAngles = FloatArray(3)

            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magneticReading)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            var azimuth = (Math.toDegrees(orientationAngles[0].toDouble()) + 360.0) % 360.0
            var pitch = (Math.toDegrees(orientationAngles[1].toDouble()) + 360.0) % 360.0
            var roll = (Math.toDegrees(orientationAngles[2].toDouble()) + 360.0) % 360.0

            txtGyro.text = "Az=" + (azimuth * 100) / 100 + "\nPitch=" + (pitch * 100) / 100 + "\nRoll=" + (roll * 100) / 100
        }

        if(p0!!.sensor.type == Sensor.TYPE_LIGHT){
            txtLight.text = p0.values[0].toString()
        }

        if (p0!!.sensor.type == Sensor.TYPE_PROXIMITY) {
            txtLight.text = p0.values[0].toString()
            if(p0.values[0] <= 0.0) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}