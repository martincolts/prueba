package com.operativa.simulator.datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.LocalDate;

import com.operativa.simulator.datamodel.DBManager;


public class DBSimulacion {

	public DBSimulacion(){
		
	}
	
	public void insert (int idIndicador, float res,LocalDate startDate ,LocalDate endDate, LocalDate fecha, int nroCorr, int tipo) {
		

			DBManager dbM = DBManager.instance();
			dbM.insert("INSERT INTO Simulacion(IdIndicador, FechaDesde, FechaHasta, Resultado, Fecha, NroCorrida, TipoSimulacion) VALUES("
					+ Integer.toString(idIndicador)
					+ ","
					+ "#"
					+  startDate.toString()
					+ "#"
					+ ","
					+ "#"
					+  endDate.toString()
					+ "#"
					+ ","
					+ Float.toString(res)
					+ ","
					+ "#"
					+ fecha.toString()
					+ "#"
					+ ","
					+ Integer.toString(nroCorr) 
					+ ","
					+ Integer.toString(tipo)+ ")");
		}

	public int getNroCorrida(int id ,LocalDate fecha) {
		// TODO Auto-generated method stub
		DBManager dbM = DBManager.instance();
		ResultSet rs = dbM
				.executeQuery("SELECT Count(IdIndicador) AS cant FROM Simulacion WHERE IdIndicador = " + Integer.toString(id) 
								+ " AND Fecha = " 
								+ "#"
								+ fecha.toString()
								+"#");
		int result = 0;
		try {
			rs.next();
			result = rs.getInt("cant");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}

	public Integer getNroCorrida() {
		DBManager dbM = DBManager.instance();
		ResultSet rs = dbM
				.executeQuery("SELECT Count(*) AS cant FROM (SELECT DISTINCT NroCorrida,Count( Nrocorrida) AS cant FROM Simulacion group by NroCorrida)");
		int result = 0;
		try {
			rs.next();
			result = rs.getInt("cant");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	}

