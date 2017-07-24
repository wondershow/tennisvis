/**
 * Author: Lei Zhang
 * raymond.zhang.us@gmail.com
 * Mar 13, 2017
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;

/** @see http://stackoverflow.com/questions/5522575 */
public class ChartPanelDemo {

    private static final String title = "Return On Investment";
    private ChartPanel chartPanel = createChart();

    public ChartPanelDemo() {
        JFrame f = new JFrame(title);
        f.setTitle(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout(0, 5));
        f.add(chartPanel, BorderLayout.CENTER);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setHorizontalAxisTrace(true);
        chartPanel.setVerticalAxisTrace(true);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(createTrace());
        panel.add(createDate());
        panel.add(createZoom());
        f.add(panel, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private JComboBox createTrace() {
        final JComboBox trace = new JComboBox();
        final String[] traceCmds = {"Enable Trace", "Disable Trace"};
        trace.setModel(new DefaultComboBoxModel(traceCmds));
        trace.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (traceCmds[0].equals(trace.getSelectedItem())) {
                    chartPanel.setHorizontalAxisTrace(true);
                    chartPanel.setVerticalAxisTrace(true);
                    chartPanel.repaint();
                } else {
                    chartPanel.setHorizontalAxisTrace(false);
                    chartPanel.setVerticalAxisTrace(false);
                    chartPanel.repaint();
                }
            }
        });
        return trace;
    }

    private JComboBox createDate() {
        final JComboBox date = new JComboBox();
        final String[] dateCmds = {"Horizontal Dates", "Vertical Dates"};
        date.setModel(new DefaultComboBoxModel(dateCmds));
        date.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFreeChart chart = chartPanel.getChart();
                XYPlot plot = (XYPlot) chart.getPlot();
                DateAxis domain = (DateAxis) plot.getDomainAxis();
                if (dateCmds[0].equals(date.getSelectedItem())) {
                    domain.setVerticalTickLabels(false);
                } else {
                    domain.setVerticalTickLabels(true);
                }
            }
        });
        return date;
    }

    private JButton createZoom() {
        final JButton auto = new JButton(new AbstractAction("Auto Zoom") {

            @Override
            public void actionPerformed(ActionEvent e) {
                chartPanel.restoreAutoBounds();
            }
        });
        return auto;
    }

    private ChartPanel createChart() {
        XYDataset roiData = createDataset();
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title, "Date", "Value", roiData, true, true, false);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer =
            (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        currency.setMaximumFractionDigits(0);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(currency);
        return new ChartPanel(chart);
    }

    private XYDataset createDataset() {
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        TimeSeries s1 = new TimeSeries("Heart Rate");
        
        String path = "/Users/leizhang/Desktop/"
        		+ "tennis/winbledon/xcorr_res/set1/xcorr_ND_backhand.txt";
        long start = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    int last = 0, max = 0, cur = 0, sum = 0, millisec = 0;
		    
		    List<Integer> buffer = new ArrayList();
		    
		    while ((line = br.readLine()) != null) {
		    		cur++;
		    		buffer.add(Integer.parseInt(line.trim()));
		    		if (cur == 44100) {
		    			int i = 0, count = 0;
		    			while (i < buffer.size()) {
		    				count++;
		    				int span = 44;
		    				if (count == 10) {
		    					span = 45;
		    				}
		    				for (int j = 1; j <= span; j++, i++) {
		    					sum += buffer.get(i);
		    				}
		    				int sec = millisec / 1000;
			    			int hour = sec / 3600;
			    			sec = sec - hour * 3600;
			    			int min = sec / 60;
			    			sec = sec - min * 60;
			    			s1.add(new Millisecond(millisec % 1000, sec, min, hour, 1, 10, 2006), sum / span);
		    				millisec++;
		    				if (count == 10) {
		    					count = 0;
		    				}
		    				sum = 0;
		    			}
		    			cur = 0;
		    			buffer.clear();
		    		}
		    		if (millisec == 300 * 1000) break;
		    }
		    System.out.println("millisec = " + millisec);
		} catch (Exception e) {
			e.printStackTrace();
		}
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
        return dataset;
        
        /*
        s1.add(new Second(33, 8, 9, 1, 10, 2006), 167);
        s1.add(new Second(10, 10, 9, 1, 10, 2006), 189);
        s1.add(new Second(19, 12, 9, 1, 10, 2006), 156);
        s1.add(new Second(5, 15, 9, 1, 10, 2006), 176);
        s1.add(new Second(12, 16, 9, 1, 10, 2006), 183);
        s1.add(new Second(6, 18, 9, 1, 10, 2006), 138);
        s1.add(new Second(11, 20, 9, 1, 10, 2006), 102);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        return dataset;*/
    }

    private TimeSeries createSeries(String name, double scale) {
        TimeSeries series = new TimeSeries(name);
        for (int i = 0; i < 6; i++) {
            series.add(new Year(2005 + i), Math.pow(2, i) * scale);
        }
        return series;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ChartPanelDemo cpd = new ChartPanelDemo();
            }
        });
    }
}
