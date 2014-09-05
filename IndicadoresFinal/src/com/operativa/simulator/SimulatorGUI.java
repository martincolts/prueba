package com.operativa.simulator;

import org.jfree.chart.ChartUtilities;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.joda.time.LocalDate;

import com.operativa.properties.Parameters;
import com.operativa.properties.PropertyManager;
import com.operativa.simulator.datamodel.DBIndicador;
import com.operativa.simulator.datamodel.DBSimulacion;
import com.operativa.simulator.datamodel.DBValorIndicador;
import com.operativa.simulator.datamodel.IndicatorEntry;
import com.operativa.simulator.datamodel.SimEntry;
import com.operativa.simulator.datamodel.SimResult;
import com.toedter.calendar.JDateChooser;


@SuppressWarnings("rawtypes")
public class SimulatorGUI extends JFrame {

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			this.setTitle("Indicadores - Simulacion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */

	private static final long serialVersionUID = -3581904533316037480L;

	private JPanel panelPrincipal;
	private JPanel panelSimulacion;
	private JPanel panelLasVegas;
	private JPanel panelMuestra;
	private JComboBox comboBox;
	private JButton btnGuardar;
	private JButton btnGuardar2;
	private JButton btnGuardarGrafico;
	private JButton btnSimular;
	private JButton btnLasVegas;
	private JLabel lblIndicador;
	private JLabel lblFechaFinPeriodo;
	private JLabel lblFechaInicioPeriodo;
	
	private Date minDate;
	private Date maxDate;
	private JDateChooser jdc1;
	private JDateChooser jdc2;


	private JLabel labCant;
	private JScrollPane jScrollPaneMuestra;
	private JScrollPane jScrollPaneSim;
	private JScrollPane jScrollPaneLV;
	private JScrollPane jScrollPaneGr;

	
	private JTable tabPreviewSim;
	private JTable tabPreviewLV;
	private JTable tabPreviewMuestra;
	private TableModel tabModel ;
	private TableModel tabModelRes;
		
	private String[][] matrizSim ;
	private String[][] matrizLV ;
	private String[][] matrizMuestra ;

	XYLineChart grafico ;
	XYLineChartReales graficoReales ;
	private JPanel panelGrafico2;
	private JLabel labelGrafico2;
	private JTabbedPane jTabbedPane1;
		
	private int contLV ;
	
	private float sim;
	private float lv ;
	private String guarda ;
	private Vector<SimResult> simResultado;
	private Vector<SimResult> lvResultado;

	List<IndicatorEntry> lOriginal ;
	List<IndicatorEntry> lOriginal2 ;
	List<IndicatorEntry> lOriginalReal ;

	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulatorGUI frame = new SimulatorGUI();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					frame.setResizable(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked" })
	public SimulatorGUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, 630, 400);

		{
			jTabbedPane1 = new JTabbedPane();
			getContentPane().add(jTabbedPane1, BorderLayout.NORTH);
			jTabbedPane1.setPreferredSize(new java.awt.Dimension(837, 346));

			
			{		
				
				panelPrincipal = new JPanel();
				jTabbedPane1.addTab("Principal", null, panelPrincipal, null);
				panelPrincipal.setLayout(null);
				panelPrincipal.setPreferredSize(new java.awt.Dimension(748, 316));
				
				matrizSim = new String[1][5];
				matrizLV = new String[1][5];
				labelGrafico2 = new JLabel("");

				
				
				{// GUARDAR SIMULACION////////////////////////////////////////////////////////////////////////////////
					btnGuardar = new JButton();
					btnGuardar.setText("Guardar Simulación");
					btnGuardar.setEnabled(false);
					btnGuardar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							
							// Crear conecciones							
							DBSimulacion simu = new DBSimulacion();
							int año = jdc1.getCalendar().get(Calendar.YEAR);
							int mes = jdc1.getCalendar().get(Calendar.MONTH) + 1;
							int dia = jdc1.getCalendar().get(Calendar.DAY_OF_MONTH);
							LocalDate fDesde = new LocalDate(año, mes, dia);
							//
							LocalDate fHasta;
							LocalDate fecha ;
							//
							DBIndicador indicador = new DBIndicador();
							int id = indicador.getIdByName(comboBox.getSelectedItem().toString());
							for (SimResult i : simResultado ) {
								 Calendar calendar = Calendar.getInstance();
							        calendar.setTime(i.getFecha());
							        año = calendar.get(Calendar.YEAR);
							        mes = calendar.get(Calendar.MONTH)+1;
							        dia = calendar.get(Calendar.DAY_OF_MONTH);
							     fecha = new LocalDate (año, mes, dia);
							        calendar.setTime(i.getFin());
							        año = calendar.get(Calendar.YEAR);
							        mes = calendar.get(Calendar.MONTH)+1;
							        dia = calendar.get(Calendar.DAY_OF_MONTH);
							     fHasta = new LocalDate (año, mes, dia);   
								simu.insert(id, i.getResultado() , fDesde, fHasta, fecha, i.getNroCorrida(),1);
							}
							
							btnGuardar.setEnabled(false);
							btnGuardarGrafico.setEnabled(true);
							guarda = "sim";
							generarGraficos(lOriginal);

						}
					});
				}
				
				
				{
					btnGuardarGrafico = new JButton();
					btnGuardarGrafico.setText("Guardar Grafico");
					btnGuardarGrafico.setEnabled(false);
					btnGuardarGrafico.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								if (guarda == "sim")
									ChartUtilities.saveChartAsJPEG(new File("Grafico Nro "+simResultado.elementAt(1).getNroCorrida()+" - Montecarlo.jpg"), grafico.getChart(), simResultado.size()*35+100, 350);
								else
									ChartUtilities.saveChartAsJPEG(new File("Grafico Nro "+lvResultado.elementAt(1).getNroCorrida()+" - Las Vegas.jpg"), grafico.getChart(), lvResultado.size()*35+100, 350);
							} catch (IOException e) {
								e.printStackTrace();
							}					
							btnGuardarGrafico.setEnabled(false);
						}
					});
				}
				
				
				{
					btnGuardar2 = new JButton();
					btnGuardar2.setText("Guardar Las Vegas");
					btnGuardar2.setEnabled(false);
					btnGuardar2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							// Crear conecciones
//							
							DBSimulacion simu = new DBSimulacion();
							int año = jdc1.getCalendar().get(Calendar.YEAR);
							int mes = jdc1.getCalendar().get(Calendar.MONTH) + 1;
							int dia = jdc1.getCalendar().get(Calendar.DAY_OF_MONTH);
							LocalDate fDesde = new LocalDate(año, mes, dia);
							//
							LocalDate fHasta;
							LocalDate fecha ;
							//
							DBIndicador indicador = new DBIndicador();
							int id = indicador.getIdByName(comboBox.getSelectedItem().toString());
							for (SimResult i : lvResultado ) {
								 Calendar calendar = Calendar.getInstance();
							        calendar.setTime(i.getFecha());
							        año = calendar.get(Calendar.YEAR);
							        mes = calendar.get(Calendar.MONTH)+1;
							        dia = calendar.get(Calendar.DAY_OF_MONTH);
							     fecha = new LocalDate (año, mes, dia);
							        calendar.setTime(i.getFin());
							        año = calendar.get(Calendar.YEAR);
							        mes = calendar.get(Calendar.MONTH)+1;
							        dia = calendar.get(Calendar.DAY_OF_MONTH);
							     fHasta = new LocalDate (año, mes, dia);   
								simu.insert(id, i.getResultado() , fDesde, fHasta, fecha, i.getNroCorrida(),2);
							}
							
							btnGuardar2.setEnabled(false);
							btnGuardarGrafico.setEnabled(true);
							guarda = "lv";
							generarGraficosLV(lOriginal2);

						}
					});
				}
				
				
				{
					panelMuestra = new JPanel();
					jTabbedPane1.addTab("Datos Seleccionados", null, panelMuestra, null);
					FlowLayout panelSimulacionLayout = new FlowLayout();
					panelMuestra.setLayout(panelSimulacionLayout);
					{
						jScrollPaneMuestra = new JScrollPane();
						panelMuestra.add(jScrollPaneMuestra);
							
						jScrollPaneMuestra.setPreferredSize(new java.awt.Dimension(610,260));
							
						{
							tabModel = new DefaultTableModel(
									new String[][] { {} }, new String[] {"Nro Fecha","Fecha", "Valor"});
							tabPreviewMuestra = new JTable();
							jScrollPaneMuestra.setViewportView(tabPreviewMuestra);
							tabPreviewMuestra.setModel(tabModel);
						}
							
							labCant = new JLabel ();
							panelMuestra.add(labCant);
					}
				}
				
			
	
				{
					panelSimulacion = new JPanel();
					jTabbedPane1.addTab("Montecarlo", null, panelSimulacion,
							null);
					FlowLayout panelSimulacionLayout = new FlowLayout();
					panelSimulacion.setLayout(panelSimulacionLayout);
					{
						jScrollPaneSim = new JScrollPane();
						panelSimulacion.add(jScrollPaneSim);
						panelSimulacion.add(btnGuardar);
						
						jScrollPaneSim.setPreferredSize(new java.awt.Dimension(610,260));
						{
							tabModelRes = new DefaultTableModel(
									new String[][] { {} }, new String[] {"Id Indicador", "Periodo", "Valor Simulado", "Valor Real", "Fecha"});
							tabPreviewSim = new JTable();
							jScrollPaneSim.setViewportView(tabPreviewSim);
							tabPreviewSim.setModel(tabModelRes);
						}
					}
				}
				
				
				{
					panelLasVegas = new JPanel();
					jTabbedPane1.addTab("Las Vegas", null, panelLasVegas,null);
					FlowLayout panelSimulacionLayout = new FlowLayout();
					panelLasVegas.setLayout(panelSimulacionLayout);
					{
						jScrollPaneLV = new JScrollPane();
						panelLasVegas.add(jScrollPaneLV);
						panelLasVegas.add(btnGuardar2);
						
						jScrollPaneLV.setPreferredSize(new java.awt.Dimension(610,260));
						{
							tabModelRes = new DefaultTableModel(
									new String[][] { {} }, new String[] {"Id Indicador", "Periodo", "Valor Simulado", "Valor Real", "Fecha"});
							tabPreviewLV = new JTable();
							jScrollPaneLV.setViewportView(tabPreviewLV);
							tabPreviewLV.setModel(tabModelRes);
						}
					}
				}
			
				{ // muestra el grafico comparativo de las simulaciones 
					panelGrafico2 = new JPanel();
					jTabbedPane1.addTab("Grafica de puntos", null, panelGrafico2,null);
					jScrollPaneGr = new JScrollPane ();
					panelGrafico2.add(jScrollPaneGr);
					jScrollPaneGr.setPreferredSize(new java.awt.Dimension(610,260));
				}
			
				//Indicador
				lblIndicador = new JLabel("Indicador:");
				panelPrincipal.add(lblIndicador);
				lblIndicador.setBounds(30, 30, 62, 14);
				
				
				// Desplegable Indicadores 
				comboBox = new JComboBox();
				panelPrincipal.add(comboBox);
				comboBox.setBounds(100, 27, 270, 20);
				comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {

					DBIndicador indicador = new DBIndicador();

						// Cambiar la fecha inicio y fin segun el indicador
						DBValorIndicador vIndicador = new DBValorIndicador();
						minDate = vIndicador.getMinDate(indicador.getIdByName(comboBox.getSelectedItem().toString()));
						maxDate = vIndicador.getMaxDate(indicador.getIdByName(comboBox.getSelectedItem().toString()));
						jdc1.setDate(minDate);
						jdc2.setDate(maxDate);
						
						lOriginalReal = vIndicador.getOriginFechaValor(indicador.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), jdc2.getDate());
		        		previsualizar (lOriginalReal);
						generarGraficoReales(lOriginalReal);
						
					}
				});
				
				{
					lblFechaInicioPeriodo = new JLabel("Fecha inicio periodo:");
					panelPrincipal.add(lblFechaInicioPeriodo);
					lblFechaInicioPeriodo.setBounds(30, 79, 116, 14);
				}
				
				{
					lblFechaFinPeriodo = new JLabel("Fecha fin periodo:");
					panelPrincipal.add(lblFechaFinPeriodo);
					lblFechaFinPeriodo.setBounds(315, 79, 116, 14);
				}
				
				{
					
					jdc1 = new JDateChooser ();
					jdc1.setBounds(145, 75, 135, 25);
					jdc1.setEnabled(false);
					panelPrincipal.add(jdc1);
					
					jdc1.getDateEditor().addPropertyChangeListener(new PropertyChangeListener(){ 
				        public void propertyChange(PropertyChangeEvent e) {
				        	
				        	tabModel = new DefaultTableModel(new String[][] { {} }, new String[] {"Id Indicador", "Fecha", "Valor"});
				    		tabPreviewMuestra.setModel(tabModel);
				    		labCant.setText("");
				    		btnGuardarGrafico.setEnabled(false);
				    		
				        	if ( (jdc1.getDate() == null) || (jdc2.getDate() == null) ) {
				        		btnSimular.setEnabled(false);
				        		btnLasVegas.setEnabled(false);
				        	}
				        	else{
				        		btnSimular.setEnabled(true);
				        		btnLasVegas.setEnabled(true);
				        		
				        		DBIndicador indicador = new DBIndicador();
								DBValorIndicador vIndicador = new DBValorIndicador();
																
				        		lOriginalReal = vIndicador.getOriginFechaValor(indicador.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), jdc2.getDate());
				        		previsualizar (lOriginalReal);
				        		generarGraficoReales(lOriginalReal);
				        	}
				        }
					});
				}
				
				{
					
					jdc2 = new JDateChooser ();
					jdc2.setBounds(415, 75, 135, 25);
					jdc2.setEnabled(false);
					panelPrincipal.add(jdc2);
					
					jdc2.getDateEditor().addPropertyChangeListener(new PropertyChangeListener(){ 
				        public void propertyChange(PropertyChangeEvent e) {
				        	
				        	tabModel = new DefaultTableModel(new String[][] { {} }, new String[] {"Id Indicador", "Fecha", "Valor"});
				    		tabPreviewMuestra.setModel(tabModel);
				    		labCant.setText("");
				    		btnGuardarGrafico.setEnabled(false);
				        	
				        	if ( (jdc2.getDate() == null) || (jdc1.getDate() == null) ){
				        		btnSimular.setEnabled(false);
				        		btnLasVegas.setEnabled(false);
				        	}
				        	else{
				        		btnSimular.setEnabled(true);
				        		btnLasVegas.setEnabled(true);
				        						        		
				        		DBIndicador indicador = new DBIndicador();
								DBValorIndicador vIndicador = new DBValorIndicador();
																
								lOriginalReal = vIndicador.getOriginFechaValor(indicador.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), jdc2.getDate());
				        		previsualizar (lOriginalReal);
								generarGraficoReales(lOriginalReal);
							}

				        }
					});
					
				}
				
				
				{
					btnSimular = new JButton("");
					btnSimular.setBounds(150, 150, 90, 28);
					btnSimular.setEnabled(false);
					btnSimular.setText("Montecarlo");
					panelPrincipal.add(btnSimular);
					btnSimular.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
						
						if ( jdc1.getDate().after(jdc2.getDate()) ){
							final JDialog error = new JDialog ();
							error.setSize(400, 150);
							error.setLocationRelativeTo(null);
							error.setTitle ("ERROR");
							error.setModal(true);
							JLabel texto = new JLabel("Se debe cumplir: Fecha Inicio Periodo <= Fecha Fin Periodo", JLabel.CENTER);
							texto.setBounds(15, 50, 390, 14);
							error.add(texto);
							error.show();

						}
						else {
							
							DBIndicador indicador = new DBIndicador();
							DBValorIndicador vIndicador = new DBValorIndicador();
							
							List<IndicatorEntry> lista = vIndicador.getRowsById(indicador.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), jdc2.getDate());
							
							//lOriginal devuelve las fechas con el promedio de valores
							lOriginal = vIndicador.getOriginFechaValor(indicador.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), jdc2.getDate());
							Vector<Date> fechas = obtenerFechasSim (lista);
		
							DBSimulacion dbs = new DBSimulacion();
							simResultado = new Vector<SimResult>();
							
							if (fechas.size()>4){
								
								previsualizar (lista);
								
								for (int i=4; i<fechas.size()-1; i++) {
									List<SimEntry> listaSim = vIndicador.getRowsSim(indicador.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), fechas.elementAt(i));
									sim = new Montecarlo().simulator(listaSim);
									simResultado.add( new SimResult(jdc1.getDate(),fechas.elementAt(i),fechas.elementAt(i+1),indicador.getIdByName(comboBox.getSelectedItem().toString()),dbs.getNroCorrida(),sim) );
								}
								
								mostrarSimulacion (simResultado,lOriginal);
								jTabbedPane1.setSelectedIndex(2);
								generarGraficos(lOriginal);
								
								btnGuardar.setEnabled(true);
								btnSimular.setEnabled(false);
								
							}
							else{
								final JDialog error = new JDialog ();
								error.setSize(400, 150);
								error.setLocationRelativeTo(null);
								error.setTitle ("ERROR");
								error.setModal(true);
								JLabel texto = new JLabel("Por favor, elija un rango de fechas mas grande.", JLabel.CENTER);
								texto.setBounds(15, 50, 390, 14);
								error.add(texto);
								error.show();
							}
							
						}
					}});
					
				}	
				
				
				
				{
					btnLasVegas = new JButton("");
					btnLasVegas.setBounds(330, 150, 90, 28);
					btnLasVegas.setEnabled(false);
					btnLasVegas.setText("Las Vegas");
					panelPrincipal.add(btnLasVegas);
					btnLasVegas.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
						
						if ( jdc1.getDate().after(jdc2.getDate()) ){
							final JDialog error = new JDialog ();
							error.setSize(400, 150);
							error.setLocationRelativeTo(null);
							error.setTitle ("ERROR");
							error.setModal(true);
							JLabel texto = new JLabel("Se debe cumplir: Fecha Inicio Periodo <= Fecha Fin Periodo", JLabel.CENTER);
							texto.setBounds(15, 50, 390, 14);
							error.add(texto);
							error.show();

						}
						else {
							
							DBIndicador indicador2 = new DBIndicador();
							DBValorIndicador vIndicador2 = new DBValorIndicador();
							
							List<IndicatorEntry> lista = vIndicador2.getRowsById(indicador2.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), jdc2.getDate());
							
							//lOriginal devuelve las fechas con el promedio de valores
							lOriginal2 = vIndicador2.getOriginFechaValor(indicador2.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), jdc2.getDate());
							Vector<Date> fechas = obtenerFechasSim (lista);
							
							DBSimulacion dbs2 = new DBSimulacion();
							lvResultado = new Vector<SimResult>();
							
							contLV = 0  ;
							
							if (fechas.size()>4) {
								
								previsualizar (lista);
								
								for (int i=4; i<fechas.size()-1; i++) {
									List<SimEntry> listaLv = vIndicador2.getRowsSim(indicador2.getIdByName(comboBox.getSelectedItem().toString()), jdc1.getDate(), fechas.elementAt(i));
									lv = new LasVegas().lasVegas(listaLv);
									if (lv != 0 )
										lvResultado.add( new SimResult(jdc1.getDate(),fechas.elementAt(i),fechas.elementAt(i+1),indicador2.getIdByName(comboBox.getSelectedItem().toString()),dbs2.getNroCorrida(),lv) );
									else
										contLV++;
								}
							
								mostrarLasVegas (lvResultado,lOriginal2);
								jTabbedPane1.setSelectedIndex(3);
								generarGraficosLV(lOriginal2);
								
								btnGuardar2.setEnabled(true);
								btnLasVegas.setEnabled(false);
								
							}	
							else{
								final JDialog error = new JDialog ();
								error.setSize(400, 150);
								error.setLocationRelativeTo(null);
								error.setTitle ("ERROR");
								error.setModal(true);
								JLabel texto = new JLabel("Por favor, elija un rango de fechas mas grande.", JLabel.CENTER);
								texto.setBounds(15, 50, 390, 14);
								error.add(texto);
								error.show();
							}
							
						}
						
					}});
				}	
								

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmAbrir = new JMenuItem("Abrir BD...");
		mnArchivo.add(mntmAbrir);
		
		JMenuItem mntmCerrar = new JMenuItem("Cerrar");
		mnArchivo.add(mntmCerrar);
		mntmCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"*.mdb , *.accdb", "mdb", "accdb");
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("./"));
				if (chooser.showOpenDialog(SimulatorGUI.this) == JFileChooser.APPROVE_OPTION) {
					comboBox.setModel(new DefaultComboBoxModel<>(
							new String[] { "Cargando datos..." }));
					loadData(chooser.getSelectedFile());
				}
				else{
					/// Cartel de error de extension
				}
			}
		});
		
	}}}


	
	

