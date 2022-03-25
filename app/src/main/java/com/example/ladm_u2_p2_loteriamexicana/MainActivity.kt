package com.example.ladm_u2_p2_loteriamexicana

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ladm_u2_p2_loteriamexicana.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var _binding : ActivityMainBinding
    var detener = true
    var pausar = false
    var continuar = false
    var terminar = false
    var hasta = Random.nextInt()
    var contador = 0
    var VectorCartas = arrayOf (R.drawable.carta1,R.drawable.carta2,R.drawable.carta3,R.drawable.carta4,R.drawable.carta5,
        R.drawable.carta6,R.drawable.carta7,R.drawable.carta8,R.drawable.carta9,R.drawable.carta10,R.drawable.carta11,
        R.drawable.carta12,R.drawable.carta13,R.drawable.carta14,R.drawable.carta15,R.drawable.carta16,R.drawable.carta17,
        R.drawable.carta18,R.drawable.carta19,R.drawable.carta20,R.drawable.carta21,R.drawable.carta22,R.drawable.carta23,
        R.drawable.carta24,R.drawable.carta25,R.drawable.carta26,R.drawable.carta27,R.drawable.carta28,R.drawable.carta29,
        R.drawable.carta30,R.drawable.carta31,R.drawable.carta32,R.drawable.carta33,R.drawable.carta34,R.drawable.carta35,
        R.drawable.carta36,R.drawable.carta37,R.drawable.carta38,R.drawable.carta39,R.drawable.carta40,R.drawable.carta41,
        R.drawable.carta42,R.drawable.carta43,R.drawable.carta44,R.drawable.carta45,R.drawable.carta46,R.drawable.carta47,
        R.drawable.carta48,R.drawable.carta49,R.drawable.carta50,R.drawable.carta51,R.drawable.carta52,R.drawable.carta53,R.drawable.carta54)
    var VectorAudios = arrayOf (R.raw.carta1,R.raw.carta2,R.raw.carta3,R.raw.carta4,R.raw.carta5,
        R.raw.carta6,R.raw.carta7,R.raw.carta8,R.raw.carta9,R.raw.carta10,R.raw.carta11,
        R.raw.carta12,R.raw.carta13,R.raw.carta14,R.raw.carta15,R.raw.carta16,R.raw.carta17,
        R.raw.carta18,R.raw.carta19,R.raw.carta20,R.raw.carta21,R.raw.carta22,R.raw.carta23,
        R.raw.carta24,R.raw.carta25,R.raw.carta26,R.raw.carta27,R.raw.carta28,R.raw.carta29,
        R.raw.carta30,R.raw.carta31,R.raw.carta32,R.raw.carta33,R.raw.carta34,R.raw.carta35,
        R.raw.carta36,R.raw.carta37,R.raw.carta38,R.raw.carta39,R.raw.carta40,R.raw.carta41,
        R.raw.carta42,R.raw.carta43,R.raw.carta44,R.raw.carta45,R.raw.carta46,R.raw.carta47,
        R.raw.carta48,R.raw.carta49,R.raw.carta50,R.raw.carta51,R.raw.carta52,R.raw.carta53,R.raw.carta54)
    var audio : MediaPlayer?= null
    var VectorBarajas = ArrayList<Int>()
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    var puntero = this
    val coroutineCartas = scope.launch(EmptyCoroutineContext, CoroutineStart.LAZY){
        while(true){
            runOnUiThread{
                if(!pausar){
                    audio = MediaPlayer.create(puntero,VectorAudios[VectorBarajas[contador]])
                    _binding.imgCartas.setImageResource(VectorCartas[VectorBarajas[contador++]])
                    audio?.start()
                    if(contador==VectorBarajas.size) terminar=true
                    if(terminar==true){
                        _binding.txtLoteria1.text = "La loteria se ha acabado"
                    }
                }else if(pausar==true){
                    _binding.txtLoteria1.text = "Tenemos un ganador"
                }else if(pausar==false && continuar==true){
                    _binding.txtLoteria1.text = "Cartas faltantes"
                    audio = MediaPlayer.create(puntero,VectorAudios[VectorBarajas[contador]])
                    _binding.imgCartas.setImageResource(VectorCartas[VectorBarajas[contador++]])
                    audio?.start()
                    if(contador==VectorBarajas.size) return@runOnUiThread
                }
            }
            if(contador == VectorCartas.size) contador=0
            delay(3000L)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        nuevaBaraja()
        _binding.btnIniciar.setOnClickListener{
            /*contador++
            _binding.imgCartas.setImageResource(VectorCartas[VectorBarajas[contador]])
            if(contador == VectorCartas.size) contador=0*/
            coroutineCartas.start()
        }
        _binding.btnSuspender.setOnClickListener{
            //Terminar juego
            pausar=true
        }
        _binding.btnVerificar.setOnClickListener{
            pausar=false;
            continuar=true
        }

    }
    fun nuevaBaraja(){
        VectorBarajas = ArrayList<Int>()
        for(i in VectorCartas){
            VectorBarajas.add(posicionAzar(VectorBarajas))
        }
    }
    fun posicionAzar(numPos:ArrayList<Int>):Int{
        hasta = Random.nextInt(54)
        while(numPos.contains(hasta)){
            return posicionAzar(numPos)
        }
        return hasta
    }
}
class HilCartas(etiquetaEnviada: TextView, act: MainActivity) : Thread(){ //hereda
    var contadorHilo=1;
    var etiquetaEnviadaGlobal = etiquetaEnviada
    var puntero = act
    override fun run() {
        super.run() //Superpadre (Metodo que ejecuta en segundo plano)
        while(true){
            puntero._binding.txtLoteria1.text ="Tiempo Segundos: ${contadorHilo++}"
            sleep(1000)
        }
    }
}