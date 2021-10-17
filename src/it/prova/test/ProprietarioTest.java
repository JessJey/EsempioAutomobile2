package it.prova.test;

import java.util.Date;
import java.util.List;

import org.hibernate.LazyInitializationException;

import it.prova.dao.EntityManagerUtil;
import it.prova.model.Automobile;
import it.prova.model.Proprietario;

import it.prova.service.MyServiceFactory;
import it.prova.service.automobile.AutomobileService;
import it.prova.service.proprietario.ProprietarioService;

public class ProprietarioTest {

	public static void main(String[] args) {

		ProprietarioService proprietarioService = MyServiceFactory.getProprietarioServiceInstance();
		AutomobileService automobileService = MyServiceFactory.getAutomobileServiceInstance();

		try {

			System.out.println("In tabella Proprietario ci sono " + proprietarioService.listAllProprietari().size()
					+ " elementi.");

			testInserisciProprietario(proprietarioService);
			System.out.println("In tabella Proprietario ci sono " + proprietarioService.listAllProprietari().size()
					+ " elementi.");

			testInserisciAutomobile(proprietarioService, automobileService);
			System.out.println("In tabella Proprietario ci sono " + proprietarioService.listAllProprietari().size()
					+ " elementi.");

			testRimozioneAutomobile(proprietarioService, automobileService);
			System.out.println("In tabella Proprietario ci sono " + proprietarioService.listAllProprietari().size()
					+ " elementi.");

			testLazyInitExc(proprietarioService, automobileService);

			testCercaByProprietarioCF(proprietarioService, automobileService);
			System.out.println("In tabella Proprietario ci sono " + proprietarioService.listAllProprietari().size()
					+ " elementi.");

			System.out.println(proprietarioService.contaImmatricolazioneDal2000inPoi());
			System.out.println("In tabella Proprietario ci sono " + proprietarioService.listAllProprietari().size()
					+ " elementi.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerUtil.shutdown();
		}

	}

	private static void testInserisciProprietario(ProprietarioService proprietarioService) throws Exception {
		System.out.println(".......testInserisciProprietario inizio.............");

		Proprietario primoProprietario = new Proprietario("Jessi", "Pet", "PTRNJS95M182LXLE", new Date());

		if (primoProprietario.getId() != null)
			throw new RuntimeException("testInserisciProprietario fallito: record già presente ");
		// insert
		proprietarioService.inserisciNuovo(primoProprietario);

		if (primoProprietario.getId() == null)
			throw new RuntimeException("testInserisciProprietario fallito ");

		System.out.println(".......testInserisciProprietario fine: PASSED.............");
	}

	private static void testInserisciAutomobile(ProprietarioService proprietarioService,
			AutomobileService automobileService) throws Exception {

		System.out.println(".......testInserisciAutomobile inizio.............");
		List<Proprietario> listaProprietariPresenti = proprietarioService.listAllProprietari();
		if (listaProprietariPresenti.isEmpty())
			throw new RuntimeException("testInserisciAutomobile fallito: non ci sono proprietari a cui collegarci ");

		Automobile primaAutomobile = new Automobile("FORD", "KA", "FA136TE", new Date());
		primaAutomobile.setProprietario(listaProprietariPresenti.get(0));

		automobileService.inserisciNuovo(primaAutomobile);
		if (primaAutomobile.getId() == null)
			throw new RuntimeException("testInserisciAutomobile fallito ");

		if (primaAutomobile.getProprietario() == null)
			throw new RuntimeException("testInserisciAutomobile fallito: non ha collegato il proprietario ");

		System.out.println(".......testInserisciAutomobile fine: PASSED.............");
	}

	private static void testRimozioneAutomobile(ProprietarioService proprietarioService,
			AutomobileService automobileService) throws Exception {

		System.out.println(".......testRimozioneAutomobile inizio.............");

		List<Proprietario> listaProprietariPresenti = proprietarioService.listAllProprietari();
		if (listaProprietariPresenti.isEmpty())
			throw new RuntimeException("testRimozioneAutomobile fallito: non ci sono proprietari a cui collegarci ");

		Automobile autoTestRimozione = new Automobile("Ford", "Focus", "DD876XA", new Date());

		autoTestRimozione.setProprietario(listaProprietariPresenti.get(0));

		automobileService.inserisciNuovo(autoTestRimozione);

		Long idAutomobileInserita = autoTestRimozione.getId();
		automobileService.rimuovi(automobileService.caricaSingolaAutomobile(idAutomobileInserita));
		// prova rimozione
		if (automobileService.caricaSingolaAutomobile(idAutomobileInserita) != null)
			throw new RuntimeException("testRimozioneAutomobile fallito: record non cancellato ");
		System.out.println(".......testRimozioneAutomobile fine: PASSED.............");
	}

	private static void testLazyInitExc(ProprietarioService proprietarioService, AutomobileService automobileService)
			throws Exception {
		System.out.println(".......testLazyInitExc inizio.............");

		List<Proprietario> listaProprietariPresenti = proprietarioService.listAllProprietari();
		if (listaProprietariPresenti.isEmpty())
			throw new RuntimeException("testLazyInitExc fallito: non ci sono proprietari a cui collegarci ");

		Proprietario proprietarioSuCuiFareIlTest = listaProprietariPresenti.get(0);
		try {
			proprietarioSuCuiFareIlTest.getAutomobile().size();
			throw new RuntimeException("testLazyInitExc fallito: eccezione non lanciata ");
		} catch (LazyInitializationException e) {

		}

		System.out.println(".......testLazyInitExc fine: PASSED.............");
	}

	private static void testCercaByProprietarioCF(ProprietarioService proprietarioService,
			AutomobileService automobileService) throws Exception {
		System.out.println(".......testCercaByProprietarioCF inizio.............");

		List<Proprietario> listaProprietariPresenti = proprietarioService.listAllProprietari();
		if (listaProprietariPresenti.isEmpty())
			throw new RuntimeException("testCercaByProprietarioCF fallito: non ci sono municipi a cui collegarci ");

		Automobile nuovoAutomobile = new Automobile("Opel", "Corsa", "asf", new Date());
		Automobile nuovoAutomobile2 = new Automobile("Opel", "Adam", "asgq", new Date());

		nuovoAutomobile.setProprietario(listaProprietariPresenti.get(0));
		nuovoAutomobile2.setProprietario(listaProprietariPresenti.get(0));

		automobileService.inserisciNuovo(nuovoAutomobile);
		automobileService.inserisciNuovo(nuovoAutomobile2);

		automobileService.cercaByProprietarioCF("PT");

		// clean up code
		automobileService.rimuovi(nuovoAutomobile);
		automobileService.rimuovi(nuovoAutomobile2);

		System.out.println(".......testCercaByProprietarioCF fine: PASSED.............");
	}

	private static void testContaImmatricolazione2000inPoi(ProprietarioService proprietarioService,
			AutomobileService automobileService) throws Exception {
		System.out.println(".......testContaImmatricolazione2000inPoi inizio.............");

		List<Proprietario> listaProprietariPresenti = proprietarioService.listAllProprietari();
		if (listaProprietariPresenti.isEmpty())
			throw new RuntimeException("testContaImmatricolazione2000inPoi fallito: non ci sono municipi a cui collegarci ");

		Automobile nuovoAutomobile = new Automobile("Opel", "Mokka", "de345gt", new Date());
		Automobile nuovoAutomobile2 = new Automobile("Opel", "Astra", "ga234er", new Date());

		nuovoAutomobile.setProprietario(listaProprietariPresenti.get(0));
		nuovoAutomobile2.setProprietario(listaProprietariPresenti.get(0));

		automobileService.inserisciNuovo(nuovoAutomobile);
		automobileService.inserisciNuovo(nuovoAutomobile2);

		proprietarioService.contaImmatricolazioneDal2000inPoi();

		// clean up code
		automobileService.rimuovi(nuovoAutomobile);
		automobileService.rimuovi(nuovoAutomobile2);

		System.out.println(".......testContaImmatricolazione2000inPoi fine: PASSED.............");
	}

}