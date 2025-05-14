package jetpac.generator;

import jetpac.mundo.Mundo;
import prof.jogos2D.util.ReguladorVelocidade;

import java.util.concurrent.ThreadLocalRandom;

public class GeneratorDefault implements Generator {
    protected int nElementos = 0;
    protected int maxElementos;
    protected int minCreationTime;
    protected long nextCreationTime;
    protected int range;
    protected Mundo mundo;

    public GeneratorDefault(int maxElementos, int minCreationTime, int range, Mundo mundo){
        this.maxElementos = maxElementos;
        this.minCreationTime = minCreationTime;
        this.range = range;
        this.mundo = mundo;
        nextCreationTime = nextCreationTime();
    }

    @Override
    public void update(){
        if(!podeCriarElemento())
            return;

        nElementos++;
        nextCreationTime = nextCreationTime();
    }

    @Override
    public boolean podeCriarElemento(){
        return (nElementos < maxElementos) // verifica se ha elementos para criar
                && (nextCreationTime <= ReguladorVelocidade.tempoRelativo()); // quando o temporizador chega a zero é altura de criar fuel
    }

    /**
     * estabelece o tempo de criação do próximo tesouro
     *
     * @return o próximo tempo de criação
     */
    @Override
    public long nextCreationTime(){
        return minCreationTime + ReguladorVelocidade.tempoRelativo() + ThreadLocalRandom.current().nextInt(range);
    }

    @Override
    public int getNElementos(){
        return nElementos;
    }

    @Override
    public int getMaxElementos(){
        return maxElementos;
    }
}
