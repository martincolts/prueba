package com.operativa.simulator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.operativa.simulator.datamodel.IndicatorEntry;

@SuppressWarnings("serial")
public class XYLineChartReales extends ImageIcon{

    private JFreeChart jfreechart ;
	
    public XYLineChartReales( List<IndicatorEntry> lReal, Integer largo){

        //se declara el grafico XY Lineal
        XYDataset xydataset = xyDataset(lReal);
        jfreechart = ChartFactory.createXYLineChart(
        "Comparativa: Datos Reales - Datos Simulación" , "Fechas", "Valores",  
        xydataset, PlotOrientation.VERTICAL,  true, true, false);               

        //personalización del grafico
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setBackgroundPaint( Color.white );
        xyplot.setDomainGridlinePaint( Color.BLACK );
        xyplot.setRangeGridlinePaint( Color.BLACK );        
        // -> Pinta Shapes en los puntos dados por el XYDataset
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        //--> muestra los valores de cada punto XY
        XYItemLabelGenerator xy = new StandardXYItemLabelGenerator();
        xylineandshaperenderer.setBaseItemLabelGenerator( xy );
        xylineandshaperenderer.setBaseItemLabelsVisible(true);
        xylineandshaperenderer.setBaseLinesVisible(true);
        xylineandshaperenderer.setBaseItemLabelsVisible(true);                
        //fin de personalización

        //se crea la imagen y se asigna a la clase ImageIcon
        BufferedImage bufferedImage  = jfreechart.createBufferedImage( largo, 240);
        this.setImage(bufferedImage);
    }

    /**
 * Datos
 */
    private XYDataset xyDataset(List<IndicatorEntry> lReal)
    {
        //se declaran las series y se llenan los datos
        XYSeries sReales = new XYSeries("Reales");
        XYSeries sSimulados = new XYSeries("Simulados"); 
        
        //serie
        for (int i = 0; i< lReal.size(); i++) {
        	Double aux2 = (double) lReal.get(i).getValue();
	        sReales.add ( i+1, aux2 );
        }

        XYSeriesCollection xyseriescollection =  new XYSeriesCollection();
        xyseriescollection.addSeries( sReales );        
        xyseriescollection.addSeries( sSimulados );        

        return xyseriescollection;
    }
    
    public JFreeChart getChart (){
    	return jfreechart ;
    }

}//-->fin clase