package jetpac.enemy;

import jetpac.astro.Astronauta;
import jetpac.astro.Direcao;
import jetpac.astro.Plataforma;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.util.ReguladorVelocidade;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Perseguidor extends InimigoDefault {
    private long changeCycle;

    public Perseguidor(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
        super(p, vel, score, dir, img);
        velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
        changeCycle = proximaPerseguicao();
    }

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

    protected void desloca() {
        if (ReguladorVelocidade.tempoRelativo() < changeCycle) {
            super.desloca();
            return;
        }

        // está na altura de mudar de direção
        Astronauta astro = getWorld().getAstronaut();
        if (getImagem().getPosicao().x < astro.getPosition().x)
            setVelX(Math.abs(getVelX()));
        else
            setVelX(-Math.abs(getVelX()));
        if (getImagem().getPosicao().y < astro.getPosition().y)
            setVelY(Math.abs(getVelX() / 2));
        else
            setVelY(-Math.abs(getVelX() / 2));
        changeCycle = proximaPerseguicao();
    }

    /**
     * Este método calcula quando deve fazer a próxima
     * perseguição
     *
     * @return o tempo da próxima perseguição
     */
    private long proximaPerseguicao() {
        return ReguladorVelocidade.tempoRelativo() + 4000 + ThreadLocalRandom.current().nextInt(2000);
    }
}
