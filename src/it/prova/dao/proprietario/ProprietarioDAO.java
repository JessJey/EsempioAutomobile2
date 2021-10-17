package it.prova.dao.proprietario;

import it.prova.dao.IBaseDAO;

import it.prova.model.Proprietario;

public interface ProprietarioDAO extends IBaseDAO<Proprietario> {

	public Proprietario getEagerAutomobile(Long id) throws Exception;

	public int countByImmatricolazione2000inPoi() throws Exception;

}
