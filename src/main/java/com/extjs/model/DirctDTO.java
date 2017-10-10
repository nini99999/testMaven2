package com.extjs.model;

/**
 * Created by poshist on 2017/3/22.
 */
public class DirctDTO {
    private String id;
    private String name;
    public DirctDTO(String id,String name){
        this.id=id;
        this.name=name;
    }
    public void setId(String id){
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
