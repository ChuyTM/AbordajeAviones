package algos;

import drawings.StdDraw;

public enum Asiento {
	
	
		OCUPADO,
		DISPONIBLE,
		BLOQUEADO,
		PASILLO_OCUPADO,
		PASILLO_DISPONIBLE;
		
	
	private Pasajero p;
	
	void recibirPasajero(Pasajero p) {
		this.p = p;
		
	}
	
	
}
