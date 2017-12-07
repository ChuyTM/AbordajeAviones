package algos;

import java.io.IOException;

import drawings.StdDraw;
import utils.Estadisticas;

public class Simulacion {
	static int numAviones = 6;
	static int avionFilas = 30;
	static int avionColumnas = 3;
	static double espacioPorAvionX;
	static double espacioPorAvionY;

	static int numPasajeros = 180;
	static int probSolo = 20;
	static int prob2pasajeros = 50;
	static int prob3pasajeros = 15;
	static int prob4pasajeros = 10;
	static int prob5pasajeros = 5;
	static Pasajero[] pasajeros;
	static int[] ordenPasajeros;
	static int[][] seatReservation;

	static int CANVAS_X = 1500;
	static int CANVAS_Y = 800;
	static int numAvionesX = 3;
	static int numAvionesY = 2;
	static int delay = 20;

	static Plane[] aviones;

	static void init() {

		pasajeros = new Pasajero[numPasajeros];
		ordenPasajeros = new int[numPasajeros];
		seatReservation = new int[avionFilas][(avionColumnas * 2) + 1];

	}

	static void initCanvas() {

		StdDraw.setCanvasSize(CANVAS_X, CANVAS_Y);

		// StdDraw.setXscale(0, ((avionFilas + 2) + 16) * numAvionesX);
		// StdDraw.setYscale(0, ((avionFilas + 2) + 16) * numAvionesY);
		double xScale = (avionFilas + 2) * 1.6 * numAvionesX;
		double yScale = 32 * numAvionesY;

		double x;
		double y;

		espacioPorAvionX = xScale / numAvionesX;
		espacioPorAvionY = yScale / numAvionesY;

		x = espacioPorAvionX / 2;
		y = espacioPorAvionY / 2;

		int k = 0;
		for (int i = 0; i < numAvionesX; i++) {
			for (int j = 0; j < numAvionesY; j++) {
				aviones[k].setCoordinates(x, y, xScale, yScale, espacioPorAvionX, espacioPorAvionY);
				
				k++;
				y += espacioPorAvionY;
			}
			x += espacioPorAvionX;
			y = espacioPorAvionY / 2;
		}

	}

	static void drawSimulation() {

		
		// Draw vertical separators
		for (int i = 1; i < numAvionesX; i++) {
			StdDraw.line(espacioPorAvionX * i, 0, espacioPorAvionX * i, espacioPorAvionY * numAvionesY); // |

		}
		
		// Draw horizontal separators
		for (int i = 1; i < numAvionesY; i++) {
			StdDraw.line(0, espacioPorAvionY * i, espacioPorAvionX * numAvionesX, espacioPorAvionY * i); // |

		}
		

	}

	static void runSimulation() {
		StdDraw.enableDoubleBuffering();
		int simulacionesTerminadas = 0;
		int j = 0;
		Plane ganando;
		Plane anterior;
		ganando = aviones[0];
		int max = 0;
		int aLaCabeza = 0;

		while (simulacionesTerminadas < numAviones) {

			StdDraw.clear();

			for (int i = 0; i < numAviones; i++) {
				if (aviones[i].simulacionTerminada) {
					aviones[i].draw();
					continue;
				}

				aviones[i].abordarPasajeros(j);
				aviones[i].draw();
			}
			drawSimulation();

			StdDraw.show();
			StdDraw.pause(delay);
			simulacionesTerminadas = 0;
			for (int i = 0; i < numAviones; i++) {

				System.out.println(
						"Avion: " + (i + 1) + "==================================================================");
				System.out.println("Algoritmo " + aviones[i].nombreDeAlgoritmoUtilizado);
				int total = aviones[i].totalTiempoHombre();
				System.out.println("Total: " + total);
				System.out.println("Esperando entrar al avion: " + aviones[i].contEsperandoEntrar + " "
						+ String.format("%.0f%%", 100.0 * aviones[i].contEsperandoEntrar / total));
				System.out.println("Esperando en pasillo bloqueado: " + aviones[i].contEsperandoEnPasillo + " "
						+ String.format("%.0f%%", 100.0 * aviones[i].contEsperandoEnPasillo / total));
				System.out.println("Caminando en el pasillo del avion: " + aviones[i].contCaminando + " "
						+ String.format("%.0f%%", 100.0 * aviones[i].contCaminando / total));
				System.out.println("Subiendo maleta: " + aviones[i].contGuardandoMaleta + " "
						+ String.format("%.0f%%", 100.0 * aviones[i].contGuardandoMaleta / total));
				System.out.println("Esperando sentarse: " + aviones[i].contEsperandoSentarse + " "
						+ String.format("%.0f%%", 100.0 * aviones[i].contEsperandoSentarse / total));
				System.out.println("Sentado: " + aviones[i].contSentado + " "
						+ String.format("%.0f%%", 100.0 * aviones[i].contSentado / total));
				System.out.println("Pasajeros sentados: " + aviones[i].pasajerosSentados);
				System.out.println("Pasajeros por ingresar: " + aviones[i].pasajeroPorIngresar);

				System.out.println(
						"=======================================================================================");
				System.out.println();

				if (aviones[i].simulacionTerminada) {
					if (simulacionesTerminadas == 0)
						aviones[i].simulacionGanadora = true;

					simulacionesTerminadas++;
				}

				if (aviones[i].pasajerosSentados > max) {
					max = aviones[i].pasajerosSentados;
					aLaCabeza = i;
				}

			}
			ganando = aviones[aLaCabeza];
			ganando.aLaCabeza = true;

			j++;

		}

	}

