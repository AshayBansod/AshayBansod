package models;

import java.util.ArrayList;

public class PetResponse {
    public long id;
    public Category category;
    public String name;
    public ArrayList<String> photoUrls;
    public ArrayList<Tag> tags;
    public String status;
}
