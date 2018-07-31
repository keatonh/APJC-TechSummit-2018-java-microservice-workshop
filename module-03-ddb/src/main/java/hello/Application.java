/**
 * Copyright © 2018 spring-data-dynamodb-example (https://github.com/derjust/spring-data-dynamodb-examples)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import hello.config.DynamoDBConfig;
import hello.repository.ddb.PhotoInfoRepository;
import hello.repository.mysql.UserRepository;
import hello.model.ddb.PhotoInfo;
import hello.model.mysql.Image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.Date;
import java.util.Optional;

import static hello.config.DynamoDBConfig.checkOrCreateTable;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, // Disable JPA
    DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
// @EnableJpaRepositories(includeFilters = { //Enable JPA
// 		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {UserRepository.class})})
@EnableDynamoDBRepositories(includeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {PhotoInfoRepository.class})})
@Configuration
@Import(DynamoDBConfig.class)
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
        // new SpringApplicationBuilder(Application.class).profiles("multirepo").run(args); //stand alone
        new SpringApplicationBuilder(Application.class).profiles("rest").web(WebApplicationType.SERVLET).run(args);
	}

    /*
        Use JPA (multi repo),
        if you want to launch multi repo then should configure JPA application properties or need to use SSM
    */
	// @Bean
	// public CommandLineRunner multirepo(ConfigurableApplicationContext ctx, UserRepository jpaRepository,
	// 		PhotoInfoRepository dynamoDBRepository, AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper,
	// 		DynamoDBMapperConfig config) {
	// 	return (args) -> {
	// 		// demoJPA(jpaRepository);

	// 		// checkOrCreateTable(amazonDynamoDB, dynamoDBMapper, config, Device.class);

	// 		demoDynamoDB(dynamoDBRepository);

	// 		ctx.close();
	// 	};
    // }
    
    /* 
        Use only DDB
        It is not using SSM just only application.properties
    */
    @Bean
	public CommandLineRunner multirepo(ConfigurableApplicationContext ctx,
			PhotoInfoRepository dynamoDBRepository, AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper,
			DynamoDBMapperConfig config) {
		return (args) -> {

			checkOrCreateTable(amazonDynamoDB, dynamoDBMapper, config, PhotoInfo.class);
			demoDynamoDB(dynamoDBRepository);

			// ctx.close();
		};
	}

	private void demoDynamoDB(PhotoInfoRepository dynamoDBRepository) {
		// save a couple of devices
		dynamoDBRepository.save(new PhotoInfo("1", "kr", "a.png"));
		dynamoDBRepository.save(new PhotoInfo("2", "en", "b.png"));

		// fetch all devices
		log.info("PhtoInfo found with findAll():");
		log.info("-------------------------------");
		for (PhotoInfo photo : dynamoDBRepository.findAll()) {
			log.info(photo.toString());
		}
		log.info("");

	}

	// private void demoJPA(CustomerRepository jpaRepository) {
	// 	// save a couple of customers
	// 	jpaRepository.save(new Customer("Jack", "Bauer"));
	// 	jpaRepository.save(new Customer("Chloe", "O'Brian"));
	// 	jpaRepository.save(new Customer("Kim", "Bauer"));
	// 	jpaRepository.save(new Customer("David", "Palmer"));
	// 	jpaRepository.save(new Customer("Michelle", "Dessler"));

	// 	// fetch all customers
	// 	log.info("Customers found with findAll():");
	// 	log.info("-------------------------------");
	// 	for (Customer customer : jpaRepository.findAll()) {
	// 		log.info(customer.toString());
	// 	}
	// 	log.info("");

	// 	// fetch an individual customer by ID
	// 	Optional<Customer> customer = jpaRepository.findById(1L);
	// 	log.info("Customer found with findOne(1L):");
	// 	log.info("--------------------------------");
	// 	log.info(customer.get().toString());
	// 	log.info("");

	// 	// fetch customers by last name
	// 	log.info("Customer found with findByLastName('Bauer'):");
	// 	log.info("--------------------------------------------");
	// 	for (Customer bauer : jpaRepository.findByLastName("Bauer")) {
	// 		log.info(bauer.toString());
	// 	}
	// 	log.info("");
	// }

}
