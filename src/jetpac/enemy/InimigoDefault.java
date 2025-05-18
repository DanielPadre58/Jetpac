package jetpac.enemy;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

import jetpac.astro.Direcao;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.util.ReguladorVelocidade;
import prof.jogos2D.util.Vector2D;
import jetpac.astro.Astronauta;
import jetpac.astro.Plataforma;
import jetpac.mundo.Mundo;

/**
 * classe que representa um inimigo
 */
public class InimigoDefault implements Inimigo {

    // constantes para identificar as animações
    protected static final int ANIM_LEFT = 0;
    protected static final int ANIM_RIGHT = 1;
    protected static final int ANIM_DIE = 2;

    protected boolean dead = false; // está morto?
    protected int velX, velY; // velocidade do inimigo em X e em Y
    protected int score; // pontuação do inimigo

    protected Mundo world;
    protected ComponenteMultiAnimado imagem;

    /**
     * cria um inimigo
     *
     * @param p     posição inicial
     * @param vel   velocidade inicial
     * @param score pontuação
     * @param dir   direção en que está virado
     * @param img   imagems
     */
    public InimigoDefault(Point p, int vel, int score, Direcao dir, ComponenteMultiAnimado img) {
        imagem = (ComponenteMultiAnimado) img.clone();

        // ver qual animação a usar
        if (dir == Direcao.LEFT) {
            imagem.setAnim(ANIM_LEFT);
            velX = -vel;
        } else {
            imagem.setAnim(ANIM_RIGHT);
            velX = vel;
        }
        imagem.setPosicao(p);
        this.score = score;

       /*{ if (tipo == TipoInimigo.LINEAR || tipo == TipoInimigo.RICOCHETE) {
            // a velocidade em Y é aleatória
            velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
        } else if (tipo == TipoInimigo.PERSEGUIDOR) {
            // a velocidade em Y é aleatória
            velY = vel / 2 - ThreadLocalRandom.current().nextInt(vel);
            changeCycle = proximaMudanca();
        } else if (tipo == TipoInimigo.SALTADOR) {
            velX = velX / 2;
            amplitude = ThreadLocalRandom.current().nextInt(6, 9);
            fase = ThreadLocalRandom.current().nextInt(180);
            mudaFase = ThreadLocalRandom.current().nextFloat(0.3f, 0.7f);
        }*/
    }

    /**
     * atualiza o inimigo updates the enemy
     */
    @Override
    public void update() {
        // se está morto não faz nada
        if (dead)
            return;

        desloca();
        checkPlataformas();
        checkAstronauta();
    }

    @Override
    public Mundo getWorld() {
        return world;
    }

    @Override
    public void setWorld(Mundo world) {
        this.world = world;
    }

    @Override
    public void draw(Graphics2D g) {
        getImagem().desenhar(g);
    }

    @Override
    public ComponenteMultiAnimado getImagem() {
        return imagem;
    }

    // ver se bate no astronauta
    // check if it hits the astronaut
    protected void checkAstronauta() {
        Rectangle ra = getWorld().getAstronaut().getBounds();
        if (ra.intersects(getImagem().getBounds())) {
            die();
            getWorld().getAstronaut().die();
        }
    }

    protected void checkPlataformas() {
        // ver se bate nas plataformas
        // check if it hits a platform
        for (Plataforma f : getWorld().getPlatforms()) {
            Vector2D toque = f.hitV(getImagem().getBounds());
            if (toque.x == 0 && toque.y < 0)
                hitsPlatformTop(f);
            else if (toque.x == 0 && toque.y > 0)
                hitsPlatformBottom(f);
            if (toque.x < 0 && toque.y == 0)
                hitsPlatformLeft(f);
            if (toque.x > 0 && toque.y == 0)
                hitsPlatformRight(f);
        }
    }

    protected void hitsPlatformTop(Plataforma f) {
        die();
        /*switch (tipo) {
            case PERSEGUIDOR:
                setVelY(-getVelY());
                break;
            case SALTADOR:
                fase = 125;
                move(0, -getVelY());
                break;
        }*/
    }

    protected void hitsPlatformBottom(Plataforma f) {
        die();
        /*switch (tipo) {
            case PERSEGUIDOR:
                setVelY(-getVelY());
                break;
            case SALTADOR:
                fase = 225;
                move(0, -getVelY());
                break;
        }*/
    }

