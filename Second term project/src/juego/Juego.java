package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.awt.*;
import java.util.Random;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo
	private Exterminador exterminador;
	private Arania[] arania;
	private Edificio[] edificio;
	private Bala[] balas;
	private Mina[] minas;
	private Red[] redes;
	private ondaExpansiva[] ondaExpansiva;
	private int xEdificio;
	private int yEdificio;
	private int anchoEdificio;
	private int largoEdificio;
	private int edificios;
	private int b,m,o,r; //Variables para indexar los arreglos al crear sus objetos
	private int cont;
	private int random,random2,random3;
	private int araniasExterminadas;
	private int puntos;
	private int nivel;
	private boolean flag;
	private boolean termino;
	Image fondo;
	Image gameOver;

	// ...

	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "UNGS bajo ataque", 800, 600);

		// Inicializar lo que haga falta para el juego
		// ...
		arania = new Arania[80];
		balas = new Bala[50];
		minas = new Mina[3];
		redes = new Red[5];
		flag = true;
		b = 0;
		m = 0;
		o = 0;
		r = 0;
		puntos = 0;
		nivel = 1; //Empieza desde el nivel 1
		cont = 0;
		araniasExterminadas = 0;

		ondaExpansiva = new ondaExpansiva[minas.length]; //Este arreglo debe tener la misma longitud que el de minas
		//Guarda las imagenes en sus respectivos lugares
		fondo = Herramientas.cargarImagen("fondo.png");
		gameOver = Herramientas.cargarImagen("gameover.gif");
		//Setea al exterminador
		setexterminador(new Exterminador(entorno.ancho() / 2, entorno.alto() / 2, 3, -Math.PI / 2));
		generarEdificios();

		// Inicia el juego!
		this.entorno.iniciar();

	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y por lo
	 * tanto es el método más importante de esta clase. Aquí se debe actualizar el
	 * estado interno del juego para simular el paso del tiempo (ver el enunciado
	 * del TP para mayor detalle).
	 */

	public void tick() {

		if (termino) {
			gameOver(entorno);
		}

		if (!termino) {
			dibujarFondo();
			exterminador.dibujar(entorno);
			colocarEdificios();
			mostrarDatos(entorno);
			generarAranias();
			mostrarAranias();
			detectarTeclas();
			InteraccionDeLasBalas();
			generarRedes();
			choqueConRedes();
			colocarEInteractuarMinas();
			colocarEInteractuarOndaExpansiva();
			premiosPorExterminar();
			gano();
		}
	}


	private void gano() {
		//Termina el juego si mato a todas las arañas
		if(araniasExterminadas == arania.length) 
			termino = true;
	}

	private void generarEdificios() {
		Random cantidad = new Random();
		edificios = cantidad.nextInt((8 - 4) + 1) + 4; //Genera un numero entre 4 y 8 
		edificio = new Edificio[edificios]; //La longitud del arreglo es del numero creado anteriormente
		for(int i = 0; i  < edificios; i++) { 
			//Crea valores aleatorios para el x,y,ancho y largo del edificio
			Random x = new Random();
			xEdificio =	(x.nextInt(700) + 50 ) ;
			Random y = new Random();
			yEdificio =	(y.nextInt(500) + 50 )  ;
			Random ancho = new Random();
			anchoEdificio =	ancho.nextInt(70) + 50;
			Random largo = new Random();
			largoEdificio =	largo.nextInt(70) + 50;
			//Se fija que no este en la posicion donde aparece el exterminador
			if ((xEdificio >320 && xEdificio < 470) && (yEdificio > 250 && yEdificio <380)) {
				edificio[i] = new Edificio(xEdificio+150,yEdificio+100,anchoEdificio,largoEdificio);
			}
			else {
				edificio[i] = new Edificio(xEdificio,yEdificio,anchoEdificio,largoEdificio);
			}
		}
	}

	private void dibujarFondo() {
		entorno.dibujarImagen(fondo,450, 300, 0, 1.35);
	}

	private void colocarEdificios() {
		for(int i = 0; i < edificio.length;i++) {
			edificio[i].dibujar(entorno);
		}
	}

	private void colocarEInteractuarOndaExpansiva() {
		for(int i = 0;i < ondaExpansiva.length;i++) {
			if(ondaExpansiva[i] != null) {
				ondaExpansiva[i].colocar(entorno);					
				if (ondaExpansiva[i].chocasteCon(exterminador))
					termino = true; //Si la onda expansiva afecto al exterminador termina el juego

				for (int p = 0; p < arania.length ; p++) {
					//Para cada araña se pregunta si fue afectada por la onda expansiva para matarla
					if (arania[p] != null && ondaExpansiva[i].chocasteCon(arania[p])) {
						araniasExterminadas++;
						puntos += 20;
						arania[p] = null;
					}
				}
				for (int q = 0; q < minas.length ; q++) {
					//Para cada mina se pregunta si fue afectada por la onda expansiva para explotarlaen cadena
					if (minas[q] != null && ondaExpansiva[i].chocasteCon(minas[q])) {
						if (r > ondaExpansiva.length-1) //si la indexacion llego a la ultima posicion del arreglo se reestablece
							r = 0;
						ondaExpansiva[r++] = minas[q].explotar();
						minas[q] = null;
					}
				}
				//Una vez que termino de interactuar se destruye
				ondaExpansiva[i] = null;
			}
		}
	}

	private void colocarEInteractuarMinas() {
		for (int i = 0 ; i < minas.length; i++) {
			if (minas[i] != null) {
				minas[i].colocar(entorno);
				for (int p = 0; p < arania.length; p++){
					//Para cada mina pregunta si la araña choco con ella
					if (arania[p] != null && arania[p].chocasteCon(minas[i])){
						//Al chocar, explota generando una onda expansiva
						ondaExpansiva[r++] = minas[i].explotar();
						araniasExterminadas++;
						puntos += 20;
						arania[p] = null;
						minas[i]=null;
						break; //Corta para que no pregunte por una posicion null
					}
				}
			}
		}
	}

	private void choqueConRedes() {
		for(int i= 0; i < redes.length; i++) {
			if (redes[i] != null) {
				redes[i].colocar(entorno);
				if (exterminador.chocasteCon(redes[i])) {
					exterminador.reducirVelocidad();
					redes[i]=null;
				}
			}
		}
	}

	private void InteraccionDeLasBalas() {
		for (int i = 0; i < balas.length; i++) {
			if (balas[i] != null) {
				balas[i].mover(entorno);
				balas[i].dibujar(entorno);
				for (int p = 0 ; p < arania.length; p++){
					//Para cada araña se fija si la bala choco con ella
					if (arania[p] != null && balas[i].chocasteCon(arania[p])){
						araniasExterminadas ++;
						puntos +=10;
						arania[p] = null;
						balas[i] = null;
						break;//Corte para que no pregunte por una posicion nula
					}
				}
				for(int q = 0; q < minas.length; q++) {
					//Para cada mina pregunta si la bala choco con ella
					if (minas[q] != null && balas[i] != null && balas[i].chocasteCon(minas[q])) {
						if (r > ondaExpansiva.length-1)//Si la indexacion llego a la ultima posicion del arreglo se reestablece
							r = 0;
						ondaExpansiva[r++] = minas[q].explotar();//Explota creando una nueva onda expansiva
						minas[q] = null;
						balas[i] = null;
					}
				}
				for(int e = 0; e < edificio.length; e++) {
					//Para cada edificio pregunta si la bala choco con el para borrarla
					if (balas[i] != null && balas[i].chocasteCon(edificio[e]))
						balas[i] = null;
				}
			}
		}
	}

	private void generarRedes() {
		for (int p = 0; p < arania.length;p++){
			if(sigPosValida(redes) >= 0 && arania[p]!= null) { //si hay una posicion valida en el arreglo y la araña no es nul
				Random cantidad = new Random();
				cont = cantidad.nextInt(3500); //
				if (cont==1) {
					redes[sigPosValida(redes)] = arania[p].colocarRed();
				}
			}
		}
	}

	private void premiosPorExterminar() {
		if(araniasExterminadas%10 == 0 && flag && araniasExterminadas != 0){
			flag = false;
			if (b > 0)
				b = b-10;
			if (m > 0)
				m = m-1;
			exterminador.aumentarVelocidad();
		}
		else{
			flag = true;
		}
		if(araniasExterminadas%10 == 0 && flag && araniasExterminadas != 0){
			flag = false;
		}
	}

	private void detectarTeclas() {
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) 
			exterminador.moverAdelante();

		if (entorno.estaPresionada(entorno.TECLA_ABAJO)) 
			exterminador.moverAtras();

		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) 
			exterminador.moverIzquierda();

		if (entorno.estaPresionada(entorno.TECLA_DERECHA)) 
			exterminador.moverDerecha();

		if (entorno.sePresiono('m')) 
			if (m < minas.length)
				minas[m++] = exterminador.colocar();

		if (entorno.sePresiono(entorno.TECLA_ESPACIO)) 
			if(b < balas.length)
				balas[b++] = exterminador.disparar();
	}

	private void mostrarAranias() {
		for (int p = 0; p < arania.length; p++){
			if (arania[p] != null) {
				arania[p].dibujar(entorno);

				if(arania[p].chocasteCon(exterminador))
					termino = true;

				for(int i = 0; i < edificio.length; i++) {
					if (arania[p].chocasteCon(edificio[i])) {
						arania[p].cambiarTrayectoria();
						//Retrocede en la posicion contraria al exterminador
						arania[p].retroceder(exterminador.x(), exterminador.y());
						arania[p].cambiarTrayectoria();
					} 
					else 
						arania[p].seguir(exterminador.x(), exterminador.y());
				}
			}
		}
	}

	private void generarAranias() {
		if (o != arania.length) { //se fija que no se hayan creado todas las arañas del arreglo
			//Se crean numeros en determinado rango segun el nivel.
			//Entre mas grande sea el rango, mas dificil de que se cree una araña.
			Random r = new Random();
			if (araniasExterminadas < 15)
				random = r.nextInt(230);
			else if(araniasExterminadas >= 15 && araniasExterminadas < 35) {
				random = r.nextInt(180);
				nivel = 2;
			}
			else if(araniasExterminadas >=35 && araniasExterminadas < 50) {
				random = r.nextInt(140);
				nivel = 3;
			}
			else {
				random = r.nextInt(70);
				nivel = 4;
			}
			//si el numero creado es 1, se crea una araña.
			if(random == 1) {
				Random r1 = new Random();
				random = r1.nextInt(4);
				Random r2 = new Random();
				random2 = r2.nextInt(entorno.ancho());
				Random r3 = new Random();
				random3 = r3.nextInt(entorno.alto());
				//Al igual que lo anterior, se hace para que la araña aparezca en un borde aleatorio.
				if(random==0)
					arania[o++]= new Arania(random2,0,0.2,0,25);
				if(random==1)
					arania[o++]= new Arania(0,random3,0.2,0,25);
				if(random==2)
					arania[o++]= new Arania(random2,entorno.alto(),0.2,0,25);
				if(random==3)
					arania[o++]= new Arania(entorno.ancho(),random3,0.2,0,25);
			}
		}
	}

	private void mostrarDatos(Entorno entorno) {
		entorno.cambiarFont("burbank big cd bd", 23, Color.ORANGE);
		entorno.escribirTexto("Arañas exterminadas: "+ araniasExterminadas, 620, 555);
		entorno.escribirTexto("Puntos: " + puntos, 620, 575);
		entorno.escribirTexto("Nivel: "+ nivel, 380,30 );
		entorno.escribirTexto("Municion restante : " + (balas.length - b), 30, 550);
		entorno.escribirTexto("Minas restantes : " + (minas.length - m), 30, 570);
	}


	private void setexterminador(Exterminador exterminador) {
		this.exterminador = exterminador;
	}

	public void gameOver(Entorno entorno){
		entorno.dibujarImagen(gameOver, 400, 300, 0, 2); //Pone el gif de fondo
		entorno.cambiarFont("burbank big cd bd", 29, Color.orange);
		entorno.escribirTexto("Arañas exterminadas: "+ araniasExterminadas, 50 , 500);
		entorno.escribirTexto("Puntos: "+ puntos, 650, 500);
		if(araniasExterminadas == arania.length) { //si se exterminaron todas las arañas ¡Ganaste!
			entorno.cambiarFont("burbank big cd bd", 70, Color.orange);
			entorno.escribirTexto("¡GANASTE!", 290, 150);
		}
	}

	public int sigPosValida(Red redes[]) {
		for(int i = 0; i < redes.length; i++) { //recorre el arreglo de redes
			if (redes[i] == null)
				return i;						//devuelve la primer posicion valida
		}
		return -1;								//si todas son validas, devuelve -1
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
