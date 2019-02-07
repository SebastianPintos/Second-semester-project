package juego;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Arania{


	private double x;
	private double y;
	private double velocidad;
	private double angulo;
	private int radio;
	Image araña;

	public Arania(double x, double y, double velocidad, double angulo, int radio) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.angulo = angulo;
		this.radio = radio;
		araña = Herramientas.cargarImagen("araña.gif"); //Devuelve el gif y lo guarda en araña

	}

	public void dibujar(Entorno e) {
		e.dibujarImagen(araña, x, y, angulo, 0.7); 
	}

	public boolean chocasteCon(Edificio edificio) {
		return x + radio*2.5   >= edificio.x() && x+radio  <= edificio.x() + edificio.ancho() && y+radio*3 >= edificio.y() && y + radio <= edificio.y() + edificio.largo();
	}

	public void cambiarTrayectoria() {
		x--;						//Da un paso atras
		angulo = angulo - Math.PI;	//Invierte el angulo
	}

	public void mover(){
		//Suma en X e Y el desplazamiento para que se pueda mover adelante segun el angulo
		this.y += velocidad * Math.sin((Math.PI/2)-angulo);
		this.x += velocidad * Math.cos((Math.PI/2)-angulo);
		
	}

	public void seguir(double x, double y) {
		//Calcula el angulo con el arco Tangente segun la posicion de la araña y del exterminador
		angulo = Math.atan((x-this.x) / (y-this.y));
		//Calcula en X e Y el desplazamiento para que se pueda mover adelante segun el nuevo angulo
		double dy = velocidad * Math.sin((Math.PI/2)-angulo);
		double dx = velocidad * Math.cos((Math.PI/2)-angulo);
		//Sigue al exterminador segun lo calculado anteriormente. Para esto lo dividimos por cuadrante
		if (this.x < x && !(y < this.y)) {
			this.y += dy;
			this.x += dx;
		}
		else if (this.x > x && !(y< this.y)) {
			this.y += dy;
			this.x += dx;
		}

		if (y < this.y && this.x < x) {
			this.y -= dy;
			this.x -= dx;

		}
		if (y < this.y && this.x > x) {
			this.y -= dy;
			this.x -= dx;
		}
	}

	public void retroceder(double x, double y) {
		//Hace exactamente lo contrario que el metodo anterior
		angulo = Math.atan((x-this.x) / (y-this.y));
		double dy = -(velocidad * Math.sin((Math.PI/2)-angulo));
		double dx = -(velocidad * Math.cos((Math.PI/2)-angulo));
		//Sigue al exterminador segun lo calculado anteriormente. Para esto lo dividimos por cuadrante
		if (this.x < x && !(y < this.y)) {
			this.y += dy;
			this.x += dx;
		}
		else if (this.x > x && !(y< this.y)) {
			this.y += dy;
			this.x += dx;
		}

		if (y < this.y && this.x<x) {
			this.y -= dy;
			this.x -= dx;

		}
		if (y < this.y && this.x>x) {
			this.y -= dy;
			this.x -= dx;
		}
	}
	public double x(){
		return x;
	}
	public double y(){
		return y;
	}
	public double radio(){
		return radio;
	}

	public Red colocarRed() {
		//Coloca una nueva red en la posicion del exterminador
		return new Red(x,y,15);
	}

	public boolean chocasteCon(Exterminador exterminador){
		return x > exterminador.x() - 10 && x < exterminador.x() + 10 && y> exterminador.y() - radio && y < exterminador.y() + 10;
	}

	public boolean chocasteCon(Mina mina) {
		return x < mina.x() + mina.radio()*2 && x > mina.x() - mina.radio()*2 && y > mina.y() - mina.radio()*2
				&& y < mina.y() + mina.radio()*2;
	}
}

