package jetpac.enemy;

import jetpac.astro.Direcao;
import jetpac.astro.Plataforma;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.util.ReguladorVelocidade;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Ziguezague extends InimigoDefault {
    private long changeCycle;
    private int startingVelY;
    private int startingVelX;

    public Ziguezague(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
        super(p, vel, score, dir, img);
        velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
        startingVelY = velY;
        startingVelX = velX;
        changeCycle = proximaTrocaSentido();
    }

    @Override
    protected void hitsPlatformTop(Plataforma f) {
        // Decide o quanto a velocida diminui/aumenta
        setVelY((int)(-1 * getVelY() * ThreadLocalRandom.current().nextDouble(0.5, 1.5)));
        if (velY < startingVelY * 0.3){
            die();
        }
    }

    @Override
    protected void hitsPlatformBottom(Plataforma f) {
        // Decide o quanto a velocida diminui/aumenta
        setVelY((int)(-1 * getVelY() * ThreadLocalRandom.current().nextDouble(0.5, 1.5)));
        if (velY < startingVelY * 0.3){
            die();
        }
    }

    @Override
    protected void hitsPlatformLeft(Plataforma f) {
        // Decide o quanto a velocida diminui/aumenta
        setVelX((int)(-1 * getVelX() * ThreadLocalRandom.current().nextDouble(0.5, 1.1)));
        if (velX < startingVelX * 0.3){
            die();
        }
    }

    @Override
    protected void hitsPlatformRight(Plataforma f) {
        // Decide o quanto a velocida diminui/aumenta
        setVelX((int)(-1 * getVelX() * ThreadLocalRandom.current().nextDouble(0.5, 1.5)));//
        if (velX < startingVelX * 0.3){
            die();
        }
    }
    protected void desloca() {
        if(ReguladorVelocidade.tempoRelativo() >= changeCycle){
            setVelX(-velX);
            setVelY(-velY);
            changeCycle = proximaTrocaSentido();
        }

        super.desloca();
    }


    /**
     * Este método calcula quando deve fazer a próxima
     * perseguição
     *
     * @return o tempo da próxima perseguição
     */
    private long proximaTrocaSentido() {
        return ReguladorVelocidade.tempoRelativo() + 2000 + ThreadLocalRandom.current().nextInt(2000);
    }
}
