package juego;

import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Mina {

	private double x;
	private double y;
	private double radio;
	Image mina;

	public Mina(double x, double y, double radio) {
		this.x = x;
		this.y = y;
		this.radio = radio;
		mina = Herramientas.cargarImagen("mina.png");
	}

	public void colocar(Entorno e) {
		e.dibujarImagen(mina, x, y,0,0.04);
	}

	public ondaExpansiva explotar() {
		//Devuelve una ondaExpansiva 
		return new ondaExpansiva(x,y,radio*15); 
	}

	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double radio() {
		return radio;
	}

}