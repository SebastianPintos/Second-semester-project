package juego;
import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class ondaExpansiva {
	public double x;
	public double y;
	public double radio;
	Image ondaExpansiva;

	public ondaExpansiva(double x, double y, double radio) {
		this.x = x;
		this.y = y;
		this.radio = radio;
		ondaExpansiva = Herramientas.cargarImagen("ondaExpansiva.png");
	}

	public void colocar(Entorno e) {
		e.dibujarImagen(ondaExpansiva, x, y, 0, 0.35);
	}
	public boolean chocasteCon(Arania arania) {
		return x < arania.x() + radio &&  x > arania.x()-radio &&  y > arania.y()-radio && y < arania.y()+radio;
	}
	public boolean chocasteCon(Exterminador exterminador) {
		return x > exterminador.x() - radio && x < exterminador.x() + radio && y> exterminador.y() - radio && y < exterminador.y() + radio;
	}
	public boolean chocasteCon(Mina mina) {
		return x < mina.x() + radio && x > mina.x() - radio & y > mina.y() - radio && y < mina.y() + radio;
	}
}
