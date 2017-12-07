package algos;

import java.awt.Font;
import java.util.Arrays;
import java.util.Random;


import drawings.StdDraw;


public class Plane {
	double initialX;
	double initialY;
	double xScale;
	double yScale;
	double espacioPorAvionX;
	double espacioPorAvionY;
	
	String nombreDeAlgoritmoUtilizado= "";
	int filas = 32;
	int columnasPorLado = 3;
	int columnasTotales = columnasPorLado *2 + 1;
	int POSICION_PASILLO = columnasPorLado;
	int numPas;
	int pasajerosSentados = 0;
	boolean simulacionTerminada = false;
	Asiento[][] asientos;
	int[][] seatReservation;
	int[] ordenPasajeros;
	Pasajero[] pasajeros;
	Pasajero[][] mapPosition;
	
	int tiempoTranscurrido = 0;
	boolean simulacionGanadora = false;
	boolean aLaCabeza = false;
	
	
	int contCaminando = 0;
	int contGuardandoMaleta = 0;
	int contEsperandoEnPasillo = 0;
	int contSentado = 0;
	int contEsperandoSentarse = 0;
	int contEsperandoEntrar = 0;
	
	
	int pasajeroPorIngresar = 0;
	boolean randomize = false;
	
	public Plane(int f, int c) {
		
		this.filas = f + 2;
		this.columnasTotales = (c * 2) + 1;
		numPas = f * (c * 2);
		asientos = new Asiento[filas][columnasTotales];
		mapPosition = new Pasajero[filas][columnasTotales];
		initAsientos();
		ordenPasajeros = new int[numPas];
		seatReservation = new int[this.filas][this.columnasTotales];
	}
	
	public void setPasajeros(Pasajero[] pasajeros) {
		//TODO make a real copy of this array
		this.pasajeros = pasajeros;
	}
	
	public void setNombreDeAlgoritmo(String s) {
		this.nombreDeAlgoritmoUtilizado = s;
	}
	
	public void setSeatReservation(int[][] seatReservation) {
		this.seatReservation = seatReservation;
	}
	
	public int totalTiempoHombre() {
		return contEsperandoEntrar + contEsperandoEnPasillo + contCaminando + contGuardandoMaleta + contEsperandoSentarse + contSentado;
	}
	
	
	
	private void initAsientos() {
		
		for(int y = 0; y < asientos[0].length; y++) {
			
			
			for(int x = 0; x < asientos.length; x++) {
				if(y==POSICION_PASILLO || x == 0 || x == filas-1)
					asientos[x][y] = Asiento.PASILLO_DISPONIBLE;
				else 
					asientos[x][y] = Asiento.DISPONIBLE; 
			}
		}
		
		//Imprimir asientos finales
		
//		for(int i = 0; i < asientos.length; i++) {
//			System.out.println("Fila: " + i + Arrays.toString(asientos[i]));
//		}
		
	}
	
    
	public void setCoordinates(double x, double y, double xScale, double yScale, double espacioPorAvionX, double espacioPorAvionY) {
		this.initialX = x;
		this.initialY = y;
		this.xScale = xScale;
		this.yScale = yScale;
		this.espacioPorAvionX = espacioPorAvionX;
		this.espacioPorAvionY = espacioPorAvionY;
	}
	
	//dibujar avion
	
