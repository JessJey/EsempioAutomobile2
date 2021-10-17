package it.prova.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "proprietario")
public class Proprietario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nome")
	private String nome;
	@Column(name = "cognome")
	private String cognome;
	@Column(name = "cf")
	private String cf;
	@Column(name = "dataDiNascita")
	private Date dataDiNascita;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "proprietario")
	private Set<Automobile> automobile = new HashSet<>();

	public Proprietario() {
		// TODO Auto-generated constructor stub
	}

	public Proprietario(String nome, String cognome, String cf, Date dataDiNascita) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.cf = cf;
		this.dataDiNascita = dataDiNascita;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public Date getDataDiNascita() {
		return dataDiNascita;
	}

	public void setDataDiNascita(Date dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	public Set<Automobile> getAutomobile() {
		return automobile;
	}

	public void setAutomobile(Set<Automobile> automobile) {
		this.automobile = automobile;
	}

	@Override
	public String toString() {
		return "Proprietario [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", cf=" + cf + ", dataDiNascita="
				+ dataDiNascita + ", proprietario=" + automobile + "]";
	}

}
