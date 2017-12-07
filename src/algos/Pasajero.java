package algos;

import java.util.ArrayList;

public class Pasajero {
	

	Plane avion;
	int tiempoPorMaleta = 20;
	int asientoX;
	int asientoY;
	int direccionDeBusquedaLugarVacio = -1;
	boolean notificado = false;
	ArrayList<Pasajero> pasajerosBloqueandoLugar;
	ArrayList<Pasajero> pasajerosDetras;
	int xPos;
	int yPos;
	boolean maletaGuardada = false;
	boolean enLugarVacio = false;
	boolean hold = false;
	int contadorAsientoOcupado = 0;
	boolean pasajeroSentado = false;
	
	
	
	
	private PasStatus status;
	private LocStatus ubicacion;
	
	public Pasajero(Plane p, PasStatus status) {
		this.avion = p;
		this.status = status; 
	}
	
	public PasStatus getStatus() {
		return this.status;
	}
	
	public Pasajero clone(Plane a) {
		Pasajero p = new Pasajero(a, this.status);
		
		p.asientoX = this.asientoX;
		p.asientoY = this.asientoY;
		
		
		return p;
	}
	
	private boolean avanzar() {
		switch (ubicacion) {
		case EN_PASILLO_PRINCIPAL:
			
//			if(xPos<asientoX)
//				moverA(xPos, yPos, xPos + 1, yPos);
//			else if(xPos>asientoX)
//				moverA(xPos, yPos, xPos - 1, yPos);
//			
//			if(xPos == asientoX && !maletaGuardada) {
//				this.status = PasStatus.GUARDANDO_MALETA;
//			} else if(xPos == asientoX && maletaGuardada) {
//				status = PasStatus.SENTARSE;
//			}
//			
//			return true;
			
			if(avion.asientos[this.xPos + 1][this.yPos] == Asiento.PASILLO_DISPONIBLE && this.xPos + 1 <= this.asientoX) {
				avion.asientos[this.xPos + 1][this.yPos] = Asiento.PASILLO_OCUPADO;
				avion.asientos[this.xPos][this.yPos] = Asiento.PASILLO_DISPONIBLE;
				avion.mapPosition[this.xPos + 1][this.yPos] = this;
				avion.mapPosition[this.xPos][this.yPos] = null;
				
				this.xPos++;
				avion.contCaminando++;
				
				if(xPos == this.asientoX) {
					this.status = PasStatus.GUARDANDO_MALETA;
					
				}  
				
				return true;
			}
			break;
		case EN_PASILLO_ENTRADA:
			
			
//			moverA(xPos, yPos, xPos, yPos + 1);	
//			
//			if(yPos == 3)
//				ubicacion = LocStatus.EN_PASILLO_PRINCIPAL;
//			
//			return true;
			
			
			
			if(avion.asientos[this.xPos][this.yPos + 1] == Asiento.PASILLO_DISPONIBLE) {
				avion.asientos[this.xPos][this.yPos + 1] = Asiento.PASILLO_OCUPADO;
				avion.asientos[this.xPos][this.yPos] = Asiento.PASILLO_DISPONIBLE;
				avion.mapPosition[this.xPos][this.yPos + 1] = this;
				avion.mapPosition[this.xPos][this.yPos] = null;
				this.yPos++;
				avion.contCaminando++;
				
				if(yPos == 3)
					ubicacion = LocStatus.EN_PASILLO_PRINCIPAL;
				
				return true;
				

			}
			break;
		}
		avion.contEsperandoEnPasillo++;
		return false;
	}
	
