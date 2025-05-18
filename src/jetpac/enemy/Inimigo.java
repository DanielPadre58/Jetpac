package jetpac.enemy;

import jetpac.mundo.Mundo;
import prof.jogos2D.image.ComponenteMultiAnimado;

import java.awt.*;

public interface Inimigo {
    void update();

    Mundo getWorld();

    void setWorld(Mundo world);

    void draw(Graphics2D g);

    ComponenteMultiAnimado getImagem();

    boolean isDead();

    boolean isDying();

    void move(int dx, int dy);

    void die();

    int getScore();

    int getVelX();

    void setVelX(int velX);

    int getVelY();

    void setVelY(int velY);

    // constantes para identificar o tipo de inimigo
    public static enum TipoInimigo {
        LINEAR, RICOCHETE, FLUTUANTE, PERSEGUIDOR, SALTADOR
    }
}
