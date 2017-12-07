package utils;

public class Estadisticas {
	private int numeroDeSimulacion;
	private int N;
	private int numAviones;
	private int numPas;

	private int filasAvion;
	private String[] nombresDeAlgoritmos;
	
	private int[] esperandoEntrar;
	private int[] esperandoEnPasillo;
	private int[] caminando;
	private int[] guardandoMaleta;
	private int[] esperandoSentar;
	private int[] sentado;
	private int[] tiempoTotalSimple;
	
	private int simulacionGanadoraIndex;

	public Estadisticas(int numeroDeSimulacion, int n, int filasAvion, int numPass, int numAviones) {
		super();
		this.numeroDeSimulacion = numeroDeSimulacion;
		N = n;
		this.filasAvion = filasAvion;
		this.numAviones = numAviones;
		this.numPas = numPass;
		
		nombresDeAlgoritmos = new String[numAviones];
		esperandoEntrar = new int[numAviones];
		esperandoEnPasillo = new int[numAviones];
		caminando = new int[numAviones];
		guardandoMaleta = new int[numAviones];
		esperandoSentar = new int[numAviones];
		sentado = new int[numAviones];
		tiempoTotalSimple = new int[numAviones];
		
		
	}
	
	public int getNumeroDeSimulacion() {
		return numeroDeSimulacion;
	}

	public void setNumeroDeSimulacion(int numeroDeSimulacion) {
		this.numeroDeSimulacion = numeroDeSimulacion;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}
	
	public int getNumAviones() {
		return numAviones;
	}

	public void setNumAviones(int numAviones) {
		this.numAviones = numAviones;
	}
	
	public int getFilasAvion() {
		return filasAvion;
	}

	public void setFilasAvion(int filasAvion) {
		this.filasAvion = filasAvion;
	}

	public String[] getNombresDeAlgoritmos() {
		return nombresDeAlgoritmos;
	}

	public void setNombresDeAlgoritmos(String nombreDeAlgoritmo, int index) {
		this.nombresDeAlgoritmos[index] = nombreDeAlgoritmo;
	}

	public int[] getEsperandoEntrar() {
		return esperandoEntrar;
	}

	public void setEsperandoEntrar(int esperandoEntrar, int index) {
		this.esperandoEntrar[index] = esperandoEntrar;
	}

	public int[] getEsperandoEnPasillo() {
		return esperandoEnPasillo;
	}

	public void setEsperandoEnPasillo(int esperandoEnPasillo, int index) {
		this.esperandoEnPasillo[index] = esperandoEnPasillo;
	}

	public int[] getCaminando() {
		return caminando;
	}

	public void setCaminando(int caminando, int index) {
		this.caminando[index] = caminando;
	}

	public int[] getGuardandoMaleta() {
		return guardandoMaleta;
	}

	public void setGuardandoMaleta(int guardandoMaleta, int index) {
		this.guardandoMaleta[index] = guardandoMaleta;
	}

	public int[] getEsperandoSentar() {
		return esperandoSentar;
	}

	public void setEsperandoSentar(int esperandoSentar, int index) {
		this.esperandoSentar[index] = esperandoSentar;
	}

	public int[] getSentado() {
		return sentado;
	}

	public void setSentado(int sentado, int index) {
		this.sentado[index] = sentado;
	}

	public int[] getTiempoTotalSimple() {
		return tiempoTotalSimple;
	}

	public void setTiempoTotalSimple(int tiempoTotalSimple, int index) {
		this.tiempoTotalSimple[index] = tiempoTotalSimple;
	}

	public int getSimulacionGanadoraIndex() {
		return simulacionGanadoraIndex;
	}

	public void setSimulacionGanadoraIndex(int simulacionGanadoraIndex) {
		this.simulacionGanadoraIndex = simulacionGanadoraIndex;
	}

	public int getTiempoTotal(int index) {
		return esperandoEntrar[index] + esperandoEnPasillo[index] + caminando[index] + guardandoMaleta[index] + esperandoSentar[index] + sentado[index];
	}
	
	public int getTodoElTiempo() {
		int todoElTiempo = 0;
		for(int i = 0; i < numAviones; i++) {
			todoElTiempo += getTiempoTotal(i);	
		}
		return todoElTiempo;
	}
	
	public void print() {
		String result;
		System.out.println("Numero de simulacion, N, numAviones, Numero de pasajeros, filas de avion, "
				+ "nombre de algoritmo, tiempo total, esperando entrar, esperando en pasillo, caminando, "
				+ "guardando maleta, esperando a sentarse, sentado, ganador(si/no)");
		for(int i = 0; i<numAviones;i++) {
			System.out.printf("%d, %d, %d, %d, %d, %s, %d, %d,  %d, %d, %d, %d, %d, %s\n", 
					numeroDeSimulacion, N, numAviones, numPas, filasAvion, nombresDeAlgoritmos[i], tiempoTotalSimple[i], 
					esperandoEntrar[i], esperandoEnPasillo[i], caminando[i], guardandoMaleta[i], esperandoSentar[i], sentado[i], simulacionGanadoraIndex==i?"Yes":"No");
		}
		
		
	}
	
}
