package juego;

import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Red {
	
	private double x;
	private double y;
	private double radio;
	Image red;
	
	public Red (double x, double y, double radio) {
		this.x = x;
		this.y = y;
		this.radio = radio;
		red = Herramientas.cargarImagen("red.png");
	}
	
	public void colocar(Entorno e) {
		e.dibujarImagen(red, x, y, 0, 0.1);		}
	
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