    public  void draw() {
    	StdDraw.setXscale(0, xScale);
        StdDraw.setYscale(0, yScale);
        StdDraw.setPenColor(StdDraw.BLACK);
        double xAdjustment = initialX - espacioPorAvionX/2 + ((8.0/48.0)*espacioPorAvionX);
        double yAdjustment = 13 + initialY - espacioPorAvionY/2;
//        double xAdjustment = 6.5 + initialX - espacioPorAvionX/2;
//        double yAdjustment = 19.2 + initialY - espacioPorAvionY/2;
//        
        double scaleX = espacioPorAvionX;//48
        double scaleY = 20;//22
        StdDraw.picture(initialX, initialY, "plane.png", scaleX, scaleY);
//        StdDraw.picture(0, 0, "plane.png", scaleX, scaleY);
//        StdDraw.picture(5, 5, "plane.png", scaleX, scaleY);
//        StdDraw.picture(10, 10, "plane.png", scaleX, scaleY);
//        StdDraw.picture(20, 20, "plane.png", scaleX, scaleY);
//        StdDraw.picture(0.5, 0.5, "plane.png", scaleX, scaleY);
//        StdDraw.picture(50, 50, "plane.png", scaleX, scaleY);
//        StdDraw.picture(100, 100, "plane.png", scaleX, scaleY);
//        StdDraw.picture(200, 200, "plane.png", scaleX, scaleY);
        
        
        
//        for(int i = 0; i < FILAS; i++) {
//			System.out.println(Arrays.toString(asientos[i]));
//		}
              
		for(int y = 0; y < asientos[0].length; y++) {
			
			
			
			for(int x = 0; x < asientos.length; x++) {
					double xPos = x + xAdjustment;
					double yPos = y + yAdjustment;
					switch (asientos[x][y]) {
					case DISPONIBLE:
						StdDraw.setPenColor(StdDraw.BLACK);
						StdDraw.rectangle(xPos, yPos, .5, .5);
						break;
					case BLOQUEADO:
						StdDraw.setPenColor(StdDraw.YELLOW);
						StdDraw.filledRectangle(xPos, yPos, .5, .5);
						StdDraw.setPenColor(StdDraw.RED);
						StdDraw.filledCircle(xPos, yPos, .5);
						break;
					case OCUPADO:
						StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
						StdDraw.filledRectangle(xPos, yPos, .5, .5);
						break;
					case PASILLO_DISPONIBLE:
						
						break;
					case PASILLO_OCUPADO:
						if(mapPosition[x][y].getStatus()==PasStatus.GUARDANDO_MALETA) {
							StdDraw.setPenColor(StdDraw.RED);
							StdDraw.filledCircle(xPos, yPos, .5);
						} else {
							StdDraw.setPenColor(StdDraw.GREEN);
							StdDraw.filledCircle(xPos, yPos, .5);
						}
						
						break;
					
					}
				
				
				

			}
		}
		Font font = new Font("Arial", Font.BOLD, 8);
		StdDraw.setFont(font);
		StdDraw.setPenColor(StdDraw.BLACK);

		for(int i = 1; i < filas-1; i++)
			if (i % 2 == 1)
				StdDraw.text(i + 1.5 + xAdjustment, 7.5 + yAdjustment, Integer.toString(i));
			else
				StdDraw.text(i + 1.5 + xAdjustment, -1.2 + yAdjustment, Integer.toString(i));
		
		StdDraw.text(  xAdjustment - 1, yAdjustment, "A");
		StdDraw.text(  xAdjustment - 1, 1 + yAdjustment, "B");
		StdDraw.text(  xAdjustment - 1, 2 + yAdjustment, "C");
		StdDraw.text(  xAdjustment - 1, 4 + yAdjustment, "D");
		StdDraw.text(  xAdjustment - 1, 5 + yAdjustment, "E");
		StdDraw.text(  xAdjustment - 1, 6 + yAdjustment, "F");
		
		font = new Font("Arial", Font.BOLD, 20);
		
		if(aLaCabeza) {
			
			StdDraw.setPenColor(StdDraw.GREEN);
			aLaCabeza = false;
			
		} else {
			StdDraw.setPenColor(StdDraw.BLACK);
			
		}
		StdDraw.setFont(font);
		
		StdDraw.text(initialX, initialY - espacioPorAvionY/2 + 5, "Tiempo: " + tiempoTranscurrido + " s");
		StdDraw.text(initialX, initialY - espacioPorAvionY/2 + 3, "" + String.format("%.0f%% completado", ((double)pasajerosSentados/numPas)*100));
		
		
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(initialX, initialY + 13, nombreDeAlgoritmoUtilizado);
		
//		int j = 0;
//		font = new Font("Arial", Font.BOLD, 10);
//		StdDraw.setFont(font);
//		
//		for(int i = (int) (initialY - espacioPorAvionY/2); i < (initialY + espacioPorAvionY/2 ); i++) {
//			StdDraw.text(1, i, "" + j);
//			j++;
//		}
//		
//		j = 0;
//		for(int i = (int) (initialX - espacioPorAvionX/2); i < (initialX + espacioPorAvionX/2 ); i++) {
//			StdDraw.text(i, 1, "" + j);
//			j++;
//		}
		
		
	}
    
    public void clear() {
    	StdDraw.clear();
    }
    
    public boolean ingresarPasajero(Pasajero pasajero) {
    	if( asientos[0][0] == Asiento.PASILLO_DISPONIBLE) {
    		asientos[0][0].recibirPasajero(pasajero);
    		asientos[0][0] = Asiento.PASILLO_OCUPADO;
    		pasajero.setStatus(PasStatus.CAMINANDO);
    		pasajero.setUbicacion(LocStatus.EN_PASILLO_ENTRADA);
    		pasajero.setPosition(0, 0);
    		mapPosition[0][0] = pasajero;
    		return true;
    	}
    	return false;
    	
    }
    
    
    //
    //
    //Algoritmos de abordaje
    //
    //
    
    //backToFront
    
