package juego;

import entorno.Entorno;
import java.awt.Color;

public class Edificio {

	private int x;
	private int y;
	private int ancho;
	private int largo;

	public Edificio(int x, int y,int  ancho, int largo){
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.largo = largo;
	}

	public void dibujar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, largo, 0, Color.darkGray);
	}

	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double ancho() {
		return ancho;
	}
	public double largo() {
		return largo;
	}
}
