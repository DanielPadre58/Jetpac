package prof.jogos2D.util;

public class ReguladorVelocidade {

	private static ReguladorVelocidade unico = null;

	private static long intervaloStandard = 33;
	private volatile long intervaloEntreAtualizacoes = intervaloStandard;
	private long ultimoReal;
	private long ultimoRelativo;

	private ReguladorVelocidade() {
		ultimoReal = System.currentTimeMillis();
		ultimoRelativo = ultimoReal;
	}

	public static void setIntervaloStandard(long is) {
		if (is < 0)
			throw new IllegalArgumentException("is tem de ser positivo: " + is);
		intervaloStandard = is;
	}

	public static long getIntervaloStandard() {
		return intervaloStandard;
	}

	public static ReguladorVelocidade getRegulador() {
		if (unico == null)
			unico = new ReguladorVelocidade();
		return unico;
	}

	/**
	 * Método de conveniência que chama o getTempoRelativo no regulador existente.
	 * Equivalente a usar ReguladorVelocidade.getRegulador().getTempoRelativo();
	 * 
	 * @return o tempo relativo atual
	 */
	public static long tempoRelativo() {
		return getRegulador().getTempoRelativo();
	}

	public void setVelocidadePercentagem(int perc) {
		if (perc < 0)
			throw new IllegalArgumentException();
		ultimoRelativo = getTempoRelativo();
		ultimoReal = System.currentTimeMillis();
		if (perc == 0)
			intervaloEntreAtualizacoes = 0;
		else
			intervaloEntreAtualizacoes = 100 * intervaloStandard / perc;
	}

	public long getIntervaloEntreAtualizacoes() {
		return intervaloEntreAtualizacoes;
	}

	public void setIntervaloEntreAtualizacoes(long sp) {
		if (sp < 0)
			throw new IllegalArgumentException();
		ultimoRelativo = getTempoRelativo();
		ultimoReal = System.currentTimeMillis();
		intervaloEntreAtualizacoes = sp;
	}

	public long getTempoRelativo() {
		if (intervaloEntreAtualizacoes == 0)
			return ultimoRelativo;
		return ultimoRelativo
				+ (System.currentTimeMillis() - ultimoReal) * intervaloStandard / intervaloEntreAtualizacoes;
	}
}
