
package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;
import villagegaulois.VillageSansChefException;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtal) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche = new Marche(nbEtal);
	}
	

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() throws VillageSansChefException {
		if (chef == null) {
			throw new VillageSansChefException("Il n'y a pas de chef pour le village !!");
		}
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	private static class Marche {
		private Etal[] etals;
		
		private Marche(int nbEtal) {
			etals = new Etal[nbEtal];
			for (int i = 0; i < nbEtal; i++) {
				etals[i] = new Etal();
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			if (!etals[indiceEtal].isEtalOccupe()) {
				etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
			}
		}
		
		private int trouverEtalLibre() {
			for (int i = 0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe()) {
					return i;
				}
			}
			return -1;
		}
		
		private Etal[] trouverEtals(String produit) {
			int indice = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].contientProduit(produit)) {
					indice++;
				}
			
			}
			Etal[] etalContenantProduit = new Etal[indice];
			indice = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].contientProduit(produit)) {
					etalContenantProduit[indice] = etals[i];
					indice++;
				}
			}
			return etalContenantProduit;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < etals.length; i++) {
				if (gaulois.equals(etals[i].getVendeur())) {
					return etals[i];
				}
			}
			return null;
		}
		
		private String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int nbEtalLibres = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					chaine.append(etals[i].getVendeur().getNom() + " vend " + etals[i].getQuantite() + " " + etals[i].getProduit() + "\n");
				} else {
					nbEtalLibres++;
				}
			}
			if (nbEtalLibres != 0) {
				chaine.append("Il reste " + nbEtalLibres + " etals non utilises dans le marche.\n");
			}
			return chaine.toString();
		}
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + "\n");
		int numEtal = marche.trouverEtalLibre();
		marche.utiliserEtal(numEtal, vendeur, produit, nbProduit);
		chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " a l'etal n°" + (numEtal + 1) + "\n");
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String Produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] etalProduit = marche.trouverEtals(Produit);
		if (etalProduit.length == 0) {
			chaine.append("Il n'y a pas de vendeur qui propose des " + Produit + " au marche.\n");
			return chaine.toString();
		} else if (etalProduit.length == 1) {
			chaine.append("Seul le vendeur " + etalProduit[0].getVendeur().getNom() + " propose des " + Produit + " au marche.\n");
		} else {
			chaine.append("Les vendeurs qui proposent des " + Produit + " sont :\n");
			for (int i = 0; i < etalProduit.length; i++) {
				chaine.append("- " + etalProduit[i].getVendeur().getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		Etal etal = marche.trouverVendeur(vendeur);
		return etal;
	}
	
	public String partirVendeur(Gaulois vendeur) {
		String chaine = rechercherEtal(vendeur).libererEtal();
		return chaine;
	}
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Le marche du village \"" + this.nom + "\" possede plusieurs etals :\n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}
}
