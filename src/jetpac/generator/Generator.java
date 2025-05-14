package jetpac.generator;

public interface Generator {
    void update();

    boolean podeCriarElemento();

    long nextCreationTime();

    int getNElementos();

    int getMaxElementos();
}
