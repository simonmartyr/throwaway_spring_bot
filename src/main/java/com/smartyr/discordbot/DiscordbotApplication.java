package com.smartyr.discordbot;

import com.smartyr.antlr.FilterListener;
import com.smartyr.discordbot.filters.filterListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class DiscordbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscordbotApplication.class, args);
//        String myString = "name eq 'milk' and name eq 'me' and you le 300 and name ct 'poo'";
        String myString = "name eq 'chiken' and name eq m";

        filterListener filter = new filterListener();
        System.out.println("filter.generateQueryString(myString) = " + filter.generateQueryString(myString));
    }

}