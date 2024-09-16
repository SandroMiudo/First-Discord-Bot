package de.self.project.bot.logic;

import java.awt.*;
import java.util.Random;

class ColorGenerator {
    int i1;
    int i2;
    int i3;

    ColorGenerator(Random random){
        i1 = random.ints(20,0,10).sum();
        i2 = random.ints(20,0,10).sum();
        i3 = random.ints(20,0,10).sum();
    }

    Color generateColor(){
        return new Color(i1,i2,i3);
    }
}
