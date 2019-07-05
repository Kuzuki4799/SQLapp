package android.trithe.sqlapp.rest.request;

public class DataUserInfoRequest {
    private String name;
    private String username;
    private String password;
    private String image;
    private String token_id;
    private String device_token;

    public DataUserInfoRequest(String name, String username, String password, String image, String token_id, String device_token) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.image = image;
        this.token_id = token_id;
        this.device_token = device_token;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
