package Gala;

import Service.ShowService.ShowService;
import Service.ShowService.SimpleShowService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
@Setter
public class Gala {

    ShowService showService;
    String name;
    public Gala(String name) {
        showService = new SimpleShowService(new ArrayList<>());
        this.name = name;
    }
}
