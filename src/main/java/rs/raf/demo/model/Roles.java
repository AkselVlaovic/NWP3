package rs.raf.demo.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Roles {

    private Boolean can_create_users = false;
    private Boolean can_read_users = false;
    private Boolean can_update_users = false;
    private Boolean can_delete_users = false;


}