	static void runMassiveSimulation() {

		int simulacionesTerminadas = 0;
		int j = 0;
		Plane ganando;
		Plane anterior;
		ganando = aviones[0];
		int max = 0;
		int aLaCabeza = 0;

		while (simulacionesTerminadas < numAviones) {
			
			for (int i = 0; i < numAviones; i++) {
				if (aviones[i].simulacionTerminada) {

					continue;
				}

				aviones[i].abordarPasajeros(j);

			}

			simulacionesTerminadas = 0;

			for (int i = 0; i < numAviones; i++) {

				if (aviones[i].simulacionTerminada) {
					if (simulacionesTerminadas == 0)
						aviones[i].simulacionGanadora = true;

					simulacionesTerminadas++;
				}

				if (aviones[i].pasajerosSentados > max) {
					max = aviones[i].pasajerosSentados;
					aLaCabeza = i;
				}

			}
			ganando = aviones[aLaCabeza];
			ganando.aLaCabeza = true;

			j++;
			
			

		}

		

	}

	static void initPasajeros(Plane avion) {
		for (int i = 0; i < avion.numPas; i++)
			pasajeros[i] = new Pasajero(avion, PasStatus.ESPERANDO);
	}

	static void asignarAsientos(Plane p) {

		int numP = 0;

		for (int i = 1; i < p.filas - 1; i++) {
			for (int j = 0; j < p.columnasTotales; j++) {
				if (j == 3)
					continue;

				p.pasajeros[numP].asientoX = i;
				p.pasajeros[numP].asientoY = j;
				p.seatReservation[i][j] = numP;
				numP++;

			}

		}

		// for (Pasajero pas : pasajeros) {
		// System.out.println("Asiento Fila: " + pas.asientoX + " Columna: " +
		// pas.asientoY);
		// }

		// System.out.println("yes");
		// Random r = new Random();
		//
		// while (asientosRestantes > 0) {
		// int k = r.nextInt(100-1) + 1;
		//
		// if(k <= probSolo) {
		//
		// } else if (k <= prob2pasajeros) {
		//
		// } else if (k <= prob3pasajeros) {
		//
		// } else if (k <= prob4pasajeros) {
		//
		// } else if (k <= prob5pasajeros) {
		//
		// }
		//
		// }
	}

	static Pasajero[] copyOfPasajeros(Pasajero[] pasajeros, Plane a) {
		Pasajero[] p = new Pasajero[pasajeros.length];
		for (int i = 0; i < pasajeros.length; i++) {
			p[i] = pasajeros[i].clone(a);
		}
		return p;
	}

	static int[][] copyOfSeatReservation(int[][] seats) {
		int[][] newSeatMap = new int[seats.length][seats[0].length];
		for (int i = 0; i < seats.length; i++) {
			for (int j = 0; j < seats[0].length; j++) {
				newSeatMap[i][j] = seats[i][j];
			}
		}
		return newSeatMap;
	}

	public static void correrSimulacionGrafica() {
		boolean randomPassengers = false;
		delay = 3;

		aviones = new Plane[numAviones];

		init();

		for (int i = 0; i < numAviones; i++) {
			aviones[i] = new Plane(avionFilas, avionColumnas);
		}

		initCanvas();
		initPasajeros(aviones[0]);
		aviones[0].setPasajeros(pasajeros);
		asignarAsientos(aviones[0]);

		for (int i = 1; i < numAviones; i++) {
			aviones[i].setPasajeros(copyOfPasajeros(aviones[0].pasajeros, aviones[i]));
			aviones[i].setSeatReservation(copyOfSeatReservation(aviones[0].seatReservation));

		}

		// Plane avion = new Plane(30, 3);

		for (int i = 0; i < numAviones; i++) {
			aviones[i].randomize = randomPassengers;
			switch (i) {
			case 0:
				aviones[i].originalSteffenMethod();
				break;
			case 1:

				aviones[i].frontToBack();
				break;
			case 2:
				aviones[i].SteffenMethod();
				break;
			case 3:
				aviones[i].backToFront();
				break;
			case 4:
				aviones[i].improvedSteffenMethod();
				break;
			case 5:
				aviones[i].randomMethod();
				break;

			default:
				break;
			}

		}

		runSimulation();
	}