///////////////////////////////////GENERAR GRAFICOS ////////////////////////////////////////////////////////////////
	
	
	public Vector<Date> obtenerFechasSim (List<IndicatorEntry> lista){
		Vector <Date> d = new Vector<Date>();
		for (IndicatorEntry i : lista) {
			Date date = i.getDate();
			if (!d.contains(date)) {
				d.add(date);
			}
		}
		
		return d;
	}
	
	private void generarGraficoReales(List<IndicatorEntry> lOriginal) {
		
		Integer largo = lOriginal.size()*40+100;
		graficoReales = new XYLineChartReales (lOriginal, largo);
		this.labelGrafico2.setIcon(graficoReales);
		this.labelGrafico2.setText("");
		
		panelGrafico2 = new JPanel();
		jTabbedPane1.remove(4);
		jTabbedPane1.addTab("Grafica de puntos", null, panelGrafico2,null);
		
		jScrollPaneGr = new JScrollPane ();
		panelGrafico2.add(jScrollPaneGr);
		jScrollPaneGr.setPreferredSize(new java.awt.Dimension(610,270));
		jScrollPaneGr.setViewportView(labelGrafico2);

	}
	

	private void generarGraficos(List<IndicatorEntry> lOriginal) {
				
		Integer largo = simResultado.size()*40+100;
		grafico = new XYLineChart (lOriginal,simResultado, largo, 5);
		this.labelGrafico2.setIcon(grafico);
		this.labelGrafico2.setText("");
		
		panelGrafico2 = new JPanel();
		jTabbedPane1.remove(4);
		jTabbedPane1.addTab("Grafica de puntos - Montecarlo", null, panelGrafico2,null);
		
		jScrollPaneGr = new JScrollPane ();
		panelGrafico2.add(jScrollPaneGr);
		jScrollPaneGr.setPreferredSize(new java.awt.Dimension(610,270));
		jScrollPaneGr.setViewportView(labelGrafico2);
		
		this.panelGrafico2.add(btnGuardarGrafico);

	}
	
	private void generarGraficosLV(List<IndicatorEntry> lOriginal) {
		
		Integer largo = lvResultado.size()*40+100;
		grafico = new XYLineChart (lOriginal,lvResultado, largo, contLV+5);
		this.labelGrafico2.setIcon(grafico);
		this.labelGrafico2.setText("");
		
		panelGrafico2 = new JPanel();
		jTabbedPane1.remove(4);
		jTabbedPane1.addTab("Grafica de puntos - Las Vegas", null, panelGrafico2,null);
		
		jScrollPaneGr = new JScrollPane ();
		panelGrafico2.add(jScrollPaneGr);
		jScrollPaneGr.setPreferredSize(new java.awt.Dimension(610,270));
		jScrollPaneGr.setViewportView(labelGrafico2);
		
		this.panelGrafico2.add(btnGuardarGrafico);

	}

		
	

