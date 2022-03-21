package com.example.demo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.Position;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

@SpringBootApplication
public class DemoApplication {
static double countOfProfit = 0;
	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SpringApplication.run(DemoApplication.class, args);
//                System.setProperty("java.awt.headless", "false");
//                new Strategys().paintStrategy();
                
//        new TechAnalyz().convertDateToZoneDateTime("2021-12-17 10:00");
		
//		                 BarSeries series = TechAnalyz.loadBitstampSeries();
//		                 Strategy strategy = null;
//		                 TradingRecord tradingRecord = null;
//		                 BarSeriesManager seriesManager = new BarSeriesManager(series);
//		                 List<Position> listPoz = null;
//         for(int i = 0; i < 50; i=i+5) {
//        	 for(int j = 20; j < 100; j= j+10) {
//        		 strategy = Strategys.simpleStrategy(series);
//        		 tradingRecord = seriesManager.run(strategy);
//        		 listPoz = tradingRecord.getPositions();
//        		 countOfProfit = 0;
//                 for(int y = 0; y < listPoz.size(); y++){
//                     countOfProfit = countOfProfit + listPoz.get(y).getGrossProfit().doubleValue(); 
//                 System.out.println(listPoz.get(0).getEntry().toString()+" ||| ");  
//                 }
//                 Strategys.writeResultToCSV(listPoz);
//                 RawStrategy strategy1 = new RawStrategy("Moving Average", "Moving Average", "org.ta4j.core.rules.CrossedDownIndicatorRule;");
//         		System.out.println("rule: " + new Strategys().createRule(strategy1).toString());
       // Analysis
//                 if(countOfProfit>0)
//               System.out.println("countOfProfit "+countOfProfit);
//       System.out.println(
//               "Total profit for the strategy: " + new GrossReturnCriterion().calculate(series, tradingRecord));
//        	 }
//         }
//         System.out.println("the end");
         
		       
		         
		        
//		        System.out.println("Number of positions for the strategy: "); 
		         
			}
	}
	