	public static void correrSimulaciónMasiva(boolean randPass) {
		boolean randomPassengers = randPass;
		

		aviones = new Plane[numAviones];

		init();

		for (int i = 0; i < numAviones; i++) {
			aviones[i] = new Plane(avionFilas, avionColumnas);
		}

		initPasajeros(aviones[0]);
		aviones[0].setPasajeros(pasajeros);
		asignarAsientos(aviones[0]);

		for (int i = 1; i < numAviones; i++) {
			aviones[i].setPasajeros(copyOfPasajeros(aviones[0].pasajeros, aviones[i]));
			aviones[i].setSeatReservation(copyOfSeatReservation(aviones[0].seatReservation));

		}

		// Plane avion = new Plane(30, 3);

		for (int i = 0; i < numAviones; i++) {
			aviones[i].randomize = randomPassengers;
			switch (i) {
			case 0:
				aviones[i].originalSteffenMethod();
				break;
			case 1:

				aviones[i].frontToBack();
				break;
			case 2:
				aviones[i].SteffenMethod();
				break;
			case 3:
				aviones[i].backToFront();
				break;
			case 4:
				aviones[i].improvedSteffenMethod();
				break;
			case 5:
				aviones[i].randomMethod();
				break;
			case 6:
				aviones[i].ventanasPrimero();
				break;
			default:
				break;
			}

		}
		
		
		
		
		runMassiveSimulation();
		
		
		
	}

	public static void getStatistics() {
		int N = 50;
		int numeroDeAviones = 7;
		int incrementosFilas = 30;
		int filas = 0;
		int numPasajeros;
		int columnasDeAsientos = 6;
		
		numAviones = numeroDeAviones;
		
		Estadisticas[] e = new Estadisticas[N];
		
		boolean randomPassengers = false;

		
		for(int i = 0; i < N; i++) {
			filas += incrementosFilas;
			numPasajeros = filas * columnasDeAsientos;
			Simulacion.numPasajeros = numPasajeros;
			avionFilas = filas;

			
			e[i] = new Estadisticas(i, i, filas, numPasajeros, numAviones);
			
			correrSimulaciónMasiva(randomPassengers);
			
			for(int j = 0; j < numAviones; j++) {
				e[i].setNombresDeAlgoritmos(aviones[j].nombreDeAlgoritmoUtilizado, j);
				e[i].setCaminando(aviones[j].contCaminando, j);
				e[i].setEsperandoEnPasillo(aviones[j].contEsperandoEnPasillo, j);
				e[i].setEsperandoEntrar(aviones[j].contEsperandoEntrar, j);
				e[i].setGuardandoMaleta(aviones[j].contGuardandoMaleta, j);
				e[i].setEsperandoSentar(aviones[j].contEsperandoSentarse, j);
				e[i].setSentado(aviones[j].contSentado, j);
				e[i].setTiempoTotalSimple(aviones[j].tiempoTranscurrido, j);
				if(aviones[j].simulacionGanadora) {
					e[i].setSimulacionGanadoraIndex(j);	
				}
				
				
			}
			
			
		}
		
		long todoElTiempo = 0;
		
		for (Estadisticas es : e) {
//			todoElTiempo += es.getTodoElTiempo();	
			es.print();
		}
		
		for (int i = 0; i < numAviones; i++) {

			System.out.println(
					"Avion: " + (i + 1) + "==================================================================");
			System.out.println("Algoritmo " + aviones[i].nombreDeAlgoritmoUtilizado);
			int total = aviones[i].totalTiempoHombre();
			System.out.println("Total: " + total);
			System.out.println("Esperando entrar al avion: " + aviones[i].contEsperandoEntrar + " "
					+ String.format("%.0f%%", 100.0 * aviones[i].contEsperandoEntrar / total));
			System.out.println("Esperando en pasillo bloqueado: " + aviones[i].contEsperandoEnPasillo + " "
					+ String.format("%.0f%%", 100.0 * aviones[i].contEsperandoEnPasillo / total));
			System.out.println("Caminando en el pasillo del avion: " + aviones[i].contCaminando + " "
					+ String.format("%.0f%%", 100.0 * aviones[i].contCaminando / total));
			System.out.println("Subiendo maleta: " + aviones[i].contGuardandoMaleta + " "
					+ String.format("%.0f%%", 100.0 * aviones[i].contGuardandoMaleta / total));
			System.out.println("Esperando sentarse: " + aviones[i].contEsperandoSentarse + " "
					+ String.format("%.0f%%", 100.0 * aviones[i].contEsperandoSentarse / total));
			System.out.println("Sentado: " + aviones[i].contSentado + " "
					+ String.format("%.0f%%", 100.0 * aviones[i].contSentado / total));
			System.out.println("Pasajeros sentados: " + aviones[i].pasajerosSentados);
			System.out.println("Pasajeros por ingresar: " + aviones[i].pasajeroPorIngresar);

			System.out
					.println("=======================================================================================");
			System.out.println();
		}
		
	}
	
	public static void main(String[] args) {

		correrSimulacionGrafica();
		//getStatistics();

		

	}

}
