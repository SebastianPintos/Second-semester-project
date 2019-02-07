package juego;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import entorno.Entorno;
import entorno.Herramientas;

public class Exterminador {

	private double x;
	private double y;
	private double velocidad;
	private double angulo;
	Image exterminador;


	public Exterminador(double x,double y,double velocidad, double angulo){
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.angulo = angulo;
		exterminador = Herramientas.cargarImagen("exterminador.gif");
	}

	public void dibujar(Entorno e){
		e.dibujarImagen(exterminador, x, y, angulo+Math.PI/2, 1); //dibuja al exterminador
	}


	public void moverAdelante() {
		y += velocidad * Math.sin(angulo); //Mueve al exterminador en Y segun la velocidad establecida y su angulo
		x += velocidad * Math.cos(angulo); //Mueve al exterminador en X segun la velocidad establecida y su angulo
	}
	public void moverAtras(){
		y -= velocidad * Math.sin(angulo); //Mueve al exterminador en Y segun la velocidad establecida y su angulo
		x -= velocidad * Math.cos(angulo); //Mueve al exterminador en X segun la velocidad establecida y su angulo
	}

	public void moverIzquierda() {
		this.angulo += - 0.05; //Cambia el angulo del exterminador hacia la izquierda

	}
	public void moverDerecha() {
		this.angulo += + 0.05; //Cambia el angulo del exterminador hacia la izquierda

	}

	public Bala disparar () {
		return new Bala(x,y,5,angulo); //Retorna una nueva bala en la misma posicion del Exterminador
									   //con su mismo angulo
	}

	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double angulo(){
		return angulo;
	}

	public boolean chocasteCon(Red red) {
		return x >= red.x() - red.radio() && x <= red.x() + red.radio() && y >= red.y() - red.radio() && y <= red.y() + red.radio();
	//Devuelve true si choco con una red. False en caso contrario
	}
	public void aumentarVelocidad() {
		velocidad = velocidad * 1.2;
	}
	public void reducirVelocidad() {
		boolean flag = true; 		//Bandera para que entre solo una vez
		velocidad = velocidad *0.5; //Reduce la velocidad a la mitad
		Timer esperar =  new Timer();//Crea un objeto de tipo Timer
		TimerTask tarea = new TimerTask() {	//Crea una nueva Tarea
			public void run() {				//Metodo que vuelve a poner la velocidad anterior
				velocidad = 3;
			}	
		};
		if (flag) {				//entra solo una vez y ejecuta la tarea durante 2 segundos
			esperar.schedule(tarea, 2000);
			flag = false;
		}
	}
	public Mina colocar () {	
		return new Mina(x,y,8);	//Retorna una nueva mina en la posicion del Exterminador
	}

}
