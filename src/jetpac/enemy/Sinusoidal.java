package jetpac.enemy;

import jetpac.astro.Direcao;
import jetpac.astro.Plataforma;
import prof.jogos2D.image.ComponenteMultiAnimado;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Sinusoidal extends InimigoDefault {
    private double fase = 0;
    private int amplitude = 10;

    public Sinusoidal(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
        super(p, vel, score, dir, img);
        amplitude = ThreadLocalRandom.current().nextInt(6, 15);
    }

    @Override
    protected void hitsPlatformTop(Plataforma f) {
        fase = Math.PI + 0.5;
        desloca();
    }

    @Override
    protected void hitsPlatformBottom(Plataforma f) {
        fase = 0 + 0.5;
        desloca();
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
        int dy = (int)(Math.sin(fase) * amplitude); // amplitude = 10 por exemplo
        move(velX, dy);
        fase += 0.1; // controle da "frequência"
    }
}
