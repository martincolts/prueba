package com.operativa.simulator.datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("deprecation")
public class DBValorIndicador {

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public DBValorIndicador() {

	}

	public Date getMinDate() {
		DBManager dbM = DBManager.instance();
		ResultSet rs = dbM
				.executeQuery("SELECT MIN(Fecha) AS FechaMin FROM ValorIndicador");
		Date result = null;
		result = null;
		try {
			rs.next();
			Date res = rs.getDate("FechaMin");
			result = new Date(res.getYear() + 1900, res.getMonth() + 1,
					res.getDate());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public Date getMaxDate() {
		DBManager dbM = DBManager.instance();
		ResultSet rs = dbM
				.executeQuery("SELECT MAX(Fecha) AS FechaMax FROM ValorIndicador");
		Date result = null;
		result = null;
		try {
			rs.next();
			Date res = rs.getDate("FechaMax");
			result = new Date(res.getYear() + 1900, res.getMonth() + 1,
					res.getDate());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public Date getMinDate(int idIndicador) {
		DBManager dbM = DBManager.instance();
		ResultSet rs = dbM
				.executeQuery("SELECT MIN(Fecha) AS FechaMin FROM ValorIndicador WHERE IdIndicador = "
						+ idIndicador);
		Date result = null;
		result = null;
		try {
			rs.next();
			Date res = rs.getDate("FechaMin");
			result = new Date(res.getYear(), res.getMonth(),
					res.getDate());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public Date getMaxDate(int idIndicador) {
		DBManager dbM = DBManager.instance();
		ResultSet rs = dbM
				.executeQuery("SELECT MAX(Fecha) AS FechaMax FROM ValorIndicador WHERE IdIndicador = "
						+ idIndicador);
		Date result = null;
		result = null;
		try {
			rs.next();
			Date res = rs.getDate("FechaMax");
			result = new Date(res.getYear(), res.getMonth(),
					res.getDate());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public List<IndicatorEntry> getRowsById(int id) {
		DBManager dbM = DBManager.instance();
		ResultSet rs = dbM
				.executeQuery("SELECT IdIndicador, Fecha, Valor FROM ValorIndicador WHERE IdIndicador = "
						+ Integer.toString(id));
		List<IndicatorEntry> result = new LinkedList<IndicatorEntry>();
		try {
			while (rs.next()) {
				Date res = rs.getDate("Fecha");
				Date date = new Date(res.getYear(),
						res.getMonth(), res.getDate());
				IndicatorEntry entry = new IndicatorEntry(
						rs.getInt("IdIndicador"), rs.getFloat("Valor"), date,
						"");
				result.add(entry);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<IndicatorEntry> getRowsById(int id, Date startDate, Date endDate) {//devuelve las fechas en el periodo, y tambien la que le sigue a endDate (tomando el promedio de valores)
		DBManager dbM = DBManager.instance();
		String sd = formatter.format(startDate);
		String ed = formatter.format(endDate);
		ResultSet rs = dbM
				.executeQuery("SELECT IdIndicador, Fecha, Valor FROM ValorIndicador WHERE IdIndicador = "
						+ Integer.toString(id)
						+ " AND "
						+ "Fecha >= "
						+ "#"
						+ sd
						+ "#"
						+ " AND Fecha <= "
						+ "#"
						+ ed + "#");
		
		ResultSet rs1 = dbM
				.executeQuery("SELECT TOP 1 IdIndicador, Fecha, AVG(Valor) As Val FROM ValorIndicador WHERE IdIndicador = "
						+ Integer.toString(id)
						+ " AND "
						+ "Fecha > "
						+ "#"
						+ ed
						+ "#" + " Group BY Fecha , IdIndicador ");
		
		List<IndicatorEntry> result = new LinkedList<IndicatorEntry>();
		try {
			while (rs.next()) {
				Date res = rs.getDate("Fecha");
				Date date = new Date(res.getYear(),
						res.getMonth(), res.getDate());
				IndicatorEntry entry = new IndicatorEntry(
						rs.getInt("IdIndicador"), rs.getFloat("Valor"), date,
						"");
				result.add(entry);
			}
			
			
			Date date2;
			int id2;
			float valor2;
			
			if (rs1.next()) { 
				Date res2 = rs1.getDate("Fecha");
				date2 = new Date(res2.getYear(),
						res2.getMonth(), res2.getDate());
				id2 = rs1.getInt("IdIndicador");
				valor2 = rs1.getFloat("Val");
			} 
			else {
				date2 = new Date (endDate.getYear()+1, endDate.getMonth(), endDate.getDate());
				id2 = id ;
				valor2 = 0 ;
			}
			IndicatorEntry entry = new IndicatorEntry(id2, valor2, date2,"");
			result.add(entry);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	

	public void updateIndicatorStates(List<IndicatorEntry> indicators) {
		DBManager dbM = DBManager.instance();
		for (IndicatorEntry entry : indicators) {
			dbM.executeUpdate("UPDATE ValorIndicador SET Estado = " + "'"
					+ entry.getState() + "'" + " WHERE IdIndicador = "
					+ Integer.toString(entry.getId()) + " AND Fecha = " + "#"
					+ entry.getDate().toString() + "#"
					+ "AND Valor = " + entry.getValue());
		}
	}
	
	
	public List<SimEntry> getRowsSim(int id, Date startDate, Date endDate) {
		DBManager dbM = DBManager.instance();
		String sd = formatter.format(startDate);
		String ed = formatter.format(endDate);
		ResultSet rs = dbM
				.executeQuery("SELECT Count(valor) AS Cantidad, Valor FROM ValorIndicador WHERE IdIndicador = "
						+ Integer.toString(id)
						+ " AND "
						+ "Fecha >= "
						+ "#"
						+ sd
						+ "#"
						+ " AND Fecha <= "
						+ "#"
						+ ed + "#"
						+ "GROUP BY valor");
		List<SimEntry> result = new LinkedList<SimEntry>();
		try {
			while (rs.next()) {
				SimEntry entry = new SimEntry(
						rs.getInt("Cantidad"), rs.getFloat("Valor"));
						
				result.add(entry);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println (result.size());
		return result;
	}

	public List<IndicatorEntry> getOriginFechaValor(int id, Date startDate, 
			Date endDate) {//devuelve las fechas con el promedio de valores en el periodo
		DBManager dbM = DBManager.instance();
		String sd = formatter.format(startDate);
		String ed = formatter.format(endDate);
		ResultSet rs = dbM
				.executeQuery("SELECT IdIndicador, Fecha, AVG(Valor) as Val FROM ValorIndicador WHERE IdIndicador = "
						+ Integer.toString(id)
						+ " AND "
						+ "Fecha >= "
						+ "#"
						+ sd
						+ "#"
						+ " AND Fecha <= "
						+ "#"
						+ ed + "#" + "Group BY Fecha , IdIndicador");
		List<IndicatorEntry> result = new LinkedList<IndicatorEntry>();

		try {
			while (rs.next()) {
				Date res = rs.getDate("Fecha");
				Date date = new Date(res.getYear(),
						res.getMonth(), res.getDate());
				IndicatorEntry entry = new IndicatorEntry(
						rs.getInt("IdIndicador"), rs.getFloat("Val"), date,
						"");
				result.add(entry);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	
}