	public void setPosition(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public void checkStatus() {
		switch (status) {
		case CAMINANDO:
			avanzar();
			break;
		case ESPERANDO:
			avion.contEsperandoEntrar++;
			break;
		case GUARDANDO_MALETA:
			if(tiempoPorMaleta > 0) {
				tiempoPorMaleta--;
				avion.contGuardandoMaleta++;
			}
			else
				this.status = PasStatus.PUEDE_SENTARSE;
			break;
		case DANDO_PASO:
			
			break;
		case PUEDE_SENTARSE:
			
			if(yPos < asientoY) {
				for(int i = avion.POSICION_PASILLO + 1; i < avion.columnasTotales; i++) {
					if(avion.estaOcupado(xPos, i) && i< asientoY) {
						contadorAsientoOcupado += 5;
						
					}
				}
			}
			else {
				for(int i = avion.POSICION_PASILLO - 1; i >= 0; i--) {
					if(avion.estaOcupado(xPos, i) && i> asientoY) {
						contadorAsientoOcupado += 5;
					}
				}
			}
			status = PasStatus.ESPERANDO_SENTARSE;
			break;
		case ESPERANDO_SENTARSE:
			if(contadorAsientoOcupado>0) {
				contadorAsientoOcupado--;
				avion.contEsperandoSentarse++;
			}
			else 
				status = PasStatus.SENTADO;
			break;
		case DEJAR_SENTAR:
			//buscarLugarVacio();
			break; 
		case SENTADO:
				if(yPos < asientoY) {
					
					
					
					avion.asientos[this.xPos][this.yPos + 1] = Asiento.BLOQUEADO;
					if(avion.POSICION_PASILLO == this.yPos)
						avion.asientos[this.xPos][this.yPos] = Asiento.PASILLO_DISPONIBLE;
					else
						avion.asientos[this.xPos][this.yPos] = Asiento.DISPONIBLE;
					this.yPos++;
					
				} else if(yPos > asientoY) {
					avion.asientos[this.xPos][this.yPos - 1] = Asiento.BLOQUEADO;
					if(avion.POSICION_PASILLO == this.yPos)
						avion.asientos[this.xPos][this.yPos] = Asiento.PASILLO_DISPONIBLE;
					else
						avion.asientos[this.xPos][this.yPos] = Asiento.DISPONIBLE;
					this.yPos--;
					
				} else {
					avion.asientos[this.xPos][this.yPos] = Asiento.OCUPADO;
					avion.setMapPosition(this.xPos, this.yPos, this);
					avion.contSentado++;
					if(!pasajeroSentado) {
						avion.pasajerosSentados++;
						pasajeroSentado = true;
					}
					
				}
				
				
					
			break;
		case SALIR_A_PASILLO:
			salirAPasillo();
			break;
		case VECINO_SENTADO:
			regresarALugar();
			break;
		case ESPERAR_NOTIFICACION:
			if(notificado) {
				status = PasStatus.VECINO_SENTADO;
				notificado = false;
			}
		}
	}
	
	public void sentarse() {
		puedeSentarse();
		pedirDejarSentar();
		buscarLugarVacio(-1);
		sentarse();
//		notificar();
//		notificarAAnteriores;
		
		
	}
	
	public boolean puedeSentarse() {
		pasajerosBloqueandoLugar = new ArrayList<Pasajero>();
		boolean result = true;
		
		if(yPos < asientoY) { //voy hacia columna 4 5 o 6
			for(int i = avion.POSICION_PASILLO + 1; i < avion.columnasTotales; i++ ) {
				if(avion.estaOcupado(xPos,  i) && i < asientoY) {
					pasajerosBloqueandoLugar.add(avion.getPasajero(xPos, i));
					result = false;
				}
			}

			
			
//			if(yPos + i == asientoY && !avion.estaOcupado(xPos, yPos + i)) {
//				avion.asientos[this.xPos][this.yPos + 1] = Asiento.OCUPADO;
//				avion.asientos[this.xPos][this.yPos] = Asiento.DISPONIBLE;
			
		} else { // voy hacia columna 0 1 o 2
			for(int i = avion.POSICION_PASILLO - 1; i >= 0; i-- ) {
				if(avion.estaOcupado(xPos,  i) && i > asientoY) {
					pasajerosBloqueandoLugar.add(avion.getPasajero(xPos, i));
					result = false;
				}
			}
		}
		return result;
	}
	
	
	public void pedirDejarSentar() {
		for(int i = 0; i < pasajerosBloqueandoLugar.size(); i++) {
			notificarSentado(pasajerosBloqueandoLugar.get(i));
		}
						
	}
	
	public void excuseMe() {
		salirAPasillo();
		buscarLugarVacio(1);
//		esperarNotificacion();
//		regresarALugar;
	}
	
	public boolean moverA(int xFrom, int yFrom, int xTo, int yTo) {
		TipoLugar from = avion.tipoDeLugar(xFrom, yFrom);
		TipoLugar to = avion.tipoDeLugar(xTo, yTo);
		boolean destinoOcupado = avion.estaOcupado(xTo, yTo);
		
		if(Math.abs(xFrom - xTo) > 1 || Math.abs(yFrom - yTo) > 1 ) {
			return false;
		}
		
		
		if(!destinoOcupado) {
			xPos = xTo;
			yPos = yTo;
			if (to == TipoLugar.ASIENTO) {
				if (from == TipoLugar.PASILLO) {
					avion.setMapPosition(xTo, yTo, this);
					avion.setMapPosition(xFrom, yFrom, null);
					avion.actualizarPosicion(xFrom, yFrom, Asiento.PASILLO_DISPONIBLE);
					avion.actualizarPosicion(xTo, yTo, Asiento.BLOQUEADO);
					ubicacion = LocStatus.EN_ASIENTO;
				} else if (from == TipoLugar.ASIENTO) {
					avion.setMapPosition(xTo, yTo, this);
					avion.setMapPosition(xFrom, yFrom, null);
					avion.actualizarPosicion(xFrom, yFrom, Asiento.DISPONIBLE);
					avion.actualizarPosicion(xTo, yTo, Asiento.BLOQUEADO);
					ubicacion = LocStatus.EN_ASIENTO;
				}
			} else if (to == TipoLugar.PASILLO) {
				if (from == TipoLugar.PASILLO) {
					avion.setMapPosition(xTo, yTo, this);
					avion.setMapPosition(xFrom, yFrom, null);
					avion.actualizarPosicion(xFrom, yFrom, Asiento.PASILLO_DISPONIBLE);
					avion.actualizarPosicion(xTo, yTo, Asiento.PASILLO_OCUPADO);
					ubicacion = LocStatus.EN_PASILLO_PRINCIPAL;
				} else if (from == TipoLugar.ASIENTO) {
					avion.setMapPosition(xTo, yTo, this);
					avion.setMapPosition(xFrom, yFrom, null);
					avion.actualizarPosicion(xFrom, yFrom, Asiento.DISPONIBLE);
					avion.actualizarPosicion(xTo, yTo, Asiento.PASILLO_OCUPADO);
					ubicacion = LocStatus.EN_PASILLO_PRINCIPAL;
				}
			}
			return true;
		}
		return false;
	}
	
	public void salirAPasillo() {
		if(yPos > avion.POSICION_PASILLO) {
			moverA(xPos, yPos, xPos, yPos - 1);		
		} else {
			moverA(xPos, yPos, xPos, yPos + 1);
		}
		
		if(avion.tipoDeLugar(xPos, yPos) == TipoLugar.PASILLO)
			status = PasStatus.DEJAR_SENTAR;
		
	}
	
	public boolean buscarLugarVacio(int direccion) {
		int i;
		
		if(moverA(xPos, yPos, xPos, 
				asientoY > avion.POSICION_PASILLO ? avion.POSICION_PASILLO - 1 : avion.POSICION_PASILLO + 1)) {
			enLugarVacio = true;
			hold = true;
			return true;
		}
		else if(moverA(xPos, yPos, xPos - (1 * direccion), yPos))
			return false;
		else 
			moverA(xPos, yPos, xPos + (1 * direccion) , yPos);
			return false;
		 
	
		
			
	}
	
	public void regresarALugar() {
		salirAPasillo();
		avanzar();
		
	}
	
//	public void desocuparONoMoverse(int n) {
//		if(int n > 0) {
//			if(avion.estaOcupado(xPos-1, yPos))
//				pasajerosDetras.add(avion.getPasajero(xPos-1, yPos));
////			pasajerosDetras
//		}
//	}
	
	public void notificarSentado(Pasajero p) {
		p.excuseMe();
	}
	
	public void estoySentado() {
		for(int i = 0; i < pasajerosBloqueandoLugar.size(); i++) {
			pasajerosBloqueandoLugar.get(i).hold = false;
			pasajerosBloqueandoLugar.get(i).regresarALugar();
		}
	}
	
	public void setStatus(PasStatus s) {
		this.status = s;
	}
	
	public void setUbicacion(LocStatus loc) {
		this.ubicacion = loc;
	}
	
	
	
}
