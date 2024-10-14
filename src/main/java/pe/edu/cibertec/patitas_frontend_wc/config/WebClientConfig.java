package pe.edu.cibertec.patitas_frontend_wc.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    //WebClient -> Las invocaciones son de forma sincrona y asincrona
    @Bean
    public WebClient webClientAutenticacion(WebClient.Builder builder){

        //Configuracion timeout en HttpClient Netty
        HttpClient httpClient=HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)//TIMEOUT DE CONEXION
                .responseTimeout(Duration.ofSeconds(10))//TIMEOUT DE LECTURA DE TODA LA  RESPUESTA
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)));//TIMEOUT DE LECTURA DE CADA PAQUETE


         return builder.baseUrl("http://localhost:8081/autenticacion")
                 .clientConnector(new ReactorClientHttpConnector(httpClient))
                 .build();
                 //Implementar timeout

    }




    @Bean
    public WebClient webClientFinanzas(WebClient.Builder builder){

        //Configuracion timeout en HttpClient Netty
        HttpClient httpClient=HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)//TIMEOUT DE CONEXION
                .responseTimeout(Duration.ofSeconds(10))//TIMEOUT DE LECTURA DE TODA LA  RESPUESTA
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)));//TIMEOUT DE LECTURA DE CADA PAQUETE


        return builder.baseUrl("http://localhost:8081/autenticacion")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        //Implementar timeout

    }





    @Bean
    public WebClient webClientReportes(WebClient.Builder builder){

        //Configuracion timeout en HttpClient Netty
        HttpClient httpClient=HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)//TIMEOUT DE CONEXION
                .responseTimeout(Duration.ofSeconds(10))//TIMEOUT DE LECTURA DE TODA LA  RESPUESTA
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)));//TIMEOUT DE LECTURA DE CADA PAQUETE


        return builder.baseUrl("http://localhost:8081/autenticacion")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        //Implementar timeout

    }







}//FIN
