package mx.tecnm.tepic.ladm_u1_practica2_archivos_de_memoria_interna_externa

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var Resultado = ""
        leer.setOnClickListener {
            if (radiointerna.isChecked) {
                var contenido = abrirDesdeMemoriaInterna()
                var mensaje = ""
                if (!(contenido.isEmpty() == true)) {

                    Toast.makeText(this,"Se leyo con exito",Toast.LENGTH_LONG).show()

                    Resultado = contenido
                    valor.setText("")
                    valor.setText(Resultado)
                    Resultado = ""
                } else {
                    mensaje = "ERROR ARCHIVO NO ENCONTRADO"
                }
                AlertDialog.Builder(this).setTitle("Resultado")
                    .setMessage(mensaje)
                    .setPositiveButton("ok") { d, i ->
                        d.dismiss()
                    }
                    .show()

        }else if (radioexterna.isChecked){

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )

            }
                abrirDesdeMemoriaEXTERNAL()
        }
    }
        guardar.setOnClickListener {
            var data = ""
            data = valor.getText().toString()
            if (nombre.text.toString()==""){
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage("Escribe un nombre")
                    .setPositiveButton("ok") { d, i ->
                        d.dismiss()
                    }
                    .show()
            }else {
                var par=valor.getText().toString()
                data=par.toString()
                if (radiointerna.isChecked) {


                    var mensaje = " "
                    if (guardarEnMemoriaInterna(data) == true) {
                        Toast.makeText(this,"Se guardo en memoria INTERNA",Toast.LENGTH_LONG).show()
                    valor.setText("")
                    } else {
                        mensaje = "Error no se pudo guardar"
                        AlertDialog.Builder(this).setTitle("Resultado")
                            .setMessage(mensaje)
                            .setPositiveButton("ok") { d, i ->
                                d.dismiss()
                            }
                            .show()
                    }


                } else if (radioexterna.isChecked){
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            0
                        )
                    }
                    guardarEnMemoriaExterna(data)
                    valor.setText("")

                }
            }
        }
    }
    private fun guardarEnMemoriaExterna(data:String){
        try {
            if(Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED){
                AlertDialog.Builder(this).setMessage("NO HAY SD").show()
                return
            }
            var rutaSD=getExternalFilesDir(null)!!.absolutePath
            var archivoEnSD=File(rutaSD,nombre.getText().toString()+".txt")
            var flujoSalida =OutputStreamWriter(FileOutputStream(archivoEnSD))
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            valor.setText("")
            Toast.makeText(this,"Se guardo en memoria EXTERNA",Toast.LENGTH_LONG).show()
        } catch (io:IOException){
            Toast.makeText(this,io.message,Toast.LENGTH_LONG).show()
        }
    }
    private fun guardarEnMemoriaInterna(data:String):Boolean{
        try {
            var flujoSalida=OutputStreamWriter(openFileOutput(nombre.getText().toString()+".txt", Context.MODE_PRIVATE))
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
        } catch (io:IOException){
            return false
        }
        return true
    }

    private fun abrirDesdeMemoriaInterna():String{
        var data=""
        try {
            val archivo = InputStreamReader(openFileInput(nombre.getText().toString()+".txt"))
            val br = BufferedReader(archivo)
            var linea = br.readLine()
            val todo = StringBuilder()
            while (linea != null) {
                todo.append(linea + "\n")
                linea = br.readLine()
            }
            br.close()
            archivo.close()
            data=todo.toString()

        } catch (e: IOException) {
        }
        return data
    }
    private fun abrirDesdeMemoriaEXTERNAL(){


    try {
        if(Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED){
            AlertDialog.Builder(this).setMessage("NO HAY SD").show()
            return
        }
        var rutaSD=getExternalFilesDir(null)!!.absolutePath
        var archivoEnSD=File(rutaSD,nombre.getText().toString()+".txt")

        val fIn = FileInputStream(archivoEnSD)
        val archivo = InputStreamReader(fIn)
        val br = BufferedReader(archivo)
        var linea = br.readLine()
        val todo = StringBuilder()
        while (linea != null) {
            todo.append(linea + "\n")
            linea = br.readLine()
        }
        br.close()
        archivo.close()
        valor.setText(todo)
        Toast.makeText(this,"Se leyo de memoria EXTERNA",Toast.LENGTH_LONG).show()

    } catch (e: IOException) {
        Toast.makeText(this, "ERROR ARCHIVO NO ENCONTRADO", Toast.LENGTH_SHORT).show()
    }
}


}