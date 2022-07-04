package com.skymicrosystems.buildmanagement;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.skymicrosystems.buildmanagement.services.DatabaseInitializingServiceImpl;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;

@ControllerAdvice
@SpringBootApplication
@EnableJpaRepositories
@Sql({"/initial-datas.sql"})
public class BuildManagementWebApplication {
	
	private final static Logger logger = LoggerFactory.getLogger(BuildManagementWebApplication.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private DatabaseInitializingServiceImpl databaseInitializingServiceImpl;
	
	public static void main(String[] args) {
		SpringApplication.run(BuildManagementWebApplication.class, args);
	}
	
	public ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
	    SpringTemplateEngine engine = new SpringTemplateEngine();
	    engine.addDialect(new LayoutDialect(new GroupingStrategy()));
	    engine.addDialect(new Java8TimeDialect());
	    engine.setTemplateResolver(templateResolver);
	    return engine;
	}
	
	@Bean
	@Description("Spring Message Resolver")
	public ResourceBundleMessageSource messageSource() {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("messages");
	    return messageSource;
	}
	
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		final DecimalFormat FORMATTER = (DecimalFormat) NumberFormat.getNumberInstance(Locale.getDefault());

		// Allow the HTML field to be empty without generating any exception
		boolean allowEmptyValue = true;

		// Creation of a new binder for the type "BigDecimal"
		CustomNumberEditor newBinder = new CustomNumberEditor(BigDecimal.class, new NumberFormat() {

			private static final long serialVersionUID = 1L;
			
			@Override
			public Number parse(String source, ParsePosition parsePosition) {
				if (source != null) {
					source = source.replace(",", ".");
					
					if (!source.contains(".")) {
						source = source.concat(".00");
					}
				}
				return FORMATTER.parse(source, parsePosition);
			}

			@Override
			public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
				return FORMATTER.format(number, toAppendTo, pos);
			}

			@Override
			public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
				return FORMATTER.format(number, toAppendTo, pos);
			}
		}, allowEmptyValue);

		// Registration of the binder 
		binder.registerCustomEditor(BigDecimal.class, newBinder);
	}
	
	@PostConstruct
	public void initialDataBase() {
		
		databaseInitializingServiceImpl.initialEssentialDataBase();
		
		if (Boolean.parseBoolean(env.getProperty("default.system.create-mockdata"))) {
			databaseInitializingServiceImpl.initialMockDataBase();
		}
		
		//logger.info("Existe arquivo:" + new File("/home/luiz_pereira/ambiente-de-trabalho/repositories/build-management/build-management-web/src/main/resources/reports/orcamento/orcamento-to-cliente.jasper").exists());
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("/orcamento-to-cliente.jasper").exists()); // false
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("orcamento-to-cliente.jasper").exists()); // false
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("classpath:orcamento-to-cliente.jasper").exists());
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("classpath:reports/orcamento/orcamento-to-cliente.jasper").exists());
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("classpath:../reports/orcamento/orcamento-to-cliente.jasper").exists());
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("classpath:/orcamento/orcamento-to-cliente.jasper").exists());
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("classpath:/reports/orcamento/orcamento-to-cliente.jasper").exists());
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("classpath:/static/reports/orcamento/orcamento-to-cliente.jasper").exists());
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("/static/reports/orcamento/orcamento-to-cliente.jasper").exists()); // false
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("../reports/orcamento/orcamento-to-cliente.jasper").exists()); // false
		//logger.info("Existe arquivo:" + ResourceUtils.getFile("/reports/orcamento/orcamento-to-cliente.jasper").exists()); //false
		
		//logger.info("Existe arquivo:" + new DefaultResourceLoader().getResource("/reports/orcamento/orcamento-to-cliente.jasper").getFile().exists());
		//logger.info("Existe arquivo:" + new DefaultResourceLoader().getResource("reports/orcamento/orcamento-to-cliente.jasper").getFile().exists());
		
		//getFileDefaultResourceLoader("reports/orcamento/orcamento-to-cliente.jasper");
		//getFileDefaultResourceLoader("/reports/orcamento/orcamento-to-cliente.jasper");
		//getFileDefaultResourceLoader("classpath:/reports/orcamento/orcamento-to-cliente.jasper");
		//getFileDefaultResourceLoader("classpath:reports/orcamento/orcamento-to-cliente.jasper");
		
		//getFileDefaultResourceLoader("static/reports/orcamento/orcamento-to-cliente.jasper");
		//getFileDefaultResourceLoader("/static/reports/orcamento/orcamento-to-cliente.jasper");
		
		//getClassLoader("reports/orcamento/orcamento-to-cliente.jasper");
		//getClassLoader("/reports/orcamento/orcamento-to-cliente.jasper");

	}
	
	private void getClassLoader(String filePath) {
		try {
			
			logger.info("carregando arquivo");
			//logger.info("Existe arquivo: " + new DefaultResourceLoader().getResource(filePath).getFile().exists());
			
			//logger.info("Existe arquivo: " + ResourceUtils.getFile(new URL(filePath)).exists());
			
			//logger.info("Existe arquivo:" +  getClass().getClassLoader().getResource(filePath).getFile());
			
			logger.info("Existe arquivo:" +  BuildManagementWebApplication.class.getClassLoader()
					.getResourceAsStream(filePath).available());
			
		} catch (Exception e) {
			logger.error(filePath + " Error: " + e.getLocalizedMessage());
		}
	}
	
	@Deprecated
	private void getFileDefaultResourceLoader(String filePath) {
		try {
			
			logger.info("carregando arquivo");
			//logger.info("Existe arquivo: " + new DefaultResourceLoader().getResource(filePath).getFile().exists());
			
			//logger.info("Existe arquivo: " + ResourceUtils.getFile(new URL(filePath)).exists());
			
			
		} catch (Exception e) {
			logger.error(filePath + " Error: " + e.getLocalizedMessage());
		}
	}
	
}

