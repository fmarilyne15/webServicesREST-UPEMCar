# Projet Services Web

Travail réalisé par Marilyne FERREIRA, Landry MPATI, Issoufou OUSMANE MOUSSA

## Sujet

La société UPEM Corp. vient d’acquérir la société RentCarsUPEM, spécialisée dans la location de véhicules. Elle désire faire profiter ses employés de ce service à prix préférentiel. Vous être chargés de la conception et l’implémentation d’une application Java de gestion de ce service, fondée sur Java RMI. Les véhicules de RentCarsUPEM peuvent être loués par tous les employés de UPEM Corp. Ces derniers peuvent ajouter des notes sur les véhicule et leur état lors de la restitution. L’application gérant la base de véhicules et celle gérant les employés s’exécutent dans deux JVM différentes.

Lorsqu’une personne demande à louer un véhicule, et que celui-ci est déjà prêté à autrui, elle est inscrite sur liste d’attente; dès que le véhicule demandé devient disponible, la personne est notifiée et loue le véhicule. En présence de plusieurs personnes en attente, le principe « premier arrivé, premier servi » est appliqué.

Dans un deuxième temps, UPEM Corp. désire valoriser sa base de véhicules, enrichie par les notes de ses salariés, et la rendre accessible à l’extérieur via un service Web appelé UPEMCarsService. Elle propose à la vente les véhicules qui ont été loués au moins une fois. Le service Web permet de consulter les prix des véhicules, de vérifier leur disponibilité, de les ajouter à un panier et de les acheter. Pour effectuer un achat sur le service Web, un autre service Web Banque est contacté par UPEMCarsService pour vérifier la disponibilité des fonds nécessaires à l'achat et effectuer le paiement. Les prix des véhicules sont en Euros, mais l’université permet des ventes dans toutes les monnaies du monde, et doit fournir les prix dans la devise demandée par l'acheteur. Les taux de change utilisés doivent être trouvés en temps-réel.

## Travail à faire 

* Implémenter les applications et les services web que vous jugez nécessaires.
* Prévoir un scénario basique, avec un certain nombre de véhicules, de salariés et de clients pour pouvoir exécuter les applications avec des saisies une intervention minimale de l’utilisateur.
* L’implémentation à fin de démonstration d’une interface graphique pour les salariés leur permettant de louer et de restituer des véhicules est un plus.
* L’implémentation à fin de démonstration d’une interface graphique pour le client de MLVCarsService, lui permettant de constituer un panier, le valider et payer et également un plus..