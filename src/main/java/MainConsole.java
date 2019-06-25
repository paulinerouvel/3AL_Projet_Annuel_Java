import org.json.JSONObject;
import services.UserInstance;

import java.util.Scanner;

public class MainConsole {
    private UserInstance userInstance;

    public void main(String[] args){
        login();

    }

    public void login(){
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Enter username :");
        String userName = myObj.nextLine();  // Read user input
        System.out.println("Enter password :");
        String password = myObj.nextLine();  // Read user input

        userInstance = new UserInstance();

        JSONObject token = userInstance.login(userName, password);

        processLoginAttempt(token);
    }

    public void processLoginAttempt(JSONObject token){
        if(token.has("error")){
            if(token.getInt("error") == 503){
                System.out.println("Timeout");
            } else if(token.getInt("error") == 400){
                System.out.println("Identifiant ou mot de passe incorrect");
            } else {
                System.out.println("Erreur interne. Veuillez re-essayer plus tard.");
            }
        } else {
            userInstance.setToken(token);

            if(userInstance.tokenIsValid()) {
                userInstance.initUser();
                userInstance.setConnected(true);

                if(userInstance.getTokenUserCategory().equals(4)){
                    displayMainEmployee();
                } else if(userInstance.getTokenUserCategory().equals(5)){
                    displayMainAdmin();
                } else if(userInstance.getTokenUserCategory().equals(2)) {
                    displayMainProfessionnal();
                }

            }
            else {
                System.out.println("Token incorrect");
            }
        }
    }

    private void displayMainProfessionnal() {
    }

    private void displayMainEmployee(){

    }

    private void displayMainAdmin() {
    }

}
