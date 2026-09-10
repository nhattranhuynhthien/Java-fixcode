
package org.example.dto;

public class TaiKhoanDTO {
    private String username;
    private String password;
    private String position;
    private boolean uyQuyen; 
    
    public enum Role{
        MANAGER,
        VICE_MANAGER,
        STAFF
    }
    
    public TaiKhoanDTO() {
    }

    public TaiKhoanDTO(String username, String password, String position) {
        this.username = username;
        this.password = password;
        this.position = position;
        this.uyQuyen = false;
    }

    public TaiKhoanDTO(String username, String password, String position, boolean uyQuyen) {
        this.username = username;
        this.password = password;
        this.position = position;
        this.uyQuyen = uyQuyen;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPosition() {
        return position;
    }

    public boolean isUyQuyen() {
        return uyQuyen;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    public void setUyQuyen(boolean uyQuyen) {
        this.uyQuyen = uyQuyen;
    }
    
    public Role getBaseRole(){
        if (position != null) {
            String p = position.trim().toLowerCase();
            if (p.equals("quản lí") || p.equals("quan li") || p.equals("quản lý") || p.equals("quan ly")) {
                return Role.MANAGER;
            }
            if (p.equals("phó quản lý") || p.equals("pho quan ly") || p.equals("phó quản lí")) {
                return Role.VICE_MANAGER;
            }
        }
        return Role.STAFF;
    }
}
