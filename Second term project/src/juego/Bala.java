package juego;
import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Bala {
	private double x;
	private double y;
	private double velocidad;
	private double angulo;
	Image bala;

	public Bala(double x,double y,double velocidad, double angulo){
		this.x = x;
		this.y = y;
		this.velocidad=velocidad;
		this.angulo = angulo;
		bala = Herramientas.cargarImagen("bala.png"); //Guarda la imagen "bala.png" en bala
	}

	public void dibujar(Entorno e) {
		e.dibujarImagen(bala, x, y, angulo, 0.1);
	}

	public void mover(Entorno e){
		//Suma en X e Y el desplazamiento para que se pueda mover adelante segun el angulo
		y += velocidad * Math.sin(angulo);
		x += velocidad * Math.cos(angulo);
	}

	public boolean chocasteCon(Arania arania){
		return x < arania.x()+arania.radio() &&  x > arania.x()-arania.radio() &&  y > arania.y()-arania.radio() && y < arania.y()+arania.radio();
	}

	public boolean chocasteCon(Mina mina) {
		return x < mina.x() + mina.radio() && x > mina.x() - mina.radio() & y > mina.y() - mina.radio() && y < mina.y() + mina.radio();
	}
	public boolean chocasteCon(Edificio edificio) {
		return x  > edificio.x() - edificio.ancho() +30 && x < edificio.x() + edificio.ancho()-30 && y > edificio.y() - edificio.largo() + 30 && y < edificio.y() + edificio.largo() -30 ;
	}

}