    void backToFront() {
    	nombreDeAlgoritmoUtilizado = "Atras hacia adelante";
		int numP = 0;
		
		for(int i = this.filas-2; i > 0; i--) {
			for(int j = 0; j < this.columnasTotales; j++) {
				if(j == 3)
					continue;
				
				
				
				ordenPasajeros[numP] = seatReservation[i][j];
				
				
				
				numP++;
				
			}
			
		}
		
		
		if(randomize) {
			shuffleOrder(ordenPasajeros, 0, (numPas/3)-1);
			shuffleOrder(ordenPasajeros, (numPas/3), (2*numPas/3)-1);
			shuffleOrder(ordenPasajeros, (2*numPas/3), numPas-1);
			
		}
	}
    
//Front to back
    
    void frontToBack() {
		
		int numP = 0;
		
		nombreDeAlgoritmoUtilizado = "Adelante hacia atrás";
		
		for(int i = 1; i < this.filas-1; i++) {
			for(int j = 0; j < this.columnasTotales; j++) {
				if(j == 3)
					continue;
				
				
				
				ordenPasajeros[numP] = seatReservation[i][j];
				
				
				
				numP++;
				
			}
			
		}
		
		if(randomize) {
			shuffleOrder(ordenPasajeros, 0, (numPas/3)-1);
			shuffleOrder(ordenPasajeros, (numPas/3), (2*numPas/3)-1);
			shuffleOrder(ordenPasajeros, (2*numPas/3), numPas-1);
			
		}
		
	}
    
    //Random method
    
    void randomMethod() {
    	nombreDeAlgoritmoUtilizado = "Abordaje aleatorio";
    	Random r = new Random();
    	int numP = 0;
    	boolean[][] visited = new boolean[filas][columnasTotales];
    	while (numP < this.numPas) {
			int i = r.nextInt(filas-1 - 1) + 1;
			int j = r.nextInt(columnasTotales - 0) + 0;
			
			if(j == 3)
				continue;
			if(visited[i][j])
				continue;
			
			ordenPasajeros[numP++] = seatReservation[i][j];
			visited[i][j] = true;
			//System.out.println(numP);
		}
    }
    
    //SteffenMethod
    
