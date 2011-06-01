
package business;


public class Login {

    private String custId;
    private String password;
    
    public Login(String custId, String password){
        this.custId= custId;
        this.password = password;
    }
    
    public String getCustId(){
        return custId;
    }
    public String getPassword() {
        return password;
    }
}
