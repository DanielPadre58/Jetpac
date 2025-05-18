package jetpac.enemy;

import jetpac.astro.Direcao;
import jetpac.astro.Plataforma;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.util.ReguladorVelocidade;

import javax.swing.plaf.multi.MultiTableHeaderUI;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Flutuante extends InimigoDefault {
    private long changeCycle;

    public Flutuante(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
        super(p, vel, score, dir, img);
        velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
        changeCycle = proximaMudancaAltura();
    }

    /**
     * Este método calcula quando deve fazer a próxima
     * mudança de altura
     *
     * @return o tempo da próxima mudança de altura
     */
    private long proximaMudancaAltura() {
        return ReguladorVelocidade.tempoRelativo() + 4000 + ThreadLocalRandom.current().nextInt(2000);
    }

    @Override
    protected void hitsPlatformTop(Plataforma f) {
        return;
    }

    @Override
    protected void hitsPlatformBottom(Plataforma f) {
        return;
    }

    @Override
    protected void hitsPlatformLeft(Plataforma f) {
        setVelX(-getVelX());
        move(getVelX(), decidirDeltaAltura());
    }

    @Override
    protected void hitsPlatformRight(Plataforma f) {
        setVelX(-getVelX());
        move(getVelX(), decidirDeltaAltura());
    }

    @Override
    protected void desloca() {
        if (ReguladorVelocidade.tempoRelativo() >= changeCycle) {
            move(getVelX(), getVelY() + decidirDeltaAltura());
            changeCycle = proximaMudancaAltura();
            return;
        }

        super.desloca();
    }

    private boolean decidir(){
        return ThreadLocalRandom.current().nextInt() > 0.5; // Retorna a decisão baseada no valor gerado aleatoriamente
    }

    private int decidirDeltaAltura(){
        int dy = ThreadLocalRandom.current().nextInt(10, 30); // Decide valor dy que o inimigo irá subir ou descer
        int sentido = decidir() ? 1 : -1; //Decide o sentido, 1 para subir, -1 para descer
        return dy * sentido;
    }
}
