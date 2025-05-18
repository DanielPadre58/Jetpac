package jetpac.enemy;

import jetpac.astro.Direcao;
import jetpac.astro.Plataforma;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.util.ReguladorVelocidade;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Ricochete extends InimigoDefault {

    public Ricochete(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
        super(p, vel, score, dir, img);
        velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
    }

//    /**
//     * Este método calcula quando deve fazer a próxima
//     * mudança de movimento
//     *
//     * @return o tempo da próxima mudança de movimento
//     */
//    private long proximaMudanca() {
//        return ReguladorVelocidade.tempoRelativo() + 3000 + ThreadLocalRandom.current().nextInt(5000);
//    }

    @Override
    protected void hitsPlatformTop(Plataforma f) {
        setVelY(-getVelY());
    }

    @Override
    protected void hitsPlatformBottom(Plataforma f) {
        setVelY(-getVelY());
    }

    @Override
    protected void hitsPlatformLeft(Plataforma f) {
        setVelX(-getVelX());
    }

    @Override
    protected void hitsPlatformRight(Plataforma f) {
        setVelX(-getVelX());
    }
}