    protected void hitsPlatformLeft(Plataforma f) {
        die();
        /*switch (tipo) {
            case FLUTUANTE:
                //Escolhe se deve subir ou descer
                move(getVelX(), (Math.random() < 0.5 ? -1 : 1) * getVelY());
                setVelY(-getVelY());
                setVelX(-getVelX());
                break;
            case PERSEGUIDOR:
                setVelX(-getVelX());
                break;
            case SALTADOR:
                setVelX(-getVelX());
                break;
        }*/
    }

    protected void hitsPlatformRight(Plataforma f) {
        die();
        /*switch (tipo) {
            case FLUTUANTE:
                //Escolhe se deve subir ou descer
                move(getVelX(), (Math.random() < 0.5 ? -1 : 1) * getVelY());
                setVelY(-getVelY());
                setVelX(-getVelX());
                break;
            case PERSEGUIDOR:
                setVelX(-getVelX());
                break;
            case SALTADOR:
                setVelX(-getVelX());
                break;
        }*/
    }

    protected void desloca() {
        move(velX, velY);
        /*switch (tipo) {
            case FLUTUANTE:
                int fatorDeMudancaMovimento = Math.random() < 0.4 ? -1 : 1;
                int mudancaY;
                if(fatorDeMudancaMovimento >= 0.7){
                    //Sobe
                    mudancaY = (int) (Math.random() * 100 + getVelY());
                } else if (fatorDeMudancaMovimento >= 0.4) {
                    //Desce
                    mudancaY = (int) (Math.random() * 100 + getVelY()) * -1;
                }
                else mudancaY = getVelY(); //Mantem

                move(getVelX(), mudancaY);
                break;
            case PERSEGUIDOR:
                if (ReguladorVelocidade.tempoRelativo() < changeCycle) {
                    move(velX, velY);
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
                changeCycle = proximaMudanca();
            case SALTADOR:
                fase += mudaFase;
                if (fase > 225)
                    fase = 225;
                double velY = amplitude * Math.sin(Math.toRadians(-fase));
                setVelY((int) velY);
                break;
        }*/
    }

    /**
     * indica se o inimigo está morto. Só está morto se a animação de morte já
     * acabou.
     *
     * @return true, se está morto
     */
    @Override
    public boolean isDead() {
        return dead && getImagem().numCiclosFeitos() > 1;
    }

    /**
     * indica se o inimigo está a morrer
     *
     * @return true se estiver a morrer
     */
    @Override
    public boolean isDying() {
        return dead;
    }

    /**
     * desloca o inimigo
     *
     * @param dx distância a mover em x
     * @param dy distância a mover em y
     */
    @Override
    public void move(int dx, int dy) {
        ComponenteMultiAnimado img = getImagem();

        int y = img.getPosicao().y;

        // se chegar a uma das extremidades passa para a outra
        if (img.getPosicao().x < 0 && dx < 0)
            img.setPosicao(new Point(getWorld().getWidth(), y));
        else if (img.getPosicao().x > getWorld().getWidth() && dx > 0)
            img.setPosicao(new Point(-img.getComprimento(), y));
        getImagem().getPosicao().translate(dx, dy);
    }

    /**
     * mata o inimigo
     */
    @Override
    public void die() {
        if (dead)
            return;
        dead = true;

        // passar para a animação de morte
        ComponenteMultiAnimado img = getImagem();
        img.setAnim(ANIM_DIE);
        img.setFrameNum(0);
        img.setCiclico(false);
    }

    /**
     * retorna a pontuação do inimigo
     *
     * @return a pontuação do inimigo
     */
    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getVelX() {
        return velX;
    }

    @Override
    public void setVelX(int velX) {
        // ver se mudou de direção
        if (velX > 0 && this.velX < 0)
            imagem.setAnim(ANIM_RIGHT);
        else if (velX < 0 && this.velX > 0)
            imagem.setAnim(ANIM_LEFT);
        this.velX = velX;
    }

    @Override
    public int getVelY() {
        return velY;
    }

    @Override
    public void setVelY(int velY) {
        this.velY = velY;
    }
}