    void SteffenMethod(){
    	int numP = 0;
    	int paresNones = 0;
    	
    	nombreDeAlgoritmoUtilizado = "Metodo Stephen 2";
    	
    	for (int i = 0; i < 6; i ++) {
    		for (int j = filas-2; j > 0; j--) {
    			int k = i /2;
    			if(j % 2 == paresNones) {
    			
					ordenPasajeros[numP++] = seatReservation[j][k];
					ordenPasajeros[numP] = seatReservation[j][columnasTotales-1-k];
					
					numP++;
    			}
    		}
    		if(paresNones == 0)
    			paresNones = 1;
    		else 
    			paresNones = 0;
    	}
    	
    	
		if(randomize) {
			shuffleOrder(ordenPasajeros, 0, (numPas/6)-1);
			shuffleOrder(ordenPasajeros, (numPas/6), (2*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (2*numPas/6), (3*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (3*numPas/6), (4*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (4*numPas/6), (5*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (5*numPas/6), numPas-1);
			
		}
    	
    	//Imprimir todos los valores:
    	
//    	for(int i = 0; i < 180; i++) {
//    		System.out.println(Arrays.toString(ordenPasajeros));
//    	}
    	
    }
    
    void originalSteffenMethod(){
    	int numP = 0;
    	int paresNones = 0;
    	
    	nombreDeAlgoritmoUtilizado = "Metodo Stephen original";
    	
    	for (int i = 0; i < 6; i ++) {
    		for (int j = filas-2; j > 0; j--) {
    			int k = i /2;
    			if(j % 2 == paresNones) {
    			
					ordenPasajeros[numP++] = seatReservation[j][k];
					
					
					
    			}
    		}
    		
    		for (int j2 = filas-2; j2 > 0; j2--) {
    			int k = i /2;
    			if(j2 % 2 == paresNones) {
    			
					
					ordenPasajeros[numP++] = seatReservation[j2][columnasTotales-1-k];
					
					
    			}
    		}
    		if(paresNones == 0)
    			paresNones = 1;
    		else 
    			paresNones = 0;
    	}
    	
//    	//Imprimir todos los valores:
//    	
//    	for(int i = 0; i < 180; i++) {
//    		System.out.println(Arrays.toString(ordenPasajeros));
//    	}
    	
    	if(randomize) {
    		shuffleOrder(ordenPasajeros, 0, (numPas/6)-1);
			shuffleOrder(ordenPasajeros, (numPas/6), (2*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (2*numPas/6), (3*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (3*numPas/6), (4*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (4*numPas/6), (5*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (5*numPas/6), numPas-1);
			
		}
    	
    }
    
    //Improved steffen method
    
    void improvedSteffenMethod(){
    	int numP = 0;
    	int paresNones = 0;
    	
    	nombreDeAlgoritmoUtilizado = "Metodo Stephen mejorado";
    	
    	for (int i = 0; i < 6; i ++) {
    		for (int j = filas-2; j > 0; j--) {
    			int k = i /2;
    			if(j % 2 == paresNones) {
        			
					
					if (j % 2 == 0) {
						ordenPasajeros[numP++] = seatReservation[j][k];
						ordenPasajeros[numP++] = seatReservation[j-1][columnasTotales-1-k];
					} else {
						ordenPasajeros[numP++] = seatReservation[j+1][columnasTotales-1-k];
						ordenPasajeros[numP++] = seatReservation[j][k];
					}
    			}
    		}
    		if(paresNones == 0)
    			paresNones = 1;
    		else 
    			paresNones = 0;
    		
    	
    	}
    	
    	//Imprimir todos los valores:
    	
//    	for(int i = 0; i < 180; i++) {
//    		System.out.println(Arrays.toString(ordenPasajeros));
//    	}
    	
    	if(randomize) {
    		shuffleOrder(ordenPasajeros, 0, (numPas/6)-1);
			shuffleOrder(ordenPasajeros, (numPas/6), (2*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (2*numPas/6), (3*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (3*numPas/6), (4*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (4*numPas/6), (5*numPas/6)-1);
			shuffleOrder(ordenPasajeros, (5*numPas/6), numPas-1);
			
		}
    	
    }
    
    
    //todo a los lados
    
    public void ventanasPrimero() {
    	int numP = 0;
    	int paresNones = 0;
    	
    	nombreDeAlgoritmoUtilizado = "Ventanillas primero";
    	
    	for (int i = 0; i < 3; i ++) {
    		for (int j = filas-2; j > 0; j--) {
    			
    			
        			ordenPasajeros[numP++] = seatReservation[j][i];
					ordenPasajeros[numP++] = seatReservation[j][columnasTotales-1-i];
					
			
    			    		
    		}
    		
    		
    	
    	}
    	
    	if(randomize) {
    		shuffleOrder(ordenPasajeros, 0, (numPas/3)-1);
			shuffleOrder(ordenPasajeros, (numPas/3), (2*numPas/3)-1);
			shuffleOrder(ordenPasajeros, (2*numPas/3), numPas-1);
			
		}
    	
    }
    
    public static void shuffleOrder(int[] arr, int start, int end) {
    	
    	Random r = new Random();
    	
    	for(int i = start; i < end-1; i++) {
    		int j = r.nextInt(end+1- i) + i;
    		int tmp = arr[i];
    		arr[i] = arr[j];
    		arr[j] = tmp;
    	}
    	
    	
    }
    
    
    
    
    
    
    public boolean estaOcupado(int x, int y) {
    	if(mapPosition[x][y] != null)
    		return true;
    	
    	return false;
    }
    
    public void setMapPosition(int x, int y, Pasajero p) {
    	mapPosition[x][y] = p;
    	
    }
    
    public void actualizarPosicion(int x, int y, Asiento a) {
    	asientos[x][y] = a;
    }
    
    public Pasajero getPasajero(int x, int y) {
    	return mapPosition[x][y];
    }
    
    public TipoLugar tipoDeLugar(int x, int y) {
    	if(asientos[x][y] == Asiento.BLOQUEADO || asientos[x][y] == Asiento.DISPONIBLE || asientos[x][y] == Asiento.OCUPADO)    	
    		return TipoLugar.ASIENTO;
    	else
    		return TipoLugar.PASILLO;
    }
    
    
    public void abordarPasajeros(int i) {
    	
		
		
			
			if (i%3 == 0 && pasajeroPorIngresar < numPas) {
								
				if(this.ingresarPasajero(pasajeros[ordenPasajeros[pasajeroPorIngresar]]) )
					pasajeroPorIngresar++;
				
			}
			
			for (Pasajero p : pasajeros) {
				p.checkStatus();
			}
			
			if(numPas == pasajerosSentados)
				simulacionTerminada = true;
			
			
			tiempoTranscurrido = i;
			
		
    }
    public static void test() {
		int[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13};
		System.out.println(Arrays.toString(arr));
		shuffleOrder(arr,0, 12);
		System.out.println(Arrays.toString(arr));
	}
    
    public static void main(String[] args) {
//		Plane p = new Plane(30, 3);
//		p.draw();
		test();
		//Imprimir asientos
		
//		for(int i = 0; i < p.filas; i++) {
//			System.out.println(Arrays.toString(p.asientos[i]));
//		}
		
	}
}
