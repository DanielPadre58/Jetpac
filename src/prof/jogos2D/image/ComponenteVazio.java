package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Representa um componente vazio, isto é, um componente que não tem qualquer
 * imagem associada e que apenas "ocupa espaço"
 */
public class ComponenteVazio implements ComponenteVisual {

    private Rectangle area;

    public ComponenteVazio(Rectangle area) {
        this.area = area;
    }

    @Override
    public void desenhar(Graphics g) {
    }

    @Override
    public Point getPosicao() {
        return area.getLocation();
    }

    @Override
    public Point getPosicaoCentro() {
        return new Point(area.x + area.width / 2, area.y + area.height / 2);
    }

    @Override
    public void setPosicao(Point p) {
        area.setLocation(p);
    }

    @Override
    public void setPosicaoCentro(Point p) {
        area.setLocation(new Point(p.x - area.width / 2, p.y - area.height / 2));
    }

    @Override
    public int getComprimento() {
        return area.width;
    }

    @Override
    public int getAltura() {
        return area.height;
    }

    @Override
    public Rectangle getBounds() {
        return area;
    }

    @Override
    public Image getSprite() {
        return null;
    }

    @Override
    public void setSprite(Image sprite) {
    }

    @Override
    public void rodar(double angulo) {
    }

    @Override
    public void setAngulo(double angulo) {
    }

    @Override
    public double getAngulo() {
        return 0;
    }

    @Override
    public int numCiclosFeitos() {
        return 0;
    }

    @Override
    public boolean eCiclico() {
        return false;
    }

    @Override
    public void setCiclico(boolean ciclico) {
    }

    @Override
    public void setPausa(boolean pausa) {
    }

    @Override
    public boolean estaPausa() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void inverter() {
    }

    @Override
    public ComponenteVazio clone() {
        return new ComponenteVazio((Rectangle) area.clone());
    }
}
