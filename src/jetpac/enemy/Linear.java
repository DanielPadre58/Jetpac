package jetpac.enemy;

import jetpac.astro.Direcao;
import jetpac.astro.Plataforma;
import prof.jogos2D.image.ComponenteMultiAnimado;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Linear extends InimigoDefault{
    public Linear(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img){
        super(p, vel, score, dir, img);
        velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
    }
}
