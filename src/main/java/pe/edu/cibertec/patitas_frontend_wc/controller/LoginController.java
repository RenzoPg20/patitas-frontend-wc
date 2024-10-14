//PARTE 1
package pe.edu.cibertec.patitas_frontend_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontend_wc.client.AutenticClient;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

//SE COLOCA CONTROLLER PARA LOS CONTROLADORES
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    WebClient webClientAutenticacion;


    @Autowired
    AutenticClient autenticClient;

    //GetMapping --> Cuando quiero acceder a una url y uso la url para acceder al Path
    //PostMapping -->Cuando envio datos de un formulario en una pagina html e internamente envio esos datos

    @GetMapping("/inicio")
    public String inicio(Model model){


           LoginModel loginModel=new LoginModel("00","","");
           model.addAttribute("loginModel",loginModel);


        return "inicio";
    }















    @PostMapping("/autenticar-viejin")
    public String autenticarviejin( @RequestParam("tipoDocumento") String tipoDocumento, @RequestParam("numeroDocumento") String numeroDocumento ,
                               @RequestParam("password") String password, Model model){


        //VALIDAR CAMPOS DE ENTRADA
        //trim() -->valida que no haya esoacios en blancos al principio


        //VALIDAMOS SI TODO ESTA MAL SI ES NULO Y TIENE ESPACIOS EN BLANCO
        if (tipoDocumento == null || tipoDocumento.trim().length()==0 ||
                numeroDocumento==null || numeroDocumento.trim().length()==0 ||
                 password==null || password.trim().length()==0)  {


            LoginModel loginModel=new LoginModel("01","Error:Debe completar correctamente sus credenciales","Bruno Diaz");
            model.addAttribute("loginModel",loginModel);

            return "inicio";
        }


      try {
          //INVOCAR API DE VALIDACION DE USUARIO

          LoginRequestDTO loginRequestDTO=new LoginRequestDTO(tipoDocumento,numeroDocumento,password);
          //LoginResponseDTO loginResponseDTO=restTemplate.postForObject("/login",loginRequestDTO, LoginResponseDTO.class);

          Mono<LoginResponseDTO> monoLoginResponseDTO = webClientAutenticacion.post().uri("/login")
                  .body(Mono.just(loginRequestDTO),LoginRequestDTO.class)
                  .retrieve()
                  .bodyToMono(LoginResponseDTO.class);

          //RECUPERAR RESULTADO DEL MONO
          //block() -->bloqueante o sincrona
          LoginResponseDTO loginResponseDTO=monoLoginResponseDTO.block();



          //VALIDAR RESPUESTA

          if(loginResponseDTO.codigo().equals("00")){

              LoginModel loginModel=new LoginModel("00","", loginResponseDTO.nombreUsuario());
              model.addAttribute("loginModel",loginModel);

              return "principal";
          }else{

              LoginModel loginModel=new LoginModel("02","Error:Autenticacion Fallida","");
              model.addAttribute("loginModel",loginModel);

              return "inicio";


          }
      }catch (Exception e){


          LoginModel loginModel=new LoginModel("99","Error:Ocurrio un Problema en la Autenticación","");
          model.addAttribute("loginModel",loginModel);

          System.out.println(e.getMessage());

          return "inicio";
      }






    }











    //Spring Claud


    @PostMapping("/autenticar")
    public String autenticar( @RequestParam("tipoDocumento") String tipoDocumento, @RequestParam("numeroDocumento") String numeroDocumento ,
                              @RequestParam("password") String password, Model model) {


        System.out.println("Consumiendo con FeignClient");

        //VALIDAR CAMPOS DE ENTRADA
        //trim() -->valida que no haya esoacios en blancos al principio


        //VALIDAMOS SI TODO ESTA MAL SI ES NULO Y TIENE ESPACIOS EN BLANCO
        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 ||
                numeroDocumento == null || numeroDocumento.trim().length() == 0 ||
                password == null || password.trim().length() == 0) {


            LoginModel loginModel = new LoginModel("01", "Error:Debe completar correctamente sus credenciales", "Bruno Diaz");
            model.addAttribute("loginModel", loginModel);

            return "inicio";
        }


        try {
            //INVOCAR API DE VALIDACION DE USUARIO


            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            //LoginResponseDTO loginResponseDTO=restTemplate.postForObject("/login",loginRequestDTO, LoginResponseDTO.class);
            ResponseEntity<LoginResponseDTO> responseEntity = autenticClient.login(loginRequestDTO);

            //Validar respuesta del servicio a traves de http

            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                LoginResponseDTO loginResponseDTO = responseEntity.getBody();
                //VALIDAR RESPUESTA

                if (loginResponseDTO.codigo().equals("00")) {

                    LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                    model.addAttribute("loginModel", loginModel);

                    return "principal";
                } else {

                    LoginModel loginModel = new LoginModel("02", "Error:Autenticacion Fallida", "");
                    model.addAttribute("loginModel", loginModel);

                    return "inicio";


                }

            } else {

                LoginModel loginModel = new LoginModel("99", "Error:Ocurrio un Problema al invocar el Servicio", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";
            }


        } catch (Exception e) {


            LoginModel loginModel = new LoginModel("99", "Error:Ocurrio un Problema en la Autenticación", "");
            model.addAttribute("loginModel", loginModel);

            System.out.println(e.getMessage());

            return "inicio";
        }


    }

}
