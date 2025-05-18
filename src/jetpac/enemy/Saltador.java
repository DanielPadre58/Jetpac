package jetpac.enemy;

import jetpac.astro.Direcao;
import jetpac.astro.Plataforma;
import prof.jogos2D.image.ComponenteMultiAnimado;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Saltador extends InimigoDefault {
    private int amplitude; // se for um saltador, afeta a altura do salto
    private float fase, mudaFase; // se for um saltador indica em que fase do salto está*/

    public Saltador(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
        super(p, vel, score, dir, img);
        velX = velX / 2;
        amplitude = ThreadLocalRandom.current().nextInt(6, 9);
        fase = ThreadLocalRandom.current().nextInt(180);
        mudaFase = ThreadLocalRandom.current().nextFloat(0.3f, 0.7f);
    }

    @Override
    protected void hitsPlatformTop(Plataforma f) {
        saltar();
    }

    @Override
    protected void hitsPlatformBottom(Plataforma f) {
        move(0, -getVelY());
    }

    @Override
    protected void hitsPlatformLeft(Plataforma f) {
        setVelX(-getVelX());
    }

    @Override
    protected void hitsPlatformRight(Plataforma f) {
        setVelX(-getVelX());
    }

    protected void desloca() {
        super.desloca();
        saltar();
    }

    private void saltar(){
        fase += mudaFase;
        if (fase > 225)
            fase = 225;
        double velY = amplitude * Math.sin(Math.toRadians(-fase));
        setVelY((int) velY);
    }
}
