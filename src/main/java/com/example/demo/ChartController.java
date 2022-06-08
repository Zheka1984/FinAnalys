package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChartController {

	ListWrapper listwrapper = new ListWrapper();
	Strategys strat;

	@GetMapping("/ema") //вывод индикаторов на график, поддерживаются одна или две пары индикаторов
	public String drawEma(Model model) {
		int shortEma1=0;
		int longEma1=0;
		int shortEma2=0;
		int longEma2=0;
		if(listwrapper.getList1().get(0) != null) {
		shortEma1 = listwrapper.getList1().get(0).getPeriod1();
		longEma1 = listwrapper.getList1().get(0).getPeriod2();
		}
		if(listwrapper.getList1().size()>1) {
			shortEma2 = listwrapper.getList1().get(1).getPeriod1();
			longEma2 = listwrapper.getList1().get(1).getPeriod2();
			}
		System.out.println(shortEma1+" "+longEma1+" "+shortEma2+" "+longEma2);
		model.addAttribute("shortEma1", shortEma1);
		model.addAttribute("longEma1", longEma1);
		model.addAttribute("shortEma2", shortEma2);
		model.addAttribute("longEma2", longEma2);
		return "Ema";
	}

	@GetMapping("/testing")
	public String testStrategy(Model model) {
		strat = new Strategys();
		listwrapper.getList1().add(new RawStrategy());
		listwrapper.getList2().add(new RawStrategy());
		model.addAttribute("listwrapper", listwrapper);
		System.out.println("GETMETHOD");
		return "testingResult";
	}

	@PostMapping("/testing")
	public ModelAndView testStrategy(@ModelAttribute("listwrapper") ListWrapper listwrapper, Model model)
			throws InterruptedException, NoSuchMethodException, SecurityException, ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			IOException {
		// сделать проверку на null и вывод результата на страницу
		System.out.println("startcont");
		model.addAttribute("listwrapper", listwrapper);
		this.listwrapper = listwrapper;
		// если длины массивов равны, т.е кол-во условый для входа и выхода равны
		if (listwrapper.getList1().size() == listwrapper.getList2().size()) {
			for (int i = 0; i < listwrapper.getList1().size(); i++) {
				listwrapper.getList2().get(i).setPeriod1(listwrapper.getList1().get(i).getPeriod1());
				listwrapper.getList2().get(i).setPeriod2(listwrapper.getList1().get(i).getPeriod2());
				System.out.println("wrappersize "+listwrapper.getList2().get(i).getPeriod2());
			}
		}
		// если длина второго массива больше, периоды переменных во втором будут равны
		// периодам в последнем условии первого
		if (listwrapper.getList2().size() > listwrapper.getList1().size()) {
			for (int y = 0; y < listwrapper.getList1().size(); y++) {
				listwrapper.getList2().get(y).setPeriod1(listwrapper.getList1().get(y).getPeriod1());
				listwrapper.getList2().get(y).setPeriod2(listwrapper.getList1().get(y).getPeriod2());
			}
			for (int i = listwrapper.getList1().size(); i < listwrapper.getList2().size(); i++) {
				listwrapper.getList2().get(i)
						.setPeriod1(listwrapper.getList1().get(listwrapper.getList1().size() - 1).getPeriod1());
				listwrapper.getList2().get(i)
						.setPeriod2(listwrapper.getList1().get(listwrapper.getList1().size() - 1).getPeriod2());
			}
		}
		// если длина первого массива больше, периоды второго равны периодам в первой
		// строке первого массива
		if (listwrapper.getList1().size() > listwrapper.getList2().size()) {
			for (int i = 0; i < listwrapper.getList2().size(); i++) {
				listwrapper.getList2().get(i).setPeriod1(listwrapper.getList1().get(i).getPeriod1());
				listwrapper.getList2().get(i).setPeriod2(listwrapper.getList1().get(i).getPeriod2());
			}
		}
		listwrapper.getList1().forEach(t -> System.out.println(t.getIndicator1().toString() + "-" + t.getPeriod1()));
		listwrapper.getList2().forEach(t -> System.out.println(t.getIndicator1().toString() + "-" + t.getPeriod2()));
		strat.perfomAndWriteResult(strat.workingStrategy(listwrapper));
		ModelAndView mav = new ModelAndView();
		mav.setStatus(HttpStatus.OK);
		mav.setViewName("testingResult");
		mav.addObject("response", "success");
		System.out.println("strrr "+strat.workingStrategy(listwrapper).getExitRule().toString());
		return mav;
//    	return new ResponseEntity<>("Hello World!", HttpStatus.OK); 
	}

	@PostMapping(value = "/testing", params = { "addRow1" })
	public String addRow1(Model model) {
		System.out.println("start");
		System.out.println("size1 " + listwrapper.getList1().size());
		if(listwrapper.getList1().size()<2) 
		listwrapper.getList1().add(new RawStrategy());
		model.addAttribute("listwrapper", listwrapper);
		System.out.println("end");
		return "testingResult";
	}

	@PostMapping(value = "/testing", params = { "addRow2" })
	public String addRow2(Model model) {
		System.out.println("start");
		System.out.println("size2 " + listwrapper.getList2().size());
		if(listwrapper.getList2().size()<2) 
		listwrapper.getList2().add(new RawStrategy());
		model.addAttribute("listwrapper", listwrapper);
		System.out.println("end");
		
		return "testingResult";
	}
	
	@PostMapping(value = "/testing", params = { "deleteRow1" })
	public String deleteRow1(Model model) {
		System.out.println("start");
		System.out.println("size1 " + listwrapper.getList1().size());
		listwrapper.getList1().remove(0);
		model.addAttribute("listwrapper", listwrapper);
		System.out.println("end");
		return "testingResult";
	}
	
	@PostMapping(value = "/testing", params = { "deleteRow2" })
	public String deleteRow2(Model model) {
		System.out.println("start");
		System.out.println("size1 " + listwrapper.getList1().size());
		listwrapper.getList2().remove(0);
		model.addAttribute("listwrapper", listwrapper);
		System.out.println("end");
		return "testingResult";
	}
}