//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private void previsualizar(List<IndicatorEntry> lista) {
		
		matrizMuestra = new String[lista.size()][3];

		for (int i=0; i< lista.size()-1; i++) {
			matrizMuestra[i][0] = String.valueOf(i+1);
			matrizMuestra[i][1] = String.valueOf(formatter.format(lista.get(i).getDate()));
			matrizMuestra[i][2] = String.valueOf(lista.get(i).getValue());
		}
		
		tabModel = new DefaultTableModel(matrizMuestra, new String[] {"Nro Fecha", "Fecha", "Valor"});
		tabPreviewMuestra.setModel(tabModel);

		labCant.setText("Cantidad: " + lista.size());

	}
	
	private void mostrarSimulacion ( Vector<SimResult> simResultado, List<IndicatorEntry> lOriginal ) {
		
		matrizSim = new String[simResultado.size()][5];

		int j = 0;
		for (SimResult i : simResultado) {
		
			matrizSim[j][0] = String.valueOf(i.getId());
			
			matrizSim[j][1] = String.valueOf(formatter.format(i.getInicio()))+" / "+String.valueOf(formatter.format(i.getFin()));
								
			matrizSim[j][2] = String.valueOf(i.getResultado());
			
			matrizSim[j][3] = String.valueOf(lOriginal.get(j).getValue());
			
			matrizSim[j][4] = String.valueOf(formatter.format(i.getFecha()));
			
			j++;
		
		}
		
		tabModel = new DefaultTableModel(matrizSim, new String[] {"Id Indicador", "Periodo", "Valor Simulado", "Valor Real", "Fecha Simulada"});
		tabPreviewSim.setModel(tabModel);

	}
	
	private void mostrarLasVegas ( Vector<SimResult> simResultado, List<IndicatorEntry> lOriginal ) {
		
		matrizLV = new String[simResultado.size()][5];

		int j = 0;
		for (SimResult i : simResultado) {
		
			matrizLV[j][0] = String.valueOf(i.getId());
			
			matrizLV[j][1] = String.valueOf(formatter.format(i.getInicio()))+" / "+String.valueOf(formatter.format(i.getFin()));   
								
			matrizLV[j][2] = String.valueOf(i.getResultado());
			
			matrizLV[j][3] = String.valueOf(lOriginal.get(j).getValue());
			
			matrizLV[j][4] = String.valueOf(formatter.format(i.getFecha()));
			
			j++;
		
		}
		
		tabModel = new DefaultTableModel(matrizLV, new String[] {"Id Indicador", "Periodo", "Valor Simulado", "Valor Real", "Fecha Simulada"});
		tabPreviewLV.setModel(tabModel);

	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void loadData(File dbPath) {
		PropertyManager.instance().setProperty(Parameters.DB_NAME.toString(),
				"jdbc:ucanaccess://" + dbPath.getAbsolutePath());

		new SwingWorker<Object[], Void>() {

			@Override
			protected Object[] doInBackground() throws Exception {
				DBValorIndicador vIndicador = new DBValorIndicador();
				minDate = vIndicador.getMinDate();
				maxDate = vIndicador.getMaxDate();
				DBIndicador indicador = new DBIndicador();
				return indicador.getIndicatorNames();
			}

			@SuppressWarnings({ "unchecked" })
			@Override
			public void done() {
				try {
					comboBox.setModel(new DefaultComboBoxModel<>(get()));
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				// Cargar el valor de periodo del indicador
				DBIndicador indicador = new DBIndicador();

				// Cambiar la fecha inicio y fin segun el indicador (AUTOCOMPLETA AL INICIAR, FECHA MIN, MAX)
				DBValorIndicador vIndicador = new DBValorIndicador();
				minDate = vIndicador.getMinDate(indicador.getIdByName(comboBox
						.getSelectedItem().toString()));
				maxDate = vIndicador.getMaxDate(indicador.getIdByName(comboBox
						.getSelectedItem().toString()));
				jdc1.setDate(minDate);
				jdc2.setDate(maxDate);
		
				btnSimular.setEnabled(true);
				jdc2.setEnabled(true);
				jdc1.setEnabled(true);

			}
		}.execute();
		;
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	
}